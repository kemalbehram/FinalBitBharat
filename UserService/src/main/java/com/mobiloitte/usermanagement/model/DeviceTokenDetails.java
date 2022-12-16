package com.mobiloitte.usermanagement.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class DeviceTokenDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long deviceTokenId;
	private String deviceToken;
	private String deviceType;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_USER_ID")
	private User user;
	
	
	public Long getDeviceTokenId() {
		return deviceTokenId;
	}
	public void setDeviceTokenId(Long deviceTokenId) {
		this.deviceTokenId = deviceTokenId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	
	
	
	
}
