package com.mobiloitte.usermanagement.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class AdminReferal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long referalId;

	private BigDecimal referalAmountRegister;

	private Long totalReferal;

	private BigDecimal distributedFund;

	private BigDecimal availablefund;

	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	private BigDecimal limit;

	public Long getReferalId() {
		return referalId;
	}

	public void setReferalId(Long referalId) {
		this.referalId = referalId;
	}

	public BigDecimal getReferalAmountRegister() {
		return referalAmountRegister;
	}

	public void setReferalAmountRegister(BigDecimal referalAmountRegister) {
		this.referalAmountRegister = referalAmountRegister;
	}

	public Long getTotalReferal() {
		return totalReferal;
	}

	public void setTotalReferal(Long totalReferal) {
		this.totalReferal = totalReferal;
	}

	public BigDecimal getDistributedFund() {
		return distributedFund;
	}

	public void setDistributedFund(BigDecimal distributedFund) {
		this.distributedFund = distributedFund;
	}

	public BigDecimal getAvailablefund() {
		return availablefund;
	}

	public void setAvailablefund(BigDecimal availablefund) {
		this.availablefund = availablefund;
	}

	public BigDecimal getLimit() {
		return limit;
	}

	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}

}
