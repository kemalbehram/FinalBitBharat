package com.mobiloitte.microservice.wallet.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class MarketPrice.
 * 
 * @author Ankush Mohapatra
 */
@Entity
@Table
public class MarketPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long marketPriceId;

	private String coinName;

	@Column(scale = 6, precision = 15)
	private BigDecimal priceInUsd;

	@Column(scale = 8, precision = 15)
	private BigDecimal priceInBtc;
	@Column(scale = 8, precision = 15)
	private BigDecimal priceInInr;
	
	
	@Column(scale = 8, precision = 15)
	private BigDecimal priceInEur;

	public BigDecimal getPriceInInr() {
		return priceInInr;
	}

	public void setPriceInInr(BigDecimal priceInInr) {
		this.priceInInr = priceInInr;
	}

	public Long getMarketPriceId() {
		return marketPriceId;
	}

	public void setMarketPriceId(Long marketPriceId) {
		this.marketPriceId = marketPriceId;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public BigDecimal getPriceInUsd() {
		return priceInUsd;
	}

	public void setPriceInUsd(BigDecimal priceInUsd) {
		this.priceInUsd = priceInUsd;
	}

	public BigDecimal getPriceInBtc() {
		return priceInBtc;
	}

	public void setPriceInBtc(BigDecimal priceInBtc) {
		this.priceInBtc = priceInBtc;
	}

	public BigDecimal getPriceInEur() {
		return priceInEur;
	}

	public void setPriceInEur(BigDecimal priceInEur) {
		this.priceInEur = priceInEur;
	}

}
