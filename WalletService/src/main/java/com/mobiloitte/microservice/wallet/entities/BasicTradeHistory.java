package com.mobiloitte.microservice.wallet.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mobiloitte.microservice.wallet.enums.OrderStatus;
import com.mobiloitte.microservice.wallet.enums.OrderType;
import com.mobiloitte.microservice.wallet.enums.PaymentMethod;

@Entity
@Table
public class BasicTradeHistory {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long basicTradeHistoryId;
	
	/** The user email. */
	private String userEmail;
	
	@Enumerated(EnumType.STRING)
	private OrderType orderType;
	
	/** The exec coin name. */
	private String execCoinName;
	
	/** The base coin name. */
	private String baseCoinName;
	
	/** The base coinamount. */
	@Column(scale = 8, precision = 15)
	private BigDecimal baseCoinamount;
	
	/** The exec coinamount. */
	@Column(scale = 8, precision = 15)
	private BigDecimal execCoinamount;
	
	/** The base coin market price. */
	@Column(scale = 8, precision = 15)
	private BigDecimal baseCoinMarketPrice;
	
	@Column(scale = 8, precision = 15)
	private BigDecimal fee;
	
	/** The payment method. */
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	/** The status. */
	@Column(columnDefinition = "varchar(32) default 'PENDING'")
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	/** The fk user id. */
	private Long fkUserId;
	
	/** The creation time. */
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updationTime;

	public Long getBasicTradeHistoryId() {
		return basicTradeHistoryId;
	}

	public void setBasicTradeHistoryId(Long basicTradeHistoryId) {
		this.basicTradeHistoryId = basicTradeHistoryId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getExecCoinName() {
		return execCoinName;
	}

	public void setExecCoinName(String execCoinName) {
		this.execCoinName = execCoinName;
	}

	public String getBaseCoinName() {
		return baseCoinName;
	}

	public void setBaseCoinName(String baseCoinName) {
		this.baseCoinName = baseCoinName;
	}

	public BigDecimal getBaseCoinamount() {
		return baseCoinamount;
	}

	public void setBaseCoinamount(BigDecimal baseCoinamount) {
		this.baseCoinamount = baseCoinamount;
	}

	public BigDecimal getExecCoinamount() {
		return execCoinamount;
	}

	public void setExecCoinamount(BigDecimal execCoinamount) {
		this.execCoinamount = execCoinamount;
	}

	public BigDecimal getBaseCoinMarketPrice() {
		return baseCoinMarketPrice;
	}

	public void setBaseCoinMarketPrice(BigDecimal baseCoinMarketPrice) {
		this.baseCoinMarketPrice = baseCoinMarketPrice;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Long getFkUserId() {
		return fkUserId;
	}

	public void setFkUserId(Long fkUserId) {
		this.fkUserId = fkUserId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUpdationTime() {
		return updationTime;
	}

	public void setUpdationTime(Date updationTime) {
		this.updationTime = updationTime;
	}

}
