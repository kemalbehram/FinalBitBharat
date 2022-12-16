package com.mobiloitte.microservice.wallet.dto;

import java.util.Map;

public class EmailDto {
	private Long userId;

	private String url;

	private String key;

	private String email;

	private Map<String, String> replacementToken;

	private Boolean isEnabled;

	private String notificationType;

	private String languageShortName;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, String> getReplacementToken() {
		return replacementToken;
	}

	public void setReplacementToken(Map<String, String> replacementToken) {
		this.replacementToken = replacementToken;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getLanguageShortName() {
		return languageShortName;
	}

	public void setLanguageShortName(String languageShortName) {
		this.languageShortName = languageShortName;
	}

	public EmailDto(Long userId, String key, String languageShortName, String email,
			Map<String, String> replacementToken) {
		super();
		this.userId = userId;
		this.key = key;
		this.email = email;
		this.languageShortName = languageShortName;
		this.replacementToken = replacementToken;
	}

}
