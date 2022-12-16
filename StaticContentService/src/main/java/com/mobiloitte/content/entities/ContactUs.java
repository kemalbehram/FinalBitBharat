package com.mobiloitte.content.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
public class ContactUs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long contactUsId;
	
	private String email;

	private String phoneNo;

	private String name;
	
	private Boolean isResolved;

	@Lob
	@Column(length = 20971520)
	private String message;
	
	private Boolean isDeleted;
	
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	public Long getContactUsId() {
		return contactUsId;
	}

	public void setContactUsId(Long contactUsId) {
		this.contactUsId = contactUsId;
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

	public ContactUs() {}
			
	public ContactUs(String email, String phoneNo, String name, String message, Boolean isResolved) {
		super();
		this.email = email;
		this.phoneNo = phoneNo;
		this.name = name;
		this.message = message;
		this.isResolved = isResolved;
	}

	public Boolean getIsResolved() {
		return isResolved;
	}

	public void setIsResolved(Boolean isResolved) {
		this.isResolved = isResolved;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
	
	
	
}
