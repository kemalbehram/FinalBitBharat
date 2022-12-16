package com.mobiloitte.content.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table
public class SubCategoryFormNew {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long subCategoryFormId;

	private String resume;

	private String fullName;

	private String email;

	private String mobileNumber;

	private String currentCompany;

	private String resumeUrl;

	@Lob
	@Column(length = 20971520)
	private String additionalInformation;

	public Long getSubCategoryFormId() {
		return subCategoryFormId;
	}

	public void setSubCategoryFormId(Long subCategoryFormId) {
		this.subCategoryFormId = subCategoryFormId;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCurrentCompany() {
		return currentCompany;
	}

	public void setCurrentCompany(String currentCompany) {
		this.currentCompany = currentCompany;
	}

	public String getResumeUrl() {
		return resumeUrl;
	}

	public void setResumeUrl(String resumeUrl) {
		this.resumeUrl = resumeUrl;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

}