package com.mobiloitte.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.enums.OrderStatus;
import com.mobiloitte.order.enums.OrderType;

/**
 * @author Rahil Husain
 *
 */
@Entity
@JsonInclude(Include.NON_NULL)
public class Order implements Serializable {
	private static final long serialVersionUID = -8097086951603035874L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
	@SequenceGenerator(name = "order_generator", sequenceName = "order_seq")
	private Long orderId;

	private String instrument;

	private Long userId;
	@Column(scale = 8, precision = 15)
	private BigDecimal quantity;
	@Column(scale = 8, precision = 15)
	private BigDecimal currentQuantity;
	@Column(scale = 8, precision = 15)
	private BigDecimal blockedBalance;
	@Column(scale = 8, precision = 15)
	private BigDecimal limitPrice;
	@Column(scale = 8, precision = 15)
	private BigDecimal stopPrice;
	@Column(scale = 8, precision = 15)
	private BigDecimal avgExecutionPrice;

	private String foreignOrderId;

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

	@Transient
	private List<Transaction> transactions;

	private Boolean triggerCondition;
	private Boolean liquiditystatus = false;
	private String baseCoin;
	private String exeCoin;

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

	public Boolean getLiquiditystatus() {
		return liquiditystatus;
	}

	public void setLiquiditystatus(Boolean liquiditystatus) {
		this.liquiditystatus = liquiditystatus;
	}

	public Boolean getTriggerCondition() {
		return triggerCondition;
	}

	public void setTriggerCondition(Boolean triggerCondition) {
		this.triggerCondition = triggerCondition;
	}

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
		try {

			if (avgExecutionPrice == null)
				avgExecutionPrice = price;
			else
				avgExecutionPrice = avgExecutionPrice.multiply(oldQuantity).add(price.multiply(quantity))
						.divide(oldQuantity.add(quantity));

			avgExecutionPrice = avgExecutionPrice.setScale(8, RoundingMode.FLOOR);

			return avgExecutionPrice;

		} catch (Exception e) {
			return price;
		}
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", instrument=" + instrument + ", userId=" + userId + ", quantity="
				+ quantity + ", currentQuantity=" + currentQuantity + ", blockedBalance=" + blockedBalance
				+ ", limitPrice=" + limitPrice + ", stopPrice=" + stopPrice + ", avgExecutionPrice=" + avgExecutionPrice
				+ ", foreignOrderId=" + foreignOrderId + ", active=" + active + ", orderSide=" + orderSide
				+ ", orderStatus=" + orderStatus + ", orderType=" + orderType + ", creationTime=" + creationTime
				+ ", lastExecutionTime=" + lastExecutionTime + ", transactions=" + transactions + "]";
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean canActivate(BigDecimal lastPrice) {
		if (this.orderType == null || this.orderType != OrderType.STOP_LIMIT)
			throw new IllegalStateException("Method not supported for " + this.orderType);
		if (this.orderSide == null)
			throw new IllegalStateException("Order side is null");
		if (this.stopPrice == null)
			throw new IllegalStateException("Stop price is null");
		if (lastPrice == null)
			return false;

		if (this.stopPrice.compareTo(lastPrice) == 0) {
			return true;
		}
		return false;

	}

	public String getForeignOrderId() {
		return foreignOrderId;
	}

	public void setForeignOrderId(String foreignOrderId) {
		this.foreignOrderId = foreignOrderId;
	}

}
