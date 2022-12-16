package com.mobiloitte.content.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class XinidiaHoldingList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long holdingId;
	private String holding;
	private String tradingFee;
	private String xindiaOtc;
	private String otherPrivilege;

	public Long getHoldingId() {
		return holdingId;
	}

	public void setHoldingId(Long holdingId) {
		this.holdingId = holdingId;
	}

	public String getHolding() {
		return holding;
	}

	public void setHolding(String holding) {
		this.holding = holding;
	}

	public String getTradingFee() {
		return tradingFee;
	}

	public void setTradingFee(String tradingFee) {
		this.tradingFee = tradingFee;
	}

	public String getXindiaOtc() {
		return xindiaOtc;
	}

	public void setXindiaOtc(String xindiaOtc) {
		this.xindiaOtc = xindiaOtc;
	}

	public String getOtherPrivilege() {
		return otherPrivilege;
	}

	public void setOtherPrivilege(String otherPrivilege) {
		this.otherPrivilege = otherPrivilege;
	}

}
