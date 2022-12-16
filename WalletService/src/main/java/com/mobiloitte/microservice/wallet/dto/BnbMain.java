package com.mobiloitte.microservice.wallet.dto;

import io.swagger.annotations.ApiModelProperty;

public class BnbMain {

	private String password;
	
	@ApiModelProperty(value = "receiver address")
	private String toAddress;
	private String amount;
	private Keystore keystore;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Keystore getKeystore() {
		return keystore;
	}

	public void setKeystore(Keystore keystore) {
		this.keystore = keystore;
	}

}