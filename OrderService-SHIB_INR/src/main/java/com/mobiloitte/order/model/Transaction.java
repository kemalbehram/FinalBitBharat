package com.mobiloitte.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.enums.OrderStatus;

/**
 * @author Rahil Husain
 *
 */
@Entity
@Cacheable
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_generator")
	@SequenceGenerator(name = "transaction_generator", sequenceName = "transaction_seq")
	private Long transactionId;
	private Long orderId;
	private Date executionTime;
	@Column(scale = 8, precision = 15)
	private BigDecimal quantity;
	@Column(scale = 8, precision = 15)
	private BigDecimal price;
	private Long userId;
	private OrderSide side;
	@Transient
	private OrderStatus executedOrderStatus;

	private Long executedUserId;

	private Long executedOrderId;


	public Long getExecutedUserId() {
		return executedUserId;
	}

	public void setExecutedUserId(Long executedUserId) {
		this.executedUserId = executedUserId;
	}

	public Long getExecutedOrderId() {
		return executedOrderId;
	}

	public void setExecutedOrderId(Long executedOrderId) {
		this.executedOrderId = executedOrderId;
	}

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

	public Transaction(Long orderId, BigDecimal quantity, BigDecimal price, Long userId,
			OrderStatus executedOrderStatus, OrderSide side) {
		super();
		this.orderId = orderId;
		this.executionTime = new Date();
		this.quantity = quantity;
		this.price = price;
		this.userId = userId;
		this.side = side;
		this.executedOrderStatus = executedOrderStatus;
	}

	public Transaction(Long orderId, BigDecimal quantity, BigDecimal price, Long userId,
			OrderStatus executedOrderStatus, OrderSide side, Long executedUserId, Long executedOrderId) {
		super();
		this.orderId = orderId;
		this.executionTime = new Date();
		this.quantity = quantity;
		this.price = price;
		this.userId = userId;
		this.side = side;
		this.executedOrderStatus = executedOrderStatus;
		this.executedUserId = executedUserId;
		this.executedOrderId = executedOrderId;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", orderId=" + orderId + ", executionTime="
				+ executionTime + ", quantity=" + quantity + ", price=" + price + ", userId=" + userId + ", side="
				+ side + ", executedOrderStatus=" + executedOrderStatus + "]";
	}

	public OrderStatus getExecutedOrderStatus() {
		return executedOrderStatus;
	}

	public void setExecutedOrderStatus(OrderStatus executedOrderStatus) {
		this.executedOrderStatus = executedOrderStatus;
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
