package com.mobiloitte.usermanagement.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonInclude;
import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobiloitte.usermanagement.enums.P2pStatus;
import com.mobiloitte.usermanagement.enums.SocialType;
import com.mobiloitte.usermanagement.util.TwoFaType;

@Entity
@Table
public class UserDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userDetailId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String phoneNo;
	@Column(columnDefinition = "varchar(32) default 'NONE'")
	@Enumerated(EnumType.STRING)
	private TwoFaType twoFaType = TwoFaType.NONE;
	private String secretKey;
	private String country;
	private String city;
	private String state;
	private String gender;
	private String imageUrl;
	private String finalReferal;
	@Enumerated(EnumType.STRING)
	private P2pStatus p2pStatus;
	private String directReferCount;
	private String indirectrefer;
	private BigDecimal totalReferalPrice;

	private BigDecimal tierTwoReferal;

	private BigDecimal tierThreeReferal;

	private BigDecimal registerBonus;

	public P2pStatus getP2pStatus() {
		return p2pStatus;
	}

	public void setP2pStatus(P2pStatus p2pStatus) {
		this.p2pStatus = p2pStatus;
	}

	public String getDirectReferCount() {
		return directReferCount;
	}

	public void setDirectReferCount(String directReferCount) {
		this.directReferCount = directReferCount;
	}

	public String getIndirectrefer() {
		return indirectrefer;
	}

	public void setIndirectrefer(String indirectrefer) {
		this.indirectrefer = indirectrefer;
	}

	public BigDecimal getTierTwoReferal() {
		return tierTwoReferal;
	}

	public void setTierTwoReferal(BigDecimal tierTwoReferal) {
		this.tierTwoReferal = tierTwoReferal;
	}

	public BigDecimal getTierThreeReferal() {
		return tierThreeReferal;
	}

	public void setTierThreeReferal(BigDecimal tierThreeReferal) {
		this.tierThreeReferal = tierThreeReferal;
	}

	public BigDecimal getRegisterBonus() {
		return registerBonus;
	}

	public void setRegisterBonus(BigDecimal registerBonus) {
		this.registerBonus = registerBonus;
	}

	public BigDecimal getTotalReferalPrice() {
		return totalReferalPrice;
	}

	public void setTotalReferalPrice(BigDecimal totalReferalPrice) {
		this.totalReferalPrice = totalReferalPrice;
	}

	public String getFinalReferal() {
		return finalReferal;
	}

	public void setFinalReferal(String finalReferal) {
		this.finalReferal = finalReferal;
	}

	@Lob
	@Column(length = 20971520)
	private String address;
	@Lob
	@Column(length = 20971520)
	private String suspendReason;
	private String dob;
	private SocialType socialType;
	private String socialId;
	private String countryCode;
	private String pnWithoutCountryCode;
	private String zipCode;

	private String myRefferalCode;

	private String referredCode;

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getSuspendReason() {
		return suspendReason;
	}

	public void setSuspendReason(String suspendReason) {
		this.suspendReason = suspendReason;
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

	public SocialType getSocialType() {
		return socialType;
	}

	public void setSocialType(SocialType socialType) {
		this.socialType = socialType;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	@OneToOne(mappedBy = "userDetail", fetch = FetchType.LAZY)
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@CreationTimestamp
	private Date createTime;

	@UpdateTimestamp
	private Date updateTime;

	public Long getUserDetailId() {
		return userDetailId;
	}

	public TwoFaType getTwoFaType() {
		return twoFaType;
	}

	public void setTwoFaType(TwoFaType twoFaType) {
		this.twoFaType = twoFaType;
	}

	public void setUserDetailId(Long userDetailId) {
		this.userDetailId = userDetailId;
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

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
