package com.mobiloitte.notification.dto;

import java.util.Date;
import java.util.Map;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.google.gson.Gson;
import com.mobiloitte.notification.enums.MessageFormat;
import com.mobiloitte.notification.enums.MessageType;
import com.mobiloitte.notification.enums.UserType;

public class ExchangePayloads {
	private Long userId;
	private Long toUserId;
	private static Gson gson = new Gson();
	private MessageType messageType;
	private Map<String, Object> params;
	@Lob
	private String message;
	private UserType userType;
	private MessageFormat messageFormat;
	private Long roleId;
	private String role;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updationTime;
	private Long orderId;
	private String tradeId;
	private String returnUrl;

	private String notificationUserType;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public static Gson getGson() {
		return gson;
	}

	public static void setGson(Gson gson) {
		ExchangePayloads.gson = gson;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public MessageFormat getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(MessageFormat messageFormat) {
		this.messageFormat = messageFormat;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUpdationTime() {
		return updationTime;
	}

	public void setUpdationTime(Date updationTime) {
		this.updationTime = updationTime;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getNotificationUserType() {
		return notificationUserType;
	}

	public void setNotificationUserType(String notificationUserType) {
		this.notificationUserType = notificationUserType;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}
