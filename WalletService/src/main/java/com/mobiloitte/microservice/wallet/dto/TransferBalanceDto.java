package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

/**
 * The Class TransferBalanceDto.
 * @author Ankush Mohapatra
 */
public class TransferBalanceDto {
	
	/** The coin 1. */
	@NotBlank
	@Pattern(regexp = "[A-Z]+", message="Should be in uppercase and should be following cryptocoin shortname standards")
	private String coin1;
	
	/** The coin 2. */
	@NotBlank
	@Pattern(regexp = "[A-Z]+", message="Should be in uppercase and should be following cryptocoin shortname standards")
	private String coin2;
	
	/** The user 1. */
	@Positive(message="userId cannot be negative or 0, must be greater than 0")
	private Long user1;
	
	/** The user 2. */
	@Positive(message="userId cannot be negative or 0, must be greater than 0")
	private Long user2;
	
	/** The amount 1. */
	@Positive(message="Amount cannot be negative or 0, must be greater than 0")
	private BigDecimal amount1;
	
	/** The amount 2. */
	@Positive(message="Amount cannot be negative or 0, must be greater than 0")
	private BigDecimal amount2;
	
	/** The order 1. */
	@Positive(message="orderId cannot be negative or 0, must be greater than 0")
	private Long order1;
	
	/** The order 2. */
	@Positive(message="orderId cannot be negative or 0, must be greater than 0")
	private Long order2;


	/**
	 * Gets the coin 1.
	 *
	 * @return the coin 1
	 */
	public String getCoin1() {
		return coin1;
	}

	/**
	 * Sets the coin 1.
	 *
	 * @param coin1 the new coin 1
	 */
	public void setCoin1(String coin1) {
		this.coin1 = coin1;
	}

	/**
	 * Gets the coin 2.
	 *
	 * @return the coin 2
	 */
	public String getCoin2() {
		return coin2;
	}

	/**
	 * Sets the coin 2.
	 *
	 * @param coin2 the new coin 2
	 */
	public void setCoin2(String coin2) {
		this.coin2 = coin2;
	}

	/**
	 * Gets the user 1.
	 *
	 * @return the user 1
	 */
	public Long getUser1() {
		return user1;
	}

	/**
	 * Sets the user 1.
	 *
	 * @param user1 the new user 1
	 */
	public void setUser1(Long user1) {
		this.user1 = user1;
	}

	/**
	 * Gets the user 2.
	 *
	 * @return the user 2
	 */
	public Long getUser2() {
		return user2;
	}

	/**
	 * Sets the user 2.
	 *
	 * @param user2 the new user 2
	 */
	public void setUser2(Long user2) {
		this.user2 = user2;
	}

	/**
	 * Gets the amount 1.
	 *
	 * @return the amount 1
	 */
	public BigDecimal getAmount1() {
		return amount1;
	}

	/**
	 * Sets the amount 1.
	 *
	 * @param amount1 the new amount 1
	 */
	public void setAmount1(BigDecimal amount1) {
		this.amount1 = amount1;
	}

	/**
	 * Gets the amount 2.
	 *
	 * @return the amount 2
	 */
	public BigDecimal getAmount2() {
		return amount2;
	}

	/**
	 * Sets the amount 2.
	 *
	 * @param amount2 the new amount 2
	 */
	public void setAmount2(BigDecimal amount2) {
		this.amount2 = amount2;
	}

	/**
	 * Gets the order 1.
	 *
	 * @return the order 1
	 */
	public Long getOrder1() {
		return order1;
	}

	/**
	 * Sets the order 1.
	 *
	 * @param order1 the new order 1
	 */
	public void setOrder1(Long order1) {
		this.order1 = order1;
	}

	/**
	 * Gets the order 2.
	 *
	 * @return the order 2
	 */
	public Long getOrder2() {
		return order2;
	}

	/**
	 * Sets the order 2.
	 *
	 * @param order2 the new order 2
	 */
	public void setOrder2(Long order2) {
		this.order2 = order2;
	}

	
}
