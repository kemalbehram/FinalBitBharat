package com.mobiloitte.notification.handler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.mobiloitte.notification.dao.ChatDetailsDao;
import com.mobiloitte.notification.dao.RoomsDao;
import com.mobiloitte.notification.dto.SocketPayloads;
import com.mobiloitte.notification.enums.Activity;
import com.mobiloitte.notification.enums.NotificationUserType;
import com.mobiloitte.notification.enums.Topics;
import com.mobiloitte.notification.exception.RunTimeException;
import com.mobiloitte.notification.model.ChatData;
import com.mobiloitte.notification.model.Rooms;

/**
 * @author Abhishek Sharma
 *
 */
@Component
public class CustomWebSocketChatHandlerNew implements WebSocketHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomWebSocketChatHandler.class);
	private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	Map<String, Set<WebSocketSession>> usersEmails = new HashMap<>();
	private static final Gson GSON = new Gson();
	@Autowired
	private ChatDetailsDao chatDetailsDao;
	@Autowired
	private RoomsDao roomsDao;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOGGER.info("Socket Connected with Session {}", session);
		sessions.add(session);

	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		try {
			SocketPayloads socketPayloads = GSON.fromJson(message.getPayload().toString(), SocketPayloads.class);
			if (socketPayloads != null) {
				newMessage(session, socketPayloads);
				if (socketPayloads.getTopic().equals(Activity.LOGOUT)) {
					LOGGER.info("LOGOUT");
					connectionClose(session, socketPayloads.getFromEmail());
				}
			}

		} catch (Exception e) {
			LOGGER.info("Exception Name {}", e);
		}

	}

	public void newMessage(WebSocketSession session, SocketPayloads socketPayloads) {
		Optional<Rooms> roomsId = roomsDao.findByRoomName(Topics.SUPPORT.name());
		final Topics messages = Topics.valueOf(socketPayloads.getTopic());
		String UserType = "";

		if (socketPayloads.getNotificationUserType() != null) {
			UserType = socketPayloads.getNotificationUserType();
		}

		Set<WebSocketSession> webSocketSessions = new HashSet<>();
		webSocketSessions.add(session);
		if (messages.equals(Topics.SUPPORT)) {
			usersEmails.put(socketPayloads.getFromEmail(), webSocketSessions);
			if (UserType.equals(NotificationUserType.ADMIN.name().toString())) {
				if (socketPayloads.getMessage() != null) {
					socketPayloads.setRoomId(roomsId.get().getRoomId());
					sendChat(webSocketSessions, saveChat(socketPayloads), socketPayloads);
				} else {
					sendAdminNowConnected(webSocketSessions, socketPayloads);
					sendWaitForAnother(webSocketSessions, "Please Wait.....");
				}

			} else {
				if (socketPayloads.getMessage() != null) {
					socketPayloads.setRoomId(roomsId.get().getRoomId());
					sendChat(webSocketSessions, saveChat(socketPayloads), socketPayloads);
				} else {
					sendAdminNowConnected(webSocketSessions, socketPayloads);
					sendWaitForAnother(webSocketSessions, "Hi, This is Support team. How can we help you?");
				}
			}
		}
	}

	private void sendChat(Set<WebSocketSession> seesions, ChatData saveChat, SocketPayloads socketPayloads) {
		for (Entry<String, Set<WebSocketSession>> emails : usersEmails.entrySet()) {

			if (!emails.getKey().equals(socketPayloads.getFromEmail())) {
				Map<String, Object> response = new HashMap<>();
				List<ChatData> unseenChatData = chatDetailsDao.findByTopicAndFromEmailAndIsSeenFalse("SUPPORT",
						socketPayloads.getFromEmail());
				unseenChatData.parallelStream().forEachOrdered(chatData -> {
					chatData.setToEmail(emails.getKey());
					chatData.setIsSeen(Boolean.TRUE);
					chatDetailsDao.save(chatData);
				});
				Set<WebSocketSession> seesionssss = emails.getValue();
				response.put("unseenmessage", unseenChatData);
				callSocket(seesionssss, unseenChatData);
			}
		}
		callSocket(seesions, saveChat);
	}

	public void sendAdminNowConnected(Set<WebSocketSession> seesions, SocketPayloads socketPayloads) {
		for (Entry<String, Set<WebSocketSession>> emails : usersEmails.entrySet()) {

			if (!emails.getKey().equals(socketPayloads.getFromEmail())) {
				Map<String, Object> response = new HashMap<>();
				List<ChatData> unseenChatData = chatDetailsDao.findByTopicAndFromEmailAndIsSeenFalse("SUPPORT",
						emails.getKey());
				unseenChatData.parallelStream().forEachOrdered(chatData -> {
					chatData.setToEmail(socketPayloads.getFromEmail());
					chatData.setIsSeen(Boolean.TRUE);
					chatDetailsDao.save(chatData);
				});

				response.put("unseenmessage", unseenChatData);
				callSocket(seesions, unseenChatData);
			}

		}
	}

	public void callSocket(Set<WebSocketSession> seesions, List<ChatData> unseenChatData) {
		if (seesions != null) {
			seesions.parallelStream().forEach(session -> {
				Object obj = session;

				try {
					synchronized (obj) {
						session.sendMessage(new TextMessage(GSON.toJson(unseenChatData)));
					}
				} catch (IOException e) {
					LOGGER.info("Error Occures {}", e);
				}
			});
		}

	}

	public void sendWaitForAnother(Set<WebSocketSession> seesions, String message) {
		if (seesions != null) {
			seesions.parallelStream().forEach(session -> {
				Object obj = session;

				try {
					synchronized (obj) {
						Map<String, Object> response = new HashMap<>();
						response.put("message", message);
						session.sendMessage(new TextMessage(GSON.toJson(response)));
					}
				} catch (IOException e) {
					LOGGER.info("Error Occures {}", e);
				}
			});
		}

	}

	public void callSocket(Set<WebSocketSession> seesions, ChatData unseenChatData) {
		if (seesions != null) {
			seesions.parallelStream().forEach(session -> {
				Object obj = session;

				try {
					synchronized (obj) {
						session.sendMessage(new TextMessage(GSON.toJson(unseenChatData)));
					}
				} catch (IOException e) {
					LOGGER.info("Error Occures {}", e);
				}
			});
		}
	}

	public ChatData saveChat(SocketPayloads socketPayloads) {
		ChatData chatData = new ChatData();
		chatData.setFileUrl(socketPayloads.getFileUrl());
		chatData.setFromEmail(socketPayloads.getFromEmail());
		chatData.setIsSeen(Boolean.FALSE);
		chatData.setMessage(socketPayloads.getMessage());
		chatData.setToEmail(socketPayloads.getToEmail());
		chatData.setTopic(socketPayloads.getTopic());
		chatData.setFkRoomId(socketPayloads.getRoomId());
		return chatDetailsDao.save(chatData);
	}

	public void sendNotification(List<ChatData> chatDatas, Set<WebSocketSession> sessionList) {

		if (sessionList != null)
			sessionList.parallelStream().forEach(session -> {
				Object obj = session;

				try {
					synchronized (obj) {
						session.sendMessage(new TextMessage(GSON.toJson(chatDatas)));
					}
				} catch (IOException e) {
					LOGGER.info("Error Occured {}", e);
				}
			});
	}

	public List<ChatData> notificaionData(String fromEmail) {
		List<ChatData> chatDatas = chatDetailsDao.findByFromEmail(fromEmail);
		if (!chatDatas.isEmpty()) {
			return chatDatas;
		} else {
			return Collections.emptyList();

		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		throw new RunTimeException("SOMTHING WENT WRONG");

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		LOGGER.info("Chat Socket Disconnected with afterConnectionClosed {}", closeStatus);
		sessions.remove(session);

	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	public void connectionClose(WebSocketSession session, String fromEmail) {
		LOGGER.info("Chat Socket Disconnected from connectionClose{}", fromEmail);
		usersEmails.entrySet().forEach(a -> {
			if (a.getKey().equals(fromEmail)) {
				a.getValue().remove(session);
			}
		});

	}

}
