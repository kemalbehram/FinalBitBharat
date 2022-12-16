package com.mobiloitte.usermanagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class FCMKeyDetails {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fcmKeyId;

	private Long adminId;
	
	private String fcmKey;

	public Long getFcmKeyId() {
		return fcmKeyId;
	}

	public void setFcmKeyId(Long fcmKeyId) {
		this.fcmKeyId = fcmKeyId;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getFcmKey() {
		return fcmKey;
	}

	public void setFcmKey(String fcmKey) {
		this.fcmKey = fcmKey;
	}
	
	
	
}
