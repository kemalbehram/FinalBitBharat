package com.mobiloitte.content.dto;

import javax.validation.constraints.NotEmpty;

public class WebsiteContentDto {

	@NotEmpty(message = "PageName can't be empty or null")
	private String pageName;

	@NotEmpty(message = "Description can't be empty or null")
	private String description;
	
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
