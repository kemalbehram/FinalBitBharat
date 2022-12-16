package com.mobiloitte.order.service.impl;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiloitte.order.dto.OrderUpdateDto;
import com.mobiloitte.order.dto.TradeHistoryDto;
import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.model.MarketData;
import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Transaction;
import com.mobiloitte.order.service.KafkaService;

@Service
@ConfigurationProperties("kafka.topic")
public class KafkaServiceImpl implements KafkaService {
	private static final Logger LOGGER = LogManager.getLogger(KafkaServiceImpl.class);
	private String orderBook;
	private String transactions;
	private String marketData;
	private static final ObjectMapper CONVERTER = new Jackson2JsonEncoder().getObjectMapper();

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public void sendMarketData(MarketData data) {
		LOGGER.trace("Sending market data {}", data);
		try {
			kafkaTemplate.send(this.marketData, CONVERTER.writeValueAsString(data));
		} catch (IOException e) {
			LOGGER.catching(e);
		}
	}

	@Override
	public void sendTradeHistory(Transaction transaction) {
		LOGGER.debug("Sending trade history {}", transaction);
		try {
			kafkaTemplate.send(this.transactions, CONVERTER.writeValueAsString(new TradeHistoryDto(transaction)));
		} catch (IOException e) {
			LOGGER.catching(e);
		}
	}

	@Override
	public void addToOrderBook(Order order) {
		LOGGER.debug("Sending order book {} to {}", order, this.orderBook);
		try {
			kafkaTemplate.send(this.orderBook, CONVERTER.writeValueAsString(
					new OrderUpdateDto(order.getLimitPrice(), order.getCurrentQuantity(), order.getOrderSide())));
		} catch (IOException e) {
			LOGGER.catching(e);
		}
	}

	@Override
	public void removeFromOrderBook(Transaction transaction) {
		LOGGER.debug("Sending order book {}", transaction);
		try {
			kafkaTemplate.send(this.orderBook,
					CONVERTER.writeValueAsString(
							new OrderUpdateDto(transaction.getPrice(), transaction.getQuantity().negate(),
									transaction.getSide() == OrderSide.BUY ? OrderSide.SELL : OrderSide.BUY)));
		} catch (IOException e) {
			LOGGER.catching(e);
		}
	}

	@Override
	public void removeFromOrderBook(Order order) {
		LOGGER.debug("Sending order book {}", order);
		try {
			kafkaTemplate.send(this.orderBook, CONVERTER.writeValueAsString(new OrderUpdateDto(order.getLimitPrice(),
					order.getCurrentQuantity().negate(), order.getOrderSide())));
		} catch (IOException e) {
			LOGGER.catching(e);
		}
	}

	public void setOrderBook(String orderBook) {
		this.orderBook = orderBook;
	}

	public void setTransactions(String transactions) {
		this.transactions = transactions;
	}

	public void setMarketData(String marketData) {
		this.marketData = marketData;
	}
}
