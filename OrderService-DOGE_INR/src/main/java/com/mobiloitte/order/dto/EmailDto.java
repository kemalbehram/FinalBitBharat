package com.mobiloitte.order.dto;

import java.util.Map;

public class EmailDto {
	private Long userId;

	private String key;
	private String email;
	Map<String, String> replacementToken;

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