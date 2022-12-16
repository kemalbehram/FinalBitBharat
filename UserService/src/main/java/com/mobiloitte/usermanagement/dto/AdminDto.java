package com.mobiloitte.usermanagement.dto;

import java.util.List;

import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.model.UserPermissions;

public class AdminDto {

	private Long userId;

	private String email;

	private String firstName;

	private String lastName;

	private String countryCode;

	private String phoneNo;

	private String gender;

	private String role;

	private UserStatus userStatus;

	private Long roleId;
	private String zipCode;
	private String dob;
	private String country;
	private String city;
	private String state;
	private String address;
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	private List<UserPermissions> userPermissions;

	private String webUrl;

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public List<UserPermissions> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(List<UserPermissions> userPermissions) {
		this.userPermissions = userPermissions;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public AdminDto(Long userId, String email, String firstName, String lastName, String phoneNo, String gender) {
		super();
		this.userId = userId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNo = phoneNo;
		this.gender = gender;
	}

	public AdminDto(Long userId, String email, String firstName, String lastName, String phoneNo, String gender,
			String role) {
		super();
		this.userId = userId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNo = phoneNo;
		this.gender = gender;
		this.role = role;
	}

	public AdminDto(Long userId, String email, String firstName, String lastName, String phoneNo, String gender,
			String role, String countryCode, Long roleId, UserStatus userStatus) {
		super();
		this.userId = userId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNo = phoneNo;
		this.gender = gender;
		this.role = role;
		this.countryCode = countryCode;
		this.roleId = roleId;
		this.userStatus = userStatus;
	}

	/**
	 * @param userId
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param countryCode
	 * @param phoneNo
	 * @param gender
	 * @param role
	 * @param userStatus
	 * @param roleId
	 * @param zipCode
	 * @param dob
	 * @param country
	 * @param city
	 * @param state
	 * @param address
	 */
//	public AdminDto(Long userId, String email, String firstName, String lastName, String countryCode, String phoneNo,
//			String gender, String role, UserStatus userStatus, Long roleId, String zipCode, String dob, String country,
//			String city, String state, String address) {
	public AdminDto(Long userId, String email, String firstName, String address, String zipCode, String dob,
			String country, String city, String state, String lastName, String phoneNo, String gender, String imageurl,
			String role, String countryCode, Long roleId, UserStatus userStatus) {
		super();
		this.userId = userId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.countryCode = countryCode;
		this.phoneNo = phoneNo;
		this.gender = gender;
		this.role = role;
		this.userStatus = userStatus;
		this.roleId = roleId;
		this.zipCode = zipCode;
		this.dob = dob;
		this.country = country;
		this.city = city;
		this.state = state;
		this.address = address;
		this.imageUrl = imageurl;
	}

	public AdminDto() {
		super();
	}

}
