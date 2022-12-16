package com.mobiloitte.microservice.wallet.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * The Class ColdStorageRequestDto.
 * @author Ankush Mohapatra
 */
public class ColdStorageRequestDto {
	
	/** The coin name. */
	@NotBlank
	@Pattern(regexp = "[A-Z]+", message="Should be in uppercase and should be following cryptocoin shortname standards")
	private String coinName;
	
	/** The cold address. */
	@NotBlank(message="Should not be blank and enter a valid address as per the following crypto coin standards")
	private String coldAddress;

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
	 * Gets the cold address.
	 *
	 * @return the cold address
	 */
	public String getColdAddress() {
		return coldAddress;
	}

	/**
	 * Sets the cold address.
	 *
	 * @param coldAddress the new cold address
	 */
	public void setColdAddress(String coldAddress) {
		this.coldAddress = coldAddress;
	}
	
	

}
