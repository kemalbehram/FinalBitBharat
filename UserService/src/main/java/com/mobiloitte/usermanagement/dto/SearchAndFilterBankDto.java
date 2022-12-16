package com.mobiloitte.usermanagement.dto;

import com.mobiloitte.usermanagement.enums.BankStatus;

public class SearchAndFilterBankDto {

	private String search;

	private BankStatus bankStatus;

	private Long fromDate;

	private Long toDate;

	private Integer page;

	private Integer pageSize;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public BankStatus getBankStatus() {
		return bankStatus;
	}

	public void setBankStatus(BankStatus bankStatus) {
		this.bankStatus = bankStatus;
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

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}