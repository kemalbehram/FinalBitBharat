package com.mobiloitte.microservice.wallet.dto;

public class CoinStatusDto {

	private Boolean isVisible;
	private Boolean isWithdrawl;
	private Boolean isDeposite;
	private String remark;
	private Boolean isDeposit;

	public Boolean getIsDeposit() {
		return isDeposit;
	}

	public void setIsDeposit(Boolean isDeposit) {
		this.isDeposit = isDeposit;
	}

	private int confermation;
	private String coinName;

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Boolean getIsWithdrawl() {
		return isWithdrawl;
	}

	public void setIsWithdrawl(Boolean isWithdrawl) {
		this.isWithdrawl = isWithdrawl;
	}

	public Boolean getIsDeposite() {
		return isDeposite;
	}

	public void setIsDeposite(Boolean isDeposite) {
		this.isDeposite = isDeposite;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getConfermation() {
		return confermation;
	}

	public void setConfermation(int confermation) {
		this.confermation = confermation;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

}
