package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

/**
 * The Class GetBalanceResponseDto.
 * @author Ankush Mohapatra
 */
public class GetBalanceResponseDto {

	/** The wallet balance. */
	private BigDecimal walletBalance;
	
	/** The blocked balance. */
	private BigDecimal blockedBalance;

	/**
	 * Gets the wallet balance.
	 *
	 * @return the wallet balance
	 */
	public BigDecimal getWalletBalance() {
		return walletBalance;
	}

	/**
	 * Sets the wallet balance.
	 *
	 * @param walletBalance the new wallet balance
	 */
	public void setWalletBalance(BigDecimal walletBalance) {
		this.walletBalance = walletBalance;
	}

	/**
	 * Gets the blocked balance.
	 *
	 * @return the blocked balance
	 */
	public BigDecimal getBlockedBalance() {
		return blockedBalance;
	}

	/**
	 * Sets the blocked balance.
	 *
	 * @param blockedBalance the new blocked balance
	 */
	public void setBlockedBalance(BigDecimal blockedBalance) {
		this.blockedBalance = blockedBalance;
	}

}
