package com.mobiloitte.ohlc.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.mobiloitte.ohlc.enums.OrderSide;

/**
 * @author Rahil Husain
 *
 */
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long transactionId;
	private Long orderId;
	private Date executionTime;
	private BigDecimal quantity;
	private BigDecimal price;
	private Long userId;
	private OrderSide side;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Date executionTime) {
		this.executionTime = executionTime;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Transaction(Long orderId, BigDecimal quantity, BigDecimal price, Long userId, OrderSide side) {
		super();
		this.orderId = orderId;
		this.executionTime = new Date();
		this.quantity = quantity;
		this.price = price;
		this.userId = userId;
		this.side = side;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", orderId=" + orderId + ", executionTime="
				+ executionTime + ", quantity=" + quantity + ", price=" + price + ", userId=" + userId + ", side="
				+ side + "]";
	}

	public Transaction() {
		super();
	}

	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

}
