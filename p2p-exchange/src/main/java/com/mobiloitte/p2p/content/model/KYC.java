package com.mobiloitte.p2p.content.model;

import java.util.Date;
import java.util.List;

import javax.swing.text.Document;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import com.mobiloitte.p2p.content.enums.KycStatus;

public class KYC {

	private Long kycId;

	private KycStatus kycStatus;

	private Date createTime;

	private Date updateTime;

	List<Document> document;

	public List<Document> getDocument() {
		return document;
	}

	public void setDocument(List<Document> document) {
		this.document = document;
	}

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


