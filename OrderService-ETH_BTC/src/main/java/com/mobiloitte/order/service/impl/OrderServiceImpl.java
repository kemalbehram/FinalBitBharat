package com.mobiloitte.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mobiloitte.order.config.AppConfig;
import com.mobiloitte.order.constants.MessageConstant;
import com.mobiloitte.order.dto.BlockBalanceDto;
import com.mobiloitte.order.dto.EmailDto;
import com.mobiloitte.order.dto.PlaceOrderDto;
import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.enums.OrderStatus;
import com.mobiloitte.order.enums.OrderType;
import com.mobiloitte.order.exception.FailedToBlockBalanceException;
import com.mobiloitte.order.exception.OrderNotFoundException;
import com.mobiloitte.order.exception.TradingException;
import com.mobiloitte.order.feign.NotificationClient;
import com.mobiloitte.order.feign.UserClient;
import com.mobiloitte.order.feign.WalletClient;
import com.mobiloitte.order.model.MarketData;
import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Response;
import com.mobiloitte.order.repo.OrderRepo;
import com.mobiloitte.order.service.OrderService;
import com.mobiloitte.order.service.TradeService;

@Service
public class OrderServiceImpl implements OrderService {
	private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private WalletClient walletClient;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private MarketData marketData;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	MarketData a = new MarketData();

