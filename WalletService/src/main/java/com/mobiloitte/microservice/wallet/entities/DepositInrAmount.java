package com.mobiloitte.microservice.wallet.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class DepositInrAmount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long depositId;

	private BigDecimal amount;

	private Long userId;
	private BigDecimal depositFee;
	private BigDecimal withdrawFee;
	private BigDecimal minimunwithdraw;
	private BigDecimal minimumdeposite;
	@Column(scale = 8, precision = 15)
	private BigDecimal kycCommision;
	@Column(scale = 8, precision = 15)
	private BigDecimal withdrawCommision;
	@Column(scale = 8, precision = 15)
	private BigDecimal depositCommision;

	public BigDecimal getKycCommision() {
		return kycCommision;
	}

	public void setKycCommision(BigDecimal kycCommision) {
		this.kycCommision = kycCommision;
	}

	public BigDecimal getWithdrawCommision() {
		return withdrawCommision;
	}

	public void setWithdrawCommision(BigDecimal withdrawCommision) {
		this.withdrawCommision = withdrawCommision;
	}

	public BigDecimal getDepositCommision() {
		return depositCommision;
	}

	public void setDepositCommision(BigDecimal depositCommision) {
		this.depositCommision = depositCommision;
	}

	public BigDecimal getMinimunwithdraw() {
		return minimunwithdraw;
	}

	public void setMinimunwithdraw(BigDecimal minimunwithdraw) {
		this.minimunwithdraw = minimunwithdraw;
	}

	public BigDecimal getMinimumdeposite() {
		return minimumdeposite;
	}

	public void setMinimumdeposite(BigDecimal minimumdeposite) {
		this.minimumdeposite = minimumdeposite;
	}

	public BigDecimal getDepositFee() {
		return depositFee;
	}

	public void setDepositFee(BigDecimal depositFee) {
		this.depositFee = depositFee;
	}

	public BigDecimal getWithdrawFee() {
		return withdrawFee;
	}

	public void setWithdrawFee(BigDecimal withdrawFee) {
		this.withdrawFee = withdrawFee;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDepositId() {
		return depositId;
	}

	public void setDepositId(Long depositId) {
		this.depositId = depositId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
