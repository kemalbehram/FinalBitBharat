package com.mobiloitte.content.dto;

import javax.persistence.Lob;

import com.mobiloitte.content.enums.LogType;

public class ActivityLogDto {

	private Long activityLogsId;

	private long entityId;

	private String entityType;

	private LogType logType;

	@Lob
	private String message;

	private String ipAddress;

	private String location;

	private Long userId;

	private String role;

	private String email;

	public Long getActivityLogsId() {
		return activityLogsId;
	}

	public void setActivityLogsId(Long activityLogsId) {
		this.activityLogsId = activityLogsId;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
