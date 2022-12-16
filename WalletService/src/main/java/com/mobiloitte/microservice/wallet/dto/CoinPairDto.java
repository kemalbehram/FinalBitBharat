package com.mobiloitte.microservice.wallet.dto;

public class CoinPairDto {

	private Long coinPairId;

	private String baseCoin;

	private String executableCoin;

	public Long getCoinPairId() {
		return coinPairId;
	}

	public void setCoinPairId(Long coinPairId) {
		this.coinPairId = coinPairId;
	}

	public String getBaseCoin() {
		return baseCoin;
	}

	public void setBaseCoin(String baseCoin) {
		this.baseCoin = baseCoin;
	}

	public String getExecutableCoin() {
		return executableCoin;
	}

	public void setExecutableCoin(String executableCoin) {
		this.executableCoin = executableCoin;
	}

}
