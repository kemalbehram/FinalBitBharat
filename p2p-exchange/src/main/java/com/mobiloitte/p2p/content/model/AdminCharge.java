package com.mobiloitte.p2p.content.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class AdminCharge {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long adminChargeId;
	private BigDecimal fees;
	private String coinName;
	private Boolean isDeleted;

	public Long getAdminChargeId() {
		return adminChargeId;
	}

	public void setAdminChargeId(Long adminChargeId) {
		this.adminChargeId = adminChargeId;
	}

	public BigDecimal getFees() {
		return fees;
	}

	public void setFees(BigDecimal fees) {
		this.fees = fees;
	}

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

}
