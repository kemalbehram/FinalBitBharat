package com.mobiloitte.notification.dto;

import java.util.Map;

import com.mobiloitte.notification.enums.NotiType;

public class EmailDto {
	private Long userId;

	private String url;

	private String key;

	private String email;
	private NotiType notiType;
	private Map<String, String> replacementToken;

	public NotiType getNotiType() {
		return notiType;
	}

	public void setNotiType(NotiType notiType) {
		this.notiType = notiType;
	}

	private Boolean isEnabled;

	private String notificationType;

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

	public EmailDto(Long userId, String key, String email, Map<String, String> replacementToken) {
		super();
		this.userId = userId;
		this.key = key;
		this.email = email;
		this.replacementToken = replacementToken;
	}

	public EmailDto() {
	}

}
