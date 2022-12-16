package com.mobiloitte.usermanagement.feign;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.usermanagement.dto.FundUserDto;
import com.mobiloitte.usermanagement.dto.StorageDetailDto;
import com.mobiloitte.usermanagement.dto.WalletDto;
import com.mobiloitte.usermanagement.model.Response;

@FeignClient("${exchange.application.wallet-service}")
public interface WalletClient {

	@GetMapping(value = "/wallet/create-wallet")
	public Response<String> createWallet(@RequestParam("id") Long userId, @RequestParam String randomId);

	@GetMapping("/admin/get-last-transaction-date")
	public Response<Date> getUserLastTransactionDate(@RequestParam Long userId);

	@GetMapping(value = "/get-storage-details-coin-hot")
	public Response<String> getStorageDetailsCoinHot(@RequestParam String coin,
			@RequestParam("storageType") String storageType);

	@GetMapping(value = "/wallet/get-storage-details-coin-hot-New")
	public Response<StorageDetailDto> getStorageDetailsCoinHotNew(@RequestParam String coin,
			@RequestParam("storageType") String storageType);

	@PostMapping(value = "/wallet/update-wallet-new")
	public Response<String> updateWalletNew(@RequestParam BigDecimal walletBalance, @RequestParam Long userId,
			@RequestParam String coinName);

	@PostMapping("/wallet/update-storage-balance")
	public Response<Object> updateBalance(@RequestParam BigDecimal storageBalance, @RequestParam String coinName);

	@PostMapping("/wallet/update-fund")
	public Response<FundUserDto> updateFund(@RequestParam Long userId, @RequestParam BigDecimal walletBalance);

	@GetMapping("/wallet/Fund-User-param")
	public Response<FundUserDto> fundUserParam(@RequestParam Long userId);

	@PostMapping("/wallet/add-nominee-comission-fee")
	public Response<Object> nomineeComision(@RequestParam BigDecimal walletBalance);
	
	@PostMapping("/wallet/update-locked")
	public Response<FundUserDto> updateLocked(@RequestParam Long userId,@RequestParam BigDecimal walletBalance);
}
