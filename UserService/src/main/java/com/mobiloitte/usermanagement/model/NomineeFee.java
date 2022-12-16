package com.mobiloitte.usermanagement.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class NomineeFee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feeId;
	private BigDecimal nomineeFee;
private Long userId;


	public Long getUserId() {
	return userId;
}

public void setUserId(Long userId) {
	this.userId = userId;
}

	public Long getFeeId() {
		return feeId;
	}

	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}

	public BigDecimal getNomineeFee() {
		return nomineeFee;
	}

	public void setNomineeFee(BigDecimal nomineeFee) {
		this.nomineeFee = nomineeFee;
	}

}
