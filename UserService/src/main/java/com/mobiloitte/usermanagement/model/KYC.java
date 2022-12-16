package com.mobiloitte.usermanagement.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobiloitte.usermanagement.enums.KycStatus;

@Entity
@Table
public class KYC {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long kycId;

	@Column(columnDefinition = "varchar(32) default 'PENDING'")
	@Enumerated(EnumType.STRING)
	private KycStatus kycStatus;

	@CreationTimestamp
	private Date createTime;

	@UpdateTimestamp
	private Date updateTime;

	@OneToMany(mappedBy = "kyc", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	List<Document> document;

	public List<Document> getDocument() {
		return document;
	}

	public void setDocument(List<Document> document) {
		this.document = document;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_USER_ID")
	private User user;
	
	private boolean latest;

	public boolean isLatest() {
		return latest;
	}

	public void setLatest(boolean latest) {
		this.latest = latest;
	}

	public Long getKycId() {
		return kycId;
	}

	public void setKycId(Long kycId) {
		this.kycId = kycId;
	}

	public KycStatus getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(KycStatus kycStatus) {
		this.kycStatus = kycStatus;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}