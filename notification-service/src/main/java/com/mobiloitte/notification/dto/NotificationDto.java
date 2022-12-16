package com.mobiloitte.notification.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class NotificationDto {
	private String notifiactionId;
	private String message;
	private String roleId;
	private String activityType;
	private String notificationSubType;
	private String notificationType;
	private String languageName;
	private String roles;
	private String key;

	@PositiveOrZero(message = "Page cannot be negative")
	private Integer page;

	@Positive(message = "Page cannot be ZERO or Negative")
	private Integer pageSize;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getNotifiactionId() {
		return notifiactionId;
	}

	public void setNotifiactionId(String notifiactionId) {
		this.notifiactionId = notifiactionId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getNotificationSubType() {
		return notificationSubType;
	}

	public void setNotificationSubType(String notificationSubType) {
		this.notificationSubType = notificationSubType;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

}
