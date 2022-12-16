package com.mobiloitte.server.authorization.model;

public class DeviceTokenDetail {

	private String deviceToken;
	private String deviceType;
	
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
	public DeviceTokenDetail(String deviceToken, String deviceType) {
		super();
		this.deviceToken = deviceToken;
		this.deviceType = deviceType;
	}
	public DeviceTokenDetail() {

	}
	
	
	
}
