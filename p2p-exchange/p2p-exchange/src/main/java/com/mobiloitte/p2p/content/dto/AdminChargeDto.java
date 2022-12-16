package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

public class AdminChargeDto {

	private BigDecimal fees;
	@NotNull(message = "Should not be NULL")
	@AssertTrue(message = "type must be in String")
	private String coinName;

	public BigDecimal getFees() {
		return fees;
	}

	public void setFees(BigDecimal fees) {
		this.fees = fees;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

}
