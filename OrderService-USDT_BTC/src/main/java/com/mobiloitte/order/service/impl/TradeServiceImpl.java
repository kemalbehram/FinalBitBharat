package com.mobiloitte.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.order.config.AppConfig;
import com.mobiloitte.order.dto.BlockBalanceDto;
import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.enums.OrderStatus;
import com.mobiloitte.order.enums.OrderType;
import com.mobiloitte.order.exception.FailedToBlockBalanceException;
import com.mobiloitte.order.feign.LiquidityClient;
import com.mobiloitte.order.feign.WalletClient;
import com.mobiloitte.order.model.LastPrice;
import com.mobiloitte.order.model.MarketData;
import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.PercentageChange;
import com.mobiloitte.order.model.Response;
import com.mobiloitte.order.model.Transaction;
import com.mobiloitte.order.repo.ExeLastPriceRepo;
import com.mobiloitte.order.repo.OrderRepo;
import com.mobiloitte.order.repo.PercentageChangeRepo;
import com.mobiloitte.order.service.KafkaService;
import com.mobiloitte.order.service.TradeService;
import com.mobiloitte.order.service.TransactionService;

/** @author Jha Shubham */
@Service
public class TradeServiceImpl implements TradeService {
	private static final Logger LOGGER = LogManager.getLogger(TradeServiceImpl.class);

	public int compareBuy(Order s1, Order s2) {
		if (s1.getLimitPrice().compareTo(s2.getLimitPrice()) != 0)
			return s1.getLimitPrice().compareTo(s2.getLimitPrice());
		else {
			Long t2 = s1.getCreationTime().getTime();
			Long t1 = s2.getCreationTime().getTime();
			return t1.compareTo(t2);
		}
	}

	public int compareSell(Order s1, Order s2) {
		if (s1.getLimitPrice().compareTo(s2.getLimitPrice()) != 0)
			return s1.getLimitPrice().compareTo(s2.getLimitPrice());
		else {
			Long t2 = s2.getCreationTime().getTime();
			Long t1 = s1.getCreationTime().getTime();
			return t1.compareTo(t2);
		}
	}

	private PriorityQueue<Order> sellOrderQueue = new PriorityQueue<>((a, b) -> compareSell(a, b));
	private PriorityQueue<Order> buyOrderQueue = new PriorityQueue<>((a, b) -> compareBuy(b, a));
	List<Order> remainOrder = new ArrayList<>();

	@Autowired
	private AppConfig appConfig;
	@Autowired
	private PercentageChangeRepo percentageChangeRepo;
	@Autowired
	private WalletClient walletClient;
	@Autowired
	private ExeLastPriceRepo exeLastPriceRepo;

	@Autowired
	private LiquidityClient liquidityClient;
	@Autowired
	private TransactionService transactionService;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private MarketData marketData;

	@Autowired
	private KafkaService kafkaService;
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private TradeService tradeService;

