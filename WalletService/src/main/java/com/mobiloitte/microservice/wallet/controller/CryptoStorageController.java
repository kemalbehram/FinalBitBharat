package com.mobiloitte.microservice.wallet.controller;

import java.math.BigDecimal;
import java.util.List;
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
import com.mobiloitte.microservice.wallet.dto.ColdStorageRequestDto;
import com.mobiloitte.microservice.wallet.dto.CreateStorageWalletRequestDto;
import com.mobiloitte.microservice.wallet.dto.HotToColdTransferDto;
import com.mobiloitte.microservice.wallet.dto.StorageAddressRequestDto;
import com.mobiloitte.microservice.wallet.dto.StorageLimitDto;
import com.mobiloitte.microservice.wallet.entities.StorageDetail;
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
 * The Class CryptoStorageController.
 * 
 * @author Ankush Mohapatra
 */
@RestController
@RequestMapping(value = "/admin/hot-cold-storage")
@Api(value = "HOT/COLD Storage Management APIs")
public class CryptoStorageController implements OtherConstants {

	/** The crypto storage service. */
	@Autowired
	private CryptoStorageService cryptoStorageService;

	@Autowired
	private HotColdManagementService transferService;
	@Autowired
	private WalletManagementService walletManagementService;

	/**
	 * Gets the storage details.
	 *
	 * @param storageType the storage type
	 * @return the storage details
	 */
	@ApiOperation(value = "API to fetch all storage details by storage type")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "storage details fetched successfully"),
			@ApiResponse(code = 205, message = "No storage found, ask admin to update the storage details / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-storage-details")
	public Response<List<StorageDetail>> getStorageDetails(@RequestParam("storageType") String storageType) {
		if (storageType != null && (storageType.equals(STORAGE_HOT) || storageType.equals(STORAGE_COLD))) {
			return cryptoStorageService.getStorageDataByStorageType(storageType);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@GetMapping("/get-storage-details-list")
	public Response<Object> getStorageList() {
		return cryptoStorageService.getStorageList();
	}

	@ApiOperation(value = "API to fetch all storage details by storage type")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "storage details fetched successfully"),
			@ApiResponse(code = 205, message = "No storage found, ask admin to update the storage details / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-storage-details-coin-hot")
	public Response<Object> getStorageDetailsCoinHot(@RequestParam String coin,
			@RequestParam("storageType") String storageType) {
		if (storageType != null && (storageType.equals(STORAGE_HOT) || storageType.equals(STORAGE_COLD))) {
			return cryptoStorageService.getStorageDetailsCoinHot(coin, storageType);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to get all admin commission amount all user for all coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = " fetched successfully ammount"),
			@ApiResponse(code = 205, message = "No user found for the wallet / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-admin-commission-amount")
	public Response<Object> getAdminCommisssionAmount() {
		return walletManagementService.getAdminCommisssionAmount();

	}

	/**
	 * Request to generate address for storage.
	 *
	 * @param storageAddressRequestDto the storage address request dto
	 * @return the response
	 */
	@ApiOperation(value = "API to update storage wallet with new Address")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "storage address generated successfully"),
			@ApiResponse(code = 205, message = "Failed to fetch address from Live Blockchain API / no such storage found / updation failed / storage address is already present / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/get-new-storage-address")
	public Response<String> requestToGenerateAddressForStorage(
			@Valid @RequestBody StorageAddressRequestDto storageAddressRequestDto) {
		if (storageAddressRequestDto != null) {
			return cryptoStorageService.generateAddressForStorage(storageAddressRequestDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Request to create storage wallet.
	 *
	 * @param createStorageWalletRequestDto the create storage wallet request dto
	 * @return the response
	 */
	@ApiOperation(value = "API to create new storage wallet")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "storage wallet created successfully"),
			@ApiResponse(code = 205, message = "Insertion Failed / no such coin found / storage wallet is already present / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/create-storage-wallet")
	public Response<String> requestToCreateStorageWallet(
			@Valid @RequestBody CreateStorageWalletRequestDto createStorageWalletRequestDto) {
		if (createStorageWalletRequestDto != null) {
			return cryptoStorageService.createStorageWallet(createStorageWalletRequestDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Update storage wallet balance.
	 *
	 * @param createStorageWalletRequestDto the create storage wallet request dto
	 * @return the response
	 */
	@ApiOperation(value = "API to update storage wallet balance")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "balance updated successfully"),
			@ApiResponse(code = 205, message = "balance updation failed / failed to fetch balance / no such storage found, or no storage address found for coin / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/update-storage-wallet-balance")
	public Response<String> updateStorageWalletBalance(
			@Valid @RequestBody CreateStorageWalletRequestDto createStorageWalletRequestDto) {
		if (createStorageWalletRequestDto != null) {
			return cryptoStorageService.updateStorageBalance(createStorageWalletRequestDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Update cold storage wallet address.
	 *
	 * @param coldStorageRequestDto the cold storage request dto
	 * @return the response
	 */
	@ApiOperation(value = "API to update COLD storage wallet address")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Cold storage address is updated successfully"),
			@ApiResponse(code = 205, message = "updation failed / storage address is already present / no such storage found, please create its storage first / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/update-cold-storage-address")
	public Response<String> updateColdStorageWalletAddress(
			@Valid @RequestBody ColdStorageRequestDto coldStorageRequestDto) {
		if (coldStorageRequestDto != null) {
			return cryptoStorageService.updateColdStorageAddress(coldStorageRequestDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to update storage limit")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Storage limit updated successfully"),
			@ApiResponse(code = 205, message = "updation failed / no such storage found, please create its storage first / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/update-storage-limit")
	public Response<Boolean> updateStorageLimit(@Valid @RequestBody StorageLimitDto storageLimitDto) {
		if (storageLimitDto != null) {
			return cryptoStorageService.updateStorageLimit(storageLimitDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to manual Transfer HOT to COLD")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Transfer Success"),
			@ApiResponse(code = 205, message = "Transfer Failed"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/manual-transfer-hot-to-cold")
	public Response<String> manualTransferHotToCold(@Valid @RequestBody HotToColdTransferDto hotToColdTransferDto,
			@RequestHeader Long userId) {
		if (hotToColdTransferDto != null) {
			return transferService.transferFromHotToCold(hotToColdTransferDto, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@GetMapping("/get-storage-details-with-latestTime")
	public Response<Map<String, Object>> getStorageDetailsWithLatestTIme(@RequestParam String coinName) {
		return cryptoStorageService.getStorageDetailsWithLatestTIme(coinName);
	}

	@GetMapping("/find-cold-wallet-by-coin")
	public Response<List<StorageDetail>> findColdWalletByCoin(@RequestParam String coinName) {
		return cryptoStorageService.findColdWalletByCoin(coinName);

	}

}
