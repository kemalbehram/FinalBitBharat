package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;
import java.util.Date;

public class UserToAdminTransferDto {

	private Long user1Id;
	private Long user2Id;
	private String toAddress;
	private String tagId;
	private BigDecimal amount;
	private String baseCoin;
	private String exeCoin;
	private String status;
	private String txnType;

	private String toRandomId;
	private Date creationTime;

	private Long validityDay;

	public String getToRandomId() {
		return toRandomId;
	}

	public void setToRandomId(String toRandomId) {
		this.toRandomId = toRandomId;
	}

	public Long getUser1Id() {
		return user1Id;
	}

	public void setUser1Id(Long user1Id) {
		this.user1Id = user1Id;
	}

	public Long getUser2Id() {
		return user2Id;
	}

	public void setUser2Id(Long user2Id) {
		this.user2Id = user2Id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBaseCoin() {
		return baseCoin;
	}

	public void setBaseCoin(String baseCoin) {
		this.baseCoin = baseCoin;
	}

	public String getExeCoin() {
		return exeCoin;
	}

	public void setExeCoin(String exeCoin) {
		this.exeCoin = exeCoin;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Long getValidityDay() {
		return validityDay;
	}

	public void setValidityDay(Long validityDay) {
		this.validityDay = validityDay;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

}
