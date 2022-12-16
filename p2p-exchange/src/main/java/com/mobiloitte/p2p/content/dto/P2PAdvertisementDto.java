package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.Fixed;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.Role;
import com.mobiloitte.p2p.content.enums.TwoFaType;
//import com.mobiloitte.usermanagement.model.UserDetail;

public class P2PAdvertisementDto {
	private String username;
	private Role role;
	private Long roleId;
	private Long peerToPeerExchangeId;

	public Long getPeerToPeerExchangeId() {
		return peerToPeerExchangeId;
	}

	public void setPeerToPeerExchangeId(Long peerToPeerExchangeId) {
		this.peerToPeerExchangeId = peerToPeerExchangeId;
	}

	private String fixed;
	private String floating;
	private BigDecimal priceValue;
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
	private List<String> paymentType;
	private int paymentWindow;
	private String country;
	private BigDecimal restrictAmount;
	private Boolean isIdentifiedPeople;
	@Enumerated(EnumType.STRING)
	private TwoFaType twpfaType;
	private String upiId;

	public String getFixed() {
		return fixed;
	}

	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

	public String getFloating() {
		return floating;
	}

	public void setFloating(String floating) {
		this.floating = floating;
	}

	public BigDecimal getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(BigDecimal priceValue) {
		this.priceValue = priceValue;
	}

	public String getUpiId() {
		return upiId;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	private BigDecimal totalAmount;

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

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

//	public PaymentType getPaymentType() {
//		return paymentType;
//	}
//
//	public void setPaymentType(PaymentType paymentType) {
//		this.paymentType = paymentType;
//	}

	public String getCryptoCoin() {
		return cryptoCoin;
	}

	public List<String> getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(List<String> paymentType) {
		this.paymentType = paymentType;
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
