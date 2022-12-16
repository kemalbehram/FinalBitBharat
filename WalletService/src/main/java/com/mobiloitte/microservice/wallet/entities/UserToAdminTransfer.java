package com.mobiloitte.microservice.wallet.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class UserToAdminTransfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userToAdminTransferId;

	private Long user1Id;
	private Long user2Id;
	private BigDecimal amount;
	private String baseCoin;
	private String exeCoin;
	private String toAddress;
	private String user1Status;
	private String user2Status;
	private String txnType;
	private String fromAddress;
	private String toTagId;

	private String toRandomId;
	private Date creationTime;
	private Date updationTime;
	public Long getUserToAdminTransferId() {
		return userToAdminTransferId;
	}
	public void setUserToAdminTransferId(Long userToAdminTransferId) {
		this.userToAdminTransferId = userToAdminTransferId;
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
	public String getUser1Status() {
		return user1Status;
	}
	public void setUser1Status(String user1Status) {
		this.user1Status = user1Status;
	}
	public String getUser2Status() {
		return user2Status;
	}
	public void setUser2Status(String user2Status) {
		this.user2Status = user2Status;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getToTagId() {
		return toTagId;
	}
	public void setToTagId(String toTagId) {
		this.toTagId = toTagId;
	}
	public String getToRandomId() {
		return toRandomId;
	}
	public void setToRandomId(String toRandomId) {
		this.toRandomId = toRandomId;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getUpdationTime() {
		return updationTime;
	}
	public void setUpdationTime(Date updationTime) {
		this.updationTime = updationTime;
	}





}
