package com.mobiloitte.content.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiloitte.content.enums.NewsLettarStatus;

@Entity
public class NewsLetterStatic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long newsLetterStaticId;

	private String image;
	
	@Enumerated(EnumType.STRING)
	private NewsLettarStatus newsLettarStatus;

	private String title;
	
	@Lob
	@Column(length = 20971520)
	private String description;
	
	@CreationTimestamp
	private Date creationDate;

	public Long getNewsLetterStaticId() {
		return newsLetterStaticId;
	}

	public void setNewsLetterStaticId(Long newsLetterStaticId) {
		this.newsLetterStaticId = newsLetterStaticId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
}
