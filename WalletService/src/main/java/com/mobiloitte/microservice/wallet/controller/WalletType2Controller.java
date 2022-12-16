package com.mobiloitte.microservice.wallet.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.dto.WithdrawRequestDto;
import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.WalletManagementForCoinType2Service;
import com.mobiloitte.microservice.wallet.service.WithdrawType2;
import com.mobiloitte.microservice.wallet.service.WithdrawType3;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class WalletType2Controller.
 * 
 * @author Ankush Mohapatra
 */
@RestController
@RequestMapping(value = "/wallet-type2")
@Api(value = "Wallet Management APIs for CryptoCoins Type 2")
public class WalletType2Controller implements WalletConstants, OtherConstants {

	/** The wallet management for coin type 2 service. */
	@Autowired
	private WalletManagementForCoinType2Service walletManagementForCoinType2Service;

	@Autowired
	private WithdrawType2 withdrawType2;

	@Autowired
	private WithdrawType3 withdrawType3;

	/**
	 * Gets the address.
	 *
	 * @param coinName the coin name
	 * @param userId   the user id
	 * @return the address
	 */
	@ApiOperation(value = "API to get wallet address of a crypto coins of type 2")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "address found successfully"),
			@ApiResponse(code = 205, message = "Failed to fetched address from Live Blockchain API / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-address")
	public Response<Wallet> getAddress(@RequestParam("coinName") String coinName, @RequestHeader Long userId) {
		if (userId != null && userId != 0 && coinName != null) {
			return walletManagementForCoinType2Service.getWalletAddress(coinName, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
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
			@ApiResponse(code = 205, message = "No deposits found / No storage details found / Wallet not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-deposits")
	public Response<Map<String, Object>> getDeposits(@RequestParam("coinName") String coinName,
			@RequestHeader Long userId, @RequestParam Integer page, @RequestParam Integer pageSize,
			@RequestHeader String username) {
		if (userId != null && userId != 0 && coinName != null) {
			if (coinName.equalsIgnoreCase(XRP) || coinName.equalsIgnoreCase(XLM)) {
				return walletManagementForCoinType2Service.depositListForXRPXLM(coinName, userId, page, pageSize,
						username);
			} else if (coinName.equalsIgnoreCase(ETH) || coinName.equalsIgnoreCase(USDC)
					|| coinName.equalsIgnoreCase(OMG)) {
				return walletManagementForCoinType2Service.depositListForERC20Tokens(coinName, userId, page, pageSize,
						username);
			} else if (coinName.equalsIgnoreCase(USDT)) {
				return walletManagementForCoinType2Service.depositListForUSDT(coinName, userId, page, pageSize,
						username);
			} else {
				throw new BadRequestException(BAD_REQUEST_EXCEPTION);
			}
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the async transfer.
	 *
	 * @param coinName the coin name
	 * @param userId   the user id
	 * @return the async transfer
	 */
	@ApiOperation(value = "API to async transfer funds of crypto coins of type 2")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Deposit Successful"),
			@ApiResponse(code = 205, message = "Deposit Failed / No storage found / wallet not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-async-transfer")
	public Response<String> getAsyncTransfer(@RequestParam("coinName") String coinName, @RequestHeader Long userId) {
		if (userId != null && userId != 0 && coinName.equals(ETH)) {
			return walletManagementForCoinType2Service.asyncTransfer(coinName, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to withdraw ERC20 tokens")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Withdraw request psoted successfully"),
			@ApiResponse(code = 205, message = "withdraw Failed / Insufficient wallet balance / No storage details found / Wallet not found / No such coin found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/withdraw")
	public Response<String> getWithdraw(@Valid @RequestBody WithdrawRequestDto withdrawRequest,
			@RequestHeader Long userId) {
		if (userId != null && userId != 0 && withdrawRequest != null && withdrawRequest.getCoinName().equals(OMG)) {
			return withdrawType2.withdrawBalance(withdrawRequest, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	@ApiOperation(value = "API to withdraw USDT omni layer")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Withdraw request psoted successfully"),
			@ApiResponse(code = 205, message = "withdraw Failed / Insufficient wallet balance / No storage details found / Wallet not found / No such coin found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/withdraw-type3")
	public Response<String> getWithdraw3(@Valid @RequestBody WithdrawRequestDto withdrawRequest,
			@RequestHeader Long userId) {
		if (userId != null && userId != 0 && withdrawRequest != null && withdrawRequest.getCoinName().equals(USDT)) {
			return withdrawType3.withdrawBalance(withdrawRequest, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

}
