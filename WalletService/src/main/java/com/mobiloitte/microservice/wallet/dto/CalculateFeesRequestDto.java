package com.mobiloitte.microservice.wallet.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * The Class CalculateFeesRequestDto.
 * 
 * @author Ankush Mohapatra
 */
public class CalculateFeesRequestDto {

	/** The coin name. */
	@NotBlank
	@Pattern(regexp = "[A-Z]+", message = "Should be in uppercase and should be following cryptocoin shortname standards")
	private String coinName;

	/** The fee type. */
	/*
	 * @NotBlank
	 * 
	 * @Pattern(regexp = "(WITHDRAW|TAKER_FEE|MAKER_FEE)",
	 * message="Should be WITHDRAW, TAKER_FEE and MAKER_FEE of type") private String
	 * feeType;
	 */
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
	 * Gets the fee type.
	 *
	 * @return the fee type
	 */

}