	@Override
	public Response<Order> processOrder(Order order) {
		Response<Order> finalResponse;
		LOGGER.debug("Order received at trading engine: {}", order);
		PriorityQueue<? extends Order> orderQueue = order.getOrderSide() == OrderSide.BUY ? sellOrderQueue
				: buyOrderQueue;
		synchronized (orderQueue) {
			switch (order.getOrderType()) {
			case LIMIT:
				order = processLimitOrder(order, orderQueue);
				break;
			case MARKET:
				if (orderQueue.isEmpty()) {
					if (appConfig.getLiquidityEnabled()) {
						executeWithLiquidity(order);
						if (!(order.getOrderStatus().equals(OrderStatus.EXECUTED))) {
							return new Response<>(205, "Order book is empty", order);
						}
					} else {
						return new Response<>(205, "Order book is empty", order);

					}
				} else {
					order = processMarketOrder(order, orderQueue);
					if (order.getOrderStatus() != OrderStatus.PARTIALLY_EXECUTED
							&& order.getOrderStatus() != OrderStatus.EXECUTED) {
						if (appConfig.getLiquidityEnabled() && executeWithLiquidity(order))
							return new Response<>(order);
						else
							return new Response<>(205, "Order book is empty", order);
					}
				}
				break;
			case STOP_LIMIT:
				processStopLimitOrder(order, orderQueue);
				break;
			default:
				throw new NotImplementedException("Order type Not Implemented");
			}
		}
		if (order.getOrderStatus() == OrderStatus.PARTIALLY_EXECUTED) {
			if (order.getOrderStatus() == OrderStatus.PARTIALLY_EXECUTED && order.getOrderType() == OrderType.MARKET) {
				finalResponse = new Response<>(200, "Order fully executed", order);

			} else {
				finalResponse = new Response<>(202, "Order partially executed", order);
			}
		} else if (order.getOrderStatus() == OrderStatus.EXECUTED) {
			finalResponse = new Response<>(200, "Order fully executed", order);

		} else {
			finalResponse = new Response<>(201, "Order placed", order);

		}
		if (!remainOrder.isEmpty()) {
			remainOrder.parallelStream().forEachOrdered(p -> addToOrderBookWithoutKafka(p));
			remainOrder.clear();
		}

		if (order.getTransactions() != null && !order.getTransactions().isEmpty())
			transactionService.publishTransactions(order);

		else
			LOGGER.debug("No transactions occured to publish");
		printOrderBook();
		updateBestOrders();
		return finalResponse;
	}

	private boolean executeWithLiquidity(Order order) {
		if (Boolean.TRUE.equals(appConfig.isBinanceEnable())) {

			liquidityBinance(order);
			if (Boolean.TRUE.equals(liquidityBinance(order))) {
				return false;
			}
		}
		if (Boolean.TRUE.equals(appConfig.isHitBtcEnable())) {
			liquidityHitBtc(order);
			if (Boolean.TRUE.equals(liquidityHitBtc(order))) {
				return false;

			}
		}
		if (Boolean.TRUE.equals(appConfig.isPoloniexEnable())) {
			liquidityPolonix(order);
			if (Boolean.TRUE.equals(liquidityPolonix(order))) {
				return false;
			}
		}
		return false;

	}

	public Boolean liquidityBinance(Order order) {
		Response<Order> response = liquidityClient.binacePlaceOrder(order);
		if (response.getStatus() == 200) {
			Order orderExecuted = response.getData();
			if (orderExecuted.getOrderStatus() == OrderStatus.EXECUTED) {
				LOGGER.error("Liquidity order exe : {}", orderExecuted);

				order.setLiquiditystatus(Boolean.TRUE);
				addTransaction(order, new Transaction(null, order.getCurrentQuantity(),
						orderExecuted.getAvgExecutionPrice(), null, null, order.getOrderSide()));
				deductQuantity(order, order.getCurrentQuantity());
				order.setOrderStatus(OrderStatus.EXECUTED);
				order.setActive(false);
				return true;
			} else if (orderExecuted.getOrderStatus() == OrderStatus.PARTIALLY_EXECUTED) {
				order.setLiquiditystatus(Boolean.TRUE);

				addTransaction(order, new Transaction(null, order.getCurrentQuantity(),
						orderExecuted.getAvgExecutionPrice(), null, null, order.getOrderSide()));
				deductQuantity(order, order.getCurrentQuantity());
				order.setOrderStatus(OrderStatus.PARTIALLY_EXECUTED);
				return true;
			}
		}
		LOGGER.error("Liquidity false");

		return false;

	}

