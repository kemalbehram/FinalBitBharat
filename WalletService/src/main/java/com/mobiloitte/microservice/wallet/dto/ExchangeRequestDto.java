package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import com.mobiloitte.microservice.wallet.enums.PaymentMethod;

public class ExchangeRequestDto {
	
	@NotEmpty
	@Pattern(regexp = "[A-Z]+", message="Should be in uppercase and should be following cryptocoin shortname standards")
	private String execCoin;
	
	@NotEmpty
	@Pattern(regexp = "(USD)", message="Should be in uppercase and should be USD")
	private String baseCoin;
	
	private PaymentMethod paymentMethod;
	
	@Positive(message="Amount cannot be negative or 0, must be greater than 0")
	private BigDecimal execCoinAmount;

	public String getExecCoin() {
		return execCoin;
	}

	public void setExecCoin(String execCoin) {
		this.execCoin = execCoin;
	}

	public String getBaseCoin() {
		return baseCoin;
	}

	public void setBaseCoin(String baseCoin) {
		this.baseCoin = baseCoin;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getExecCoinAmount() {
		return execCoinAmount;
	}

	public void setExecCoinAmount(BigDecimal execCoinAmount) {
		this.execCoinAmount = execCoinAmount;
	}
	
	
}
