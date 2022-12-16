package com.mobiloitte.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Response;

@FeignClient("liquidity-service")
public interface LiquidityClient {


@PostMapping("/hitbtc-place-order")
public Response<Order> hitbtcPlaceOrder(@RequestBody Order order);



@PostMapping("/binance-place-order")
public Response<Order> binacePlaceOrder(@RequestBody Order order);


@PostMapping("/poloniex-place-order")
public Response<Order> poloniexPlaceOrder(@RequestBody Order order);


}