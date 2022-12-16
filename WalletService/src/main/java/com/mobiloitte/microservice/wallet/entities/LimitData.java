package com.mobiloitte.microservice.wallet.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
public class LimitData {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long limitDataId;
	
	private String limitName;
	
	@Column(scale = 3, precision = 15)
	private BigDecimal limitPrice;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updationTime;

	public Long getLimitDataId() {
		return limitDataId;
	}

	public void setLimitDataId(Long limitDataId) {
		this.limitDataId = limitDataId;
	}

	public String getLimitName() {
		return limitName;
	}

	public void setLimitName(String limitName) {
		this.limitName = limitName;
	}

	public BigDecimal getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(BigDecimal limitPrice) {
		this.limitPrice = limitPrice;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUpdationTime() {
		return updationTime;
	}

	public void setUpdationTime(Date updationTime) {
		this.updationTime = updationTime;
	}
	
	
}
