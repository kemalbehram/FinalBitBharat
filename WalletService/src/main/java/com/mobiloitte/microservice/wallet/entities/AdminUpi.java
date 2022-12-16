package com.mobiloitte.microservice.wallet.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class AdminUpi {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long adminUpiId;

	private String upiId;

	private String name;

	private Boolean isVisible;

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Long getAdminUpiId() {
		return adminUpiId;
	}

	public void setAdminUpiId(Long adminUpiId) {
		this.adminUpiId = adminUpiId;
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
