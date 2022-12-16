package com.mobiloitte.usermanagement.controller;

import java.math.BigDecimal;

public class ReferalUpdateDto {
	private BigDecimal referalAmountRegister;
	private String limit;

	public BigDecimal getReferalAmountRegister() {
		return referalAmountRegister;
	}

	public void setReferalAmountRegister(BigDecimal referalAmountRegister) {
		this.referalAmountRegister = referalAmountRegister;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

}
