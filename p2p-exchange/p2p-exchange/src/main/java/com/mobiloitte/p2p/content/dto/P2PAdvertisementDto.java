package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.Role;
import com.mobiloitte.p2p.content.enums.TwoFaType;

public class P2PAdvertisementDto {
	private String username;
	private Role role;
	private Long roleId;
	@NotNull(message = "Should not be NULL")
	@AssertTrue(message = "type must be in String")
	private String cryptoCoin;
	@NotNull(message = "Should not be NULL")
	@AssertTrue(message = "type must be in String")
	private String fiatCoin;
	private BigDecimal margin;
	private ExchangeStatusType orderStatus;
	private String termsOfTrade;
	@NotNull(message = "Should not be NULL")
	@AssertTrue(message = "type must be in String")
	private BigDecimal minValue;
	@NotNull(message = "Should not be NULL")
	@AssertTrue(message = "type must be in String")
	private BigDecimal maxValue;
	@NotNull(message = "Should not be NULL")
	@AssertTrue(message = "type must be in String")
	private OrderType orderType;
	private PaymentType paymentType;
	private int paymentWindow;
	private String country;
	private BigDecimal restrictAmount;
	private Boolean isIdentifiedPeople;
	@Enumerated(EnumType.STRING)
	private TwoFaType twpfaType;
	@Lob
	private Boolean isKycAccepted;
	private BigDecimal price;

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

	public int getPaymentWindow() {
		return paymentWindow;
	}

	public void setPaymentWindow(int paymentWindow) {
		this.paymentWindow = paymentWindow;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
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

	public ExchangeStatusType getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(ExchangeStatusType orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTermsOfTrade() {
		return termsOfTrade;
	}

	public void setTermsOfTrade(String termsOfTrade) {
		this.termsOfTrade = termsOfTrade;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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

	public TwoFaType getTwpfaType() {
		return twpfaType;
	}

	public void setTwpfaType(TwoFaType twpfaType) {
		this.twpfaType = twpfaType;
	}

	public Boolean getIsKycAccepted() {
		return isKycAccepted;
	}

	public void setIsKycAccepted(Boolean isKycAccepted) {
		this.isKycAccepted = isKycAccepted;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
