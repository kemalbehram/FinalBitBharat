package com.mobiloitte.ohlc.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.ohlc.model.CoinPair;
import com.mobiloitte.ohlc.model.Response;

@FeignClient("${exchange.application.wallet-service}")
public interface WalletClient {

	@GetMapping(value = "/coin/get-coin-list")
	public Response<List<Object>> getCoinList();

	@GetMapping(value = "/coin/get-coin-details")
	public Response<Object> getParticularCoinDetails(@RequestParam("coinName") String coinName);

	@GetMapping(value = "/coin/get-symbol-list")
	public Response<List<Object>> getCoinPairSymbolList(
			@RequestParam(value = "exeCoin", required = false) String exeCoin);

	@GetMapping(value = "/coin/get-symbol-list")
	public Response<List<CoinPair>> getAllCoinPairSymbols();

	@GetMapping(value = "/coin/get-symbol-list-for-ohlc")
	public Response<List<Object>> getCoinPairSymbolListOhlc(
			@RequestParam(value = "exeCoin", required = false) String exeCoin);
}
