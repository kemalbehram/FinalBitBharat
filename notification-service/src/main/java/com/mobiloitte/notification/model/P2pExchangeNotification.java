package com.mobiloitte.notification.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiloitte.notification.enums.OrderType;
import com.mobiloitte.notification.enums.TradeStatus;
import com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.RequestType;

@Entity
@Table
public class P2pExchangeNotification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notificationId;
	private Boolean isSeen;
	@CreationTimestamp
	private Date CreatedAt;
	private String tradePartner;
	private String notificationStatus;
	private BigDecimal tradingPrice;
	private BigDecimal noOfCoins;
	private String tradeId;
	@Enumerated(EnumType.STRING)

	private TradeStatus tradeStatus;
	private Date timeStamp;

	private Long toUserId;

	private Long fromUserId;
	private int paymentWindow;
	@Enumerated(EnumType.STRING)
	private OrderType orderType;
	@Enumerated(EnumType.STRING)

	private RequestType requestType;
	private String requestMessage;
	private Long fromRequestAcceptRejectId;
	private Long toRequestAcceptRejectId;
	private Long peerToPeerExchangeId;

	public Long getPeerToPeerExchangeId() {
		return peerToPeerExchangeId;
	}

	public void setPeerToPeerExchangeId(Long peerToPeerExchangeId) {
		this.peerToPeerExchangeId = peerToPeerExchangeId;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
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

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public int getPaymentWindow() {
		return paymentWindow;
	}

	public void setPaymentWindow(int paymentWindow) {
		this.paymentWindow = paymentWindow;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTradePartner() {
		return tradePartner;
	}

	public void setTradePartner(String tradePartner) {
		this.tradePartner = tradePartner;
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

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

}
