package com.mobiloitte.order.dto;

import java.math.BigDecimal;

import com.mobiloitte.order.enums.OrderSide;

public class OrderUpdateDto {
private BigDecimal avgExecutionPrice;
	
private BigDecimal price;
private BigDecimal amount;
private OrderSide side;

public BigDecimal getPrice() {
	return price;
}

public BigDecimal getAmount() {
	return amount;
}

public void setAmount(BigDecimal amount) {
	this.amount = amount;
}

public OrderSide getSide() {
	return side;
}

public void setSide(OrderSide side) {
	this.side = side;
}

@Override
public String toString() {
	return "OrderUpdateDto [price=" + price + ", amount=" + amount + ", side=" + side + "]";
}

public OrderUpdateDto() {
	super();
}

public OrderUpdateDto(BigDecimal price, BigDecimal amount, OrderSide side) {
	super();
	this.price = price;
	this.amount = amount;
	this.side = side;
}

public BigDecimal getAvgExecutionPrice() {
	return avgExecutionPrice;
}

public void setAvgExecutionPrice(BigDecimal avgExecutionPrice) {
	this.avgExecutionPrice = avgExecutionPrice;
}

}
