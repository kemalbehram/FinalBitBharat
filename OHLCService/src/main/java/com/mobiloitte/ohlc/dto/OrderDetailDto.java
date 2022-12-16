
package com.mobiloitte.ohlc.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiloitte.ohlc.enums.OrderSide;
import com.mobiloitte.ohlc.enums.OrderStatus;
import com.mobiloitte.ohlc.enums.OrderType;

public class OrderDetailDto {
	private Long orderId;

	private String instrument;

	private Long userId;

	@Column(scale = 6, precision = 15)
	private BigDecimal quantity;

	@Column(scale = 6, precision = 15)
	private BigDecimal currentQuantity;

	@Column(scale = 6, precision = 15)
	private BigDecimal blockedBalance;

	@Column(scale = 6, precision = 15)
	private BigDecimal limitPrice;

	@Column(scale = 6, precision = 15)
	private BigDecimal stopPrice;

	@Column(scale = 6, precision = 15)
	private BigDecimal avgExecutionPrice;

	private boolean active;

	@Enumerated
	private OrderSide orderSide;

	@Enumerated
	private OrderStatus orderStatus;

	@Enumerated
	private OrderType orderType;

	@CreationTimestamp

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastExecutionTime;

	private String baseCoin;
	private String exeCoin;

	private String customerId;

	private String name;

	private String email;

	private String phoneNo;

	private Long executedUserId;

	private BigDecimal userQuantity;

	private String clientOrderId;
	private String validity;

	private Date updatedAt;
	private Long transactionId;

	private Boolean triggerCondition;

	private String symbol;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Boolean getTriggerCondition() {
		return triggerCondition;
	}

	public void setTriggerCondition(Boolean triggerCondition) {
		this.triggerCondition = triggerCondition;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public Long getOrderId() {
		return orderId;
	}

	public String getBaseCoin() {
		return baseCoin;
	}

	public void setBaseCoin(String baseCoin) {
		this.baseCoin = baseCoin;
	}

	public String getExeCoin() {
		return exeCoin;
	}

	public void setExeCoin(String exeCoin) {
		this.exeCoin = exeCoin;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		String[] split = instrument.split("_");
		this.baseCoin = split[1];
		this.exeCoin = split[0];
		this.instrument = instrument;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(BigDecimal currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public BigDecimal getBlockedBalance() {
		return blockedBalance;
	}

	public void setBlockedBalance(BigDecimal blockedBalance) {
		this.blockedBalance = blockedBalance;
	}

	public BigDecimal getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(BigDecimal limitPrice) {
		this.limitPrice = limitPrice;
	}

	public BigDecimal getStopPrice() {
		return stopPrice;
	}

	public void setStopPrice(BigDecimal stopPrice) {
		this.stopPrice = stopPrice;
	}

	public BigDecimal getAvgExecutionPrice() {
		return avgExecutionPrice;
	}

	public void setAvgExecutionPrice(BigDecimal avgExecutionPrice) {
		this.avgExecutionPrice = avgExecutionPrice;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public OrderSide getOrderSide() {
		return orderSide;
	}

	public void setOrderSide(OrderSide orderSide) {
		this.orderSide = orderSide;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastExecutionTime() {
		return lastExecutionTime;
	}

	public void setLastExecutionTime(Date lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Long getExecutedUserId() {
		return executedUserId;
	}

	public void setExecutedUserId(Long executedUserId) {
		this.executedUserId = executedUserId;
	}

	public BigDecimal getUserQuantity() {
		return userQuantity;
	}

	public void setUserQuantity(BigDecimal userQuantity) {
		this.userQuantity = userQuantity;
	}

	public String getClientOrderId() {
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}

}
