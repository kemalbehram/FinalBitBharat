package com.mobiloitte.usermanagement.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TradeCountDto {
	private Long tradeCount;
	private BigDecimal tradeValume;
	private Date firstPurchase;

	public Long getTradeCount() {
		return tradeCount;
	}

	public void setTradeCount(Long tradeCount) {
		this.tradeCount = tradeCount;
	}

	public BigDecimal getTradeValume() {
		return tradeValume;
	}

	public void setTradeValume(BigDecimal tradeValume) {
		this.tradeValume = tradeValume;
	}

	public Date getFirstPurchase() {
		return firstPurchase;
	}

	public void setFirstPurchase(Date firstPurchase) {
		this.firstPurchase = firstPurchase;
	}

}