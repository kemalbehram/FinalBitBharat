package com.mobiloitte.usermanagement.dto;

import java.util.Date;

import com.mobiloitte.usermanagement.enums.FeedbackStatus;

public class FeedbackListDto {

	private Long feedbackId;

	private FeedbackStatus feedbackStatus;

	private String feedbackMessage;

	private long submittedBy;

	private Date createTime;

	public Long getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public FeedbackStatus getFeedbackStatus() {
		return feedbackStatus;
	}

	public void setFeedbackStatus(FeedbackStatus feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}

	public String getFeedbackMessage() {
		return feedbackMessage;
	}

	public void setFeedbackMessage(String feedbackMessage) {
		this.feedbackMessage = feedbackMessage;
	}

	public long getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(long submittedBy) {
		this.submittedBy = submittedBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}