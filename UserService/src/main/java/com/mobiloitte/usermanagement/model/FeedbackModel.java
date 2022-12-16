package com.mobiloitte.usermanagement.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.mobiloitte.usermanagement.enums.FeedbackEnum;

@Entity
@Table
public class FeedbackModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long feedbackNewId;

	private Long userId;

	@Lob
	private String message;

	private String phoneNo;

	private String email;

	@Enumerated(EnumType.STRING)
	private FeedbackEnum feedbackEnum;

	public FeedbackEnum getFeedbackEnum() {
		return feedbackEnum;
	}

	public void setFeedbackEnum(FeedbackEnum feedbackEnum) {
		this.feedbackEnum = feedbackEnum;
	}

	public Long getFeedbackNewId() {
		return feedbackNewId;
	}

	public void setFeedbackNewId(Long feedbackNewId) {
		this.feedbackNewId = feedbackNewId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
