package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import com.mobiloitte.microservice.wallet.enums.Network;

public class WithdrawRequestDtoUpdated {
	/** The coin name. */
	@NotBlank
	@Pattern(regexp = "[A-Z]+", message = "Should be in uppercase and should be following cryptocoin shortname standards")
	private String coinName;

	/** The to address. */
	@NotBlank(message = "Should not be blank and enter a valid address")
	private String toAddress;

	/** The amount. */
	@Positive(message = "Amount cannot be negative or 0, must be greater than 0")
	private BigDecimal amount;

	/** The tag. */
	private String tag;

	/** The is withdraw. */
	@NotNull(message = "isWithdraw not be NULL")
	@AssertFalse(message = "Must be 'false' and type must be in Boolean")
	private Boolean isWithdraw;

	@NotNull(message = "isKycAccepted not be NULL")
	private Boolean isKycAccepted;

	private Boolean isExternal;

	private Network network;

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	/** The url. */
	@NotBlank
	private String url;

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
	 * Gets the to address.
	 *
	 * @return the to address
	 */
	public String getToAddress() {
		return toAddress;
	}

	/**
	 * Sets the to address.
	 *
	 * @param toAddress the new to address
	 */
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
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
	 * Gets the tag.
	 *
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the tag.
	 *
	 * @param tag the new tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Gets the checks if is withdraw.
	 *
	 * @return the checks if is withdraw
	 */
	public Boolean getIsWithdraw() {
		return isWithdraw;
	}

	/**
	 * Sets the checks if is withdraw.
	 *
	 * @param isWithdraw the new checks if is withdraw
	 */
	public void setIsWithdraw(Boolean isWithdraw) {
		this.isWithdraw = isWithdraw;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getIsKycAccepted() {
		return isKycAccepted;
	}

	public void setIsKycAccepted(Boolean isKycAccepted) {
		this.isKycAccepted = isKycAccepted;
	}

	public final Boolean getIsExternal() {
		return isExternal;
	}

	public final void setIsExternal(Boolean isExternal) {
		this.isExternal = isExternal;
	}

}
