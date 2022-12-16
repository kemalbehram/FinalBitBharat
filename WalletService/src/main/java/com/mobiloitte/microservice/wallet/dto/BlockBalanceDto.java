package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

/**
 * The Class BlockBalanceDto.
 * @author Ankush Mohapatra
 */
public class BlockBalanceDto {
	
	/** The coin. */
	@NotBlank
	@Pattern(regexp = "[A-Z]+", message="Should be in uppercase and should be following cryptocoin shortname standards")
	private String coin;
	
	/** The amount to block. */
	@Positive(message="Amount cannot be negative or 0, must be greater than 0")
	private BigDecimal amountToBlock;
	
	/** The user id. */
	@Positive(message="userId cannot be negative or 0, must be greater than 0")
	private Long userId;
	
	/** The order id. */
	@Positive(message="orderId cannot be negative or 0, must be greater than 0")
	private Long orderId;
	
	/**
	 * Gets the coin.
	 *
	 * @return the coin
	 */
	public String getCoin() {
		return coin;
	}

	/**
	 * Sets the coin.
	 *
	 * @param coin the new coin
	 */
	public void setCoin(String coin) {
		this.coin = coin;
	}

	/**
	 * Gets the amount to block.
	 *
	 * @return the amount to block
	 */
	public BigDecimal getAmountToBlock() {
		return amountToBlock;
	}

	/**
	 * Sets the amount to block.
	 *
	 * @param amountToBlock the new amount to block
	 */
	public void setAmountToBlock(BigDecimal amountToBlock) {
		this.amountToBlock = amountToBlock;
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
