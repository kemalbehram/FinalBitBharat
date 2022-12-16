package com.mobiloitte.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

import com.mobiloitte.order.dto.PlaceOrderDto;
import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Response;

public interface OrderService {

	Response<Order> cancelOrder(Long orderId, Long userId);

	Response<List<Order>> getActiveOrdersByUserId(Long userId);

	Response<List<Order>> getOrderBook(OrderSide orderSide);

	Response<List<Order>> syncOrderBook();

	Response<Order> placeOrder(PlaceOrderDto order, Long userId, String username);

	Future<List<Order>> triggerOrders(OrderSide orderSide, BigDecimal price);

	Response<Object> change24();

}
