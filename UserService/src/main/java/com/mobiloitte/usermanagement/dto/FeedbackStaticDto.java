package com.mobiloitte.usermanagement.dto;

import com.mobiloitte.usermanagement.enums.FeedbackEnum;

public class FeedbackStaticDto {

	private String message;

	private String phoneNo;

	private String email;

	private Long feedbackNewId;

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
