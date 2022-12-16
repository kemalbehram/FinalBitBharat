package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import com.mobiloitte.p2p.content.enums.StatusType;
import com.mobiloitte.p2p.content.enums.TransactionStatus;

@Service
public class TradingDto {
	private String tradeId;
	private String tradingPartner;
	private Long tradingId;
	@NotNull(message = "Should not be NULL")
	@AssertTrue(message = "type must be in String")
	private String type;
	@NotNull(message = "Should not be NULL")
	@AssertTrue(message = "type must be in String")
	private String cryptoCoin;
	@NotNull(message = "Should not be NULL")
	@AssertTrue(message = "type must be in String")
	private String fiatCoin;
	private StatusType statusType;
	private BigDecimal tradeAmount;
	private BigDecimal paybleAmount;

	private BigDecimal tradeFee;

	private BigDecimal totalBTC;
	private TransactionStatus transactionStatus;
	private Long fkUserId;
	private Long peerToPeerExchangeId;
	private String buyer;
	private String seller;
	private String disputer;

	public String getDisputer() {
		return disputer;
	}

	public void setDisputer(String disputer) {
		this.disputer = disputer;
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

	public Long getPeerToPeerExchangeId() {
		return peerToPeerExchangeId;
	}

	public void setPeerToPeerExchangeId(Long peerToPeerExchangeId) {
		this.peerToPeerExchangeId = peerToPeerExchangeId;
	}

	public Long getTradingId() {
		return tradingId;
	}

	public void setTradingId(Long tradingId) {
		this.tradingId = tradingId;
	}

	public Long getFkUserId() {
		return fkUserId;
	}

	public void setFkUserId(Long fkUserId) {
		this.fkUserId = fkUserId;
	}

	public BigDecimal getPaybleAmount() {
		return paybleAmount;
	}

	public void setPaybleAmount(BigDecimal paybleAmount) {
		this.paybleAmount = paybleAmount;
	}

	public String getCryptoCoin() {
		return cryptoCoin;
	}

	public void setCryptoCoin(String cryptoCoin) {
		this.cryptoCoin = cryptoCoin;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public StatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(StatusType statusType) {
		this.statusType = statusType;
	}

	private BigDecimal totalPrice;

	public String getTradingPartner() {
		return tradingPartner;
	}

	public void setTradingPartner(String tradingPartner) {
		this.tradingPartner = tradingPartner;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFiatCoin() {
		return fiatCoin;
	}

	public void setFiatCoin(String fiatCoin) {
		this.fiatCoin = fiatCoin;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public BigDecimal getTradeFee() {
		return tradeFee;
	}

	public void setTradeFee(BigDecimal tradeFee) {
		this.tradeFee = tradeFee;
	}

	public BigDecimal getTotalBTC() {
		return totalBTC;
	}

	public void setTotalBTC(BigDecimal totalBTC) {
		this.totalBTC = totalBTC;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
