package com.mobiloitte.microservice.wallet.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * The Class CryptoResponseModel.
 * 
 * @author Ankush Mohapatra
 */
public class CryptoResponseModel {

	/** The address. */
	private String address;

	/** The private key. */
	private String privateKey;

	/** The balance. */
	private BigDecimal balance;

	/** The to address. */
	private String toAddress;

	private String fromAddress;

	/** The txn hash. */
	private String txnHash;

	/** The txn fee. */
	private BigDecimal txnFee;

	/** The coin type. */
	private String coinType;

	/** The actual amount. */
	private BigDecimal actualAmount;

	/** The storage type. */
	private String storageType;

	/** The txn type. */
	private String txnType;

	/** The eos acc name. */
	private String eosAccName;

	private BigDecimal depositeCurrentPrice;
	private BigDecimal livePrice;

	public BigDecimal getLivePrice() {
		return livePrice;
	}

	public void setLivePrice(BigDecimal livePrice) {
		this.livePrice = livePrice;
	}
	public BigDecimal getDepositeCurrentPrice() {
		return depositeCurrentPrice;
	}

	public void setDepositeCurrentPrice(BigDecimal depositeCurrentPrice) {
		this.depositeCurrentPrice = depositeCurrentPrice;
	}

	private String hexAddress;

	private String wif;

	/** The deposit list. */
	private List<Map<String, Object>> depositList;

	/** The deposit list type 2. */
	private List<Map<String, Object>> depositListType2;

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
	 * Gets the private key.
	 *
	 * @return the private key
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * Sets the private key.
	 *
	 * @param privateKey the new private key
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * Gets the balance.
	 *
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * Sets the balance.
	 *
	 * @param balance the new balance
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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
	 * Gets the txn fee.
	 *
	 * @return the txn fee
	 */
	public BigDecimal getTxnFee() {
		return txnFee;
	}

	/**
	 * Sets the txn fee.
	 *
	 * @param txnFee the new txn fee
	 */
	public void setTxnFee(BigDecimal txnFee) {
		this.txnFee = txnFee;
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
	 * Gets the actual amount.
	 *
	 * @return the actual amount
	 */
	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	/**
	 * Sets the actual amount.
	 *
	 * @param actualAmount the new actual amount
	 */
	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
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
	 * Gets the eos acc name.
	 *
	 * @return the eos acc name
	 */
	public String getEosAccName() {
		return eosAccName;
	}

	/**
	 * Sets the eos acc name.
	 *
	 * @param eosAccName the new eos acc name
	 */
	public void setEosAccName(String eosAccName) {
		this.eosAccName = eosAccName;
	}

	public String getHexAddress() {
		return hexAddress;
	}

	public void setHexAddress(String hexAddress) {
		this.hexAddress = hexAddress;
	}

	/**
	 * Gets the deposit list.
	 *
	 * @return the deposit list
	 */
	public List<Map<String, Object>> getDepositList() {
		return depositList;
	}

	/**
	 * Sets the deposit list.
	 *
	 * @param depositList the new deposit list
	 */
	public void setDepositList(List<Map<String, Object>> depositList) {
		this.depositList = depositList;
	}

	/**
	 * Gets the deposit list type 2.
	 *
	 * @return the deposit list type 2
	 */
	public List<Map<String, Object>> getDepositListType2() {
		return depositListType2;
	}

	/**
	 * Sets the deposit list type 2.
	 *
	 * @param depositListType2 the deposit list type 2
	 */
	public void setDepositListType2(List<Map<String, Object>> depositListType2) {
		this.depositListType2 = depositListType2;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getWif() {
		return wif;
	}

	public void setWif(String wif) {
		this.wif = wif;
	}

}
