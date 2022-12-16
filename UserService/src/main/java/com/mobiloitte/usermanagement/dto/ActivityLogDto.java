package com.mobiloitte.usermanagement.dto;

import java.util.Date;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiloitte.usermanagement.enums.LogType;

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
	
	
	public ActivityLogDto(String entityType, Long entityId, Long userId, String ipAddress,
			String location) {
		
		super();
		this.entityType = entityType;
		this.entityId = entityId;
		this.userId = userId;
		this.ipAddress = ipAddress;
		this.location = location;
		
	}
	
	public ActivityLogDto(LogType logType, String message, String email, String role) {
		super();
		this.logType = logType;
		this.message = message;
		this.email = email;
		this.role = role;
	}
	
	
	
	public ActivityLogDto() {
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
