package com.mobiloitte.usermanagement.dto;

import java.util.List;

import javax.validation.constraints.Email;

import com.mobiloitte.usermanagement.enums.Authority;
import com.mobiloitte.usermanagement.enums.RoleStatus;

public class AddAdminDto {
//	@NotBlank(message = "should not be blank and enter valid first name")
	private String firstName;

//	@NotBlank(message = "should not be blank and enter valid last name")
	private String lastName;

//	@NotBlank(message = "should not be blank and enter valid email")
	@Email
	private String email;

	/*
	 * @NotBlank(message = "should not be blank and enter valid password")
	 * 
	 * @Size(min=8 , message="password should be atleast 8 characters")
	 * 
	 * @Size(max=16 , message="password should be atmost 16 characters")
	 */
	private String password;
	/* @ApiModelProperty(hidden=true) */

//	@NotBlank(message = "should not be blank and enter valid phone no")
	private String phoneNo;

	// @NotBlank(message = "should not be blank and enter valid role status")
	private RoleStatus roleStatus;

	private List<Authority> previlage;

//	@NotBlank(message = "webUrl should not be blank")
	private String webUrl;

	private String gender;

	private String roleId;

	private String location;

	private String ipAddress;

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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

}
