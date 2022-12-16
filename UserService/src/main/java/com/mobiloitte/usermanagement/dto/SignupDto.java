package com.mobiloitte.usermanagement.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.RandomStringUtils;

import com.mobiloitte.usermanagement.enums.RoleStatus;

public class SignupDto {

//	@NotBlank(message = "should not be blank and enter valid first name")
	private String firstName;
	// @NotBlank(message = "should not be blank and enter valid last name")
	private String lastName;
	// @NotBlank(message = "should not be blank and enter valid last name")
	private String middleName;

//	@NotBlank(message = "should not be blank and enter valid email")
	@Email
	private String email;

//	@NotBlank(message = "should not be blank and enter valid password")
	@Size(min = 8, message = "password should be atleast 8 characters")
	@Size(max = 16, message = "password should be atmost 16 characters")
	private String password;
	/* @ApiModelProperty(hidden=true) */

	// @NotBlank(message = "should not be blank and enter valid phone no")
	private String phoneNo;

//	@NotBlank(message = "should not be blank and enter valid role status")
	private RoleStatus roleStatus;

	private String country;

	private String city;

	private String state;

	private String dob;

	private String address;

	private String imageUrl;

//	@NotBlank(message = "webUrl should not be blank")
	private String webUrl;

	private String deviceToken;

	private String deviceType;

	private String socialType;

	private String socialId;

	private String myRefferalCode;

	private String referredCode;
	
	private String zipCode;

	// s@Pattern(regexp = "^[a-zA-Z0-9]{3}", message = "length must be 8",)
	// @Size(max = 8)
	private String randomId;
	

	public final String getZipCode() {
		return zipCode;
	}

	public final void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMyRefferalCode() {
		return myRefferalCode;
	}

	public void setMyRefferalCode(String myRefferalCode) {
		this.myRefferalCode = myRefferalCode;
	}

	public String getReferredCode() {
		return referredCode;
	}

	public void setReferredCode(String referredCode) {
		this.referredCode = referredCode;
	}

	public String getSocialType() {
		return socialType;
	}

	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public RoleStatus getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(RoleStatus roleStatus) {
		this.roleStatus = roleStatus;

	}

	private String countryCode;
	private String pnWithoutCountryCode;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPnWithoutCountryCode() {
		return pnWithoutCountryCode;
	}

	public void setPnWithoutCountryCode(String pnWithoutCountryCode) {
		this.pnWithoutCountryCode = pnWithoutCountryCode;
	}

	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

}
