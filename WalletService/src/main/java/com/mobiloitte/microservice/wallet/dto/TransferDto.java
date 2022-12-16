package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class TransferDto {
	@NotBlank
	@Pattern(regexp = "[A-Z]+", message = "Should be in uppercase and should be following cryptocoin shortname standards")
	private String coinName;

	/** The to address. */
	@NotBlank(message = "Should not be blank and enter a valid address")
	private String toAddress;

	/** The amount. */
	@Positive(message = "Amount cannot be negative or 0, must be greater than 0")
	private BigDecimal amount;

	/** The is withdraw. */
	@NotNull(message = "isWithdraw not be NULL")
	@AssertFalse(message = "Must be 'false' and type must be in Boolean")
	private Boolean isWithdraw;

	private String fromAddress;

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Boolean getIsWithdraw() {
		return isWithdraw;
	}

	public void setIsWithdraw(Boolean isWithdraw) {
		this.isWithdraw = isWithdraw;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

}
