package com.mobiloitte.microservice.wallet.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Vijay Sahu
 *
 */
public class BankDetailsDto {

	@ApiModelProperty(value = "Name of the Bank Account Holder")
//	@NotBlank(message = "Account Holder Name Can Not be Left Blank")
	private String accountHolderName;

	@ApiModelProperty(value = "Bank Account Number")
//	@NotBlank(message = "Account Number Can Not be Left Blank")
	private String accountNo;

	@ApiModelProperty(value = "Name of The Bank")
//	@NotBlank(message = "Bank Name Can Not be Left Blank")
	private String bankName;

	@ApiModelProperty(value = "Swift Number of the Bank")
//	@NotBlank(message = "Swift Number Can Not be Left Blank")
	private String swiftNo;

	@ApiModelProperty(value = "IBAN Number of the Bank")
//	@NotBlank(message = "IBAN Number Can Not be Left Blank")
	private String ibanNo;

	@ApiModelProperty(value = "Mobile Number of the Bank Account Honder")
//	@NotBlank(message = "Mobile Number Can Not be Left Blank")
	private String contactNo;

	@ApiModelProperty(value = "IFSCCode Number of the Bank")
	private String ifsc;

	private String imageUrl;

	private String accountType;

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
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

	public String getSwiftNo() {
		return swiftNo;
	}

	public void setSwiftNo(String swiftNo) {
		this.swiftNo = swiftNo;
	}

	public String getIbanNo() {
		return ibanNo;
	}

	public void setIbanNo(String ibanNo) {
		this.ibanNo = ibanNo;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}