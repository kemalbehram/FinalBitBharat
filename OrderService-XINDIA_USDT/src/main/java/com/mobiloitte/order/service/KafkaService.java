package com.mobiloitte.order.service;

import com.mobiloitte.order.model.MarketData;
import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Transaction;

public interface KafkaService {
	void sendMarketData(MarketData marketData);

	void sendTradeHistory(Transaction transaction);

	void addToOrderBook(Order order);

	void removeFromOrderBook(Transaction transaction);

	void removeFromOrderBook(Order order);
}
