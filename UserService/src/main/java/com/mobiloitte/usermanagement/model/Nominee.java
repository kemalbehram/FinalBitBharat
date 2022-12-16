package com.mobiloitte.usermanagement.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mobiloitte.usermanagement.enums.NomineeStatus;

/**
 * The Class Nominee.
 * 
 * @author Priyank Mishra
 */

@Entity
@Table
public class Nominee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nomineeId;

	private Long userId;
	private BigDecimal nomineeFee;

	private String email;

	private String firstName;

	private String lastName;

	private String dob;

	private String address;

	private String phoneNo;

	private Float sharePercentage;

	private String relationShip;

	private String imageUrl;

	private String imageUrl1;

	private String reason;

	public BigDecimal getNomineeFee() {
		return nomineeFee;
	}

	public void setNomineeFee(BigDecimal nomineeFee) {
		this.nomineeFee = nomineeFee;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public final String getAddress() {
		return address;
	}

	public final void setAddress(String address) {
		this.address = address;
	}

	@Enumerated(EnumType.STRING)
	private NomineeStatus nomineeStatus;

	public final String getDob() {
		return dob;
	}

	public final void setDob(String dob) {
		this.dob = dob;
	}

	public final String getPhoneNo() {
		return phoneNo;
	}

	public final void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public final Float getSharePercentage() {
		return sharePercentage;
	}

	public final void setSharePercentage(Float sharePercentage) {
		this.sharePercentage = sharePercentage;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public String getImageUrl1() {
		return imageUrl1;
	}

	public void setImageUrl1(String imageUrl1) {
		this.imageUrl1 = imageUrl1;
	}

	public NomineeStatus getNomineeStatus() {
		return nomineeStatus;
	}

	public void setNomineeStatus(NomineeStatus nomineeStatus) {
		this.nomineeStatus = nomineeStatus;
	}

	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
