package com.mobiloitte.content.dto;

import javax.validation.constraints.NotEmpty;

import com.mobiloitte.content.enums.BannerStatus;

public class BannerDto {

	@NotEmpty(message = "Description cant be empty or null")
	private String description;

	private BannerStatus bannerStatus;
	private String url;
	@NotEmpty(message = "Image cant be empty or null")
	private String imageUrl;

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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public BannerStatus getBannerStatus() {
		return bannerStatus;
	}

	public void setBannerStatus(BannerStatus bannerStatus) {
		this.bannerStatus = bannerStatus;
	}

}
