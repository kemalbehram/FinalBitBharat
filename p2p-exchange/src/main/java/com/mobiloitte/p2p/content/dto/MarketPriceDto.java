package com.mobiloitte.p2p.content.dto;

import java.math.BigDecimal;

public class MarketPriceDto {
	private BigDecimal priceInUsd;
	private BigDecimal priceInInr;
	private BigDecimal priceInEuro;
	private BigDecimal priceInNgn;
	private BigDecimal priceInPounds;
	private BigDecimal priceInRand;
	private BigDecimal priceInCedis;
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
	public BigDecimal getPriceInEuro() {
		return priceInEuro;
	}
	public void setPriceInEuro(BigDecimal priceInEuro) {
		this.priceInEuro = priceInEuro;
	}
	public BigDecimal getPriceInNgn() {
		return priceInNgn;
	}
	public void setPriceInNgn(BigDecimal priceInNgn) {
		this.priceInNgn = priceInNgn;
	}
	public BigDecimal getPriceInPounds() {
		return priceInPounds;
	}
	public void setPriceInPounds(BigDecimal priceInPounds) {
		this.priceInPounds = priceInPounds;
	}
	public BigDecimal getPriceInRand() {
		return priceInRand;
	}
	public void setPriceInRand(BigDecimal priceInRand) {
		this.priceInRand = priceInRand;
	}
	public BigDecimal getPriceInCedis() {
		return priceInCedis;
	}
	public void setPriceInCedis(BigDecimal priceInCedis) {
		this.priceInCedis = priceInCedis;
	}
	

}
