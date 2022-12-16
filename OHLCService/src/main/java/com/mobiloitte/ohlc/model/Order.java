package com.mobiloitte.ohlc.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mobiloitte.ohlc.enums.OrderSide;
import com.mobiloitte.ohlc.enums.OrderStatus;
import com.mobiloitte.ohlc.enums.OrderType;

/**
 * @author Rahil Husain
 *
 */
@JsonInclude(Include.NON_NULL)
public class Order implements Serializable {
	private static final long serialVersionUID = -8097086951603035874L;

	private Long orderId;

	private String instrument;

	private Long userId;
	private BigDecimal quantity;
	private BigDecimal currentQuantity;
	private BigDecimal blockedBalance;
	private BigDecimal limitPrice;
	private BigDecimal stopPrice;
	private BigDecimal avgExecutionPrice;

	private OrderSide orderSide;

	private OrderStatus orderStatus;

	private OrderType orderType;

	private Date creationTime;

	private Date lastExecutionTime;

	private List<Transaction> transactions;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
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

	/**
	 * @return true if current quantity becomes 0 after deductions
	 * @return false otherwise
	 */
	public boolean deductQuantity(BigDecimal quantityToDeduct) {
		currentQuantity = currentQuantity.subtract(quantityToDeduct);
		return currentQuantity.compareTo(BigDecimal.ZERO) == 0;
	}

	/**
	 * @return true if current blocked balance becomes 0 after deductions
	 * @return false otherwise
	 */
	public boolean deductBlockedBalance(BigDecimal balanceToDeduct) {
		blockedBalance = blockedBalance.subtract(balanceToDeduct);
		return blockedBalance.compareTo(BigDecimal.ZERO) == 0;
	}

	public BigDecimal getBlockedBalance() {
		return blockedBalance;
	}

	public void setBlockedBalance(BigDecimal blockedBalance) {
		this.blockedBalance = blockedBalance;
	}

	public void addBlockBalance(BigDecimal amountToBlock) {
		if (blockedBalance == null)
			blockedBalance = amountToBlock;
		else
			blockedBalance = blockedBalance.add(amountToBlock);
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

	public BigDecimal addToAvgExecutionPrice(BigDecimal price, BigDecimal quantity, BigDecimal oldQuantity) {
		if (avgExecutionPrice == null)
			avgExecutionPrice = price;
		else
			avgExecutionPrice = avgExecutionPrice.multiply(oldQuantity).add(price.multiply(quantity))
					.divide(oldQuantity.add(quantity), RoundingMode.HALF_UP);
		return avgExecutionPrice;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", instrument=" + instrument + ", userId=" + userId + ", quantity="
				+ quantity + ", currentQuantity=" + currentQuantity + ", blockedBalance=" + blockedBalance
				+ ", limitPrice=" + limitPrice + ", stopPrice=" + stopPrice + ", avgExecutionPrice=" + avgExecutionPrice
				+ ", orderSide=" + orderSide + ", orderStatus=" + orderStatus + ", orderType=" + orderType
				+ ", creationTime=" + creationTime + ", lastExecutionTime=" + lastExecutionTime + ", transactions="
				+ transactions + "]";
	}

}
