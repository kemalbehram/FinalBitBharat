package com.mobiloitte.notification.dto;

import com.google.gson.Gson;

public class SocketPayloads {
	private static Gson gson = new Gson();
	private String activity;
	private String message;
	private String fromEmail;
	private String toEmail;
	private String topic;
	private String roomId;
	private String fileUrl;
	private String notificationUserType;

	public static Gson getGson() {
		return gson;
	}

	public static void setGson(Gson gson) {
		SocketPayloads.gson = gson;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getNotificationUserType() {
		return notificationUserType;
	}

	public void setNotificationUserType(String notificationUserType) {
		this.notificationUserType = notificationUserType;
	}

}
