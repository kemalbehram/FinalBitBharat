package com.mobiloitte.server.authorization.model;

public class LoginDetail {
	private String ipAddress;
	private String userAgent;
	private String browserPrint;
	private String email;
	private String socialType;
	private String deviceToken;
	private String deviceType;
	
	
	public String getSocialType() {
		return socialType;
	}

	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getBrowserPrint() {
		return browserPrint;
	}

	public void setBrowserPrint(String browserPrint) {
		this.browserPrint = browserPrint;
	}

	public LoginDetail() {
		super();
	}

	public LoginDetail(String ipAddress, String userAgent, String browserPrint) {
		super();
		this.ipAddress = ipAddress;
		this.userAgent = userAgent;
		this.browserPrint = browserPrint;
	}

}