	@Autowired
	private NotificationClient notificationClient;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Response<Order> placeOrder(PlaceOrderDto dto, Long userId, String username) {

		if (dto.getOrderType() == OrderType.MARKET && marketData.getLastPrice() == null) {
			return new Response<>(200, "Could not place order. The market seems empty at orderservice.");

		}

		else if (dto.getOrderType() == OrderType.STOP_LIMIT && marketData.getLastPrice() == null) {
			return new Response<>(200, "Could not place order. The market seems empty for stop.");

		} else if (dto.getOrderType() == OrderType.STOP_LIMIT
				&& marketData.getLastPrice().compareTo(dto.getStopPrice()) == 0) {
			return new Response<>(200, "StopPrice cannot be equal to market Price");
		}

		else if (dto.getOrderType() == OrderType.STOP_LIMIT && dto.getStopPrice().compareTo(BigDecimal.ZERO) == 0
				|| dto.getOrderType() == OrderType.STOP_LIMIT && dto.getLimitPrice().compareTo(BigDecimal.ZERO) == 0
				|| dto.getOrderType() == OrderType.STOP_LIMIT && dto.getQuantity().compareTo(BigDecimal.ZERO) == 0
				|| dto.getOrderType() == OrderType.LIMIT && dto.getLimitPrice().compareTo(BigDecimal.ZERO) == 0
				|| dto.getOrderType() == OrderType.LIMIT && dto.getQuantity().compareTo(BigDecimal.ZERO) == 0
				|| dto.getOrderType() == OrderType.MARKET && dto.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
			return new Response<>(700, "Please enter valid value");

		}
		Order order = getOrderFromPlaceOrderDto(dto, userId);
		LOGGER.debug("{}----------->", order);
		Response<Order> finalResponse;
		order.setBaseCoin(appConfig.getBaseCoin());
		order.setExeCoin(appConfig.getExeCoin());
		order = orderRepo.save(order);
		entityManager.flush();
		if (order.getOrderType() != OrderType.MARKET) {
			String coin = order.getOrderSide() == OrderSide.BUY ? appConfig.getBaseCoin() : appConfig.getExeCoin();
			BigDecimal b = order.getOrderSide() == OrderSide.BUY ? order.getQuantity().multiply(order.getLimitPrice())
					: order.getQuantity();
			BigDecimal balanceToBlock = b.setScale(8, RoundingMode.FLOOR);

			Response<Object> response = walletClient.verifyAndBlockBalance(
					new BlockBalanceDto(order.getUserId(), coin, balanceToBlock, order.getOrderId()));
			order.setBlockedBalance(balanceToBlock);
			if (response.getStatus() != 200) {
				order.setOrderStatus(OrderStatus.REJECTED);
				throw new FailedToBlockBalanceException(response.getMessage());
			}
			if (order.getOrderType() == OrderType.LIMIT) {
				order.setOrderStatus(OrderStatus.QUEUED);
				order.setActive(true);
			}
		}

		Response<Order> response = tradeService.processOrder(order);
		switch (response.getStatus()) {
		case 200:
			order.setOrderStatus(OrderStatus.COMPLETED);
			finalResponse = new Response<>(200, "Order has been executed completely", order);

			Map<String, String> orderExcutedMailMap = new HashMap<>();

			orderExcutedMailMap.put(MessageConstant.EMAIL_TOKEN, username);
			orderExcutedMailMap.put(MessageConstant.DATE_TOKEN, new Date().toString());
			orderExcutedMailMap.put(MessageConstant.ORDER_ID, String.valueOf(
					String.valueOf(new Date().getTime()) + String.valueOf(finalResponse.getData().getOrderId())));
			orderExcutedMailMap.put(MessageConstant.STATUS_TOKEN, " Completed");
			orderExcutedMailMap.put(MessageConstant.TRADINGPAIR_TOKEN, dto.getSymbol().replace("_", "/"));

			if (order.getOrderType().equals(OrderType.MARKET)) {
				orderExcutedMailMap.put(MessageConstant.PRICE, " Market");
			} else {
				orderExcutedMailMap.put(MessageConstant.PRICE, String.valueOf(dto.getLimitPrice()));
			}

			orderExcutedMailMap.put(MessageConstant.PRICEANDCONDITION_TOKEN,
					String.valueOf(finalResponse.getData().getAvgExecutionPrice()));
			orderExcutedMailMap.put(MessageConstant.VOLUME_TOKEN,
					String.valueOf(finalResponse.getData().getQuantity()));

			try {

				orderExcutedMailMap.put(MessageConstant.EXECUTED_ORDER_AMOUNT, String.valueOf(
						finalResponse.getData().getQuantity().subtract(finalResponse.getData().getCurrentQuantity())));

			} catch (Exception e) {
				orderExcutedMailMap.put(MessageConstant.EXECUTED_ORDER_AMOUNT, " 0");
			}

			if (dto.getStopPrice() == null) {
				orderExcutedMailMap.put(MessageConstant.STOP_PRICE, " NA");

			} else {
				orderExcutedMailMap.put(MessageConstant.STOP_PRICE, String.valueOf(dto.getStopPrice()));
			}
			orderExcutedMailMap.put(MessageConstant.MODE_OF_TRADING,
					WordUtils.capitalizeFully(String.valueOf(dto.getOrderSide()).toLowerCase()));

			try {
				orderExcutedMailMap.put(MessageConstant.AMOUNTFILL_TOKEN, String.valueOf(
						finalResponse.getData().getQuantity().subtract(finalResponse.getData().getCurrentQuantity())));

			} catch (ArithmeticException e) {
				orderExcutedMailMap.put(MessageConstant.AMOUNTFILL_TOKEN, " 0");
			}
			try {

				orderExcutedMailMap.put(MessageConstant.REMAINING_AMOUNT,
						String.valueOf(finalResponse.getData().getCurrentQuantity()));
			} catch (Exception e) {
				orderExcutedMailMap.put(MessageConstant.REMAINING_AMOUNT, " 0");
			}

			orderExcutedMailMap.put(MessageConstant.AMOUNT, String.valueOf(dto.getQuantity()));

			notificationClient
					.sendNotification(new EmailDto(userId, "trade_execution_status", username, orderExcutedMailMap));
			break;

		case 201:
			Map<String, String> orderplacedMailMap = new HashMap<>();

			finalResponse = new Response<>(200, "Order has been placed successfully", order);

			orderplacedMailMap.put(MessageConstant.EMAIL_TOKEN, username);
			orderplacedMailMap.put(MessageConstant.DATE_TOKEN, new Date().toString());

			orderplacedMailMap.put(MessageConstant.ORDER_ID, String.valueOf(
					String.valueOf(new Date().getTime()) + String.valueOf(finalResponse.getData().getOrderId())));
			orderplacedMailMap.put(MessageConstant.STATUS_TOKEN, " Created");

			orderplacedMailMap.put(MessageConstant.TRADINGPAIR_TOKEN, dto.getSymbol().replace("_", "/"));
			orderplacedMailMap.put(MessageConstant.PRICE, String.valueOf(dto.getLimitPrice()));
			orderplacedMailMap.put(MessageConstant.VOLUME_TOKEN, finalResponse.getData().getQuantity().toString());
			if (dto.getStopPrice() == null) {
				orderplacedMailMap.put(MessageConstant.STOP_PRICE, " NA");

			} else {
				orderplacedMailMap.put(MessageConstant.STOP_PRICE, String.valueOf(dto.getStopPrice()));
			}
			orderplacedMailMap.put(MessageConstant.MODE_OF_TRADING,
					WordUtils.capitalizeFully(String.valueOf(dto.getOrderSide()).toLowerCase()));
			orderplacedMailMap.put(MessageConstant.COIN_NAME_1, String.valueOf(finalResponse.getData().getBaseCoin()));
			orderplacedMailMap.put(MessageConstant.COIN_NAME_2, String.valueOf(finalResponse.getData().getExeCoin()));
			orderplacedMailMap.put(MessageConstant.AMOUNT, String.valueOf(dto.getQuantity()));

			EmailDto emailDto = new EmailDto(userId, "order_place", username, orderplacedMailMap);
			notificationClient.sendNotification(emailDto);
			break;
		case 202:
			finalResponse = new Response<>(200, "Order has been executed partially.", order);
			Map<String, String> orderExcutedPartiallyMailMap = new HashMap<>();

			orderExcutedPartiallyMailMap.put(MessageConstant.EMAIL_TOKEN, username);
			orderExcutedPartiallyMailMap.put(MessageConstant.DATE_TOKEN, new Date().toString());
			orderExcutedPartiallyMailMap.put(MessageConstant.ORDER_ID, String.valueOf(
					String.valueOf(new Date().getTime()) + String.valueOf(finalResponse.getData().getOrderId())));
			orderExcutedPartiallyMailMap.put(MessageConstant.STATUS_TOKEN, " Partially");
			orderExcutedPartiallyMailMap.put(MessageConstant.TRADINGPAIR_TOKEN, dto.getSymbol().replace("_", "/"));

			if (order.getOrderType().equals(OrderType.MARKET)) {
				orderExcutedPartiallyMailMap.put(MessageConstant.PRICE, " Market");

			} else {
				orderExcutedPartiallyMailMap.put(MessageConstant.PRICE, String.valueOf(dto.getLimitPrice()));
			}
			orderExcutedPartiallyMailMap.put(MessageConstant.PRICEANDCONDITION_TOKEN,
					String.valueOf(finalResponse.getData().getAvgExecutionPrice()));
			orderExcutedPartiallyMailMap.put(MessageConstant.VOLUME_TOKEN,
					String.valueOf(finalResponse.getData().getQuantity()));
			try {

				orderExcutedPartiallyMailMap.put(MessageConstant.EXECUTED_ORDER_AMOUNT, String.valueOf(
						finalResponse.getData().getQuantity().subtract(finalResponse.getData().getCurrentQuantity())));

			} catch (Exception e) {
				orderExcutedPartiallyMailMap.put(MessageConstant.EXECUTED_ORDER_AMOUNT, " 0");
			}
			if (dto.getStopPrice() == null) {
				orderExcutedPartiallyMailMap.put(MessageConstant.STOP_PRICE, " NA");

			} else {
				orderExcutedPartiallyMailMap.put(MessageConstant.STOP_PRICE, String.valueOf(dto.getStopPrice()));
			}
			orderExcutedPartiallyMailMap.put(MessageConstant.MODE_OF_TRADING,
					WordUtils.capitalizeFully(String.valueOf(dto.getOrderSide()).toLowerCase()));

			try {
				orderExcutedPartiallyMailMap.put(MessageConstant.REMAINING_AMOUNT,
						String.valueOf(finalResponse.getData().getCurrentQuantity()));

			} catch (ArithmeticException e) {
				orderExcutedPartiallyMailMap.put(MessageConstant.REMAINING_AMOUNT, " 0");
			}
			try {
				orderExcutedPartiallyMailMap.put(MessageConstant.AMOUNTFILL_TOKEN, String.valueOf(
						finalResponse.getData().getQuantity().subtract(finalResponse.getData().getCurrentQuantity())));

			} catch (ArithmeticException e) {
				orderExcutedPartiallyMailMap.put(MessageConstant.AMOUNTFILL_TOKEN, " 0");
			}
			orderExcutedPartiallyMailMap.put(MessageConstant.AMOUNT, String.valueOf(dto.getQuantity()));

			notificationClient.sendNotification(
					new EmailDto(userId, "trade_execution_status", username, orderExcutedPartiallyMailMap));

			break;

		case 205:
			finalResponse = new Response<>(200, "Could not place order. The market seems empty.", order);
			break;
		default:
			LOGGER.error("Unknown order response : {}", response);
			throw new TradingException(response.getMessage());
		}

		if (order.getOrderStatus().equals(OrderStatus.COMPLETED) || order.getOrderStatus().equals(OrderStatus.EXECUTED)
				|| order.getOrderStatus().equals(OrderStatus.PARTIALLY_COMPLETED)
				|| order.getOrderStatus().equals(OrderStatus.PARTIALLY_EXECUTED)) {
			tradeService.checkAndActiveStopLimitOrders(order);

		}
		return finalResponse;
	}

