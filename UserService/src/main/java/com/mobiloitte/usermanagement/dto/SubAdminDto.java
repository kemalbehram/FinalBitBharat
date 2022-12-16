package com.mobiloitte.usermanagement.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.mobiloitte.usermanagement.enums.Authority;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.model.UserPermissions;

public class SubAdminDto {

//	@NotBlank(message = "should not be blank and enter valid first name")
	private String firstName;

//	@NotBlank(message = "should not be blank and enter valid last name")
	private String lastName;

//	@NotBlank(message = "should not be blank and enter valid email")
	@Email
	private String email;

	private Long userIdToUpdate;

	/*
	 * @NotBlank(message = "should not be blank and enter valid password")
	 * 
	 * @Size(min=8 , message="password should be atleast 8 characters")
	 * 
	 * @Size(max=16 , message="password should be atmost 16 characters")
	 */
	private String password;
	/* @ApiModelProperty(hidden=true) */

	@NotBlank(message = "should not be blank and enter valid phone no")
	private String phoneNo;

	// @NotBlank(message = "should not be blank and enter valid role status")
	private RoleStatus roleStatus;

	private String country;

	private String city;

	private String address;

	private String imageUrl;

	private List<Authority> previlage;

//	@NotBlank(message = "webUrl should not be blank")
	private String webUrl;

	private String gender;

	private List<UserPermissions> userPermissions;

	private String roleId;

	private String countryCode;

	private String dob;

	private String state;

	private String location;

	private String ipAddress;

	private String zipCode;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Long getUserIdToUpdate() {
		return userIdToUpdate;
	}

	public void setUserIdToUpdate(Long userIdToUpdate) {
		this.userIdToUpdate = userIdToUpdate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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

	public RoleStatus getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(RoleStatus roleStatus) {
		this.roleStatus = roleStatus;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<Authority> getPrevilage() {
		return previlage;
	}

	public void setPrevilage(List<Authority> previlage) {
		this.previlage = previlage;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<UserPermissions> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(List<UserPermissions> userPermissions) {
		this.userPermissions = userPermissions;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
