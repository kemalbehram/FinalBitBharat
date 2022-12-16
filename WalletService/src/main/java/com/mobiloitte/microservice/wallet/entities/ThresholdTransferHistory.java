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

@Entity
@Table
public class ThresholdTransferHistory {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long thresholdTransferHistorysId;
	
	private String fromCoin;
	
	private String fromCoinAddress;
	
	private String toCoin;
	
	private String toCoinAddress;
	
	private String txnHash;
	
	@Column(scale = 8, precision = 15)
	private BigDecimal units;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date txnTime;

	public Long getThresholdTransferHistorysId() {
		return thresholdTransferHistorysId;
	}

	public void setThresholdTransferHistorysId(Long thresholdTransferHistorysId) {
		this.thresholdTransferHistorysId = thresholdTransferHistorysId;
	}

	public String getFromCoin() {
		return fromCoin;
	}

	public void setFromCoin(String fromCoin) {
		this.fromCoin = fromCoin;
	}

	public String getFromCoinAddress() {
		return fromCoinAddress;
	}

	public void setFromCoinAddress(String fromCoinAddress) {
		this.fromCoinAddress = fromCoinAddress;
	}

	public String getToCoin() {
		return toCoin;
	}

	public void setToCoin(String toCoin) {
		this.toCoin = toCoin;
	}

	public String getToCoinAddress() {
		return toCoinAddress;
	}

	public void setToCoinAddress(String toCoinAddress) {
		this.toCoinAddress = toCoinAddress;
	}

	public String getTxnHash() {
		return txnHash;
	}

	public void setTxnHash(String txnHash) {
		this.txnHash = txnHash;
	}

	public BigDecimal getUnits() {
		return units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
	}

	public Date getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(Date txnTime) {
		this.txnTime = txnTime;
	}
	
	
}