	public Boolean liquidityHitBtc(Order order) {
		Response<Order> response = liquidityClient.hitbtcPlaceOrder(order);
		if (response.getStatus() == 200) {
			Order orderExecuted = response.getData();
			if (orderExecuted.getOrderStatus() == OrderStatus.EXECUTED) {
				addTransaction(order, new Transaction(null, order.getCurrentQuantity(),
						orderExecuted.getAvgExecutionPrice(), null, null, order.getOrderSide()));
				deductQuantity(order, order.getCurrentQuantity());
				order.setOrderStatus(OrderStatus.EXECUTED);
				return true;
			} else if (orderExecuted.getOrderStatus() == OrderStatus.PARTIALLY_EXECUTED) {
				addTransaction(order, new Transaction(null, order.getCurrentQuantity(),
						orderExecuted.getAvgExecutionPrice(), null, null, order.getOrderSide()));
				deductQuantity(order, order.getCurrentQuantity());
				order.setOrderStatus(OrderStatus.PARTIALLY_EXECUTED);
				return true;
			}
		}
		return false;
	}

	public Boolean liquidityPolonix(Order order) {

		Response<Order> response = liquidityClient.poloniexPlaceOrder(order);
		if (response.getStatus() == 200) {
			Order orderExecuted = response.getData();
			if (orderExecuted.getOrderStatus() == OrderStatus.EXECUTED) {
				addTransaction(order, new Transaction(null, order.getCurrentQuantity(),
						orderExecuted.getAvgExecutionPrice(), null, null, order.getOrderSide()));
				deductQuantity(order, order.getCurrentQuantity());
				order.setOrderStatus(OrderStatus.EXECUTED);
				return true;
			}
		}

		return false;

	}

	private Order addToOrderBookWithoutKafka(Order order) {
		if (appConfig.getLiquidityEnabled()) {
			if (executeWithLiquidity(order))
				return order;
		}
		if (order.getOrderSide() == OrderSide.BUY)
			buyOrderQueue.add(order);
		else
			sellOrderQueue.add(order);
		return order;
	}

	private void updateBestOrders() {
		Order bestBidOrder = buyOrderQueue.peek();
		if (bestBidOrder != null)
			marketData.setBestBid(bestBidOrder.getLimitPrice());
		else
			marketData.setBestBid(null);
		Order bestOfferOrder = sellOrderQueue.peek();
		if (bestOfferOrder != null)
			marketData.setBestOffer(bestOfferOrder.getLimitPrice());
		else
			marketData.setBestOffer(null);
	}

	private Order processStopLimitOrder(Order order, PriorityQueue<? extends Order> orderBook) {
		if (order.isActive() || order.canActivate(marketData.getLastPrice())) {
			order.setActive(true);
			order.setOrderStatus(OrderStatus.QUEUED);
			return processLimitOrder(order, orderBook);
		} else if (order.getStopPrice().compareTo(marketData.getLastPrice()) <= 0) {
			order.setTriggerCondition(false);
			addToOrderBook(order);
		} else {
			order.setTriggerCondition(true);
			addToOrderBook(order);
		}
		return order;
	}

	private Order processMarketOrder(Order order, PriorityQueue<? extends Order> orderBook) {
		Order matchOrder = orderBook.peek();
		if (matchOrder != null) {
			return executeOrder(order, orderBook, this::processMarketOrder);
		}
		return order;
	}

	private Order checkForActiveOrders(Order order) {
		if (order.getTriggerCondition() == Boolean.FALSE) {
			if (marketData.getLastPrice().compareTo(order.getStopPrice()) <= 0) {
				order.setActive(true);
				order.setOrderStatus(OrderStatus.QUEUED);
			}
		} else {
			if (marketData.getLastPrice().compareTo(order.getStopPrice()) >= 0) {
				order.setActive(true);
				order.setOrderStatus(OrderStatus.QUEUED);
			}
		}

		return order;
	}

