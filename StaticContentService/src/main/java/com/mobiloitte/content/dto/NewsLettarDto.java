package com.mobiloitte.content.dto;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.mobiloitte.content.enums.NewsLettarStatus;

public class NewsLettarDto {
	
    private String userEmail;
	
	@Enumerated(EnumType.STRING)
	private NewsLettarStatus newsLettarStatus;

	private String title;
	
	private String subject;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	

}
