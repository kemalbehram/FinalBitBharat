/*package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiloitte.p2p.content.enums.TradeStatus;

public class NotificationDto {
	private Boolean isSeen;
		@CreationTimestamp
		private Date CreatedAt;
		private String tradePartner;
		private String notificationSatatus;
		private BigDecimal tradingPrice;
		private BigDecimal noOfCoins;
		private String tradeId;
		@Enumerated(EnumType.STRING)
	
		private TradeStatus tradeStatus;
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
		public String getNotificationSatatus() {
			return notificationSatatus;
		}
		public void setNotificationSatatus(String notificationSatatus) {
			this.notificationSatatus = notificationSatatus;
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
		
}

*/