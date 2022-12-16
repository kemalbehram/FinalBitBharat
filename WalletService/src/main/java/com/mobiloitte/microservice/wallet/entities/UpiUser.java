package com.mobiloitte.microservice.wallet.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class UpiUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userUpiId;

	private Long userId;

	private String upiId;

	private String name;

	public Long getUserUpiId() {
		return userUpiId;
	}

	public void setUserUpiId(Long userUpiId) {
		this.userUpiId = userUpiId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUpiId() {
		return upiId;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
