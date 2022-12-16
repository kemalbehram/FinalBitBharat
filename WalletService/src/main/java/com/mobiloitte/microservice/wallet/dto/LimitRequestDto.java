package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Positive;

public class LimitRequestDto {

	@Positive(message = "limitId cannot be negative")
	private Long limitId;
	
	@Positive(message = "limitAmount cannot be negative")
	private BigDecimal limitAmount;

	public Long getLimitId() {
		return limitId;
	}

	public void setLimitId(Long limitId) {
		this.limitId = limitId;
	}

	public BigDecimal getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(BigDecimal limitAmount) {
		this.limitAmount = limitAmount;
	}
	
	
}
