package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.mobiloitte.p2p.content.enums.StatusType;
import com.mobiloitte.p2p.content.enums.TransactionStatus;


@Service
public class EscrowDto {
	
	private Long escrowId;
	private Long TradingId;
	private Long fkUserId;
	private BigDecimal blockedBalance;
	private String tradingPartner;
	private BigDecimal coinQuantity;
	private String tradeId;
	private StatusType statusType;
	private BigDecimal walletBalance;

	private BigDecimal tradeFees;
	private BigDecimal paybleAmount;
	private TransactionStatus transactionStatus;
	private Long peerToPeerExchangeId;

	public Long getPeerToPeerExchangeId() {
		return peerToPeerExchangeId;
	}

	public void setPeerToPeerExchangeId(Long peerToPeerExchangeId) {
		this.peerToPeerExchangeId = peerToPeerExchangeId;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public BigDecimal getCoinQuantity() {
		return coinQuantity;
	}

	public void setCoinQuantity(BigDecimal coinQuantity) {
		this.coinQuantity = coinQuantity;
	}

	public StatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(StatusType statusType) {
		this.statusType = statusType;
	}

	public BigDecimal getPaybleAmount() {
		return paybleAmount;
	}

	public void setPaybleAmount(BigDecimal paybleAmount) {
		this.paybleAmount = paybleAmount;
	}

	public Long getTradingId() {
		return TradingId;
	}

	public void setTradingId(Long tradingId) {
		TradingId = tradingId;
	}

	public Long getFkUserId() {
		return fkUserId;
	}

	public void setFkUserId(Long fkUserId) {
		this.fkUserId = fkUserId;
	}

	public BigDecimal getBlockedBalance() {
		return blockedBalance;
	}

	public void setBlockedBalance(BigDecimal blockedBalance) {
		this.blockedBalance = blockedBalance;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public BigDecimal getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(BigDecimal walletBalance) {
		this.walletBalance = walletBalance;
	}

	public BigDecimal getTradeFees() {
		return tradeFees;
	}

	public void setTradeFees(BigDecimal tradeFees) {
		this.tradeFees = tradeFees;
	}

	public Long getEscrowId() {
		return escrowId;
	}

	public void setEscrowId(Long escrowId) {
		this.escrowId = escrowId;
	}

	public String getTradingPartner() {
		return tradingPartner;
	}

	public void setTradingPartner(String tradingPartner) {
		this.tradingPartner = tradingPartner;
	}

	

	

}
