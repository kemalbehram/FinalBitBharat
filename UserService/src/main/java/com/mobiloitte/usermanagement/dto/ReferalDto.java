package com.mobiloitte.usermanagement.dto;

import java.math.BigDecimal;

public class ReferalDto {

	private BigDecimal referalAmountRegister;
	private BigDecimal limit;

	public BigDecimal getLimit() {
		return limit;
	}

	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}

	public BigDecimal getReferalAmountRegister() {
		return referalAmountRegister;
	}

	public void setReferalAmountRegister(BigDecimal referalAmountRegister) {
		this.referalAmountRegister = referalAmountRegister;
	}

}
