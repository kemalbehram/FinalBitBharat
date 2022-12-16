package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.mobiloitte.p2p.content.enums.DisputeStatus;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.TradeStatus;

public class SearchTradeDto {
	private Long tradingId;
	private String tradeId;
	private String buyer;
	private String seller;
	private TradeStatus tradeStatus;
	private PaymentType paymentType;
	private OrderType type;
	private BigDecimal tradeAmount;
	private BigDecimal tradeFee;
	private BigDecimal totalBTC;
	private Date creationTime;
	private Long userId;
	private Long partnerId;
	private String disputeId;
	private DisputeStatus disputeStatus;
	private String disputer;
	private Date disputeDate;
	private String fiatCoin;
	private String staffId;
	private String staffName;
	private BigDecimal feeCollection;
	private BigDecimal appliedValue;

	public BigDecimal getAppliedValue() {
		return appliedValue;
	}

	public void setAppliedValue(BigDecimal appliedValue) {
		this.appliedValue = appliedValue;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
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

	public TradeStatus getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
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

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Long getTradingId() {
		return tradingId;
	}

	public void setTradingId(Long tradingId) {
		this.tradingId = tradingId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getDisputeId() {
		return disputeId;
	}

	public void setDisputeId(String disputeId) {
		this.disputeId = disputeId;
	}

	public DisputeStatus getDisputeStatus() {
		return disputeStatus;
	}

	public void setDisputeStatus(DisputeStatus disputeStatus) {
		this.disputeStatus = disputeStatus;
	}

	public String getDisputer() {
		return disputer;
	}

	public void setDisputer(String disputer) {
		this.disputer = disputer;
	}

	public Date getDisputeDate() {
		return disputeDate;
	}

	public void setDisputeDate(Date disputeDate) {
		this.disputeDate = disputeDate;
	}

	public String getFiatCoin() {
		return fiatCoin;
	}

	public void setFiatCoin(String fiatCoin) {
		this.fiatCoin = fiatCoin;
	}

	public BigDecimal getFeeCollection() {
		return feeCollection;
	}

	public void setFeeCollection(BigDecimal feeCollection) {
		this.feeCollection = feeCollection;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

}
