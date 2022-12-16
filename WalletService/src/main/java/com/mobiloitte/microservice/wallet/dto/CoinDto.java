package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

public class CoinDto {
	/** The coin id. */

	/** The coin full name. */
	private String coinFullName;

	/** The coin image. */
	private String coinImage;

	/** The coin short name. */
	private String coinShortName;

	private String category;
	/** The coin type. */
	private String coinType;

	private BigDecimal marketPriceInInr;

	private BigDecimal marketPriceInEur;

	// bi-directional many-to-one association to Wallet

	private String contractAddress;

	/** The storage details. */
	// bi-directional many-to-one association to StorageDetail

	/**
	 * Gets the coin id.
	 *
	 * @return the coin id
	 */

	/**
	 * Gets the coin full name.
	 *
	 * @return the coin full name
	 */

	public String getCoinFullName() {
		return coinFullName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Sets the coin full name.
	 *
	 * @param coinFullName the new coin full name
	 */
	public void setCoinFullName(String coinFullName) {
		this.coinFullName = coinFullName;
	}

	/**
	 * Gets the coin image.
	 *
	 * @return the coin image
	 */
	public String getCoinImage() {
		return coinImage;
	}

	/**
	 * Sets the coin image.
	 *
	 * @param coinImage the new coin image
	 */
	public void setCoinImage(String coinImage) {
		this.coinImage = coinImage;
	}

	/**
	 * Gets the coin short name.
	 *
	 * @return the coin short name
	 */
	public String getCoinShortName() {
		return coinShortName;
	}

	/**
	 * Sets the coin short name.
	 *
	 * @param coinShortName the new coin short name
	 */
	public void setCoinShortName(String coinShortName) {
		this.coinShortName = coinShortName;
	}

	/**
	 * Gets the coin type.
	 *
	 * @return the coin type
	 */
	public String getCoinType() {
		return coinType;
	}

	/**
	 * Sets the coin type.
	 *
	 * @param coinType the new coin type
	 */
	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}

	/**
	 * Gets the wallets.
	 *
	 * @return the wallets
	 */

	/**
	 * Gets the withdrawal amount.
	 *
	 * @return the withdrawal amount
	 */

	/**
	 * Gets the taker fee.
	 *
	 * @return the taker fee
	 */

	/**
	 * Gets the maker fee.
	 *
	 * @return the maker fee
	 */

	/**
	 * Gets the basic buy fee.
	 *
	 * @return the basic buy fee
	 */

	/**
	 * Gets the basic sell fee.
	 *
	 * 
	 * /** Gets the market price in usd.
	 *
	 * 
	 * /** Gets the wallet image url.
	 *
	 * @return the wallet image url
	 */

	/**
	 * Gets the wallet background url.
	 *
	 * @return the wallet background url
	 */

	/**
	 * Gets the wallet dot url.
	 *
	 * @return the wallet dot url
	 */

	public BigDecimal getMarketPriceInInr() {
		return marketPriceInInr;
	}

	public void setMarketPriceInInr(BigDecimal marketPriceInInr) {
		this.marketPriceInInr = marketPriceInInr;
	}

	public BigDecimal getMarketPriceInEur() {
		return marketPriceInEur;
	}

	public void setMarketPriceInEur(BigDecimal marketPriceInEur) {
		this.marketPriceInEur = marketPriceInEur;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

}
