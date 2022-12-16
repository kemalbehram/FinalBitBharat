package com.mobiloitte.order.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mobiloitte.order.service.KafkaService;

@Component

public class MarketData {
	private BigDecimal lastPrice;
	private BigDecimal totalVolume;
	private BigDecimal bestBid;
	private BigDecimal bestOffer;
	private TimeList<BigDecimal> priceRecord24Hour;
	private TimeList<BigDecimal> volumeRecord24Hour;
	private BigDecimal getExecutableLastPrice;
	private BigDecimal percentageChange;
	private BigDecimal percentage24Change;

	private static final Logger LOGGER = LogManager.getLogger(MarketData.class);
	@Autowired
	private KafkaService kafkaService;

	public BigDecimal getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice) {

		priceRecord24Hour.addItem(new TimeWrapper<>(lastPrice, System.currentTimeMillis() + 86400000));
		this.lastPrice = lastPrice;

	}

	public void init24HourPrice(BigDecimal price, long executionTime) {
		priceRecord24Hour.addItem(new TimeWrapper<>(price, executionTime + 86400000));
		this.lastPrice = price;
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
		return priceRecord24Hour.getList().parallelStream().min((a, b) -> a.compareTo(b)).orElse(null);
	}

	public BigDecimal getHighest24HourPrice() {
		return priceRecord24Hour.getList().parallelStream().max((a, b) -> a.compareTo(b)).orElse(null);
	}

	public BigDecimal getVolume24Hour() {
		return volumeRecord24Hour.getList().parallelStream().reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);
	}

	public void addVolume(BigDecimal volume) {
		init24HourVolume(volume, System.currentTimeMillis());
		this.totalVolume = this.totalVolume.add(volume);
	}

	public void init24HourVolume(BigDecimal volume, long executionTime) {
		this.volumeRecord24Hour.addItem(new TimeWrapper<>(volume, executionTime + 86400000));
	}

	public MarketData() {
		super();
		volumeRecord24Hour = new TimeList<>(new ArrayList<>());
		priceRecord24Hour = new TimeList<>(new ArrayList<>());
		this.totalVolume = BigDecimal.ZERO;
	}

	@Scheduled(fixedRate = 1000)
	void sendDataToSocket() {
		try {
			kafkaService.sendMarketData(this);
		} catch (Exception e) {
			LOGGER.catching(e);
		}
	}

	@Override
	public String toString() {
		return "MarketData [lastPrice=" + lastPrice + ",getExecutableLastPrice=" + getGetExecutableLastPrice()
				+ ", totalVolume=" + totalVolume + ", bestBid=" + bestBid + ", bestOffer=" + bestOffer
				+ ", getLowest24HourPrice()=" + getLowest24HourPrice() + ", getHighest24HourPrice()="
				+ getHighest24HourPrice() + ", getVolume24Hour()=" + getVolume24Hour() + "]";
	}

	
	
	public Map<String, Object> extractToMap() {
		Map<String, Object> data = new HashMap<>();

		data.put("lastPrice", lastPrice);
		data.put("totalVolume", totalVolume);
		data.put("bestBid", bestBid);
		data.put("bestOffer", bestOffer);
		data.put("lowest24HourPrice", getLowest24HourPrice());
		data.put("highest24HourPrice", getHighest24HourPrice());
		data.put("volume24Hour", getVolume24Hour());
		data.put("getExecutableLastPrice", getGetExecutableLastPrice());
		data.put("percentageChange", percentageChange);

		return data;
	}

	public BigDecimal getGetExecutableLastPrice() {
		return getExecutableLastPrice;
	}

	public void setGetExecutableLastPrice(BigDecimal getExecutableLastPrice) {
		this.getExecutableLastPrice = getExecutableLastPrice;
	}

	public BigDecimal getPercentageChange() {
		return percentageChange;
	}

	public void setPercentageChange(BigDecimal percentageChange) {
		this.percentageChange = percentageChange;
	}
	
	
	
}
