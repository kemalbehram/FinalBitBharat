package com.mobiloitte.microservice.wallet.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.dto.AdminBankDto;
import com.mobiloitte.microservice.wallet.dto.AdminUpiDto;
import com.mobiloitte.microservice.wallet.dto.ApproveWithdrawRequestDto;
import com.mobiloitte.microservice.wallet.dto.FiatDto;
import com.mobiloitte.microservice.wallet.dto.FiatWithdrawRequestDto;
import com.mobiloitte.microservice.wallet.dto.FundUserDto;
import com.mobiloitte.microservice.wallet.dto.GetBalanceResponseDto;
import com.mobiloitte.microservice.wallet.dto.P2pHistoryDto;
import com.mobiloitte.microservice.wallet.dto.SearchAndFilterCoinWithdrawlDepositDto;
import com.mobiloitte.microservice.wallet.dto.UpiUserDto;
import com.mobiloitte.microservice.wallet.dto.WithdrawDtoInr;
import com.mobiloitte.microservice.wallet.dto.WithdrawRequestDto;
import com.mobiloitte.microservice.wallet.dto.WithdrawRequestDtoUpdated;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.enums.FiatStatus;
import com.mobiloitte.microservice.wallet.enums.Network;
import com.mobiloitte.microservice.wallet.enums.TransactionType;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.CryptoStorageService;
import com.mobiloitte.microservice.wallet.service.HotColdManagementService;
import com.mobiloitte.microservice.wallet.service.WalletManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class WalletController.
 * 
 * @author Ankush Mohapatra
 */
@RestController
@RequestMapping(value = "/wallet")
@Api(value = "Wallet Management APIs")
public class WalletController implements OtherConstants {

	/** The wallet management service. */
	@Autowired
	private WalletManagementService walletManagementService;

	/** The hot cold management service. */
	@Autowired
	private HotColdManagementService hotColdManagementService;
	@Autowired
	private CryptoStorageService cryptoStorageService;

