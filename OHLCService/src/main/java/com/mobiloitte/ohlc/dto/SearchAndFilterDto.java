
package com.mobiloitte.ohlc.dto;

import com.mobiloitte.ohlc.enums.OrderSide;

public class SearchAndFilterDto {
	private Long fromDate;

	private Long toDate;

	private OrderSide side;

	private String baseCoin;

	private String exeCoin;

	private Long userId;

	private Long transactionId;
	
	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getFromDate() {
		return fromDate;
	}

	public void setFromDate(Long fromDate) {
		this.fromDate = fromDate;
	}

	public Long getToDate() {
		return toDate;
	}

	public void setToDate(Long toDate) {
		this.toDate = toDate;
	}

	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

	public String getBaseCoin() {
		return baseCoin;
	}

	public void setBaseCoin(String baseCoin) {
		this.baseCoin = baseCoin;
	}

	public String getExeCoin() {
		return exeCoin;
	}

	public void setExeCoin(String exeCoin) {
		this.exeCoin = exeCoin;
	}

}
