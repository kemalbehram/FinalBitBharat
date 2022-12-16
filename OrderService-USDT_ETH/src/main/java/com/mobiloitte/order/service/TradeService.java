package com.mobiloitte.order.service;

import java.util.List;
import java.util.Optional;

import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Response;

/**
 * @author Rahil Husain
 *
 */
public interface TradeService {
	Response<Order> processOrder(Order order);

	Optional<Order> removeOrder(Long orderId, OrderSide orderSide);

	void initializeOrderBook(List<Order> orders);

	void resetOrderBook();

	void checkAndActiveStopLimitOrders(Order order);

}
