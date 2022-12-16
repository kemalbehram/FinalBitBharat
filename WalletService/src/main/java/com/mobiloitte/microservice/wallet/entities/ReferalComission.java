package com.mobiloitte.microservice.wallet.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class ReferalComission {
	private Long referalComissionId;

	private String userName;

	private String email;

	private Long userId;

	private String mobileNo;

	private Long referedId;
	private BigDecimal liveAmount;
	private String coinName;
	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	private BigDecimal depositLiveAmount;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getLiveAmount() {
		return liveAmount;
	}

	public void setLiveAmount(BigDecimal liveAmount) {
		this.liveAmount = liveAmount;
	}

	public BigDecimal getDepositLiveAmount() {
		return depositLiveAmount;
	}

	public void setDepositLiveAmount(BigDecimal depositLiveAmount) {
		this.depositLiveAmount = depositLiveAmount;
	}

	public Long getReferalComissionId() {
		return referalComissionId;
	}

	public void setReferalComissionId(Long referalComissionId) {
		this.referalComissionId = referalComissionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Long getReferedId() {
		return referedId;
	}

	public void setReferedId(Long referedId) {
		this.referedId = referedId;
	}

}
