package com.mobiloitte.order.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.order.dto.CancelOrderDto;
import com.mobiloitte.order.dto.OrderUpdateDto;
import com.mobiloitte.order.dto.PlaceOrderDto;
import com.mobiloitte.order.dto.TradeHistoryDto;
import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.model.MarketData;
import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Response;
import com.mobiloitte.order.service.OrderService;
import com.mobiloitte.order.service.TransactionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private MarketData marketData;

	@ApiOperation(value = "Place a new Order", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Order Successfully placed") })
	@PostMapping("place-order")
	public Response<Order> placeOrder(@RequestBody @Valid PlaceOrderDto order, @RequestHeader Long userId,
			@RequestHeader String username) {
		return orderService.placeOrder(order, userId,username);
	}

	@PostMapping("cancel-order")
	public Response<Order> cancelOrder(@RequestHeader Long userId, @RequestBody CancelOrderDto dto) {
		return orderService.cancelOrder(dto.getOrderId(), userId);
	}

	/*
	 * @GetMapping("order-book") public Response<List<OrderUpdateDto>>
	 * getOrderBook(@RequestParam(required = false) OrderSide orderSide,
	 * 
	 * @RequestParam String symbol) { Response<List<Order>> response =
	 * orderService.getOrderBook(orderSide); List<OrderUpdateDto> orderBook =
	 * response.getData().parallelStream() .map(o -> new OrderUpdateDto(o.get,
	 * o.getCurrentQuantity(), o.getOrderSide(), "DB",
	 * o.getOrderStatus(),o.getOrderId(),o.getUserId(),o.getOrderType(),o.
	 * getInstrument(),o.getCreationTime())) .collect(Collectors.toList()); return
	 * new Response<>(orderBook); }
	 */
	/*
	 * @GetMapping("order-book") public Response<List<OrderUpdateDto>>
	 * getOrderBook(@RequestParam(required = false) OrderSide orderSide,
	 * 
	 * @RequestParam String symbol) { Response<List<Order>> response =
	 * orderService.getOrderBook(orderSide); List<OrderUpdateDto> orderBook =
	 * response.getData().parallelStream() .map(o -> new
	 * OrderUpdateDto(o.getAvgExecutionPrice(), o.getQuantity(), o.getOrderSide(),
	 * "DB",o.getOrderStatus(), o.getOrderId(), o.getUserId(), o.getOrderType(),
	 * o.getInstrument(), o.getCreationTime(), o.getLimitPrice(),
	 * o.getCurrentQuantity())) .collect(Collectors.toList()); return new
	 * Response<>(orderBook); }
	 */

	@GetMapping("order-book")
	public Response<List<OrderUpdateDto>> getOrderBook(@RequestParam(required = false) OrderSide orderSide,
			@RequestParam String symbol) {
		Response<List<Order>> response = orderService.getOrderBook(orderSide);
		List<OrderUpdateDto> orderBook = response.getData().parallelStream()
				.map(o -> new OrderUpdateDto(o.getLimitPrice(), o.getCurrentQuantity(), o.getOrderSide()))
				.collect(Collectors.toList());
		return new Response<>(orderBook);
	}

	@GetMapping("trade-history")
	public Response<List<TradeHistoryDto>> getTradeHistory(@RequestParam(required = false) Long size,
			@RequestParam String symbol) {
		return transactionService.getTradeHistory(size);
	}

	@GetMapping("market-data")
	public Response<Object> getMarketData(@RequestParam String symbol) {
		return new Response<>(marketData.extractToMap());
	}

	@GetMapping("my-active-orders")
	public Response<List<Order>> getActiveOrders(@RequestHeader Long userId, @RequestParam String symbol) {
		return orderService.getActiveOrdersByUserId(userId);
	}

	@GetMapping("my-order-history")
	public Response<List<Order>> getOrderList(@RequestHeader Long userId, @RequestParam String symbol) {
		return transactionService.getOrderHistoryByUserId(userId);
	}

	@GetMapping("/sync-order-book")
	public Response<List<Order>> syncOrderBook(@RequestParam String symbol) {
		return orderService.syncOrderBook();
	}

	@GetMapping("get-24Change")
	public Response<Object> change() {
		return orderService.change24();
	}
}
