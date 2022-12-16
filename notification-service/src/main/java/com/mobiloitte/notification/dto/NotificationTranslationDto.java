package com.mobiloitte.notification.dto;

import java.util.Date;

public class NotificationTranslationDto {

	private Long translationId;

	private String message;

	private Date createdAt;

	private Date updatedAt;

	private String createdBy;

	private String updatedBy;

	private Long fkLanguageId;

	public Long getTranslationId() {
		return translationId;
	}

	public void setTranslationId(Long translationId) {
		this.translationId = translationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getFkLanguageId() {
		return fkLanguageId;
	}

	public void setFkLanguageId(Long fkLanguageId) {
		this.fkLanguageId = fkLanguageId;
	}

	public NotificationTranslationDto(Long translationId, String message, Date createdAt, Date updatedAt,
			Long fkLanguageId) {
		super();
		this.translationId = translationId;
		this.message = message;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.fkLanguageId = fkLanguageId;
	}

}
