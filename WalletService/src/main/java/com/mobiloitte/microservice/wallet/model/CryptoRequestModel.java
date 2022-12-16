package com.mobiloitte.microservice.wallet.model;

import java.math.BigDecimal;

/**
 * The Class CryptoRequestModel.
 * 
 * @author Ankush Mohapatra
 */
public class CryptoRequestModel {

	/** The account. */
	private String account;

	private String contract;

	/** The coin type. */
	private String coinType;

	/** The address. */
	private String address;

	/** The to address. */
	private String toAddress;

	/** The from address. */
	private String fromAddress;

	/** The wallet file. */
	private String walletFile;

	/** The storage type. */
	private String storageType;

	/** The amount. */
	private BigDecimal amount;

	/** The tag. */
	private String tag;

	/** The user id. */
	private String userId;

	/** The secret key. */
	private String secretKey;

	private String toHexAddress;

	private String fromHexAddress;

	/** The wallet password. */
	private String walletPassword;

	/** The to eos account. */
	private String fromEosAccount;


	private String wif;

	/**
	 * Gets the account.
	 *
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Sets the account.
	 *
	 * @param account the new account
	 */
	public void setAccount(String account) {
		this.account = account;
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
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the from address.
	 *
	 * @return the from address
	 */
	public String getFromAddress() {
		return fromAddress;
	}

	/**
	 * Sets the from address.
	 *
	 * @param fromAddress the new from address
	 */
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
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

	/**
	 * Gets the wallet file.
	 *
	 * @return the wallet file
	 */
	public String getWalletFile() {
		return walletFile;
	}

	/**
	 * Sets the wallet file.
	 *
	 * @param walletFile the new wallet file
	 */
	public void setWalletFile(String walletFile) {
		this.walletFile = walletFile;
	}

	/**
	 * Gets the secret key.
	 *
	 * @return the secret key
	 */
	public String getSecretKey() {
		return secretKey;
	}

	/**
	 * Sets the secret key.
	 *
	 * @param secretKey the new secret key
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * Gets the wallet password.
	 *
	 * @return the wallet password
	 */
	public String getWalletPassword() {
		return walletPassword;
	}

	/**
	 * Sets the wallet password.
	 *
	 * @param walletPassword the new wallet password
	 */
	public void setWalletPassword(String walletPassword) {
		this.walletPassword = walletPassword;
	}

	/**
	 * Gets the from eos account.
	 *
	 * @return the from eos account
	 */
	public String getFromEosAccount() {
		return fromEosAccount;
	}

	/**
	 * Sets the from eos account.
	 *
	 * @param fromEosAccount the new from eos account
	 */
	public void setFromEosAccount(String fromEosAccount) {
		this.fromEosAccount = fromEosAccount;
	}

	public String getToHexAddress() {
		return toHexAddress;
	}

	public void setToHexAddress(String toHexAddress) {
		this.toHexAddress = toHexAddress;
	}

	public String getFromHexAddress() {
		return fromHexAddress;
	}

	public void setFromHexAddress(String fromHexAddress) {
		this.fromHexAddress = fromHexAddress;
	}

	public String getWif() {
		return wif;
	}

	public void setWif(String wif) {
		this.wif = wif;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

}
