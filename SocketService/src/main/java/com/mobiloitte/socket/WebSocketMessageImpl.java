package com.mobiloitte.socket;

import java.util.Map;

import org.springframework.web.socket.WebSocketMessage;

import com.google.gson.Gson;
import com.mobiloitte.socket.enums.MessageType;

public class WebSocketMessageImpl implements WebSocketMessage<Map<String, Object>> {
	private static Gson gson = new Gson();
	private MessageType messageType;
	private Map<String, Object> payload;

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public void setPayload(Map<String, Object> payload) {
		this.payload = payload;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	@Override
	public int getPayloadLength() {
		String json = gson.toJson(payload);
		return json.length();
	}

	@Override
	public boolean isLast() {
		return false;
	}

	@Override
	public String toString() {
		return "WebSocketMessageImpl [messageType=" + messageType + ", payload=" + payload + "]";
	}

	public WebSocketMessageImpl() {
		super();
	}

	public WebSocketMessageImpl(MessageType messageType, Map<String, Object> payload) {
		super();
		this.messageType = messageType;
		this.payload = payload;
	}

}
