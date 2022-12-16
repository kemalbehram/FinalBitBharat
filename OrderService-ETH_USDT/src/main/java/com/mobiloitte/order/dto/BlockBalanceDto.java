package com.mobiloitte.order.dto;

import java.math.BigDecimal;

public class BlockBalanceDto {
	private Long userId;
	private String coin;
	private BigDecimal amountToBlock;
	private Long orderId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public BigDecimal getAmountToBlock() {
		return amountToBlock;
	}

	public void setAmountToBlock(BigDecimal amountToBlock) {
		this.amountToBlock = amountToBlock;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "BlockBalanceDto [userId=" + userId + ", coin=" + coin + ", amountToBlock=" + amountToBlock
				+ ", orderId=" + orderId + "]";
	}

	public BlockBalanceDto() {
		super();
	}

	public BlockBalanceDto(Long userId, String coin, BigDecimal amountToBlock, Long orderId) {
		super();
		this.userId = userId;
		this.coin = coin;
		this.amountToBlock = amountToBlock;
		this.orderId = orderId;
	}

}
