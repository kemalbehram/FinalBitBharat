package com.mobiloitte.notification.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.mobiloitte.notification.constants.EmailConstants;
import com.mobiloitte.notification.constants.MessageConstant;
import com.mobiloitte.notification.dao.ChatDao;
import com.mobiloitte.notification.dto.EmailDto;
import com.mobiloitte.notification.dto.ExchangeBellNotiticationDto;
import com.mobiloitte.notification.dto.ExchangePayloads;
import com.mobiloitte.notification.dto.UserEmailAndNameDto;
import com.mobiloitte.notification.dto.UserProfileDto;
import com.mobiloitte.notification.enums.RoleStatus;
import com.mobiloitte.notification.exception.RunTimeException;
import com.mobiloitte.notification.feign.UserClient;
import com.mobiloitte.notification.model.ChatDetails;
import com.mobiloitte.notification.model.P2pExchangeNotification;
import com.mobiloitte.notification.model.Response;
import com.mobiloitte.notification.service.NotificationService;

/**
 * @author Abhishek Sharma
 *
 */
@Component
public class CustomWebSocketChatHandler extends MessageConstant implements WebSocketHandler {
	private static final Logger LOGGER = LogManager.getLogger(CustomWebSocketHandler.class);
	private static final Gson GSON = new Gson();
	Set<WebSocketSession> allSessions = new HashSet<>();
	private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	Map<Long, Set<WebSocketSession>> users = new HashMap<>();
	@Autowired
	private ChatDao chatDao;
	@Autowired
	private UserClient userClient;
	@Autowired
	private NotificationService notificationService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOGGER.debug("P2p Socket Connected with Session  {}", session);
		allSessions.add(session);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
		try {
			ExchangePayloads responseMessage = GSON.fromJson(message.getPayload().toString(), ExchangePayloads.class);
			System.err.println(responseMessage.getUserId());
			System.err.println(responseMessage.getToUserId());
			newMessage(session, responseMessage);

		} catch (Exception e) {
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}

	}

	private void newMessage(WebSocketSession session, ExchangePayloads webSocketmessage) throws IOException {
		Set<WebSocketSession> sessions = new HashSet<>();
		sessions.add(session);
		users.put(webSocketmessage.getUserId(), sessions);
		if (webSocketmessage.getMessage() != null) {
			sendMessage(sessions, webSocketmessage);
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		LOGGER.debug("error has occured with the following session {}", session);
		try {
			session.close();
		} catch (IOException e) {
			LOGGER.error("Cannot close session on handleTransportError ", e);
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		LOGGER.info("Chat Socket Disconnected {}", closeStatus);
		sessions.remove(session);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	private void sendMessage(Set<WebSocketSession> webSocketSession, ExchangePayloads sockResponseImpl)
			throws IOException {
		System.err.println(users.entrySet());
		ChatDetails savedNewChat = saveChat(sockResponseImpl);
		users.entrySet().parallelStream().forEachOrdered(a -> {
			if (a.getKey().equals(sockResponseImpl.getUserId()) || a.getKey().equals(sockResponseImpl.getToUserId())) {
				Set<WebSocketSession> seesions = a.getValue();
				connection(seesions, sockResponseImpl, savedNewChat);
			}
		});
	}

	private void connection(Set<WebSocketSession> webSocketSessions, ExchangePayloads socketResponseImpl,
			ChatDetails savedNewChat) {
		if (webSocketSessions != null) {
			webSocketSessions.parallelStream().forEach(a -> {
				Object obj = a;
				try {
					synchronized (obj) {
						Map<String, Object> response = new HashMap<>();
						if (socketResponseImpl.getToUserId() != null && socketResponseImpl.getUserId() != null) {
							List<ChatDetails> unseenDataToUpdate = chatDao
									.findByTradeIdAndToUserIdAndFromUserIdAndIsSeenFalse(
											socketResponseImpl.getTradeId(), socketResponseImpl.getToUserId(),
											socketResponseImpl.getUserId());
							unseenDataToUpdate.parallelStream().forEachOrdered(aaa -> {
								aaa.setIsSeen(Boolean.TRUE);
								chatDao.save(aaa);
							});
						}
						response.put("newChatData", savedNewChat);
						a.sendMessage(new TextMessage(GSON.toJson(response)));

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			});
		}
	}

	private ChatDetails saveChat(ExchangePayloads socketResponseImpl) throws IOException {
		if (!socketResponseImpl.getMessage().equalsIgnoreCase("")
				&& !socketResponseImpl.getMessage().equalsIgnoreCase(" ")) {
			ChatDetails chatDetails = new ChatDetails();
			chatDetails.setMessage(socketResponseImpl.getMessage());
			chatDetails.setFromUserId(socketResponseImpl.getUserId());
			chatDetails.setIsSeen(false);
			chatDetails.setMessageFormat(socketResponseImpl.getMessageFormat());
			chatDetails.setToUserId(socketResponseImpl.getToUserId());
			chatDetails.setRoleId(socketResponseImpl.getRoleId());
			chatDetails.setRole(socketResponseImpl.getRole());
			chatDetails.setCreationTime(socketResponseImpl.getCreationTime());
			chatDetails.setUpdationTime(socketResponseImpl.getUpdationTime());
			chatDetails.setTradeId(socketResponseImpl.getTradeId());
			System.err.println(chatDetails);
			ChatDetails savedNewChat = chatDao.save(chatDetails);
			Response<UserProfileDto> da = userClient.getUserByUserId(socketResponseImpl.getUserId());
			if (da.getData().getRole() == RoleStatus.ADMIN) {
				List<ChatDetails> list = chatDao.findByTradeIdAndFromUserId(socketResponseImpl.getTradeId(),
						socketResponseImpl.getUserId());

				if ((list.isEmpty()) || (list.size() <= 1)) {
					Response<UserEmailAndNameDto> emailAndName2 = userClient
							.getEmailAndName(socketResponseImpl.getToUserId());
					Map<String, String> setData = new HashMap<>();
//					String url = socketResponseImpl.getReturnUrl()+ "&toUserId=" + socketResponseImpl.getToUserId();
					setData.put(EmailConstants.EMAIL_TOKEN, emailAndName2.getData().getEmail());
					setData.put(EmailConstants.TRADE_TOKEN, socketResponseImpl.getTradeId());
					setData.put(EmailConstants.RETURN_URL_TOKEN, socketResponseImpl.getReturnUrl());
					setData.put("returnUrl", socketResponseImpl.getReturnUrl());

					EmailDto emailDto = new EmailDto(socketResponseImpl.getToUserId(), "admin_notification",
							emailAndName2.getData().getEmail(), setData);
					notificationService.sendNotification(emailDto);

				}
			}
			return savedNewChat;

		}
		return null;

	}

	public void sendDataToUserExchange(List<ExchangeBellNotiticationDto> notificationData) {
		List<ExchangeBellNotiticationDto> notificationDataSend = new ArrayList<>();
		users.entrySet().parallelStream().forEachOrdered(a -> {
			Set<WebSocketSession> currentSession = new HashSet<>();
			notificationData.parallelStream().forEachOrdered(b -> {
				if (a.getKey().equals(b.getToUserId())) {
					Set<WebSocketSession> currentSessions = new HashSet<>();
					notificationDataSend.add(b);
					currentSessions = users.get(a.getKey());
					currentSession.addAll(currentSessions);
				}
				System.err.println(users.toString());

			});
			sendNotification(notificationDataSend, currentSession);
			System.err.println(currentSession.toString());
			System.err.println(notificationDataSend.toString());

		});
	}

	private void sendNotification(List<ExchangeBellNotiticationDto> notificationDatas,
			Set<WebSocketSession> sessionList) {
		List<P2pExchangeNotification> notificationData = new LinkedList<P2pExchangeNotification>();
		long c = notificationDatas.size();
		System.out.println(c);
		for (ExchangeBellNotiticationDto notificationDatassssss : notificationDatas) {
			P2pExchangeNotification notfi = new P2pExchangeNotification();
			notfi.setNotificationId(notificationDatassssss.getNotificationId());
			notfi.setCreatedAt(notificationDatassssss.getTimeStamp());
			notfi.setFromRequestAcceptRejectId(notificationDatassssss.getFromRequestAcceptRejectId());
			notfi.setFromUserId(notificationDatassssss.getFromUserId());
			notfi.setIsSeen(notificationDatassssss.getIsSeen());
			notfi.setNoOfCoins(notificationDatassssss.getNoOfCoins());
			notfi.setNotificationStatus(notificationDatassssss.getNotificationStatus());
			notfi.setOrderType(notificationDatassssss.getOrderType());
			notfi.setPaymentWindow(notificationDatassssss.getPaymentWindow());
			notfi.setPeerToPeerExchangeId(notificationDatassssss.getPeerToPeerExchangeId());
			notfi.setRequestMessage(notificationDatassssss.getRequestMessage());
			notfi.setTimeStamp(notificationDatassssss.getTimeStamp());
			notfi.setToRequestAcceptRejectId(notificationDatassssss.getFromRequestAcceptRejectId());
			notfi.setToUserId(notificationDatassssss.getToUserId());
			notfi.setTradeId(notificationDatassssss.getTradeId());
			notfi.setTradePartner(notificationDatassssss.getTradePartner());
			notfi.setTradeStatus(notificationDatassssss.getTradeStatus());
			notfi.setTradingPrice(notificationDatassssss.getTradingPrice());

			notificationData.add(notfi);

		}

		if (sessionList != null)
			sessionList.parallelStream().forEach(session -> {
				try {
					synchronized (session) {
						LOGGER.info("Before Send Message() Web Socket reched Successfully to End");
						session.sendMessage(new TextMessage(GSON.toJson(notificationData)));
						LOGGER.info("After Send Message() Web Socket reched Successfully to End ", notificationData);
					}
				} catch (IOException e) {
					LOGGER.catching(e);
				}
			});
	}

	public void connectionClose(WebSocketSession session, Long userId) {
		LOGGER.info("Chat Socket Disconnected when connectionClose{}", userId);
		users.entrySet().forEach(a -> {
			if (a.getKey().equals(userId)) {
				a.getValue().remove(session);
			}
		});

	}
}
