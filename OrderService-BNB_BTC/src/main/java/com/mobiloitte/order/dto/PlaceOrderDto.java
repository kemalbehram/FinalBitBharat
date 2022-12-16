package com.mobiloitte.order.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.enums.OrderType;

public class PlaceOrderDto {
	@Positive
	@NotNull
	private BigDecimal quantity;
	@Positive
	private BigDecimal limitPrice;
	@Positive
	private BigDecimal stopPrice;
	@NotNull
	private OrderSide orderSide;
	@NotNull
	private OrderType orderType;
	@NotNull
	private String symbol;

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
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

	public OrderSide getOrderSide() {
		return orderSide;
	}

	public void setOrderSide(OrderSide orderSide) {
		this.orderSide = orderSide;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "PlaceOrderDto [quantity=" + quantity + ", limitPrice=" + limitPrice + ", stopPrice=" + stopPrice
				+ ", orderSide=" + orderSide + ", orderType=" + orderType + "]";
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
