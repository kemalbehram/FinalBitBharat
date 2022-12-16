package com.mobiloitte.content.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiloitte.content.enums.SubCategoryStatus;

@Entity
@Table
public class SubCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Long subTeamId;

	@CreationTimestamp
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private String careerType;

	@Lob
	@Column(length = 20971520)
	private String description;

	@Lob
	@Column(length = 20971520)
	private String requirement;

//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "team_name")
//	private Career career;

	private String subTeamName;

	private String category;
	
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubTeamName() {
		return subTeamName;
	}

	public void setSubTeamName(String subTeamName) {
		this.subTeamName = subTeamName;
	}

//	public Career getCareer() {
//		return career;
//	}
//
//	public void setCareer(Career career) {
//		this.career = career;
//	}

	@Enumerated(EnumType.STRING)
	private SubCategoryStatus subCategoryStatus;

	public SubCategoryStatus getSubCategoryStatus() {
		return subCategoryStatus;
	}

	public void setSubCategoryStatus(SubCategoryStatus subCategoryStatus) {
		this.subCategoryStatus = subCategoryStatus;
	}

	public Long getSubTeamId() {
		return subTeamId;
	}

	public void setSubTeamId(Long subTeamId) {
		this.subTeamId = subTeamId;
	}

	public String getCareerType() {
		return careerType;
	}

	public void setCareerType(String careerType) {
		this.careerType = careerType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

}
