package com.mobiloitte.usermanagement.model;

import java.util.Date;

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
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobiloitte.usermanagement.enums.DocumentStatus;

@Entity
@Table
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long documentId;

	private String docName;

	private String docIdNumber;

	private String frontIdUrl;

	private String backIdUrl;

	private String reason;

	private boolean latest;

	private Long documentNumber;

	private String docIdNumber2;

	private String docName2;

	private String frontIdUrl2;

	private String backIdUrl2;

	public String getDocIdNumber2() {
		return docIdNumber2;
	}

	public void setDocIdNumber2(String docIdNumber2) {
		this.docIdNumber2 = docIdNumber2;
	}

	public String getDocName2() {
		return docName2;
	}

	public void setDocName2(String docName2) {
		this.docName2 = docName2;
	}

	public String getFrontIdUrl2() {
		return frontIdUrl2;
	}

	public void setFrontIdUrl2(String frontIdUrl2) {
		this.frontIdUrl2 = frontIdUrl2;
	}

	public String getBackIdUrl2() {
		return backIdUrl2;
	}

	public void setBackIdUrl2(String backIdUrl2) {
		this.backIdUrl2 = backIdUrl2;
	}

	@Column(columnDefinition = "varchar(32) default 'PENDING'")
	@Enumerated(EnumType.STRING)
	private DocumentStatus documentStatus;

	@CreationTimestamp
	private Date createTime;

	@UpdateTimestamp
	private Date updateTime;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_kyc_Id")
	private KYC kyc;

	private String selfieUrl;

	public final String getSelfieUrl() {
		return selfieUrl;
	}

	public final void setSelfieUrl(String selfieUrl) {
		this.selfieUrl = selfieUrl;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
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

	public KYC getKyc() {
		return kyc;
	}

	public void setKyc(KYC kyc) {
		this.kyc = kyc;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isLatest() {
		return latest;
	}

	public void setLatest(boolean latest) {
		this.latest = latest;
	}

	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}

	public DocumentStatus getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(DocumentStatus documentStatus) {
		this.documentStatus = documentStatus;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocIdNumber() {
		return docIdNumber;
	}

	public void setDocIdNumber(String docIdNumber) {
		this.docIdNumber = docIdNumber;
	}

	public String getFrontIdUrl() {
		return frontIdUrl;
	}

	public void setFrontIdUrl(String frontIdUrl) {
		this.frontIdUrl = frontIdUrl;
	}

	public String getBackIdUrl() {
		return backIdUrl;
	}

	public void setBackIdUrl(String backIdUrl) {
		this.backIdUrl = backIdUrl;
	}

}