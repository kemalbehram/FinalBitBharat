package com.mobiloitte.content.dto;

import com.mobiloitte.content.enums.Status;

public class LinkUpdateDto {

	private String link;

	private String linkName;

	private Status status;

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
