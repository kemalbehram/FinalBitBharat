package com.mobiloitte.microservice.wallet.dto;

public class WalletBalanceDto {

	private Long userId;
	private String coinName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

}
