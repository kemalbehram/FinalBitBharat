package com.mobiloitte.usermanagement.dto;

import java.util.Date;

import com.mobiloitte.usermanagement.enums.NomineeStatus;

public class NomineeListDto {

	private Long nomineeId;

	private Long userId;

	private String email;

	private String phoneNo;

	private Float sharePercentage;

	private NomineeStatus nomineeStatus;
	private String firstName;

	private String lastName;

	private String relationShip;

	private Date date;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRelationShip() {
		return relationShip;
	}

	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getNomineeId() {
		return nomineeId;
	}

	public void setNomineeId(Long nomineeId) {
		this.nomineeId = nomineeId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Float getSharePercentage() {
		return sharePercentage;
	}

	public void setSharePercentage(Float sharePercentage) {
		this.sharePercentage = sharePercentage;
	}

	public NomineeStatus getNomineeStatus() {
		return nomineeStatus;
	}

	public void setNomineeStatus(NomineeStatus nomineeStatus) {
		this.nomineeStatus = nomineeStatus;
	}

}
