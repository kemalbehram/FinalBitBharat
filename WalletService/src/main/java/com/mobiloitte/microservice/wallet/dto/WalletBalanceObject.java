package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

public class WalletBalanceObject {
	
	private String coinName;

	private BigDecimal walletBalance;

	private BigDecimal blockedBalance;

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public BigDecimal getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(BigDecimal walletBalance) {
		this.walletBalance = walletBalance;
	}

	public BigDecimal getBlockedBalance() {
		return blockedBalance;
	}

	public void setBlockedBalance(BigDecimal blockedBalance) {
		this.blockedBalance = blockedBalance;
	}
	
	
}