	private void addTransaction(Order order, Transaction transaction) {
		BigDecimal percentageChange;

		if (order.getOrderType() == OrderType.MARKET) {
			BigDecimal b = order.getOrderSide() == OrderSide.BUY
					? transaction.getQuantity().multiply(transaction.getPrice())
					: transaction.getQuantity();
			BigDecimal balanceToBlock = b.setScale(8, RoundingMode.FLOOR);

			blockAndVerifyBalance(order, balanceToBlock);
		}
		List<Transaction> transactions = order.getTransactions();

		entityManager.persist(transaction);
		if (transactions == null) {
			transactions = new ArrayList<>();
			order.setTransactions(transactions);
		}
		transactions.add(transaction);
		BigDecimal b = order.getOrderSide() == OrderSide.SELL ? transaction.getQuantity()
				: transaction.getPrice().multiply(transaction.getQuantity());
		BigDecimal balanceToDeduct = b.setScale(8, RoundingMode.FLOOR);

		order.deductBlockedBalance(balanceToDeduct);
		order.addToAvgExecutionPrice(transaction.getPrice(), transaction.getQuantity(),
				order.getQuantity().subtract(order.getCurrentQuantity()));
		if (getExeLastPrice().equals(BigDecimal.ZERO)) {
			percentageChange = BigDecimal.ZERO;
		} else {
			percentageChange = (((transaction.getPrice().subtract(getExeLastPrice())).divide(getExeLastPrice(), 6))
					.multiply(BigDecimal.valueOf(100)));
		}
		setPercentageChange(percentageChange);
		marketData.setPercentageChange(percentageChange);
		marketData.setGetExecutableLastPrice(getExeLastPrice());
		setExeLastPrice(transaction.getPrice());
		marketData.setLastPrice(transaction.getPrice());
		marketData.addVolume(transaction.getQuantity());
		kafkaService.removeFromOrderBook(transaction);
		kafkaService.sendTradeHistory(transaction);
		LOGGER.debug("Transaction added: {}", transactions);
	}

	private BigDecimal getExeLastPrice() {
		List<LastPrice> getLastPrice = exeLastPriceRepo.findAll();
		if (getLastPrice.isEmpty())
			return BigDecimal.ZERO;
		else {
			if (getLastPrice.get(getLastPrice.size() - 1).getExeLastPrice().compareTo(BigDecimal.ZERO) > 0)
				return (getLastPrice.get(getLastPrice.size() - 1).getExeLastPrice());
			else
				return BigDecimal.ZERO;
		}

	}

	private LastPrice setExeLastPrice(BigDecimal lastPrice) {
		List<LastPrice> getLastPrice = exeLastPriceRepo.findAll();
		if (getLastPrice.isEmpty())
			return exeLastPriceRepo.save(new LastPrice(lastPrice));
		else {
			getLastPrice.get(getLastPrice.size() - 1).setExeLastPrice(lastPrice);
			return exeLastPriceRepo.save(getLastPrice.get(getLastPrice.size() - 1));
		}

	}

	private PercentageChange setPercentageChange(BigDecimal change) {
		List<PercentageChange> getChangePrice = percentageChangeRepo.findAll();
		if (getChangePrice.isEmpty())
			return percentageChangeRepo.save(new PercentageChange(change));
		else {
			getChangePrice.get(getChangePrice.size() - 1).setPercentageChange(change);
			return percentageChangeRepo.save(getChangePrice.get(getChangePrice.size() - 1));
		}

	}

	private void blockAndVerifyBalance(Order order, BigDecimal amountToBlock) {
		String coin = order.getOrderSide() == OrderSide.BUY ? appConfig.getBaseCoin() : appConfig.getExeCoin();
		Response<Object> response = walletClient
				.verifyAndBlockBalance(new BlockBalanceDto(order.getUserId(), coin, amountToBlock, order.getOrderId()));
		if (response.getStatus() != 200)
			throw new FailedToBlockBalanceException(response.getMessage());
		order.addBlockBalance(amountToBlock);
	}

	private Order processLimitOrder(Order order, PriorityQueue<? extends Order> orderBook) {
		Order firstOrder = orderBook.peek();
		if (firstOrder != null) {
			int compareTo = order.getLimitPrice().compareTo(firstOrder.getLimitPrice());
			if ((order.getOrderSide() == OrderSide.BUY && compareTo < 0)
					|| (order.getOrderSide() == OrderSide.SELL && compareTo > 0)) {
				return addToOrderBook(order);
			} else {
				return executeOrder(order, orderBook, this::processLimitOrder);
			}
		} else {
			addToOrderBook(order);
		}
		return order;
	}

