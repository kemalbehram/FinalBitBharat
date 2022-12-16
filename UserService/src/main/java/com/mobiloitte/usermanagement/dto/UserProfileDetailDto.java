package com.mobiloitte.usermanagement.dto;

import java.math.BigDecimal;
import java.util.Date;

public class UserProfileDetailDto {

	private Long userId;

	private String userName;

	private Date accountCreated;

	private Date lastSeen;

	private Date emailVerificationTime;

	private Date idPassportVerificationTime;

	private Long blocksCount;

	private BigDecimal tradeVolume;

	private Long noOfConfirmTrades;

	private Date firstPurchase;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getAccountCreated() {
		return accountCreated;
	}

	public void setAccountCreated(Date accountCreated) {
		this.accountCreated = accountCreated;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	public Date getEmailVerificationTime() {
		return emailVerificationTime;
	}

	public void setEmailVerificationTime(Date emailVerificationTime) {
		this.emailVerificationTime = emailVerificationTime;
	}

	public Date getIdPassportVerificationTime() {
		return idPassportVerificationTime;
	}

	public void setIdPassportVerificationTime(Date idPassportVerificationTime) {
		this.idPassportVerificationTime = idPassportVerificationTime;
	}

	public Long getBlocksCount() {
		return blocksCount;
	}

	public void setBlocksCount(Long blocksCount) {
		this.blocksCount = blocksCount;
	}

	public BigDecimal getTradeVolume() {
		return tradeVolume;
	}

	public void setTradeVolume(BigDecimal tradeVolume) {
		this.tradeVolume = tradeVolume;
	}

	public Long getNoOfConfirmTrades() {
		return noOfConfirmTrades;
	}

	public void setNoOfConfirmTrades(Long noOfConfirmTrades) {
		this.noOfConfirmTrades = noOfConfirmTrades;
	}

	public Date getFirstPurchase() {
		return firstPurchase;
	}

	public void setFirstPurchase(Date firstPurchase) {
		this.firstPurchase = firstPurchase;
	}

}