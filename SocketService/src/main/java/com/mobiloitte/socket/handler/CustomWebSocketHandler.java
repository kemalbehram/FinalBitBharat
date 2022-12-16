package com.mobiloitte.socket.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.mobiloitte.socket.WebSocketMessageImpl;
import com.mobiloitte.socket.constant.SocketConstant;
import com.mobiloitte.socket.enums.MessageType;
import com.mobiloitte.socket.feign.OrderClient;
import com.mobiloitte.socket.response.Response;
import com.mobiloitte.socket.serviceimpl.SocketResponseImpl;

/**
 * @author Krishan Sharma
 *
 */
@Component
public class CustomWebSocketHandler extends SocketConstant implements WebSocketHandler {
	@Autowired
	private OrderClient orderClient;
	private static final Logger LOGGER = LogManager.getLogger(CustomWebSocketHandler.class);

	private static final Gson GSON = new Gson();

	private static final String MESSAGE_TYPE = "messageType";

	private static final String SYMBOL = "symbol";

	@Value("${exchange.application.symbols-supported}")
	private List<String> symbolsSupported;

	Set<WebSocketSession> allSessions = new HashSet<>();

	Map<String, Set<WebSocketSession>> tickerSubscribers = new HashMap<>();

	Map<String, Set<WebSocketSession>> orderBookSubscribers = new HashMap<>();

	Map<String, Set<WebSocketSession>> tradeHistorySubscribers = new HashMap<>();