	private Order addToOrderBook(Order order) {
		if (appConfig.getLiquidityEnabled()) {
			if (executeWithLiquidity(order))
				return order;
		}
		LOGGER.debug("Adding to order book : {}", order);
		if (order.getOrderSide() == OrderSide.BUY)
			buyOrderQueue.add(order);
		else
			sellOrderQueue.add(order);
		kafkaService.addToOrderBook(order);
		return order;
	}

	private Order executeOrder(Order order, PriorityQueue<? extends Order> orderBook,
			BiFunction<Order, PriorityQueue<? extends Order>, Order> func) {
		Order orderToExecuteWith = orderBook.poll();

		if (orderToExecuteWith != null && !orderToExecuteWith.getUserId().equals(order.getUserId())
				&& Boolean.TRUE.equals(orderToExecuteWith.isActive())) {

			int compareTo = order.getCurrentQuantity().compareTo(orderToExecuteWith.getCurrentQuantity());
			try {
				if (compareTo > 0) {
					addTransaction(order,
							new Transaction(orderToExecuteWith.getOrderId(), orderToExecuteWith.getCurrentQuantity(),
									orderToExecuteWith.getLimitPrice(), orderToExecuteWith.getUserId(),
									OrderStatus.EXECUTED, order.getOrderSide(), order.getUserId(), order.getOrderId()));

					deductQuantity(order, orderToExecuteWith.getCurrentQuantity());
					deductQuantity(orderToExecuteWith, orderToExecuteWith.getCurrentQuantity());
					order.setOrderStatus(OrderStatus.PARTIALLY_EXECUTED);
					return func.apply(order, orderBook);

				} else {
					Transaction transaction = new Transaction(orderToExecuteWith.getOrderId(),
							order.getCurrentQuantity(), orderToExecuteWith.getLimitPrice(),
							orderToExecuteWith.getUserId(), null, order.getOrderSide(), order.getUserId(),
							order.getOrderId());
					addTransaction(order, transaction);
					deductQuantity(orderToExecuteWith, order.getCurrentQuantity());
					deductQuantity(order, order.getCurrentQuantity());
					order.setOrderStatus(OrderStatus.EXECUTED);
					order.setActive(false);
					if (orderToExecuteWith.getCurrentQuantity().compareTo(BigDecimal.ZERO) == 0) {
						transaction.setExecutedOrderStatus(OrderStatus.EXECUTED);
					} else {
						transaction.setExecutedOrderStatus(OrderStatus.PARTIALLY_EXECUTED);
						addToOrderBook(orderToExecuteWith);
					}
				}
			} catch (FailedToBlockBalanceException e) {
				addToOrderBook(orderToExecuteWith);
			}
			return order;
		} else {
			remainOrder.add(orderToExecuteWith);

			return func.apply(order, orderBook);
		}
	}

	private void deductQuantity(Order order, BigDecimal quantityToDeduct) {
		order.setCurrentQuantity(order.getCurrentQuantity().subtract(quantityToDeduct));
	}

	private void printOrderBook() {
		buyOrderQueue.forEach(o -> LOGGER.debug(String.format("%5d	%3.5f/%3.5f  At	%3.5f BUY%n", o.getOrderId(),
				o.getCurrentQuantity(), o.getQuantity(), o.getLimitPrice())));
		sellOrderQueue.forEach(o -> LOGGER.debug(String.format("%5d	%3.5f/%3.5f  At	%3.5f SELL%n", o.getOrderId(),
				o.getCurrentQuantity(), o.getQuantity(), o.getLimitPrice())));
	}

