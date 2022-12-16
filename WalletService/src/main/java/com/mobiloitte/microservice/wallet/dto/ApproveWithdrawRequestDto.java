package com.mobiloitte.microservice.wallet.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * The Class ApproveWithdrawRequestDto.
 * @author Ankush Mohapatra
 */
public class ApproveWithdrawRequestDto {
	
	/** The withdraw token. */
	@NotBlank(message="Should not be blank")
	private String withdrawToken;
	
	/** The is withdraw. */
	@NotNull(message="Should not be NULL")
	@AssertTrue(message = "Must be 'true' and type must be in Boolean")
	private Boolean isWithdraw;

	/**
	 * Gets the withdraw token.
	 *
	 * @return the withdraw token
	 */
	public String getWithdrawToken() {
		return withdrawToken;
	}

	/**
	 * Sets the withdraw token.
	 *
	 * @param withdrawToken the new withdraw token
	 */
	public void setWithdrawToken(String withdrawToken) {
		this.withdrawToken = withdrawToken;
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
}
