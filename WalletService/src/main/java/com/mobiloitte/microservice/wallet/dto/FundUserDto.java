package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

public class FundUserDto {
	private BigDecimal availableFund;
	private BigDecimal lockedAmount;

	public BigDecimal getLockedAmount() {
		return lockedAmount;
	}

	public void setLockedAmount(BigDecimal lockedAmount) {
		this.lockedAmount = lockedAmount;
	}

	public BigDecimal getAvailableFund() {
		return availableFund;
	}

	public void setAvailableFund(BigDecimal availableFund) {
		this.availableFund = availableFund;
	}

}
