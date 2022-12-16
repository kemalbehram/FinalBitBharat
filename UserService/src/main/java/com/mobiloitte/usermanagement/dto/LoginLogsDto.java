package com.mobiloitte.usermanagement.dto;

import java.util.Date;

public class LoginLogsDto {

	private Long userId;

	private String email;

	private Date createTime;

	private String ipAddress;

	private String browserPrint;

	private String userAgent;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getBrowserPrint() {
		return browserPrint;
	}

	public void setBrowserPrint(String browserPrint) {
		this.browserPrint = browserPrint;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

}
