package com.mobiloitte.ohlc.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.ohlc.model.MarketData;
import com.mobiloitte.ohlc.model.Response;

@FeignClient("gateway-server")
public interface OrderClient {

	@GetMapping(value = "/order/market-data")
	public Response<MarketData> getMarketData(@RequestParam("symbol") String symbol);

	@GetMapping(value = "/order/my-order-history")
	public Response<List<Object>> getOrderHistory(@RequestParam("symbol") String symbol,
			@RequestHeader(name = "userId") Long userId);

	@GetMapping(value = "/order/my-active-orders")
	public Response<List<Object>> getActiveOrders(@RequestParam("symbol") String symbol,
			@RequestHeader(name = "userId") Long userId);

}
