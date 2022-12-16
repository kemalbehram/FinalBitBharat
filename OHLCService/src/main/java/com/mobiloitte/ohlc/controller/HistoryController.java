package com.mobiloitte.ohlc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.ohlc.dto.SearchAndFilterDto;
import com.mobiloitte.ohlc.model.Response;
import com.mobiloitte.ohlc.service.OrderService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController

public class HistoryController {

	@Autowired
	private OrderService orderService;

	@ApiOperation(value = "Api to get user Active order history.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 201, message = "Data not found") })
	@PostMapping(value = "get-active-order-history")
	public Response<Object> getActiveOrderHistoryUsingFilter(@RequestHeader("userId") Long userId,
			@RequestBody SearchAndFilterDto searchAndFilterDto) {
		return orderService.getActiveOrderHistory(userId, searchAndFilterDto);
	}

	@ApiOperation(value = "Api to get user Trade history.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 201, message = "Data not found") })
	@PostMapping(value = "get-trade-history")
	public Response<Object> getTrade(@RequestHeader("userId") Long userId,
			@RequestBody SearchAndFilterDto searchAndFilterDto) {
		return orderService.getTrade( searchAndFilterDto);
	}

	@ApiOperation(value = "Api to get user Active order history.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 201, message = "Data not found") })
	@PostMapping(value = "get-order-history")
	public Response<Object> getOrderHistoryUsingFilter(@RequestHeader("userId") Long userId,
			@RequestBody SearchAndFilterDto searchAndFilterDto) {
		return orderService.getOrderHistory(userId, searchAndFilterDto);
	}

	@PostMapping(value = "get-trade-history-allUser")
	public Response<Object> getTradeHistoryAllUser(@RequestBody SearchAndFilterDto searchAndFilterDto) {

		return orderService.getTradeHistoryAllUser(searchAndFilterDto);

	}
}
