package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

/**
 * The Class TakerMakerFeesRequestDto.
 * @author Ankush Mohapatra
 */
public class TakerMakerFeesRequestDto {
	
	/** The coin name. */
	@NotBlank
	@Pattern(regexp = "[A-Z]+", message="Should be in uppercase and should be following cryptocoin shortname standards")
	private String coinName;
	
	/** The taker fee. */
	@Positive(message="Taker Fee cannot be negative or 0, must be greater than 0")
	private BigDecimal takerFee;
	
	/** The maker fee. */
	@Positive(message="Maker Fee cannot be negative or 0, must be greater than 0")
	private BigDecimal makerFee;

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
	 * Gets the taker fee.
	 *
	 * @return the taker fee
	 */
	public BigDecimal getTakerFee() {
		return takerFee;
	}

	/**
	 * Sets the taker fee.
	 *
	 * @param takerFee the new taker fee
	 */
	public void setTakerFee(BigDecimal takerFee) {
		this.takerFee = takerFee;
	}

	/**
	 * Gets the maker fee.
	 *
	 * @return the maker fee
	 */
	public BigDecimal getMakerFee() {
		return makerFee;
	}

	/**
	 * Sets the maker fee.
	 *
	 * @param makerFee the new maker fee
	 */
	public void setMakerFee(BigDecimal makerFee) {
		this.makerFee = makerFee;
	}
	
	

}