	@Override
	public Optional<Order> removeOrder(Long orderId, OrderSide orderSide) {
		PriorityQueue<Order> orderQueue = orderSide == OrderSide.BUY ? buyOrderQueue : sellOrderQueue;
		synchronized (orderQueue) {
			Optional<Order> order = orderQueue.parallelStream().filter(o -> o.getOrderId().equals(orderId)).findAny();
			if (order.isPresent()) {
				orderQueue.remove(order.get());
				kafkaService.removeFromOrderBook(order.get());
			}
			return order;
		}
	}

	@Override
	public void initializeOrderBook(List<Order> orders) {
		LOGGER.info("Initializing order book");
		Map<Boolean, List<Order>> buyOrderMap = orders.parallelStream()
				.collect(Collectors.partitioningBy(o -> o.getOrderSide() == OrderSide.BUY));
		buyOrderQueue.addAll(buyOrderMap.get(true));
		sellOrderQueue.addAll(buyOrderMap.get(false));
		LOGGER.info("Found {} active buy orders", buyOrderQueue.size());
		LOGGER.info("Found {} active sell orders", sellOrderQueue.size());
		updateBestOrders();
		printOrderBook();
	}

	@Override
	public void resetOrderBook() {
		buyOrderQueue = new PriorityQueue<>((a, b) -> b.getLimitPrice().compareTo(a.getLimitPrice()));
		sellOrderQueue = new PriorityQueue<>((a, b) -> a.getLimitPrice().compareTo(b.getLimitPrice()));
	}

	@Transactional
	private void checkAndActiveStopLimitOrders(PriorityQueue<? extends Order> orderBook) {
		List<Order> orders = orderRepo.findAllByOrderTypeAndActiveFalse(OrderType.STOP_LIMIT);
		if (!orders.isEmpty()) {
			for (Order o : orders) {
				o = checkForActiveOrders(o);
				if (Boolean.TRUE.equals(o.isActive())) {
					orderRepo.save(o);
				}
			}
		}
	}

	private void processActiveStopLimit(Order stopOrder) {

		removeOrder(stopOrder.getOrderId(), stopOrder.getOrderSide());

		tradeService.processOrder(stopOrder);

	}

	public void checkAndActiveStopLimitOrders(Order order) {
		LOGGER.info("ordderBook on chekstoplimit order -=================");
		List<Order> orders = orderRepo.findAllByOrderTypeAndActiveFalseAndOrderStatusNotIn(OrderType.STOP_LIMIT,
				Arrays.asList(OrderStatus.CANCELLED, OrderStatus.COMPLETED, OrderStatus.PARTIALLY_COMPLETED,
						OrderStatus.PARTIALLY_EXECUTED, OrderStatus.EXECUTED, OrderStatus.EXECUTED));
		List<Order> activeOrders = new ArrayList<>();

		if (!orders.isEmpty()) {

			orders.parallelStream().forEachOrdered(o -> {
				if (o.getOrderId() != order.getOrderId()) {

					o = checkForActiveOrders(o);

					if (o.isActive() == Boolean.TRUE) {
						activeOrders.add(o);
						/*
						 * removeOrder(o.getOrderId(), o.getOrderSide()); addToOrderBook(o);
						 */

					}
				}
			});

			if (!activeOrders.isEmpty()) {
				activeOrders.parallelStream().forEachOrdered(a -> {
					processActiveStopLimit(a);
				});

			}

			if (order.getOrderType().equals(OrderType.MARKET)
					&& order.getCurrentQuantity().compareTo(BigDecimal.ZERO) > 0) {
				order.setTransactions(null);

				tradeService.processOrder(order);

			}
		}
		if (order.getOrderType() == OrderType.MARKET) {
			if (order.getCurrentQuantity().compareTo(BigDecimal.ZERO) > 0) {
				transactionService.returnLeftBlockBalance(order);
			}
			order.setOrderStatus(OrderStatus.EXECUTED);
			kafkaService.addToOrderBook(order);
			LOGGER.debug("Changing status of market order to partially completed: {}", order);
			order.setOrderStatus(OrderStatus.COMPLETED);
			order.setActive(false);
			orderRepo.save(order);
		}
	}
}
