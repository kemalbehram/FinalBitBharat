package com.mobiloitte.order.dto;

import com.mobiloitte.order.enums.OrderSide;
import com.mobiloitte.order.model.Transaction;

public class TradeHistoryDto implements Comparable<TradeHistoryDto> {
	private Long time;
	private Double price;
	private Double amount;
	private OrderSide side;

	public Long getTime() {
		return time;
	}

	public Double getPrice() {
		return price;
	}

	public Double getAmount() {
		return amount;
	}

	public OrderSide getSide() {
		return side;
	}

	public TradeHistoryDto(long time, Double price, Double amount, OrderSide side) {
		super();
		this.time = time;
		this.price = price;
		this.amount = amount;
		this.side = side;
	}

	public TradeHistoryDto(Transaction transaction) {
		super();
		this.time = transaction.getExecutionTime().getTime();
		this.price = transaction.getPrice().doubleValue();
		this.amount = transaction.getQuantity().doubleValue();
		this.side = transaction.getSide();
	}

	@Override
	public int compareTo(TradeHistoryDto o) {
		return this.getTime().compareTo(o.getTime());
	}

}