	Map<Long, WebSocketSession> authenticatedUsers = new HashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOGGER.debug("Socket Connected with Session  {}", session);
		allSessions.add(session);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		SocketResponseImpl responseMessage = GSON.fromJson(message.getPayload().toString(), SocketResponseImpl.class);
		if (responseMessage != null)
			newMessage(session, responseMessage);
	}

	private void newMessage(WebSocketSession session, SocketResponseImpl webSocketmessage) {
		LOGGER.debug("Request Received {}", webSocketmessage);
		MessageType messageType = webSocketmessage.getMessageType();
		if (messageType == null)
			return;
		String symbol = (String) webSocketmessage.getParams().get(SYMBOL);
		Set<WebSocketSession> orderBookSubscribersList = orderBookSubscribers.get(symbol);
		Set<WebSocketSession> tradeHistorySubscriberList = tradeHistorySubscribers.get(symbol);
		Set<WebSocketSession> tickerSubscribersList = tickerSubscribers.get(symbol);
		if (!symbolsSupported.contains(symbol)) {
			try {
				session.sendMessage(new TextMessage(symbol + " symbol not supported"));
			} catch (IOException e) {
				LOGGER.catching(e);
			}
			return;
		}
		getSwithCaseFutionality(session, messageType, orderBookSubscribersList, symbol, tradeHistorySubscriberList,
				tickerSubscribersList);
	}

	private void getSwithCaseFutionality(WebSocketSession session, MessageType messageType,
			Set<WebSocketSession> orderBookSubscribersList, String symbol,
			Set<WebSocketSession> tradeHistorySubscriberList, Set<WebSocketSession> tickerSubscribersList) {
		switch (messageType) {
		case SUBSCRIBE_ORDER_BOOK:
			if (orderBookSubscribersList == null) {
				orderBookSubscribersList = new HashSet<>();
				orderBookSubscribers.put(symbol, orderBookSubscribersList);
			} else if (orderBookSubscribersList.contains(session)) {
				sendAlreadySubscribedMsg(session, messageType);
				break;
			}
			sendDataToOne(getOrderBookData(symbol), session);
			orderBookSubscribersList.add(session);
			break;
		case UNSUBSCRIBE_ORDER_BOOK:
			if (orderBookSubscribersList == null || !orderBookSubscribersList.contains(session)) {
				sendListEmptyMsg(session, messageType);
				break;
			}
			sendUnsubscriberSucessfullyMsg(session, messageType);
			orderBookSubscribersList.remove(session);
			break;
		case SUBSCRIBE_TICKER:
			if (tickerSubscribersList == null) {
				tickerSubscribersList = new HashSet<>();
				tickerSubscribers.put(symbol, tickerSubscribersList);
			} else if (tickerSubscribersList.contains(session)) {
				sendAlreadySubscribedMsg(session, messageType);
				break;
			}
			sendDataToOne(getTickerData(symbol), session);
			tickerSubscribersList.add(session);
			break;

		case UNSUBSCRIBE_TICKER:
			if (tickerSubscribersList == null || !tickerSubscribersList.contains(session)) {
				sendListEmptyMsg(session, messageType);
				break;
			}
			sendUnsubscriberSucessfullyMsg(session, messageType);
			tickerSubscribersList.remove(session);
			break;
		case SUBSCRIBE_TRADE_HISTORY:
			if (tradeHistorySubscriberList == null) {
				tradeHistorySubscriberList = new HashSet<>();
				tradeHistorySubscribers.put(symbol, tradeHistorySubscriberList);
			} else if (tradeHistorySubscriberList.contains(session)) {
				sendAlreadySubscribedMsg(session, messageType);
				break;
			}
			sendDataToOne(tradeHistoryData(symbol), session);
			tradeHistorySubscriberList.add(session);
			break;

		case UNSUBSCRIBE_TRADE_HISTORY:
			if (tradeHistorySubscriberList == null || !tradeHistorySubscriberList.contains(session)) {
				sendListEmptyMsg(session, messageType);
				break;
			}
			sendUnsubscriberSucessfullyMsg(session, messageType);
			tradeHistorySubscriberList.remove(session);
			break;
		default:
		}
	}

	private void sendUnsubscriberSucessfullyMsg(WebSocketSession session, MessageType messageType) {
		Map<String, Object> data = new HashMap<>();
		data.put(SocketConstant.MESSAGE, "Unsubscribed Sucessfully");
		data.put(MESSAGE_TYPE, messageType);
		sendDataToOne(data, session);
	}

	private Map<String, Object> tradeHistoryData(String symbol) {
		Map<String, Object> data = new HashMap<>();
		try {
			Response<Object> tradeHistory = orderClient.getTradeHistory(symbol);
			data.put(SocketConstant.DATA, tradeHistory.getData());
			data.put(SYMBOL, symbol);
			data.put(MESSAGE_TYPE, MessageType.TRADE_HISTORY_UPDATE);
			return data;
		} catch (Exception e) {
			data.put(SocketConstant.DATA, null);
			data.put(SYMBOL, symbol);
			data.put(MESSAGE_TYPE, MessageType.TRADE_HISTORY_UPDATE);
			return data;
		}
	}

	private Map<String, Object> getTickerData(String symbol) {
		Map<String, Object> data = new HashMap<>();
		try {
			Response<Object> marketData = orderClient.getMarketData(symbol);
			data.put(SocketConstant.DATA, marketData.getData());
			data.put(SYMBOL, symbol);
			data.put(MESSAGE_TYPE, MessageType.TICKER_UPDATE);
			return data;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			data.put(SocketConstant.DATA, null);
			data.put(SYMBOL, symbol);
			data.put(MESSAGE_TYPE, MessageType.TICKER_UPDATE);
			return data;
		}
	}

	private Map<String, Object> getOrderBookData(String symbol) {
		Map<String, Object> data = new HashMap<>();
		try {
			Response<Object> orderBook = orderClient.getOrderBook(symbol);
			data.put(SocketConstant.DATA, orderBook.getData());
			data.put(SYMBOL, symbol);
			data.put(MESSAGE_TYPE, MessageType.ORDER_BOOK_UPDATE);
			return data;
		} catch (Exception e) {
			data.put(SocketConstant.DATA, null);
			data.put(SYMBOL, symbol);
			data.put(MESSAGE_TYPE, MessageType.ORDER_BOOK_UPDATE);
			return data;
		}
	}

	private void sendListEmptyMsg(WebSocketSession session, MessageType messageType) {
		Map<String, Object> data = new HashMap<>();
		data.put(SocketConstant.MESSAGE, SocketConstant.NOT_A_SUBSCRIBER);
		data.put(MESSAGE_TYPE, messageType);
		sendDataToOne(data, session);
	}

	private void sendAlreadySubscribedMsg(WebSocketSession session, MessageType messageType) {
		Map<String, Object> data = new HashMap<>();
		data.put(SocketConstant.MESSAGE, SocketConstant.ALREADY_SUBSCRIBED);
		data.put(MESSAGE_TYPE, messageType);
		sendDataToOne(data, session);

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		allSessions.remove(session);
		orderBookSubscribers.entrySet().parallelStream().forEach(m -> m.getValue().remove(session));
		tickerSubscribers.entrySet().parallelStream().forEach(m -> m.getValue().remove(session));
		tradeHistorySubscribers.entrySet().parallelStream().forEach(m -> m.getValue().remove(session));
	}

	public void sendToTradeHistory(String symbol, Object tradHistory) {
		WebSocketMessageImpl message = new WebSocketMessageImpl();
		Map<String, Object> data = new HashMap<>();
		data.put(MESSAGE_TYPE, MessageType.TRADE_HISTORY_UPDATE);
		data.put(SYMBOL, symbol);
		data.put("data", Arrays.asList(tradHistory));
		message.setPayload(data);
		sendData(message, tradeHistorySubscribers.get(symbol));
	}

	public void sendToTicker(String symbol, Object tickerData) {
		WebSocketMessageImpl message = new WebSocketMessageImpl();
		Map<String, Object> data = new HashMap<>();
		data.put(MESSAGE_TYPE, MessageType.TICKER_UPDATE);
		data.put(SYMBOL, symbol);
		data.put("data", tickerData);
		message.setPayload(data);
		sendData(message, tickerSubscribers.get(symbol));
	}

	public void sendToOrderBookHistory(String symbol, Object orderBook) {
		WebSocketMessageImpl message = new WebSocketMessageImpl();
		Map<String, Object> data = new HashMap<>();
		data.put(SYMBOL, symbol);
		data.put("data", Arrays.asList(orderBook));
		data.put(MESSAGE_TYPE, MessageType.ORDER_BOOK_UPDATE);
		message.setPayload(data);
		sendData(message, orderBookSubscribers.get(symbol));

	}

	private void sendData(WebSocketMessageImpl message, Set<WebSocketSession> sessionList) {
		if (sessionList != null)
			sessionList.parallelStream().forEach(session -> {
				try {
					Object obj = new Object();
					synchronized (obj) {
						session.sendMessage(new TextMessage(GSON.toJson(message.getPayload())));
					}
				} catch (IOException e) {
					LOGGER.catching(e);
				}
			});
	}

	private void sendDataToOne(Map<String, Object> data, WebSocketSession session) {
		try {
			Object obj = new Object();
			synchronized (obj) {
				session.sendMessage(new TextMessage(GSON.toJson(data)));
			}
		} catch (IOException e) {
			LOGGER.catching(e);
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
