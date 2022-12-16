package com.mobiloitte.order.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.enums.OrderStatus;
import com.mobiloitte.order.enums.OrderType;
import com.mobiloitte.order.model.Order;

@Repository
public interface OrderRepo extends PagingAndSortingRepository<Order, Long> {
	List<Order> findAllOrderByOrderSide(OrderSide orderSide);

	List<Order> findAllByOrderStatusIn(List<OrderStatus> asList);

	List<Order> findAllByOrderSideAndOrderStatusIn(OrderSide orderSide, List<OrderStatus> asList);

	List<Order> findAllByUserIdAndOrderStatus(Long userId, OrderStatus completed);

	List<Order> findAllOrderByUserIdAndOrderStatusIn(Long userId, List<OrderStatus> asList);

	List<Order> findAllByActiveTrue();

	List<Order> findAllByOrderSideAndActiveTrue(OrderSide orderSide);

	List<Order> findAllOrderByUserIdAndActiveTrue(Long userId);

	List<Order> findAllOrderByUserIdAndActiveFalse(Long userId);

	List<Order> findAllByUserId(Long userId);

	List<Order> findAllByUserIdAndOrderStatusNot(Long userId, OrderStatus status);

	List<Order> findAllByUserIdAndOrderStatusNotIn(Long userId, List<OrderStatus> status);

	List<Order> findByOrderTypeAndOrderStatusAndOrderSideAndStopPriceGreaterThanEqual(OrderType orderType,
			OrderStatus orderStatus, OrderSide orderSide, BigDecimal price);

	List<Order> findByOrderTypeAndOrderStatusAndOrderSideAndStopPriceLessThanEqual(OrderType orderType,
			OrderStatus orderStatus, OrderSide orderSide, BigDecimal price);

	List<Order> findAllOrderByUserIdAndActiveFalseAndOrderStatusNotIn(Long userId, List<OrderStatus> asList);

	List<Order> findAllByOrderTypeAndActiveFalse(OrderType stopLimit);

	List<Order> findAllByOrderTypeAndActiveFalseAndOrderStatusNotIn(OrderType stopLimit, List<OrderStatus> asList);

	List<Order> findByOrderIdAndUserId(Long orderId, Long userId);
}
