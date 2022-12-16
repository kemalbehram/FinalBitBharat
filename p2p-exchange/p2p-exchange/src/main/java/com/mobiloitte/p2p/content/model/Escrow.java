package com.mobiloitte.p2p.content.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mobiloitte.p2p.content.enums.StatusType;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.enums.TransactionStatus;

@Entity
@Table
public class Escrow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long escrowId;
	private Long TradingId;
	private Long fkUserId;
	private TransactionStatus transactionStatus;
	@Column(scale = 6, precision = 15)
	private BigDecimal blockedBalance;
	private String tradingPartner;
	private BigDecimal coinQuantity;
	private String tradeId;
	@Enumerated(EnumType.STRING)
	private StatusType statusType;
	@Enumerated(EnumType.STRING)
	private TradeStatus tradeStatus;
	@Column(scale = 6, precision = 15)
	private BigDecimal walletBalance;
	@Column(scale = 6, precision = 15)
	private BigDecimal tradeFees;
	@Column(scale = 6, precision = 15)
	private BigDecimal paybleAmount;
	private Long peerToPeerExchangeId;

	private String coinName;

	private Long blockBalanceUserId;

	public Long getBlockBalanceUserId() {
		return blockBalanceUserId;
	}

	public void setBlockBalanceUserId(Long blockBalanceUserId) {
		this.blockBalanceUserId = blockBalanceUserId;
	}

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

	public TradeStatus getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

}
