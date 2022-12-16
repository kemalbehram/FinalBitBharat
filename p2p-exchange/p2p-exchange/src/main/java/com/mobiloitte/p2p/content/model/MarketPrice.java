package com.mobiloitte.p2p.content.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class MarketPrice {

	/** The market price id. */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long marketPriceId;
	
	/** The coin name. */
	private String coinName;
	
	/** The price in usdt. */
	@Column(scale = 6, precision = 15)
	private BigDecimal priceInUsd;

	/**
	 * Gets the market price id.
	 *
	 * @return the market price id
	 */
	/*
	 * public Long getMarketPriceId() { return marketPriceId; }
	 */

	/**
	 * Sets the market price id.
	 *
	 * @param marketPriceId the new market price id
	 */
	/*
	 * public void setMarketPriceId(Long marketPriceId) { this.marketPriceId =
	 * marketPriceId; }
	 */
	

	/**
	 * Gets the coin name.
	 *
	 * @return the coin name
	 */
	public String getCoinName() {
		return coinName;
	}

	/**
	 * Sets the coin name.
	 *
	 * @param coinName the new coin name
	 */
	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	/**
	 * Gets the price in usd.
	 *
	 * @return the price in usd
	 */
	public BigDecimal getPriceInUsd() {
		return priceInUsd;
	}

	/**
	 * Sets the price in usd.
	 *
	 * @param priceInUsd the new price in usd
	 */
	public void setPriceInUsd(BigDecimal priceInUsd) {
		this.priceInUsd = priceInUsd;
	}

}
