package com.mobiloitte.content.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ContactUsCryptobiz {

	@NotNull(message = "Email cannot be empty.")
	@Email(message = "should be according to Email format")
	private String email;

	@NotEmpty(message = "phoneNo cannot be empty.")
	private String phoneNo;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	@NotEmpty(message = "message cannot be empty.")
	private String message;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
