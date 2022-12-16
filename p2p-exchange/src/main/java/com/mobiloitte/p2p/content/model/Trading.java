package com.mobiloitte.p2p.content.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mobiloitte.p2p.content.enums.DisputeStatus;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.StatusType;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.enums.TransactionStatus;

@Entity
@Table
public class Trading {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tradingId;
	private String tradeId;
	@Enumerated(EnumType.STRING)
	private StatusType statusType;
	private String tradingPartner;
	@Enumerated(EnumType.STRING)
	private TransactionStatus transactionStatus;
	@Enumerated(EnumType.STRING)
	private OrderType type;
	private String cryptoCoin;
	private String recommendation;
	@Column(scale = 6, precision = 15)
	private BigDecimal tradeAmount;
	@Column(scale = 6, precision = 15)
	private BigDecimal tradeFee;
	@Column(scale = 6, precision = 15)
	private BigDecimal totalBTC;
	@Column(scale = 6, precision = 15)
	private BigDecimal totalPrice;
	@Column(scale = 6, precision = 15)
	private BigDecimal paybleAmount;
	private Long peerToPeerExchangeId;
	private BigDecimal appliedValue;
	private Long fkUserId;
	private Long partnerId;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updationTime;
	@Enumerated(EnumType.STRING)
	private TradeStatus tradeStatus;
	private Boolean isDeleted;
	private String buyer;
	private String seller;
	private String disputer;
	private int paymentWindow;
	@Column(name = "payment_type")
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;
	private String disputeId;
	@Enumerated(EnumType.STRING)
	private DisputeStatus disputeStatus;
	private String cancelReason;
	private String country;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date disputeDate;
	private String fiatCoin;
	private String staffId;
	private String staffName;
	private BigDecimal totalAmount;
	private BigDecimal price;

	public BigDecimal getAppliedValue() {
		return appliedValue;
	}

	public void setAppliedValue(BigDecimal appliedValue) {
		this.appliedValue = appliedValue;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getPaymentWindow() {
		return paymentWindow;
	}

	public void setPaymentWindow(int paymentWindow) {
		this.paymentWindow = paymentWindow;
	}

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

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
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

	public Date getUpdationTime() {
		return updationTime;
	}

	public void setUpdationTime(Date updationTime) {
		this.updationTime = updationTime;
	}

	public Long getPeerToPeerExchangeId() {
		return peerToPeerExchangeId;
	}

	public void setPeerToPeerExchangeId(Long peerToPeerExchangeId) {
		this.peerToPeerExchangeId = peerToPeerExchangeId;
	}

	public Long getFkUserId() {
		return fkUserId;
	}

	public void setFkUserId(Long fkUserId) {
		this.fkUserId = fkUserId;
	}

	public String getCryptoCoin() {
		return cryptoCoin;
	}

	public void setCryptoCoin(String cryptoCoin) {
		this.cryptoCoin = cryptoCoin;
	}

	public BigDecimal getPaybleAmount() {
		return paybleAmount;
	}

	public void setPaybleAmount(BigDecimal paybleAmount) {
		this.paybleAmount = paybleAmount;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Long getTradingId() {
		return tradingId;
	}

	public void setTradingId(Long tradingId) {
		this.tradingId = tradingId;
	}

	public StatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(StatusType statusType) {
		this.statusType = statusType;
	}

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

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getDisputeDate() {
		return disputeDate;
	}

	public void setDisputeDate(Date disputeDate) {
		this.disputeDate = disputeDate;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

}
