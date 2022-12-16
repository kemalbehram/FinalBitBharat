package com.mobiloitte.microservice.wallet.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Mnemonic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mnemonicId;

	private String mnemonic;

	private Boolean isDeleted;

	private String coinName;

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getMnemonicId() {
		return mnemonicId;
	}

	public void setMnemonicId(Long mnemonicId) {
		this.mnemonicId = mnemonicId;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

}
