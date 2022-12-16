package com.mobiloitte.content.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiloitte.content.enums.NewsLettarStatus;


@Entity
@Table
public class NewsLettar {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long newsLetterId;

	private String userEmail;
	
	@Enumerated(EnumType.STRING)
	private NewsLettarStatus newsLettarStatus;

	private String title;
	
	private String subject;
	
	@CreationTimestamp
	private Date sentDateTime;

	public Long getNewsLetterId() {
		return newsLetterId;
	}

	public void setNewsLetterId(Long newsLetterId) {
		this.newsLetterId = newsLetterId;
	}

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

	public Date getSentDateTime() {
		return sentDateTime;
	}

	public void setSentDateTime(Date sentDateTime) {
		this.sentDateTime = sentDateTime;
	}

	


	

	
	
	
	
}
