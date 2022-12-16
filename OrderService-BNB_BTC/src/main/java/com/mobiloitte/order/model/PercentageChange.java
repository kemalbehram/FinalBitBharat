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
public class PercentageChange implements Serializable {

	private static final long serialVersionUID = -174382016832709295L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long PercentageChangeId;

	@Column(scale = 8, precision = 20, columnDefinition = "DECIMAL(20,8) default 0.0")
	private BigDecimal percentageChange;

	public PercentageChange() {
		super();
	}

	public Long getPercentageChangeId() {
		return PercentageChangeId;
	}

	public void setPercentageChangeId(Long percentageChangeId) {
		PercentageChangeId = percentageChangeId;
	}

	public BigDecimal getPercentageChange() {
		return percentageChange;
	}

	public void setPercentageChange(BigDecimal percentageChange) {
		this.percentageChange = percentageChange;
	}

	public PercentageChange(BigDecimal percentageChange) {
		super();
		this.percentageChange = percentageChange;
	}
}
