package com.mobiloitte.content.entities;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Table
public class Partnership {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long partnershipId;
	@Lob
	@Column(length = 20971520)
	private String text;
	private String logo;
	private String url;
	@Lob
	@Column(length = 20971520)
	private String description;

	private Boolean visible;

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Long getPartnershipId() {
		return partnershipId;
	}

	public void setPartnershipId(Long partnershipId) {
		this.partnershipId = partnershipId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
