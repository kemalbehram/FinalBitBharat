package com.mobiloitte.socket.kafka;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiloitte.socket.handler.CustomWebSocketHandler;

@Service
public class KafkaReceiver {

	private static final Logger LOGGER = LogManager.getLogger(KafkaReceiver.class);
	@Autowired
	CustomWebSocketHandler webSocketHandler;

	@KafkaListener(topicPattern = ".*?${kafka.topic.trade-history}")
	public void tradeHistoryData(@Payload String json, @Header(name = "kafka_receivedTopic") String topic)
			throws IOException {
		String symbol = topic.split("-")[0];
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonMap = mapper.readValue(json, new TypeReference<Map<String, String>>() {
		});
		LOGGER.debug("Received trade history {} : {}: ", symbol, jsonMap);
		webSocketHandler.sendToTradeHistory(symbol, jsonMap);
	}

	@KafkaListener(topicPattern = ".*?${kafka.topic.order-book}")
	public void orderBookData(@Payload String json, @Header(name = "kafka_receivedTopic") String topic)
			throws IOException {
		String symbol = topic.split("-")[0];
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonMap = mapper.readValue(json, new TypeReference<Map<String, String>>() {
		});
		LOGGER.debug("Transactions received for {}:  {}  : ", symbol, jsonMap);
		webSocketHandler.sendToOrderBookHistory(symbol, jsonMap);
	}

	@KafkaListener(topicPattern = ".*?${kafka.topic.market-data}")
	public void marketData(@Payload String json, @Header(name = "kafka_receivedTopic") String topic)
			throws IOException {
		String symbol = topic.split("-")[0];
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonMap = mapper.readValue(json, new TypeReference<Map<String, String>>() {
		});
		LOGGER.debug("Received Market data {} : {}  : ", symbol, jsonMap);
		webSocketHandler.sendToTicker(symbol, jsonMap);
	}

}