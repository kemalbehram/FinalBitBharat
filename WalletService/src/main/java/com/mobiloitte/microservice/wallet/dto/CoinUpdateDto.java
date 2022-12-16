package com.mobiloitte.microservice.wallet.dto;

public class CoinUpdateDto {

	private String coinFullName;

	private String coinImage;

	private String coinShortName;

	private String coinType;

	private String contractAddress;
	private String category;

	public String getCoinFullName() {
		return coinFullName;
	}

	public void setCoinFullName(String coinFullName) {
		this.coinFullName = coinFullName;
	}

	public String getCoinImage() {
		return coinImage;
	}

	public void setCoinImage(String coinImage) {
		this.coinImage = coinImage;
	}

	public String getCoinShortName() {
		return coinShortName;
	}

	public void setCoinShortName(String coinShortName) {
		this.coinShortName = coinShortName;
	}

	public String getCoinType() {
		return coinType;
	}

	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
