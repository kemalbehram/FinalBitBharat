package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;
import java.util.Date;

public class SmallAssetListDto {
	private Long smallAssetId;
	private String coinName;
	private BigDecimal usdAmount;
	private BigDecimal totalUsdAmount;
	private Long userId;
	private Date createDate;

	public Long getSmallAssetId() {
		return smallAssetId;
	}

	public void setSmallAssetId(Long smallAssetId) {
		this.smallAssetId = smallAssetId;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public BigDecimal getUsdAmount() {
		return usdAmount;
	}

	public void setUsdAmount(BigDecimal usdAmount) {
		this.usdAmount = usdAmount;
	}

	public BigDecimal getTotalUsdAmount() {
		return totalUsdAmount;
	}

	public void setTotalUsdAmount(BigDecimal totalUsdAmount) {
		this.totalUsdAmount = totalUsdAmount;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
