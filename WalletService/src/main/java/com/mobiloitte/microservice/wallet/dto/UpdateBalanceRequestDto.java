package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

/**
 * The Class UpdateBalanceRequestDto.
 * @author Ankush Mohapatra
 */
public class UpdateBalanceRequestDto {
	
	/** The coin name. */
	@NotBlank
	@Pattern(regexp = "[A-Z]+", message="Should be in uppercase and should be following cryptocoin shortname standards")
	private String coinName;
	
	/** The amount. */
	@Positive(message="Amount cannot be negative or 0, must be greater than 0")
	private BigDecimal amount;
	
	/** The user id. */
	@Positive(message="userId cannot be negative or 0, must be greater than 0")
	private Long userId;
	
	/** The order id. */
	@Positive(message="orderId cannot be negative or 0, must be greater than 0")
	private Long orderId;

	/**
	 * Gets the coin name.
	 *
	 * @return the coin name
	 */
	public String getCoinName() {
		return coinName;
	}

	/**
	 * Sets the coin name.
	 *
	 * @param coinName the new coin name
	 */
	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Sets the amount.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the order id.
	 *
	 * @return the order id
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * Sets the order id.
	 *
	 * @param orderId the new order id
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	
}
