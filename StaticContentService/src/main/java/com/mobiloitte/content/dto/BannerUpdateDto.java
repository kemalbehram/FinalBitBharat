package com.mobiloitte.content.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class BannerUpdateDto {

	@Positive(message = "Id cant be negative or zero")
	private Long bannerId;

	@NotEmpty(message = "Description cant be empty or null")
	private String description;
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

	public Long getBannerId() {
		return bannerId;
	}

	public void setBannerId(Long bannerId) {
		this.bannerId = bannerId;
	}

}
