package com.mobiloitte.socket.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.socket.response.Response;

@FeignClient("${exchange.application.gateway-server}")
public interface OrderClient {

	@GetMapping("order/order-book")
	public Response<Object> getOrderBook(@RequestParam String symbol);

	@GetMapping("order/trade-history")
	public Response<Object> getTradeHistory(@RequestParam String symbol);

	@GetMapping("order/market-data")
	public Response<Object> getMarketData(@RequestParam String symbol);
}