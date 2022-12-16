package com.mobiloitte.microservice.wallet.dto;

import java.util.Date;

import com.mobiloitte.microservice.wallet.enums.BankStatus;



public class SearchBankDetailDto {

	private Long bankDetailId;

	private String accountHolderName;

	private String accountNo;

	private String bankName;

	private Date creationTime;

	private Date updationTime;

	private BankStatus bankStatus;

	public Long getBankDetailId() {
		return bankDetailId;
	}

	public void setBankDetailId(Long bankDetailId) {
		this.bankDetailId = bankDetailId;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public BankStatus getBankStatus() {
		return bankStatus;
	}

	public void setBankStatus(BankStatus bankStatus) {
		this.bankStatus = bankStatus;
	}

}