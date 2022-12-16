package com.mobiloitte.content.dto;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import com.mobiloitte.content.enums.NewsLettarStatus;

public class NewsLetterStaticDto {
	
	@Lob
	private String description;
	
	@Enumerated(EnumType.STRING)
	private NewsLettarStatus newsLettarStatus;

	private String title;
	
	private String image;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public NewsLettarStatus getNewsLettarStatus() {
		return newsLettarStatus;
	}

	public void setNewsLettarStatus(NewsLettarStatus newsLettarStatus) {
		this.newsLettarStatus = newsLettarStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	

}
