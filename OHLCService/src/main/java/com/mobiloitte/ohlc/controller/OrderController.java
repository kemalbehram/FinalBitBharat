package com.mobiloitte.ohlc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.ohlc.dto.SearchAndFilterDto;
import com.mobiloitte.ohlc.model.MarketData;
import com.mobiloitte.ohlc.model.Response;
import com.mobiloitte.ohlc.service.OrderService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@ApiOperation(value = "Api to get order history.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 201, message = "Data not found") })
	@GetMapping(value = "order-history")
	public Response<Object> getOrderHistory(@RequestParam("userId") Long userId) {
		return orderService.getOrderHistoryByUserId(userId);
	}

	@ApiOperation(value = "Api to get market data.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 201, message = "Data not found") })
	@GetMapping(value = "market-data")
	public Response<List<MarketData>> getMarketData(@RequestParam(required = false) String baseCoin,
			@RequestParam(required = false) String exeCoin) {
		return orderService.getMarketData(baseCoin, exeCoin);
	}

	@ApiOperation(value = "Api to get active orders of a user.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 201, message = "Data not found") })
	@GetMapping(value = "my-active-orders")
	public Response<List<Object>> getActiveOrders(@RequestParam(required = false) String baseCoin,
			@RequestParam(required = false) String exeCoin, @RequestHeader Long userId) {
		return orderService.getActiveOrders(baseCoin, exeCoin, userId);
	}

	@ApiOperation(value = "Api to get user order history.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 201, message = "Data not found") })
	@GetMapping(value = "my-order-history")
	public Response<List<Object>> getOrderHistory(@RequestParam(required = false) String baseCoin,
			@RequestParam(required = false) String exeCoin, @RequestHeader Long userId) {
		return orderService.getOrderHistory(baseCoin, exeCoin, userId);
	}

	@PostMapping(value = "get-trade-history-using-filter")
	public Response<Object> getTrade(@RequestHeader("userId") Long userId,
			@RequestBody SearchAndFilterDto searchAndFilterDto) {
		return orderService.getTrade( searchAndFilterDto);
	}
}
