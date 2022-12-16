package com.mobiloitte.usermanagement.dto;

public class NomimeeUpdateDto {

	private Long nomineeId;

	private String email;

	private String firstName;

	private String lastName;

	private String relationShip;

	private String dob;

	private String address;

	private String phoneNo;

	public final Long getNomineeId() {
		return nomineeId;
	}

	public final void setNomineeId(Long nomineeId) {
		this.nomineeId = nomineeId;
	}

	public final String getEmail() {
		return email;
	}

	public final void setEmail(String email) {
		this.email = email;
	}

	public final String getFirstName() {
		return firstName;
	}

	public final void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public final String getLastName() {
		return lastName;
	}

	public final void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public final String getRelationShip() {
		return relationShip;
	}

	public final void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}

	public final String getDob() {
		return dob;
	}

	public final void setDob(String dob) {
		this.dob = dob;
	}

	public final String getAddress() {
		return address;
	}

	public final void setAddress(String address) {
		this.address = address;
	}

	public final String getPhoneNo() {
		return phoneNo;
	}

	public final void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

}
