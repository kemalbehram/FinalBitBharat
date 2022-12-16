package com.mobiloitte.notification.dto;

import java.util.Date;
import java.util.Map;

public class ExchangeAlertControlDto {
	private Long id;
	private String alertType;
	private String value;
	private Integer timeframe;
	private String alertTo;
	private String message;
	private String subject;
	private Map<String, String> replacementToken;

	public String getSubject() {
		return subject;
	}

	public Map<String, String> getReplacementToken() {
		return replacementToken;
	}

	public void setReplacementToken(Map<String, String> replacementToken) {
		this.replacementToken = replacementToken;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getTimeframe() {
		return timeframe;
	}

	public void setTimeframe(Integer timeframe) {
		this.timeframe = timeframe;
	}

	public String getAlertTo() {
		return alertTo;
	}

	public void setAlertTo(String alertTo) {
		this.alertTo = alertTo;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	private Date createdAt;
	private Long createdBy;

	private Date updatedAt;
	private Long updatedBy;

}
