package com.mobiloitte.order.dto;

import java.math.BigDecimal;

import com.mobiloitte.order.enums.OrderSide;

public class TransferBalanceDto {
	private String coin1;
	private String coin2;
	private Long user1;
	private Long user2;
	private BigDecimal amount1;
	private BigDecimal amount2;
	private Long order1;
	private Long order2;
	private OrderSide side;

	public String getCoin1() {
		return coin1;
	}

	public void setCoin1(String coin1) {
		this.coin1 = coin1;
	}

	public String getCoin2() {
		return coin2;
	}

	public void setCoin2(String coin2) {
		this.coin2 = coin2;
	}

	public Long getUser1() {
		return user1;
	}

	public void setUser1(Long user1) {
		this.user1 = user1;
	}

	public Long getUser2() {
		return user2;
	}

	public void setUser2(Long user2) {
		this.user2 = user2;
	}

	public BigDecimal getAmount1() {
		return amount1;
	}

	public void setAmount1(BigDecimal amount1) {
		this.amount1 = amount1;
	}

	public BigDecimal getAmount2() {
		return amount2;
	}

	public void setAmount2(BigDecimal amount2) {
		this.amount2 = amount2;
	}

	public Long getOrder1() {
		return order1;
	}

	public void setOrder1(Long order1) {
		this.order1 = order1;
	}

	public Long getOrder2() {
		return order2;
	}

	public void setOrder2(Long order2) {
		this.order2 = order2;
	}

	public TransferBalanceDto(String coin1, String coin2, Long user1, Long user2, BigDecimal amount1,
			BigDecimal amount2, Long order1, Long order2, OrderSide side) {
		super();
		this.coin1 = coin1;
		this.coin2 = coin2;
		this.user1 = user1;
		this.user2 = user2;
		this.amount1 = amount1;
		this.amount2 = amount2;
		this.order1 = order1;
		this.order2 = order2;
		this.side = side;
	}

	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

}
