package com.mobiloitte.microservice.wallet.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

/**
 * The Class CoinDepositWithdrawal.
 * 
 * @author Ankush Mohapatra
 */
@Entity
@Table(name = "coin_deposit_withdrawal")
public class CoinDepositWithdrawal {

	/** The txn id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "txn_id")
	private Long txnId;

	/** The coin type. */
	@Column(name = "coin_type")
	private String coinType;

	/** The status. */
	@Column(name = "status")
	private String status;

	/** The txn hash. */
	@Column(name = "txn_hash")
	private String txnHash;

	/** The txn time. */
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "txn_time")
	private Date txnTime;

	/** The txn type. */
	@Column(name = "txn_type")
	private String txnType;

	/** The units. */
	@Column(name = "units", scale = 8, precision = 15)
	private BigDecimal units;

	/** The address. */
	@Column(name = "address")
	private String address;

	/** The fees. */
	@Column(name = "fees", scale = 8, precision = 15)
	private BigDecimal fees;

	/** The fk user id. */
	@Column(name = "fk_user_id")
	private Long fkUserId;

	/** The to tag. */
	@Column(name = "to_tag")
	private String toTag;
	@Column(scale = 8, precision = 15)
	private BigDecimal livePrice;
	/** The user name. */
	@Column(name = "user_name")
	private String userName;

	/** The user email. */
	@Column(name = "user_email")
	private String userEmail;

	private Boolean isExternal;
	@Column(scale = 8, precision = 15)
	private BigDecimal depositeCurrentPrice;

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
	 * Gets the units.
	 *
	 * @return the units
	 */
	public BigDecimal getUnits() {
		return units;
	}

	/**
	 * Sets the units.
	 *
	 * @param units the new units
	 */
	public void setUnits(BigDecimal units) {
		this.units = units;
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
	 * Gets the fees.
	 *
	 * @return the fees
	 */
	public BigDecimal getFees() {
		return fees;
	}

	/**
	 * Sets the fees.
	 *
	 * @param fees the new fees
	 */
	public void setFees(BigDecimal fees) {
		this.fees = fees;
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
	 * Gets the to tag.
	 *
	 * @return the to tag
	 */
	public String getToTag() {
		return toTag;
	}

	/**
	 * Sets the to tag.
	 *
	 * @param toTag the new to tag
	 */
	public void setToTag(String toTag) {
		this.toTag = toTag;
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

	public final Boolean getIsExternal() {
		return isExternal;
	}

	public final void setIsExternal(Boolean isExternal) {
		this.isExternal = isExternal;
	}

}
