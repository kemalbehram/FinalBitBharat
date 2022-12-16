package com.mobiloitte.order.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class LastPrice implements Serializable {

	private static final long serialVersionUID = -174382016832709295L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lastPriceId;

	@Column(scale = 8, precision = 20, columnDefinition = "DECIMAL(20,8) default 0.0")
	private BigDecimal exeLastPrice;

	
	public Long getLastPriceId() {
		return lastPriceId;
	}

	public void setLastPriceId(Long lastPriceId) {
		this.lastPriceId = lastPriceId;
	}

	public BigDecimal getExeLastPrice() {
		return exeLastPrice;
	}

	public void setExeLastPrice(BigDecimal exeLastPrice) {
		this.exeLastPrice = exeLastPrice;
	}

	public LastPrice(BigDecimal exeLastPrice) {
		super();
		this.exeLastPrice = exeLastPrice;
	}

	public LastPrice() {
		super();
	}

	
}
