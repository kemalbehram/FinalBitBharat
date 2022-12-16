package com.mobiloitte.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("exchange")
public class AppConfig {
	private String instrument;
	private String baseCoin;
	private String exeCoin;
	private boolean liquidityEnabled;
	private boolean binanceEnable;
	private boolean hitBtcEnable;
	private boolean poloniexEnable;

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		String[] split = instrument.split("_");
		this.baseCoin = split[1];
		this.exeCoin = split[0];
		this.instrument = instrument;
	}

	public String getBaseCoin() {
		return baseCoin;
	}

	public String getExeCoin() {
		return exeCoin;
	}

	public boolean getLiquidityEnabled() {
		return liquidityEnabled;
	}

	public void setLiquidityEnabled(boolean liquidityEnabled) {
		this.liquidityEnabled = liquidityEnabled;
	}

	public boolean isBinanceEnable() {
		return binanceEnable;
	}

	public void setBinanceEnable(boolean binanceEnable) {
		this.binanceEnable = binanceEnable;
	}

	public boolean isHitBtcEnable() {
		return hitBtcEnable;
	}

	public void setHitBtcEnable(boolean hitBtcEnable) {
		this.hitBtcEnable = hitBtcEnable;
	}

	public boolean isPoloniexEnable() {
		return poloniexEnable;
	}

	public void setPoloniexEnable(boolean poloniexEnable) {
		this.poloniexEnable = poloniexEnable;
	}

	public void setBaseCoin(String baseCoin) {
		this.baseCoin = baseCoin;
	}

	public void setExeCoin(String exeCoin) {
		this.exeCoin = exeCoin;
	}
	
}
