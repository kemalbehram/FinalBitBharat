package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;

public class P2pHistoryDto {
	private String type;

	private BigDecimal amount;

	private BigDecimal liveAmount;

	private BigDecimal depositLiveAmount;

	private String tradeId;

	private Long p2pId;

	private String transactionStatus;

	private String tradeStatus;
	private String coinName;
	
	
	

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getLiveAmount() {
		return liveAmount;
	}

	public void setLiveAmount(BigDecimal liveAmount) {
		this.liveAmount = liveAmount;
	}

	public BigDecimal getDepositLiveAmount() {
		return depositLiveAmount;
	}

	public void setDepositLiveAmount(BigDecimal depositLiveAmount) {
		this.depositLiveAmount = depositLiveAmount;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Long getP2pId() {
		return p2pId;
	}

	public void setP2pId(Long p2pId) {
		this.p2pId = p2pId;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}



}
