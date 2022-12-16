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
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.Role;
import com.mobiloitte.p2p.content.enums.StatusType;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.enums.TwoFaType;

@Entity
@Table
public class P2PAdvertisement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long peerToPeerExchangeId;
	@Enumerated(EnumType.STRING)
	private Role role;
	private Long fkUserId;
	private String cryptoCoin;
	private String fiatCoin;
	@Column(scale = 6, precision = 15)
	private BigDecimal price;
	@Column(scale = 6, precision = 15)
	private BigDecimal margin;
	@Column(scale = 6, precision = 15)
	private BigDecimal minValue;
	@Column(scale = 6, precision = 15)
	private BigDecimal maxValue;
	@Column(scale = 6, precision = 15)
	private BigDecimal restrictAmount;
	@Enumerated(EnumType.STRING)
	private OrderType orderType;
	@Enumerated(EnumType.STRING)
	private ExchangeStatusType exchangeStatusType;
	@Lob
	private String termsOfTrade;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updationTime;
	@Enumerated(EnumType.STRING)
	private TradeStatus tradeStatus;
	private Boolean isDeleted;
	@Column(name = "payment_type")
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;
	private int paymentWindow;
	private String userName;
	private String email;
	private String country;
	private Boolean isIdentifiedPeople;
	@Enumerated(EnumType.STRING)
	private TwoFaType twpfaType;
	private BigDecimal tradeLimit;
	private String percentage;
	@Enumerated(EnumType.STRING)
	private StatusType statusType;
	private Boolean isKycAccepted;

	private String paymentDetail;
	private String location;
	private String bankName;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getPaymentDetail() {
		return paymentDetail;
	}

	public void setPaymentDetail(String paymentDetail) {
		this.paymentDetail = paymentDetail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPaymentWindow() {
		return paymentWindow;
	}

	public void setPaymentWindow(int paymentWindow) {
		this.paymentWindow = paymentWindow;
	}

	public Long getFkUserId() {
		return fkUserId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setFkUserId(Long fkUserId) {
		this.fkUserId = fkUserId;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Long getPeerToPeerExchangeId() {
		return peerToPeerExchangeId;
	}

	public void setPeerToPeerExchangeId(Long peerToPeerExchangeId) {
		this.peerToPeerExchangeId = peerToPeerExchangeId;
	}

	public String getCryptoCoin() {
		return cryptoCoin;
	}

	public void setCryptoCoin(String cryptoCoin) {
		this.cryptoCoin = cryptoCoin;
	}

	public String getFiatCoin() {
		return fiatCoin;
	}

	public void setFiatCoin(String fiatCoin) {
		this.fiatCoin = fiatCoin;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getMargin() {
		return margin;
	}

	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getTermsOfTrade() {
		return termsOfTrade;
	}

	public void setTermsOfTrade(String termsOfTrade) {
		this.termsOfTrade = termsOfTrade;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public BigDecimal getRestrictAmount() {
		return restrictAmount;
	}

	public void setRestrictAmount(BigDecimal restrictAmount) {
		this.restrictAmount = restrictAmount;
	}

	public Boolean getIsIdentifiedPeople() {
		return isIdentifiedPeople;
	}

	public void setIsIdentifiedPeople(Boolean isIdentifiedPeople) {
		this.isIdentifiedPeople = isIdentifiedPeople;
	}

	public ExchangeStatusType getExchangeStatusType() {
		return exchangeStatusType;
	}

	public void setExchangeStatusType(ExchangeStatusType exchangeStatusType) {
		this.exchangeStatusType = exchangeStatusType;
	}

	public TwoFaType getTwpfaType() {
		return twpfaType;
	}

	public void setTwpfaType(TwoFaType twpfaType) {
		this.twpfaType = twpfaType;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getTradeLimit() {
		return tradeLimit;
	}

	public void setTradeLimit(BigDecimal tradeLimit) {
		this.tradeLimit = tradeLimit;
	}

	public StatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(StatusType statusType) {
		this.statusType = statusType;
	}

	public Boolean getIsKycAccepted() {
		return isKycAccepted;
	}

	public void setIsKycAccepted(Boolean isKycAccepted) {
		this.isKycAccepted = isKycAccepted;
	}

}
