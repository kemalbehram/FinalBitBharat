package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The Class TransactionListDto.
 * 
 * @author Ankush Mohapatra
 */
public class TransactionListDto {

	/** The txn id. */
	private Long txnId;

	/** The coin type. */
	private String coinType;

	/** The status. */
	private String status;

	/** The txn hash. */
	private String txnHash;

	/** The amount. */
	private BigDecimal amount;

	/** The txn time. */
	private Date txnTime;

	/** The txn type. */
	private String txnType;

	/** The user name. */
	private String userName;

	/** The user email. */
	private String userEmail;

	private String address;

	/**
	 * Gets the txn id.
	 *
	 * @return the txn id
	 */
	public Long getTxnId() {
		return txnId;
	}

	/**
	 * Sets the txn id.
	 *
	 * @param txnId the new txn id
	 */
	public void setTxnId(Long txnId) {
		this.txnId = txnId;
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
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the txn hash.
	 *
	 * @return the txn hash
	 */
	public String getTxnHash() {
		return txnHash;
	}

	/**
	 * Sets the txn hash.
	 *
	 * @param txnHash the new txn hash
	 */
	public void setTxnHash(String txnHash) {
		this.txnHash = txnHash;
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
	 * Gets the txn time.
	 *
	 * @return the txn time
	 */
	public Date getTxnTime() {
		return txnTime;
	}

	/**
	 * Sets the txn time.
	 *
	 * @param txnTime the new txn time
	 */
	public void setTxnTime(Date txnTime) {
		this.txnTime = txnTime;
	}

	/**
	 * Gets the txn type.
	 *
	 * @return the txn type
	 */
	public String getTxnType() {
		return txnType;
	}

	/**
	 * Sets the txn type.
	 *
	 * @param txnType the new txn type
	 */
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the user email.
	 *
	 * @return the user email
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * Sets the user email.
	 *
	 * @param userEmail the new user email
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
