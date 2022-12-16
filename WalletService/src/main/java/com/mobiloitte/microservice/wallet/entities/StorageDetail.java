package com.mobiloitte.microservice.wallet.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class StorageDetail.
 * @author Ankush Mohapatra
 */
@Entity
@Table(name="storage_details")
public class StorageDetail {
	
	/** The storage id. */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="storage_id")
	private Long storageId;

	/** The address. */
	@Column(name="address")
	private String address;

	/** The coin limit. */
	@Column(name="coin_limit", scale = 6, precision = 15)
	private BigDecimal coinLimit;

	/** The coin type. */
	@Column(name="coin_type")
	private String coinType;

	/** The hot wallet balance. */
	@Column(name="hot_wallet_balance", scale = 8, precision = 15)
	private BigDecimal hotWalletBalance;

	/** The storage type. */
	@Column(name="storage_type")
	private String storageType;

	/** The wallet file. */
	@Column(name="wallet_file")
	private String walletFile;

	/** The wallet password. */
	@Column(name="wallet_password")
	private String walletPassword;
	
	/** The secret key. */
	@Column(name="secret_key")
	private String secretKey;
	
	@Column(name="hex_address")
	private String hexAddress;
	
	/** The tag. */
	@Column(name="tag")
	private String tag;
	
	@Column(scale = 6, precision = 15)
	private BigDecimal coinThreshold;
	
	@Column(scale = 6, precision = 15)
	private BigDecimal supportThreshold;

	/** The coin. */
	//bi-directional many-to-one association to Coin
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fk_coin_id")
	private Coin coin;
	
	private String wif;

	/**
	 * Gets the storage id.
	 *
	 * @return the storage id
	 */
	public Long getStorageId() {
		return storageId;
	}

	/**
	 * Sets the storage id.
	 *
	 * @param storageId the new storage id
	 */
	public void setStorageId(Long storageId) {
		this.storageId = storageId;
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
	 * Gets the coin limit.
	 *
	 * @return the coin limit
	 */
	public BigDecimal getCoinLimit() {
		return coinLimit;
	}

	/**
	 * Sets the coin limit.
	 *
	 * @param coinLimit the new coin limit
	 */
	public void setCoinLimit(BigDecimal coinLimit) {
		this.coinLimit = coinLimit;
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
	 * Gets the hot wallet balance.
	 *
	 * @return the hot wallet balance
	 */
	public BigDecimal getHotWalletBalance() {
		return hotWalletBalance;
	}

	/**
	 * Sets the hot wallet balance.
	 *
	 * @param hotWalletBalance the new hot wallet balance
	 */
	public void setHotWalletBalance(BigDecimal hotWalletBalance) {
		this.hotWalletBalance = hotWalletBalance;
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

	
	public String getHexAddress() {
		return hexAddress;
	}

	public void setHexAddress(String hexAddress) {
		this.hexAddress = hexAddress;
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
	 * Gets the coin.
	 *
	 * @return the coin
	 */
	public Coin getCoin() {
		return coin;
	}

	/**
	 * Sets the coin.
	 *
	 * @param coin the new coin
	 */
	public void setCoin(Coin coin) {
		this.coin = coin;
	}

	public BigDecimal getCoinThreshold() {
		return coinThreshold;
	}

	public void setCoinThreshold(BigDecimal coinThreshold) {
		this.coinThreshold = coinThreshold;
	}

	public BigDecimal getSupportThreshold() {
		return supportThreshold;
	}

	public void setSupportThreshold(BigDecimal supportThreshold) {
		this.supportThreshold = supportThreshold;
	}

	public String getWif() {
		return wif;
	}

	public void setWif(String wif) {
		this.wif = wif;
	}
	
	

}
