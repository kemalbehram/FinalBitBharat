package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

public class DepositInrDto {

	private Long depositId;
	private BigDecimal amount;

	public Long getDepositId() {
		return depositId;
	}

	public void setDepositId(Long depositId) {
		this.depositId = depositId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
