package com.mobiloitte.content.dto;

import java.util.Date;

import com.mobiloitte.content.enums.TicketStatus;

public class TicketDetailDto {

	private Long ticketId;

	private String name;

	private String email;

	private String subject;

	private String description;

	private String imnageUrl;

	private Date createdAt;

	private TicketStatus ticketStatus;

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImnageUrl() {
		return imnageUrl;
	}

	public void setImnageUrl(String imnageUrl) {
		this.imnageUrl = imnageUrl;
	}

	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(TicketStatus ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
