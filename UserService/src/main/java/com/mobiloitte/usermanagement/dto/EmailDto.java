package com.mobiloitte.usermanagement.dto;

import java.util.Map;

import com.mobiloitte.usermanagement.enums.NotiType;

public class EmailDto {
	private Long userId;
	private String key;
	private String email;
	private NotiType notiType;
	Map<String, String> replacementToken;

	public NotiType getNotiType() {
		return notiType;
	}

	public void setNotiType(NotiType notiType) {
		this.notiType = notiType;
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

	public EmailDto(Long userId, String key, String email, Map<String, String> replacementToken) {
		super();
		this.userId = userId;
		this.key = key;
		this.email = email;
		this.replacementToken = replacementToken;
	}

	/**
	 * @param userId
	 * @param key
	 * @param replacementToken
	 */
	public EmailDto(Long userId, String key, Map<String, String> replacementToken) {
		super();
		this.userId = userId;
		this.key = key;
		this.replacementToken = replacementToken;
	}

	/**
	 * @param userId
	 * @param key
	 * @param notiType
	 * @param replacementToken
	 */
	public EmailDto(Long userId, String key, NotiType notiType, Map<String, String> replacementToken) {
		super();
		this.userId = userId;
		this.key = key;
		this.notiType = notiType;
		this.replacementToken = replacementToken;
	}

}