package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.mobiloitte.p2p.content.enums.TradeStatus;

public class TradingListDto {
	private Long tradingId;

	private String tradeId;
	private TradeStatus tradeStatus;
	private Date creationTime;
	private String buyer;
	private String seller;
	private BigDecimal totalPrice;
	public Long getTradingId() {
		return tradingId;
	}
	public void setTradingId(Long tradingId) {
		this.tradingId = tradingId;
	}
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public TradeStatus getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