	private Order getOrderFromPlaceOrderDto(PlaceOrderDto order, Long userId) {
		Order o = modelMapper.map(order, Order.class);
		o.setCurrentQuantity(o.getQuantity());
		o.setUserId(userId);
		o.setInstrument(appConfig.getInstrument());
		o.setOrderStatus(OrderStatus.CREATED);
		if (order.getOrderType() != OrderType.STOP_LIMIT)
			o.setStopPrice(null);
		if (order.getOrderType() == OrderType.MARKET)
			o.setLimitPrice(o.getLimitPrice());
		return o;
	}

	@Override
	@Transactional
	public Response<Order> cancelOrder(Long orderId, Long userId) {
		Optional<Order> optionalOrder = orderRepo.findById(orderId);
		if (optionalOrder.isPresent()) {
			if (!optionalOrder.get().getUserId().equals(userId))
				throw new OrderNotFoundException(
						"Could not cancel order. Order does not belong to the logged in user.");
			Optional<Order> optional = tradeService.removeOrder(orderId, optionalOrder.get().getOrderSide());
			if (optional.isPresent())
				LOGGER.debug("Order remove from trade engine {}", optional.get());
			else
				LOGGER.debug("Order not found in trade engine queue. Id is {}", orderId);
			Order order = optionalOrder.get();
			order.setOrderStatus(OrderStatus.CANCELLED);
			orderRepo.save(order);
			String coin = order.getOrderSide() == OrderSide.BUY ? appConfig.getBaseCoin() : appConfig.getExeCoin();
			Response<Object> response = walletClient.returnBlockBalance(
					new BlockBalanceDto(order.getUserId(), coin, order.getBlockedBalance(), orderId));
			if (response.getStatus() != 200) {
				LOGGER.error(
						"Failed to return balance after cancelling order({}). Response received from wallet client {}",
						orderId, response);
				throw new TradingException("Failed to return balance after cancelling order.");
			}
			order.setBlockedBalance(BigDecimal.ZERO);
			order.setActive(false);
			LOGGER.debug("Order cancelled. Id is {}", orderId);
			return new Response<>(200, "Order Successfully removed", order);
		} else {
			throw new OrderNotFoundException("Could not cancel order. No order found with id: " + orderId);
		}
	}

