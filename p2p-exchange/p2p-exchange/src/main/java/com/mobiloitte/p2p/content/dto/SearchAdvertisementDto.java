package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.TwoFaType;

public class SearchAdvertisementDto {
	private Long peerToPeerExchangeId;
	private OrderType orderType;
	private ExchangeStatusType orderStatus;
	private Date creationTime;
	private PaymentType paymentType;
	private String userName;
	private String country;
	private String fiatCoin;
	private BigDecimal maxValue;
	private Long userId;
	private BigDecimal minValue;
	private BigDecimal price;
	private String termsOfTrade;
	private Boolean isIdentifiedPeople;
	private TwoFaType twpfaType;
	public Long getPeerToPeerExchangeId() {
		return peerToPeerExchangeId;
	}
	public void setPeerToPeerExchangeId(Long peerToPeerExchangeId) {
		this.peerToPeerExchangeId = peerToPeerExchangeId;
	}
	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	public ExchangeStatusType getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(ExchangeStatusType orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public PaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getFiatCoin() {
		return fiatCoin;
	}
	public void setFiatCoin(String fiatCoin) {
		this.fiatCoin = fiatCoin;
	}
	public BigDecimal getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BigDecimal getMinValue() {
		return minValue;
	}
	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getTermsOfTrade() {
		return termsOfTrade;
	}
	public void setTermsOfTrade(String termsOfTrade) {
		this.termsOfTrade = termsOfTrade;
	}
	public Boolean getIsIdentifiedPeople() {
		return isIdentifiedPeople;
	}
	public void setIsIdentifiedPeople(Boolean isIdentifiedPeople) {
		this.isIdentifiedPeople = isIdentifiedPeople;
	}
	public TwoFaType getTwpfaType() {
		return twpfaType;
	}
	public void setTwpfaType(TwoFaType twpfaType) {
		this.twpfaType = twpfaType;
	}
	



}
