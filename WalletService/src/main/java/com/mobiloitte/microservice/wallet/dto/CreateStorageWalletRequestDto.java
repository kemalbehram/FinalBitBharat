package com.mobiloitte.microservice.wallet.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * The Class CreateStorageWalletRequestDto.
 * @author Ankush Mohapatra
 */
public class CreateStorageWalletRequestDto {
	
	/** The coin name. */
	@NotBlank
	@Pattern(regexp = "[A-Z]+", message="Should be in uppercase and should be following cryptocoin shortname standards")
	private String coinName;
	
	/** The storage type. */
	@NotBlank
	@Pattern(regexp = "(HOT|COLD)", message="Should be HOT or COLD in uppercase")
	private String storageType;

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
	 * Gets the storage type.
	 *
	 * @return the storage type
	 */
	public String getStorageType() {
		return storageType;
	}

	/**
	 * Sets the storage type.
	 *
	 * @param storageType the new storage type
	 */
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
}
