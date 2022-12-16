package com.mobiloitte.usermanagement.dto;

public class CommonDto {

	private String ipAddress;

	private String location;

	private Long primaryIdCommonPerRequest;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getPrimaryIdCommonPerRequest() {
		return primaryIdCommonPerRequest;
	}

	public void setPrimaryIdCommonPerRequest(Long primaryIdCommonPerRequest) {
		this.primaryIdCommonPerRequest = primaryIdCommonPerRequest;
	}

}
