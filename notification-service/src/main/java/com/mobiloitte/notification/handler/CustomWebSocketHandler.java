package com.mobiloitte.notification.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.mobiloitte.notification.constants.MessageConstant;
import com.mobiloitte.notification.dao.NotificationDataDao;
import com.mobiloitte.notification.enums.NotificationUserType;
import com.mobiloitte.notification.exception.RunTimeException;
import com.mobiloitte.notification.feign.UserClient;
import com.mobiloitte.notification.model.NotificationData;
import com.mobiloitte.notification.model.Response;
import com.mobiloitte.notification.serviceimpl.SocketMessageImpl;

/**
 * @author Abhishek Sharma
 *
 */
@Component
public class CustomWebSocketHandler extends MessageConstant implements WebSocketHandler {
	@Autowired
	private NotificationDataDao notificationDataDao;
	@Autowired
	private UserClient userClient;

	private static final Logger LOGGER = LogManager.getLogger(CustomWebSocketHandler.class);
	private static final Gson GSON = new Gson();

	Set<WebSocketSession> allSessions = new HashSet<>();
	Map<Long, WebSocketSession> authenticatedUsers = new HashMap<>();
	Map<Long, Set<WebSocketSession>> users = new HashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		LOGGER.debug("Socket Connected with Session {}", session);
		allSessions.add(session);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		try {
			SocketMessageImpl responseMessage = GSON.fromJson(message.getPayload().toString(), SocketMessageImpl.class);

			if (responseMessage != null) {
				newMessage(session, responseMessage);
				if (responseMessage.getNotificationUserType().equals("LOGOUT")) {
					LOGGER.info(LOGOUT);
					connectionClose(session, responseMessage.getUserId());
				}
			}

		} catch (Exception e) {
			LOGGER.info(e);
		}

	}

	private void newMessage(WebSocketSession session, SocketMessageImpl message) {
		Response<Long> response = userClient.getRoleIdByName(message.getNotificationUserType());
		final NotificationUserType messages = NotificationUserType.valueOf(message.getNotificationUserType());
		Set<WebSocketSession> webSocketSessions = new HashSet<>();
		webSocketSessions.add(session);
		if (messages.equals(NotificationUserType.USER)) {
			users.put(message.getUserId(), webSocketSessions);
			sendNotification(notificaionData(response.getData(), message.getUserId()), users.get(message.getUserId()));
		}
		if (messages.equals(NotificationUserType.ADMIN)) {
			users.put(message.getUserId(), webSocketSessions);
			sendNotification(notificaionData(response.getData(), message.getUserId()), users.get(message.getUserId()));
		}
		if (messages.equals(NotificationUserType.DEVELOPER)) {
			users.put(message.getUserId(), webSocketSessions);
			sendNotification(notificaionData(response.getData(), message.getUserId()), users.get(message.getUserId()));
		}
		if (messages.equals(NotificationUserType.SUBADMIN)) {
			users.put(message.getUserId(), webSocketSessions);
			sendNotification(notificaionData(response.getData(), message.getUserId()), users.get(message.getUserId()));
		}

	}

	public List<NotificationData> notificaionData(Long roleId, Long userId) {
		List<NotificationData> data = notificationDataDao.findByRoleIdAndFkUserIdAndIsSeenFalse(roleId, userId);
		if (!data.isEmpty()) {
			return data;
		} else {
			return Collections.emptyList();

		}

	}

	public List<NotificationData> notificationBackEndData(Long roleId) {
		List<NotificationData> data = notificationDataDao.findByRoleId(roleId);
		if (!data.isEmpty()) {
			return data;
		} else {
			return Collections.emptyList();

		}

	}

	public void sendNotification(List<NotificationData> notificationDatas, Set<WebSocketSession> sessionList) {
		List<NotificationData> notificationData = new LinkedList<>();
		for (NotificationData notificationDatassssss : notificationDatas) {
			NotificationData notfi = new NotificationData();
			notfi.setNotificationId(notificationDatassssss.getNotificationId());
			notfi.setFkUserId(notificationDatassssss.getFkUserId());
			notfi.setIsSeen(notificationDatassssss.getIsSeen());
			notfi.setIsEnabled(notificationDatassssss.getIsEnabled());
			notfi.setMessage(notificationDatassssss.getMessage());
			notfi.setData(notificationDatassssss.getData());
			notfi.setNotificationType(notificationDatassssss.getNotificationType());
			notfi.setRoleId(notificationDatassssss.getRoleId());
			notfi.setCreatedAt1(notificationDatassssss.getCreatedAt().getTime());
			notfi.setReturnUrl(notificationDatassssss.getReturnUrl());
			notificationData.add(notfi);
		}

		if (sessionList != null)
			sessionList.parallelStream().forEach(session -> {
				Object obj = session;

				try {
					synchronized (obj) {
						Map<String, Object> response = new HashMap<>();
						session.sendMessage(new TextMessage(GSON.toJson(notificationData)));
					}
				} catch (IOException e) {
					LOGGER.catching(e);
				}
			});
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		throw new RunTimeException("SOMTHING WENT WRONG");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		allSessions.remove(session);
		users.entrySet().parallelStream().forEach(a -> a.getValue().remove(session));
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	public void sendDataToUser(List<NotificationData> notificationData) {

		List<NotificationData> notificationDataSend = new ArrayList<>();
		users.entrySet().parallelStream().forEachOrdered(a -> {
			Set<WebSocketSession> currentSession = new HashSet<>();
			notificationData.parallelStream().forEachOrdered(b -> {
				if (a.getKey().equals(b.getFkUserId())) {
					Set<WebSocketSession> currentSessions = new HashSet<>();
					notificationDataSend.add(b);
					currentSessions = users.get(a.getKey());
					currentSession.addAll(currentSessions);
				}
			});
			LOGGER.info("Send Data to User Method : ");
			sendNotification(notificationDataSend, currentSession);
		});
	}

	public void connectionClose(WebSocketSession session, Long userId) {
		users.entrySet().forEach(a -> {
			if (a.getKey().equals(userId)) {
				a.getValue().remove(session);
			}
		});

	}
}