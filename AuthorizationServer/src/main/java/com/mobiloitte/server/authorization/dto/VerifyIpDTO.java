package com.mobiloitte.server.authorization.dto;

public class VerifyIpDTO {
	private Long userId;
	private String ipAddress;
	private String webUrl;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public VerifyIpDTO() {

		super();
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	@Override
	public String toString() {
		return "VerifyIpDTO [userId=" + userId + ", ipAddress=" + ipAddress + ", webUrl=" + webUrl + "]";
	}

	public VerifyIpDTO(Long userId, String ipAddress, String webUrl) {
		super();
		this.userId = userId;
		this.ipAddress = ipAddress;
		this.webUrl = webUrl;
	}

}
