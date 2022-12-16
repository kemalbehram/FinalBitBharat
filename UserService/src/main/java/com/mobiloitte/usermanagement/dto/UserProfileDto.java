package com.mobiloitte.usermanagement.dto;

import java.util.Date;
import java.util.List;

import com.mobiloitte.usermanagement.enums.Authority;
import com.mobiloitte.usermanagement.enums.P2pStatus;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.model.KYC;
import com.mobiloitte.usermanagement.util.TwoFaType;

public class UserProfileDto {

	private Long userId;
	private String email;
	private TwoFaType twoFaType;
	private UserStatus userStatus;
	private RoleStatus role;
	private String firstName;
	private String lastName;
	private String middleName;
	private String phoneNo;
	private String country;
	private KYC kyc;
	private String city;
	private String state;
	private String gender;
	private String imageUrl;
	private String address;
	private List<Authority> previlage;
	private String dob;
	private String countryCode;
	private String pnWithoutCountryCode;
	private Date creationTime;
	private String zipCode;
	private String randomId;
	private Date emailVerificationTime;
	private String myRefferalCode;
	private P2pStatus p2pStatus;

	public P2pStatus getP2pStatus() {
		return p2pStatus;
	}

	public void setP2pStatus(P2pStatus p2pStatus) {
		this.p2pStatus = p2pStatus;
	}

	private String referredCode;

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

	public Date getEmailVerificationTime() {
		return emailVerificationTime;
	}

	public void setEmailVerificationTime(Date emailVerificationTime) {
		this.emailVerificationTime = emailVerificationTime;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

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

	public List<Authority> getPrevilage() {
		return previlage;
	}

	public void setPrevilage(List<Authority> previlage) {
		this.previlage = previlage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public KYC getKyc() {
		return kyc;
	}

	public void setKyc(KYC kyc) {
		this.kyc = kyc;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public TwoFaType getTwoFaType() {
		return twoFaType;
	}

	public void setTwoFaType(TwoFaType twoFaType) {
		this.twoFaType = twoFaType;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public RoleStatus getRole() {
		return role;
	}

	public void setRole(RoleStatus role) {
		this.role = role;
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

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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
