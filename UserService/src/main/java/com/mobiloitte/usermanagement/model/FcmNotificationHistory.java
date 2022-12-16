package com.mobiloitte.usermanagement.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
public class FcmNotificationHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fcmHistoryId;
	
	private String type;
	
	private String message;
	
	private String status;
	
	@CreationTimestamp
	private Date creationTime;
	
	private String userName;
	
	private String userEmail;

	public Long getFcmHistoryId() {
		return fcmHistoryId;
	}

	public void setFcmHistoryId(Long fcmHistoryId) {
		this.fcmHistoryId = fcmHistoryId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
}
