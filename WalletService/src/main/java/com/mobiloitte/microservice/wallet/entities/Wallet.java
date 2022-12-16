package com.mobiloitte.microservice.wallet.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class Wallet.
 * 
 * @author Ankush Mohapatra
 */
@Entity
@Table(name = "wallet")
public class Wallet {

	/** The wallet id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wallet_id")
	private Long walletId;

	/** The blocked balance. */
	@Column(name = "blocked_balance", scale = 8, precision = 15)
	private BigDecimal blockedBalance;
	private BigDecimal inrBlockedAmount;
	private BigDecimal inrAmount;
	private BigDecimal fundData;
	private BigDecimal investAmount;
	private BigDecimal averagePrice;

	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public BigDecimal getFundData() {
		return fundData;
	}

	public void setFundData(BigDecimal fundData) {
		this.fundData = fundData;
	}

	public BigDecimal getInrBlockedAmount() {
		return inrBlockedAmount;
	}

	public void setInrBlockedAmount(BigDecimal inrBlockedAmount) {
		this.inrBlockedAmount = inrBlockedAmount;
	}

	public BigDecimal getInrAmount() {
		return inrAmount;
	}

	public void setInrAmount(BigDecimal inrAmount) {
		this.inrAmount = inrAmount;
	}

	/** The coin name. */
	@Column(name = "coin_name")
	private String coinName;

	/** The tag. */
	@Column(name = "tag")
	private String tag;

	/** The wallet address. */
	@Column(name = "wallet_address")
	private String walletAddress;

	/** The wallet balance. */
	@Column(name = "wallet_balance", scale = 8, precision = 15)
	private BigDecimal walletBalance;

	/** The wallet creating time. */
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "wallet_creating_time")
	private Date walletCreatingTime;

	/** The wallet file name. */
	@Column(name = "wallet_file_name")
	private String walletFileName;

	/** The wallet password. */
	@Column(name = "wallet_password")
	private String walletPassword;

	/** The eos account name. */
	@Column(name = "eos_account_name")
	private String eosAccountName;

	@Column(name = "hex_address")
	private String hexAddress;

	private BigDecimal usdAmount;

	public BigDecimal getUsdAmount() {
		return usdAmount;
	}

	public void setUsdAmount(BigDecimal usdAmount) {
		this.usdAmount = usdAmount;
	}

	private String randomId;

	/** The coin. */
	// bi-directional many-to-one association to Coin
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_coin_id")
	private Coin coin;

	/** The fk user id. */
	@Column(name = "fk_user_id")
	private Long fkUserId;

	/** The coin type. */
	@Column(name = "coin_type")
	private String coinType;

	private String wif;

	/**
	 * Gets the wallet id.
	 *
	 * @return the wallet id
	 */
	public Long getWalletId() {
		return walletId;
	}

	/**
	 * Sets the wallet id.
	 *
	 * @param walletId the new wallet id
	 */
	public void setWalletId(Long walletId) {
		this.walletId = walletId;
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
	 * Gets the wallet address.
	 *
	 * @return the wallet address
	 */
	public String getWalletAddress() {
		return walletAddress;
	}

	/**
	 * Sets the wallet address.
	 *
	 * @param walletAddress the new wallet address
	 */
	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}

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
	 * Gets the wallet creating time.
	 *
	 * @return the wallet creating time
	 */
	public Date getWalletCreatingTime() {
		return walletCreatingTime;
	}

	/**
	 * Sets the wallet creating time.
	 *
	 * @param walletCreatingTime the new wallet creating time
	 */
	public void setWalletCreatingTime(Date walletCreatingTime) {
		this.walletCreatingTime = walletCreatingTime;
	}

	/**
	 * Gets the wallet file name.
	 *
	 * @return the wallet file name
	 */
	public String getWalletFileName() {
		return walletFileName;
	}

	/**
	 * Sets the wallet file name.
	 *
	 * @param walletFileName the new wallet file name
	 */
	public void setWalletFileName(String walletFileName) {
		this.walletFileName = walletFileName;
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
	 * Gets the eos account name.
	 *
	 * @return the eos account name
	 */
	public String getEosAccountName() {
		return eosAccountName;
	}

	/**
	 * Sets the eos account name.
	 *
	 * @param eosAccountName the new eos account name
	 */
	public void setEosAccountName(String eosAccountName) {
		this.eosAccountName = eosAccountName;
	}

	public String getHexAddress() {
		return hexAddress;
	}

	public void setHexAddress(String hexAddress) {
		this.hexAddress = hexAddress;
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

	/**
	 * Gets the fk user id.
	 *
	 * @return the fk user id
	 */
	public Long getFkUserId() {
		return fkUserId;
	}

	/**
	 * Sets the fk user id.
	 *
	 * @param fkUserId the new fk user id
	 */
	public void setFkUserId(Long fkUserId) {
		this.fkUserId = fkUserId;
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

	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

	public String getWif() {
		return wif;
	}

	public void setWif(String wif) {
		this.wif = wif;
	}

}
