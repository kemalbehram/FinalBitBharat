package com.mobiloitte.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mobiloitte.order.config.AppConfig;
import com.mobiloitte.order.constants.MessageConstant;
import com.mobiloitte.order.dto.BlockBalanceDto;
import com.mobiloitte.order.dto.EmailDto;
import com.mobiloitte.order.dto.TradeHistoryDto;
import com.mobiloitte.order.dto.TransferBalanceDto;
import com.mobiloitte.order.dto.UserEmailAndNameDto;
import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.enums.OrderStatus;
import com.mobiloitte.order.enums.OrderType;
import com.mobiloitte.order.exception.OrderNotFoundException;
import com.mobiloitte.order.feign.NotificationClient;
import com.mobiloitte.order.feign.UserClient;
import com.mobiloitte.order.feign.WalletClient;
import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Response;
import com.mobiloitte.order.model.Transaction;
import com.mobiloitte.order.repo.OrderRepo;
import com.mobiloitte.order.repo.TransactionRepo;
import com.mobiloitte.order.service.TransactionService;

/**
 * @author Jha Shubham
 *
 */
@Service
public class TransactionServiceImpl implements TransactionService {
	private static final Logger LOGGER = LogManager.getLogger(TransactionServiceImpl.class);

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private TransactionRepo transactionRepo;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private WalletClient walletClient;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private NotificationClient notificationClient;

	@Autowired
	private UserClient userClient;

	@Override
	@Transactional
	public void publishTransactions(Order order) {
		order.getTransactions().parallelStream().forEachOrdered(t -> {
			transferBalance(order, t);
			if (order.getLiquiditystatus() == Boolean.TRUE) {
				t.setUserId(order.getUserId());

			}
			order.setLastExecutionTime(new Date());
			if (t.getOrderId() != null && t.getUserId() != null) {
				Optional<Order> executedOrder = orderRepo.findById(t.getOrderId());

				if (executedOrder.isPresent()) {

					Order order2 = executedOrder.get();

					String query = "select current_quantity from order_" + order.getInstrument() + " where order_id= "
							+ order2.getOrderId();
					List<Map<String, Object>> currentQuantityRepo = jdbcTemplate.queryForList(query);
					BigDecimal currentQuantityRepoDecimal = BigDecimal.valueOf(
							Double.valueOf(String.valueOf(currentQuantityRepo.get(0).get("current_quantity"))));

					BigDecimal b = t.getQuantity().multiply(t.getPrice());
					BigDecimal amount = b.setScale(8, RoundingMode.FLOOR);

					order2.deductBlockedBalance(order2.getOrderSide() == OrderSide.SELL ? t.getQuantity() : amount);
					if (t.getExecutedOrderStatus() == OrderStatus.EXECUTED) {
						order2.setOrderStatus(OrderStatus.COMPLETED);
						order2.setActive(false);
						if (order2.getBlockedBalance().compareTo(BigDecimal.ZERO) > 0)
							returnLeftBlockBalance(order2);
					} else {
						order2.setOrderStatus(t.getExecutedOrderStatus());
					}
					order2.setLastExecutionTime(new Date());
					order2.addToAvgExecutionPrice(t.getPrice(), t.getQuantity(),
							order.getQuantity().subtract(order.getCurrentQuantity()));
					if (currentQuantityRepoDecimal.compareTo(order2.getCurrentQuantity()) == 0) {
						order2.deductQuantity(t.getQuantity());

					}
					orderRepo.save(order2);

					try {
						Response<UserEmailAndNameDto> getUserDetails = userClient.getEmailAndName(
								order2.getUserId());
						if (getUserDetails.getStatus() == 200) {
							getUserDetails.getData().getEmail();

							Map<String, String> orderExcutedMailMap = new HashMap<>();

							orderExcutedMailMap.put(MessageConstant.EMAIL_TOKEN,
									String.valueOf(getUserDetails.getData().getEmail()));

							orderExcutedMailMap.put(MessageConstant.DATE_TOKEN, new Date().toString());

							orderExcutedMailMap.put(MessageConstant.ORDER_ID, String.valueOf(
									String.valueOf(new Date().getTime()) + String.valueOf(order2.getOrderId())));
							if (order2.getOrderStatus().equals(OrderStatus.COMPLETED)
									|| order2.getOrderStatus().equals(OrderStatus.EXECUTED)) {
								orderExcutedMailMap.put(MessageConstant.STATUS_TOKEN, " Completed");

							}
							if (order2.getOrderStatus().equals(OrderStatus.PARTIALLY_COMPLETED)
									|| order2.getOrderStatus().equals(OrderStatus.PARTIALLY_EXECUTED)) {
								orderExcutedMailMap.put(MessageConstant.STATUS_TOKEN, " Partially");

							}

							orderExcutedMailMap.put(MessageConstant.TRADINGPAIR_TOKEN,
									order.getInstrument().replace("_", "/"));

							if (order.getOrderType().equals(OrderType.MARKET)) {
								orderExcutedMailMap.put(MessageConstant.PRICE, " Market");
							} else {
								orderExcutedMailMap.put(MessageConstant.PRICE, String.valueOf(order2.getLimitPrice()));
							}
							orderExcutedMailMap.put(MessageConstant.PRICEANDCONDITION_TOKEN,
									String.valueOf(order2.getAvgExecutionPrice()));
							orderExcutedMailMap.put(MessageConstant.VOLUME_TOKEN, String.valueOf(order2.getQuantity()));
							try {

								orderExcutedMailMap.put(MessageConstant.EXECUTED_ORDER_AMOUNT,
										String.valueOf(order2.getQuantity().subtract(order2.getCurrentQuantity())));

							} catch (Exception e) {
								orderExcutedMailMap.put(MessageConstant.EXECUTED_ORDER_AMOUNT, " 0");
							}
							if (order2.getStopPrice() == null) {
								orderExcutedMailMap.put(MessageConstant.STOP_PRICE, " NA");

							} else {
								orderExcutedMailMap.put(MessageConstant.STOP_PRICE,
										String.valueOf(order2.getStopPrice()));
							}
							//List<Order> data = basicOrderDetails(order2.getOrderId(), order2.getUserId());

							// orderExcutedMailMap.put(MessageConstant.SUPPORT_LINK_TOKEN,
							// MessageConstant.SUPPORT_LINK);
							orderExcutedMailMap.put(MessageConstant.MODE_OF_TRADING,
									WordUtils.capitalizeFully(String.valueOf(order2.getOrderSide()).toLowerCase()));

							try {
								orderExcutedMailMap.put(MessageConstant.AMOUNTFILL_TOKEN,
										String.valueOf(order2.getQuantity().subtract(order2.getCurrentQuantity())));

							} catch (ArithmeticException e) {
								orderExcutedMailMap.put(MessageConstant.AMOUNTFILL_TOKEN, " 0");
							}
							try {

								orderExcutedMailMap.put(MessageConstant.REMAINING_AMOUNT,
										String.valueOf(order2.getCurrentQuantity()));
							} catch (Exception e) {
								orderExcutedMailMap.put(MessageConstant.REMAINING_AMOUNT, " 0");
							}
							orderExcutedMailMap.put(MessageConstant.AMOUNT, String.valueOf(order2.getQuantity()));

							// orderExcutedMailMap.put("isEmail", String.valueOf(true));
							// orderExcutedMailMap.put("isWeb", String.valueOf(true));

							notificationClient.sendNotification(new EmailDto(order2.getUserId(),
									"trade_execution_status", String.valueOf(getUserDetails.getData().getEmail()),
									orderExcutedMailMap));

						}

					} catch (Exception e) {
						LOGGER.info("mail not sent to user2");
					}
				} else {
					throw new OrderNotFoundException("Failed to change executed order status " + t.getOrderId());
				}
			}
		});
		if (order.getOrderStatus() == OrderStatus.EXECUTED
				&& order.getBlockedBalance().compareTo(BigDecimal.ZERO) > 0) {
			returnLeftBlockBalance(order);
		}

	}

