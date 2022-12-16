package com.mobiloitte.notification.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.mobiloitte.notification.enums.OrderType;
import com.mobiloitte.notification.enums.TradeStatus;

public class ExchangeBellNotiticationDto {
	private Long notificationId;
	private Boolean isSeen;
	private Date CreatedAt;
	private String tradePartner;
	private String notificationStatus;
	private BigDecimal tradingPrice;
	private BigDecimal noOfCoins;
	private String tradeId;
	private TradeStatus tradeStatus;
	private Date timeStamp;
	private Long toUserId;
	private Long fromUserId;
	private int paymentWindow;
	private OrderType orderType;
	private String requestMessage;
	private Long fromRequestAcceptRejectId;
	private Long toRequestAcceptRejectId;
	private Long peerToPeerExchangeId;

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public Boolean getIsSeen() {
		return isSeen;
	}

	public void setIsSeen(Boolean isSeen) {
		this.isSeen = isSeen;
	}

	public Date getCreatedAt() {
		return CreatedAt;
	}

	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}

	public String getTradePartner() {
		return tradePartner;
	}

	public void setTradePartner(String tradePartner) {
		this.tradePartner = tradePartner;
	}

	public String getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public BigDecimal getTradingPrice() {
		return tradingPrice;
	}

	public void setTradingPrice(BigDecimal tradingPrice) {
		this.tradingPrice = tradingPrice;
	}

	public BigDecimal getNoOfCoins() {
		return noOfCoins;
	}

	public void setNoOfCoins(BigDecimal noOfCoins) {
		this.noOfCoins = noOfCoins;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public TradeStatus getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public Long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public int getPaymentWindow() {
		return paymentWindow;
	}

	public void setPaymentWindow(int paymentWindow) {
		this.paymentWindow = paymentWindow;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public Long getFromRequestAcceptRejectId() {
		return fromRequestAcceptRejectId;
	}

	public void setFromRequestAcceptRejectId(Long fromRequestAcceptRejectId) {
		this.fromRequestAcceptRejectId = fromRequestAcceptRejectId;
	}

	public Long getToRequestAcceptRejectId() {
		return toRequestAcceptRejectId;
	}

	public void setToRequestAcceptRejectId(Long toRequestAcceptRejectId) {
		this.toRequestAcceptRejectId = toRequestAcceptRejectId;
	}

	public Long getPeerToPeerExchangeId() {
		return peerToPeerExchangeId;
	}

	public void setPeerToPeerExchangeId(Long peerToPeerExchangeId) {
		this.peerToPeerExchangeId = peerToPeerExchangeId;
	}

}
