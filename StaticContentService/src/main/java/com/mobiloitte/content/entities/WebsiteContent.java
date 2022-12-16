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
public class WebsiteContent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long websiteContentId;

	private String pageName;

	@Lob
	@Column(length = 20971520)
	private String description;
	
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getWebsiteContentId() {
		return websiteContentId;
	}

	public void setWebsiteContentId(Long websiteContentId) {
		this.websiteContentId = websiteContentId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
