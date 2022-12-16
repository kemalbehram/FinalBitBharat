package com.mobiloitte.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.mobiloitte.order.dto.TradeHistoryDto;
import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Response;
import com.mobiloitte.order.model.Transaction;

/**
 * @author Rahil Husain
 *
 */
public interface TransactionService {
	void publishTransactions(Order order);

	List<Transaction> getLast24HourTransactions();

	Response<List<Order>> getOrderHistoryByUserId(Long userId);

	Response<List<TradeHistoryDto>> getTradeHistory(Long size);

	BigDecimal getTotalVolume();

	Optional<Transaction> getLastTransaction();

	void returnLeftBlockBalance(Order order);

}
