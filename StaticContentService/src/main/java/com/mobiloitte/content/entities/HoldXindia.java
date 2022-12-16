package com.mobiloitte.content.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class HoldXindia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long holdXindiaId;

	private String tradingFeeDiscount;

	private String otcDiscount;

	private String toBecome;

	private String weekly;

	public Long getHoldXindiaId() {
		return holdXindiaId;
	}

	public void setHoldXindiaId(Long holdXindiaId) {
		this.holdXindiaId = holdXindiaId;
	}

	public String getTradingFeeDiscount() {
		return tradingFeeDiscount;
	}

	public void setTradingFeeDiscount(String tradingFeeDiscount) {
		this.tradingFeeDiscount = tradingFeeDiscount;
	}

	public String getOtcDiscount() {
		return otcDiscount;
	}

	public void setOtcDiscount(String otcDiscount) {
		this.otcDiscount = otcDiscount;
	}

	public String getToBecome() {
		return toBecome;
	}

	public void setToBecome(String toBecome) {
		this.toBecome = toBecome;
	}

	public String getWeekly() {
		return weekly;
	}

	public void setWeekly(String weekly) {
		this.weekly = weekly;
	}

}
