package com.mobiloitte.ohlc.model;

import java.math.BigDecimal;

public class MarketData {
	private BigDecimal lastPrice;
	private BigDecimal totalVolume;
	private BigDecimal bestBid;
	private BigDecimal bestOffer;
	private BigDecimal lowest24HourPrice;
	private BigDecimal highest24HourPrice;
	private BigDecimal volume24Hour;
	private String symbol;

	public BigDecimal getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	public BigDecimal getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}

	public BigDecimal getBestBid() {
		return bestBid;
	}

	public void setBestBid(BigDecimal bestBid) {
		this.bestBid = bestBid;
	}

	public BigDecimal getBestOffer() {
		return bestOffer;
	}

	public void setBestOffer(BigDecimal bestOffer) {
		this.bestOffer = bestOffer;
	}

	public BigDecimal getLowest24HourPrice() {
		return lowest24HourPrice;
	}

	public void setLowest24HourPrice(BigDecimal lowest24HourPrice) {
		this.lowest24HourPrice = lowest24HourPrice;
	}

	public BigDecimal getHighest24HourPrice() {
		return highest24HourPrice;
	}

	public void setHighest24HourPrice(BigDecimal highest24HourPrice) {
		this.highest24HourPrice = highest24HourPrice;
	}

	public BigDecimal getVolume24Hour() {
		return volume24Hour;
	}

	public void setVolume24Hour(BigDecimal volume24Hour) {
		this.volume24Hour = volume24Hour;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return "MarketData [lastPrice=" + lastPrice + ", totalVolume=" + totalVolume + ", bestBid=" + bestBid
				+ ", bestOffer=" + bestOffer + ", lowest24HourPrice=" + lowest24HourPrice + ", highest24HourPrice="
				+ highest24HourPrice + ", volume24Hour=" + volume24Hour + ", symbol=" + symbol + "]";
	}

}
