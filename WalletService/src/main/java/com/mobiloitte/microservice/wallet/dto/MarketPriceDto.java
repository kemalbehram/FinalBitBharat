package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

public class MarketPriceDto {

	private BigDecimal priceInUsd;
	private BigDecimal priceInInr;
	private BigDecimal priceInEur;

	public BigDecimal getPriceInEur() {
		return priceInEur;
	}

	public void setPriceInEur(BigDecimal priceInEur) {
		this.priceInEur = priceInEur;
	}

	public BigDecimal getPriceInUsd() {
		return priceInUsd;
	}

	public void setPriceInUsd(BigDecimal priceInUsd) {
		this.priceInUsd = priceInUsd;
	}

	public BigDecimal getPriceInInr() {
		return priceInInr;
	}

	public void setPriceInInr(BigDecimal priceInInr) {
		this.priceInInr = priceInInr;
	}

}