	private void transferBalance(Order order, Transaction t) {
		String baseCoin = appConfig.getBaseCoin();
		String exeCoin = appConfig.getExeCoin();
		BigDecimal b = t.getQuantity().multiply(t.getPrice());
		BigDecimal amount = b.setScale(8, RoundingMode.FLOOR);
		if (order.getOrderSide() == OrderSide.BUY) {
			Response<Object> response = walletClient
					.transferBalance(new TransferBalanceDto(baseCoin, exeCoin, order.getUserId(), t.getUserId(), amount,
							t.getQuantity(), order.getOrderId(), t.getOrderId(), OrderSide.BUY));
			LOGGER.debug("Response recived: {}", response);
			LOGGER.debug("Transfering {} coin {} balance from user {} to {}", amount, baseCoin, order.getUserId(),
					t.getUserId());
			LOGGER.debug("Transfering {} coin {} balance from user {} to {}", t.getQuantity(), exeCoin, t.getUserId(),
					order.getUserId());
		} else {
			Response<Object> response = walletClient
					.transferBalance(new TransferBalanceDto(exeCoin, baseCoin, order.getUserId(), t.getUserId(),
							t.getQuantity(), amount, order.getOrderId(), t.getOrderId(), OrderSide.SELL));
			LOGGER.debug("Response recived: {}", response);
			LOGGER.debug("Transfering {} coin {} balance from user {} to {}", t.getQuantity(), exeCoin,
					order.getUserId(), t.getUserId());
			LOGGER.debug("Transfering {} coin {} balance from user {} to {}", amount, baseCoin, t.getUserId(),
					order.getUserId());
		}
	}

	public void returnLeftBlockBalance(Order order) {
		String coin = order.getOrderSide() == OrderSide.BUY ? appConfig.getBaseCoin() : appConfig.getExeCoin();
		BlockBalanceDto dto = new BlockBalanceDto(order.getUserId(), coin, order.getCurrentQuantity(),
				order.getOrderId());
		Response<Object> response = walletClient.returnBlockBalance(dto);
		if (response.getStatus() != 200) {
			LOGGER.error("Could not return blocked blance for {}. Response:  {}", order, response);
		} else {
			order.setBlockedBalance(BigDecimal.ZERO);
			LOGGER.debug("Extra blocked Balance returned {}", dto);
		}
	}

	@Override
	public List<Transaction> getLast24HourTransactions() {
		return transactionRepo.findByExecutionTimeGreaterThan(new Date(System.currentTimeMillis() - 86400000));
	}

	@Override
	public Response<List<Order>> getOrderHistoryByUserId(Long userId) {
		return new Response<>(orderRepo.findAllOrderByUserIdAndActiveFalseAndOrderStatusNotIn(userId,
				Arrays.asList(OrderStatus.REJECTED, OrderStatus.CREATED)));
	}

	@Override
	public Response<List<TradeHistoryDto>> getTradeHistory(Long size) {
		List<Transaction> transaction = transactionRepo.findAll();
		return new Response<>(
				transaction.parallelStream().map(TradeHistoryDto::new).sorted().collect(Collectors.toList()));
	}

	@Override
	public BigDecimal getTotalVolume() {
		return transactionRepo.sumByQuantity().orElse(BigDecimal.ZERO);
	}

	@Override
	public Optional<Transaction> getLastTransaction() {
		return transactionRepo.findTopByOrderByTransactionIdDesc();
	}

	

}
