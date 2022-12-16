package com.mobiloitte.p2p.content.feign;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.p2p.content.dto.GetBalanceResponseDto;
import com.mobiloitte.p2p.content.dto.MarketPriceDto;
import com.mobiloitte.p2p.content.dto.P2pHistoryDto;
import com.mobiloitte.p2p.content.dto.UpiDto;
import com.mobiloitte.p2p.content.model.Response;

@FeignClient("${exchange.application.wallet-service}")
public interface WalletClient {

	@GetMapping(value = "/wallet/get-balance")
	public Response<GetBalanceResponseDto> getBalance(@RequestParam("coinName") String coinName,
			@RequestHeader Long userId);

	@PostMapping(value = "/wallet/update-wallet")
	public Response<String> updateWallet(@RequestParam BigDecimal walletBalance, @RequestParam Long userId,
			@RequestParam String coinName);

	@GetMapping(value = "/coin/get-market-price")
	public Response<MarketPriceDto> getMarketPrice(@RequestParam("coinName") String coinName);

	@GetMapping(value = "/admin/fee-management/set-trade-fee")
	public Response<MarketPriceDto> setUpdateTradeFee(@RequestParam("coinName") String coinName,
			@RequestParam BigDecimal tradeFee);

	@GetMapping(value = "/admin/hot-cold-storage/get-storage-details-coin-hot-for-p2p")
	public Response<Object> getStorageDetailsCoinHotForP2p(@RequestParam String coin,
			@RequestParam("storageType") String storageType, @RequestParam BigDecimal fees);

	@GetMapping("/wallet/get-upi-data")
	public Response<UpiDto> getUpidata(@RequestParam String upiId, @RequestParam Long userId);

	@PostMapping(value = "/wallet/update-blocked-balance")
	public Response<String> updateBlocked(@RequestParam BigDecimal blockedBalance, @RequestParam Long userId,
			@RequestParam String coinName);
	
	@PostMapping("/wallet/p2p-data")
	public Response<String> updateP2pData(@RequestBody P2pHistoryDto p2pHistoryDto ,@RequestParam Long userId);

}
