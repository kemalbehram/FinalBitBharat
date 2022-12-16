package com.mobiloitte.usermanagement.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobiloitte.usermanagement.enums.FeedbackStatus;

@Entity
@Table
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedBackId;

	@Column(columnDefinition = "varchar(32) default 'POSITIVE'")
	@Enumerated(EnumType.STRING)
	private FeedbackStatus feedbackStatus = FeedbackStatus.POSITIVE;

	@Lob
	private String feedbackMessage;

	private Long submittedBy;

	private Long tradeId;

	@CreationTimestamp
	private Date createTime;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "fk_user_id")
	private User user;

	public Long getFeedBackId() {
		return feedBackId;
	}

	public void setFeedBackId(Long feedBackId) {
		this.feedBackId = feedBackId;
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

	public Long getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(Long submittedBy) {
		this.submittedBy = submittedBy;
	}

	public Long getTradeId() {
		return tradeId;
	}

	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}