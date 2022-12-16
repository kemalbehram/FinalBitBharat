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
 * The Class WalletHistory.
 * 
 * @author Ankush Mohapatra
 */
@Entity
@Table(name = "wallet_history")
public class WalletHistory {

	/** The wallet history id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wallet_history_id")
	private Long walletHistoryId;

	/** The user id. */
	@Column(name = "user_id")
	private Long userId;

	/** The order id. */
	@Column(name = "order_id")
	private Long orderId;

	/** The coin name. */
	@Column(name = "coin_name")
	private String coinName;

	/** The amount. */
	@Column(name = "amount", scale = 8, precision = 15)
	private BigDecimal amount;
	@Column(scale = 8, precision = 15)
	private BigDecimal liveAmount;

	@Column( scale = 8, precision = 15)
	private BigDecimal depositAmount;

	public BigDecimal getLiveAmount() {
		return liveAmount;
	}

	public void setLiveAmount(BigDecimal liveAmount) {
		this.liveAmount = liveAmount;
	}

	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	/** The action. */
	@Column(name = "action")
	private String action;

	/** The creation time. */
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_time")
	private Date creationTime;

	/**
	 * Gets the wallet history id.
	 *
	 * @return the wallet history id
	 */
	public Long getWalletHistoryId() {
		return walletHistoryId;
	}

	/**
	 * Sets the wallet history id.
	 *
	 * @param walletHistoryId the new wallet history id
	 */
	public void setWalletHistoryId(Long walletHistoryId) {
		this.walletHistoryId = walletHistoryId;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the order id.
	 *
	 * @return the order id
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * Sets the order id.
	 *
	 * @param orderId the new order id
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
	 * Gets the action.
	 *
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action.
	 *
	 * @param action the new action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
}
