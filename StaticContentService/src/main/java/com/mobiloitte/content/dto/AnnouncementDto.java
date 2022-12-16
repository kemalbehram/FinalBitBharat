package com.mobiloitte.content.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import com.mobiloitte.content.enums.AnnouncementStatus;

public class AnnouncementDto {

	private String title;

	@Lob
	private String description;

	@Enumerated(EnumType.STRING)
	private AnnouncementStatus announcementStatus;

	private String image;

	public final String getImage() {
		return image;
	}

	public final void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AnnouncementStatus getAnnouncementStatus() {
		return announcementStatus;
	}

	public void setAnnouncementStatus(AnnouncementStatus announcementStatus) {
		this.announcementStatus = announcementStatus;
	}

}