	/**
	 * Gets the wallet.
	 *
	 * @param userId the user id
	 * @return the wallet
	 */
	@ApiOperation(value = "API to create wallet")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "wallet created successfully"),
			@ApiResponse(code = 205, message = "No coins found to create wallet / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/create-wallet")
	public Response<String> getWallet(@RequestParam("id") Long userId, @RequestParam String randomId) {
		if (userId != null && userId != 0) {
			return walletManagementService.createWallet(userId, randomId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@PutMapping("update-randomId")
	public Response<Object> updateId(@RequestHeader Long userId, @RequestParam String oldRandomId,
			@RequestParam String newRandomId) {
		return walletManagementService.updateId(userId, oldRandomId, newRandomId);
	}

	/**
	 * Gets the balance.
	 *
	 * @param coinName the coin name
	 * @param userId   the user id
	 * @return the balance
	 */
	@ApiOperation(value = "API to get wallet balance of a crypto/fiat coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "balance fetched successfully"),
			@ApiResponse(code = 205, message = "No wallet found for the user / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-balance")
	public Response<GetBalanceResponseDto> getBalance(@RequestParam("coinName") String coinName,
			@RequestHeader Long userId) {
		if (userId != null && userId != 0 && coinName != null) {
			return walletManagementService.getBalance(coinName, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the address.
	 *
	 * @param coinName the coin name
	 * @param userId   the user id
	 * @return the address
	 */
	@ApiOperation(value = "API to get wallet address of a crypto coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "address found successfully"),
			@ApiResponse(code = 205, message = "Failed to fetched address from Live Blockchain API / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-address")
	public Response<Wallet> getAddress(@RequestParam("coinName") String coinName, @RequestHeader Long userId) {
		if (userId != null && userId != 0 && coinName != null) {
			return walletManagementService.getWalletAddress(coinName, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@DeleteMapping("/delete-user-upi-account")
	public Response<Object> deleteUserBankAccount(@RequestHeader Long userId, @RequestParam Long upiId) {
		if (userId != null && userId != 0) {
			if (upiId != null && upiId != 0) {
				return walletManagementService.deleteUserBankAccount(userId, upiId);
			} else {
				throw new BadRequestException("Invalid Bank Acoount Id");
			}
		} else {
			throw new BadRequestException("Invalid User Id");
		}
	}

	@PostMapping("/p2p-data")
	public Response<String> updateP2pData(@RequestBody P2pHistoryDto p2pHistoryDto, @RequestParam Long userId) {
		return walletManagementService.updateP2pData(p2pHistoryDto, userId);
	}

	/**
	 * Gets the deposits.
	 *
	 * @param coinName the coin name
	 * @param userId   the user id
	 * @return the deposits
	 */
	@ApiOperation(value = "API to get deposit list of a crypto coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "deposit list fetched successfully"),
			@ApiResponse(code = 205, message = "Deposit Failed / No storage details found / Wallet not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-deposits")
	public Response<Map<String, Object>> getDeposits(@RequestParam("coinName") String coinName,
			@RequestHeader Long userId, @RequestParam Integer page, @RequestParam Integer pageSize,
			@RequestHeader String username) {
		if (userId != null && userId != 0 && coinName != null) {
			return hotColdManagementService.deposit(coinName, userId, page, pageSize, username);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the withdraw.
	 *
	 * @param withdrawRequest the withdraw request
	 * @param userId          the user id
	 * @return the withdraw
	 */
	@ApiOperation(value = "API to withdraw crypto coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Withdraw request psoted successfully"),
			@ApiResponse(code = 205, message = "withdraw Failed / Insufficient wallet balance / Insufficient storage balance / No storage details found / Wallet not found / No such coin found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/withdraw")
	public Response<String> getWithdraw(@Valid @RequestBody WithdrawRequestDto withdrawRequest,
			@RequestHeader Long userId) {
		if (userId != null && userId != 0 && withdrawRequest != null) {
			return hotColdManagementService.withdrawBalance(withdrawRequest, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	/**
	 * Gets the withdraw.
	 *
	 * @param withdrawRequest the withdraw request
	 * @param userId          the user id
	 * @return the withdraw
	 */
	@ApiOperation(value = "API to withdraw crypto coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Withdraw request psoted successfully"),
			@ApiResponse(code = 205, message = "withdraw Failed / Insufficient wallet balance / Insufficient storage balance / No storage details found / Wallet not found / No such coin found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/withdraw-erc-BEP")
	public Response<String> withdrawBalanceErcBep(@Valid @RequestBody WithdrawRequestDtoUpdated withdrawRequest,
			@RequestHeader Long userId) {
		if (userId != null && userId != 0 && withdrawRequest != null) {
			return hotColdManagementService.withdrawBalanceErcBep(withdrawRequest, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	/**
	 * Gets the approve withdraw.
	 *
	 * @param approveWithdrawRequestDto the approve withdraw request dto
	 * @param userId                    the user id
	 * @return the approve withdraw
	 */
	@ApiOperation(value = "API to approve withdraw crypto coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "withdraw successful"),
			@ApiResponse(code = 205, message = "withdraw token expired / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/approve-withdraw")
	public Response<String> getApproveWithdraw(
			@Valid @RequestBody ApproveWithdrawRequestDto approveWithdrawRequestDto) {
		if (approveWithdrawRequestDto != null) {
			return hotColdManagementService.approveWithdraw(approveWithdrawRequestDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	/**
	 * Gets the reject withdraw.
	 *
	 * @param token  the token
	 * @param userId the user id
	 * @return the reject withdraw
	 */
	@ApiOperation(value = "API to reject withdraw crypto coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "withdraw request cancelled successfully"),
			@ApiResponse(code = 205, message = "withdraw token expired / updation failed / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/cancel-withdraw")
	public Response<String> getRejectWithdraw(@RequestParam("token") String token) {
		if (token != null) {
			return hotColdManagementService.rejectWithdraw(token);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the fiat withdraw.
	 *
	 * @param fiatWithdrawRequest the fiat withdraw request
	 * @param userId              the user id
	 * @return the fiat withdraw
	 */
	@ApiOperation(value = "API to withdraw fiat coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Withdraw request posted successfully"),
			@ApiResponse(code = 205, message = "Insufficient wallet balance, please update your balance / No such coin found / Wallet not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/fiat-withdraw")
	public Response<String> getFiatWithdraw(@Valid @RequestBody FiatWithdrawRequestDto fiatWithdrawRequest,
			@RequestHeader Long userId) {
		if (userId != null && userId != 0 && fiatWithdrawRequest != null) {
			return hotColdManagementService.withdrawFiatBalance(fiatWithdrawRequest, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	@ApiOperation(value = "API to get withdraw list of a crypto coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "withdraw list fetched successfully"),
			@ApiResponse(code = 205, message = "No withdraw found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-withdrawlist")
	public Response<Map<String, Object>> getWithdrawList(@RequestParam("coinName") String coinName,
			@RequestHeader Long userId, @RequestParam Integer page, @RequestParam Integer pageSize) {
		if (userId != null && userId != 0 && coinName != null) {
			return hotColdManagementService.withdrawList(coinName, userId, page, pageSize);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the withdraw list.
	 *
	 * @param coinName the coin name
	 * @param userId   the user id
	 * @return the withdraw list
	 */
	@ApiOperation(value = "API to get withdraw list of a crypto coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "withdraw list fetched successfully"),
			@ApiResponse(code = 205, message = "No withdraw found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/filter-get-withdrawlist")
	public Response<Map<String, Object>> getWithdrawList(@RequestHeader Long userId,
			@RequestBody SearchAndFilterCoinWithdrawlDepositDto serAndFilterCoinWithdrawlDepositDto) {
		if (serAndFilterCoinWithdrawlDepositDto.getUserId() != null
				&& serAndFilterCoinWithdrawlDepositDto.getUserId() != 0
				&& serAndFilterCoinWithdrawlDepositDto.getCoinName() != null) {
			return hotColdManagementService.withdrawListFilter(userId, serAndFilterCoinWithdrawlDepositDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to get withdraw list of a crypto coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "withdraw list fetched successfully"),
			@ApiResponse(code = 205, message = "No withdraw found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/filter-get-depositelist")
	public Response<Map<String, Object>> getDepositeList(@RequestHeader Long userId,
			@RequestBody SearchAndFilterCoinWithdrawlDepositDto serAndFilterCoinWithdrawlDepositDto) {
		if (serAndFilterCoinWithdrawlDepositDto.getUserId() != null
				&& serAndFilterCoinWithdrawlDepositDto.getUserId() != 0
				&& serAndFilterCoinWithdrawlDepositDto.getCoinName() != null) {
			return hotColdManagementService.getDepositeFilter(userId, serAndFilterCoinWithdrawlDepositDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the wallet details by coin nameand user.
	 *
	 * @param coinName the coin name
	 * @param userId   the user id
	 * @return the wallet details by coin nameand user
	 */
	@ApiOperation(value = "API to get wallet details by user and coinName")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Wallet details fetched successfully"),
			@ApiResponse(code = 205, message = "Failed to fetch wallet details / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-wallet-details")
	public Response<Wallet> getWalletDetails(@RequestParam("coinName") String coinName, @RequestHeader Long userId) {
		if (userId != null && userId != 0 && coinName != null) {
			return walletManagementService.getWalletDetailsByCoinNameAndUser(coinName, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@GetMapping(value = "/get-smallAsset-History")
	public Response<Object> getSmallAssetHistory(@RequestParam(required = false) String coinName,
			@RequestHeader Long userId, @RequestParam Integer page, @RequestParam Integer pageSize,
			@RequestParam(required = false) Long fromDate, @RequestParam(required = false) Long toDate,
			@RequestParam(required = false) BigDecimal amount) {
		if (!userId.equals(null)) {
			return walletManagementService.getSmallAssetHistory(coinName, userId, page, pageSize, fromDate, toDate,amount);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the all user balance and coin list.
	 *
	 * @param userId the user id
	 * @return the all user balance and coin list
	 */
	@ApiOperation(value = "API to get all Wallet Balance and CoinList")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All user balance and coinlist fetched successfully"),
			@ApiResponse(code = 205, message = "Wallet not found / Coin not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-all-user-balance-and-coinlist")
	public Response<Map<String, Object>> getAllUserBalanceAndCoinList(@RequestHeader Long userId) {
		if (userId != null && userId != 0) {
			return walletManagementService.getUserAllBalanceWithCoinList(userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	public Response<Object> getAveragePrice(@RequestHeader Long userId) {
		if (userId != null && userId != 0) {
			return walletManagementService.getAveragePrice(userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	/**
	 * Gets the all user balance and coin list.
	 *
	 * @param userId the user id
	 * @return the all user balance and coin list
	 */
	@ApiOperation(value = "API to get all Wallet Address of a user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "all user address fetched successfully"),
			@ApiResponse(code = 205, message = "Wallet not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-all-user-address")
	public Response<List<Map<String, Object>>> getAllUserWalletAddress(@RequestHeader Long userId) {
		if (userId != null && userId != 0) {
			return walletManagementService.getAllUserAddress(userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	@PostMapping(value = "/update-wallet")
	public Response<String> updateWallet(@RequestParam BigDecimal walletBalance, @RequestParam Long userId,
			@RequestParam String coinName) {
		if (userId != null && userId != 0) {
			return walletManagementService.updateWallet(walletBalance, userId, coinName);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@PostMapping(value = "/update-blocked-balance")
	public Response<String> updateBlocked(@RequestParam BigDecimal blockedBalance, @RequestParam Long userId,
			@RequestParam String coinName) {
		if (userId != null && userId != 0) {
			return walletManagementService.updateBlocked(blockedBalance, userId, coinName);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@PostMapping(value = "/update-wallet-new")
	public Response<String> updateWalletNew(@RequestParam BigDecimal walletBalance, @RequestParam Long userId,
			@RequestParam String coinName) {
		if (userId != null && userId != 0) {
			return walletManagementService.updateWalletNew(walletBalance, userId, coinName);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to get wallet address of a ERC20 tokens")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "address found successfully"),
			@ApiResponse(code = 205, message = "Failed to fetched address from Live Blockchain API / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-address-for-erc20")
	public Response<String> getAddressOfERC20(@RequestParam("coinName") String coinName, @RequestHeader Long userId) {
		if (userId != null && userId != 0 && coinName != null) {
			return walletManagementService.getAddressforERC20Tokens(coinName, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@GetMapping(value = "/get-all-user-balance-and-coinlist-withName")
	public Response<Map<String, Object>> getAllUserBalanceAndCoinListWithName(@RequestParam Long userId) {
		if (userId != null && userId != 0) {
			return walletManagementService.getAllUserBalanceAndCoinListWithName(userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	@ApiOperation(value = "API to get wallet balance of a crypto/fiat coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "balance fetched successfully"),
			@ApiResponse(code = 205, message = "No wallet found for the user / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-balance-in-BTC")
	public Response<Map<String, BigDecimal>> getBalanceInUsd(
			@RequestParam(value = "coinName", required = false) String coinName,
			@RequestParam(required = false) String converterCoin, @RequestHeader Long userId) {
		if (userId != null && userId != 0) {
			return walletManagementService.getBalanceInUsd(coinName, converterCoin, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to get price of crypto/fiat coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "price fetched successfully"),
			@ApiResponse(code = 205, message = "No price found for the user / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/get-price")
	public BigDecimal getPrice(@RequestParam String coinName)
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		return hotColdManagementService.getPrice(coinName);

	}

	@ApiOperation(value = "API to get price of crypto/fiat coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "price fetched successfully"),
			@ApiResponse(code = 205, message = "No price found for the user / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/get-price-Usd")
	public BigDecimal getPriceUsd(@RequestParam String coinName)
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		return hotColdManagementService.getPriceUsd(coinName);

	}

	@GetMapping("/total-balance-for-user")
	public Response<Object> totalbalance(@RequestParam Long fkUserId) {
		return walletManagementService.totalbalance(fkUserId);
	}

	@ApiOperation(value = "API to get total blocked balance of user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "balance fetched successfully"),
			@ApiResponse(code = 205, message = "No balance found for the user / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/total-total-blocked-balance-for-user")
	public Response<Object> totalblockedbalance(@RequestParam Long fkUserId) {
		return walletManagementService.totalblockedbalance(fkUserId);
	}

	@ApiOperation(value = "API to get total balance subtracted from total blocked balance  of user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "balance subtracted successfully"),
			@ApiResponse(code = 205, message = "No balance found for the user / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/total-balance-subtracted-from-total-blocked-balance-for-user")
	public Response<Object> totalbalanceSubtractedFromtotalblockedbalance(@RequestParam Long fkUserId) {
		return walletManagementService.totalbalanceSubtractedFromtotalblockedbalance(fkUserId);
	}

	@ApiOperation(value = "API to get percentage  of user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "percentage fetched successfully"),
			@ApiResponse(code = 205, message = "No balance found for the user / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/percentage-for-user")
	public Response<Object> percentage(@RequestHeader Long userId) {
		return walletManagementService.percentage(userId);
	}

	@GetMapping(value = "/get-admin-address")
	public Response<Object> getAdminAddress(@RequestParam("coinName") String coinType) {
		return hotColdManagementService.getAdminAddress(coinType);
	}

	/**
	 * Gets the deposits network based.
	 *
	 * @param coinName the coin name
	 * @param userId   the user id
	 * @return the deposits
	 * @author Priyank Mishra
	 */
	@ApiOperation(value = "API to get deposit list of a ERC_BEP coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "deposit list fetched successfully"),
			@ApiResponse(code = 205, message = "Deposit Failed / No storage details found / Wallet not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-deposits-for-erc-bep")
	public Response<Map<String, Object>> getDepositsErcBep(@RequestParam("coinName") String coinName,
			@RequestHeader Long userId, @RequestParam Integer page, @RequestParam Integer pageSize,
			@RequestHeader String username, @RequestParam Network network) {
		if (userId != null && userId != 0 && coinName != null) {
			return hotColdManagementService.getDepositsErcBep(coinName, userId, page, pageSize, username, network,
					null);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the approve withdraw network based.
	 *
	 * @param approveWithdrawRequestDto the approve withdraw request dto
	 * @param userId                    the user id
	 * @return the approve withdraw
	 */
	@ApiOperation(value = "API to approve withdraw for Bep-Erc coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "withdraw successful"),
			@ApiResponse(code = 205, message = "withdraw token expired / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/approve-withdraw-Bep-Erc")
	public Response<String> getApproveWithdrawBepErc(
			@Valid @RequestBody ApproveWithdrawRequestDto approveWithdrawRequestDto, @RequestParam Network network) {
		if (approveWithdrawRequestDto != null) {
			return hotColdManagementService.getApproveWithdrawBepErc(approveWithdrawRequestDto, network);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	@ApiOperation(value = "API for referal Tier-1")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@GetMapping("/Referal-Tier-1")
	public Response<Object> getReferalTier1(@RequestHeader Long userId, @RequestParam String myReferralCode) {
		return walletManagementService.getReferalTier1(myReferralCode, userId);
	}

	@ApiOperation(value = "API to deduct amount from user wallet and transfer to admin wallet")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "deposit list fetched successfully"),
			@ApiResponse(code = 205, message = "Deposit Failed / No storage details found / Wallet not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/subscription-amount-deduct-new")
	public Response<Map<String, Object>> referalAmountDeductNew(@RequestParam("coinName") String coinName,
			@RequestHeader Long fkUserId, @RequestParam BigDecimal amount) throws Exception {
		if (fkUserId != null && fkUserId != 0 && coinName != null) {
			return hotColdManagementService.referalAmountDeductNew(coinName, fkUserId, amount);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@PostMapping("/Usd-Amount")
	public Response<Object> usdAmountConvert(@RequestHeader Long userId)
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		return hotColdManagementService.usdAmountConvert(userId);
	}

	@GetMapping("/Usd-Amount-List")
	public Response<Object> usdAmountList() {
		return hotColdManagementService.usdAmountList();
	}

	@PostMapping("/Usd-Amount-Total-ParticularUser")
	public Response<Object> usdAmountConvertParticularUser(@RequestBody List<String> coinName,
			@RequestHeader Long userId) {
		return hotColdManagementService.usdAmountConvertParticularUser(coinName, userId);
	}

	@PostMapping("/Deposit-INR")
	public Response<Object> depositInr(@RequestHeader Long userId, @RequestBody FiatDto fiatDto) {
		return walletManagementService.depositInr(userId, fiatDto);
	}

	@GetMapping("/Deposi-Inr-List")
	public Response<Object> getListDeposit(@RequestHeader Long userId, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate, @RequestParam(required = false) String email,
			@RequestParam(required = false) String name, @RequestParam(required = false) String utrNo,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) BigDecimal amount) {
		return walletManagementService.getListDeposit(userId, fromDate, toDate, email, name, utrNo, page, pageSize,
				amount);
	}

	@PostMapping("/Withdraw-INR")
	public Response<Object> getListWithdraw(@RequestHeader Long userId, @RequestBody WithdrawDtoInr withdrawDtoInr) {
		return walletManagementService.getListWithdraw(userId, withdrawDtoInr);
	}

	@GetMapping("/List-Withdraw-Inr")
	public Response<Object> getListWithdrawInr(@RequestHeader Long userId,
			@RequestParam(required = false) Long fromDate, @RequestParam(required = false) Long toDate,
			@RequestParam(required = false) String email, @RequestParam(required = false) String name,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) BigDecimal amount) {
		return walletManagementService.getListWithdrawInr(userId, fromDate, toDate, email, name, page, pageSize,
				amount);
	}

	@GetMapping("/upi-List-web")
	public Response<Object> listUpiUser() {
		return walletManagementService.listUpiUser();
	}

	@GetMapping("/bank-List-web")
	public Response<Object> bankListUser() {
		return walletManagementService.bankListUser();
	}

	@PostMapping("/add-Upi-details-User")
	public Response<Object> addUpiUser(@RequestBody UpiUserDto upiUserDto, @RequestHeader Long userId) {
		return walletManagementService.addUpiUser(upiUserDto, userId);
	}

	@GetMapping("/User-Upi-List")
	public Response<Object> userListUpi(@RequestHeader Long userId) {
		return walletManagementService.userListUpi(userId);
	}

	@GetMapping("/Fund-User")
	public Response<Object> fundUser(@RequestHeader Long userId) {
		return walletManagementService.fundUser(userId);
	}

	@GetMapping("/Fund-User-param")
	public Response<FundUserDto> fundUserParam(@RequestParam Long userId) {
		return walletManagementService.fundUserParam(userId);
	}

	@GetMapping(value = "/get-address-all")
	public Response<Wallet> getWalletAddressNew() {

		return walletManagementService.getWalletAddressNew();
	}

	@PostMapping("/Usd-Amount-wallet-data")
	public Response<Object> inrAmountConvert(@RequestHeader Long userId)
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		return hotColdManagementService.inrAmountConvert(userId);
	}

	@GetMapping("/Deposit-withdraw-Inr-Fund-data")
	public Response<Object> fullData(@RequestHeader Long userId) {
		return hotColdManagementService.fullData(userId);
	}

	@PostMapping("/update-storage-balance")
	public Response<Object> updateBalance(@RequestParam BigDecimal storageBalance, @RequestParam String coinName) {
		return cryptoStorageService.updateBalance(storageBalance, coinName);
	}

	@GetMapping(value = "/get-storage-details-coin-hot-New")
	public Response<Object> getStorageDetailsCoinHotNew(@RequestParam String coin,
			@RequestParam("storageType") String storageType) {

		return cryptoStorageService.getStorageDetailsCoinHot(coin, storageType);
	}

	@PostMapping("/update-INR")
	public Response<Object> updateINR(@RequestHeader Long userId) {

		return walletManagementService.updateINR(userId);
	}

	@PostMapping("/update-fund")
	public Response<Object> updateFund(@RequestParam Long userId, @RequestParam BigDecimal walletBalance) {

		return walletManagementService.updateFund(userId, walletBalance);
	}

	@PostMapping("/update-locked")
	public Response<Object> updateLocked(@RequestParam Long userId, @RequestParam BigDecimal walletBalance) {

		return walletManagementService.updateLocked(userId, walletBalance);
	}

	@GetMapping("/get-upi-data")
	public Response<Object> getUpidata(@RequestParam String upiId, @RequestParam Long userId) {
		return walletManagementService.getView(userId, upiId);
	}

	@GetMapping("/get-live-price")
	public Response<Object> getLivePrice(@RequestParam String coinName) {
		return hotColdManagementService.getLivePrice(coinName);
	}

	@PostMapping("/add-nominee-comission-fee")
	public Response<Object> nomineeComision(@RequestParam BigDecimal walletBalance) {
		return walletManagementService.nomineeComision(walletBalance);
	}

}