	@Override
	public Response<List<Order>> getOrderBook(OrderSide orderSide) {
		List<Order> orderList;
		if (orderSide == null)
			orderList = orderRepo.findAllByActiveTrue();
		else
			orderList = orderRepo.findAllByOrderSideAndActiveTrue(orderSide);
		if (orderList.isEmpty())
			return new Response<>(200, "No Orders", orderList);
		return new Response<>(orderList);
	}

	@Override
	public Response<List<Order>> getActiveOrdersByUserId(Long userId) {
		List<Order> orderList;
		orderList = orderRepo.findAllOrderByUserIdAndOrderStatusIn(userId,
				Arrays.asList(OrderStatus.CREATED, OrderStatus.QUEUED, OrderStatus.PARTIALLY_EXECUTED));
		if (orderList.isEmpty()) {
			return new Response<>(201, "No Order of User", new ArrayList<>());
		}
		return new Response<>(orderList);
	}

	@Override
	public Response<List<Order>> syncOrderBook() {
		Response<List<Order>> orderBook = getOrderBook(null);
		tradeService.initializeOrderBook(orderBook.getData());
		return orderBook;
	}

	@Override
	public Future<List<Order>> triggerOrders(OrderSide orderSide, BigDecimal price) {
		return Executors.newSingleThreadExecutor().submit(() -> processStopOrders(orderSide, price));
	}

	@Transactional
	private List<Order> processStopOrders(OrderSide orderSide, BigDecimal price) {
		List<Order> orders;
		if (orderSide == OrderSide.SELL)
			orders = orderRepo.findByOrderTypeAndOrderStatusAndOrderSideAndStopPriceGreaterThanEqual(
					OrderType.STOP_LIMIT, OrderStatus.CREATED, OrderSide.SELL, price);
		else
			orders = orderRepo.findByOrderTypeAndOrderStatusAndOrderSideAndStopPriceLessThanEqual(OrderType.STOP_LIMIT,
					OrderStatus.CREATED, OrderSide.BUY, price);
		orders.forEach(order -> {
			order.setActive(true);
			try {
				tradeService.processOrder(order);
			} catch (Exception e) {
				LOGGER.catching(e);
			}
		});

		return orders;
	}

	@Override
	public Response<Object> change24() {

		Date d = new Date(new Date().getTime() - 28800000);
		String s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(d);
		String query = "select price FROM crypto_order.transaction_eth_btc where execution_time = ' " + s + " ' ";

		List<Map<String, Object>> currentQuantityRepo = jdbcTemplate.queryForList(query);
		BigDecimal currentQuantityRepoDecimal = BigDecimal
				.valueOf(Double.valueOf(String.valueOf(currentQuantityRepo.get(0).get("price"))));
		System.err.println(currentQuantityRepoDecimal);
		if (currentQuantityRepoDecimal.equals(null)) {

		}
		return new Response<Object>(currentQuantityRepoDecimal);
	}
}
