package com.mobiloitte.content.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class WebContentUpdateDto {

	@Positive(message = "Id cant be negative or zero")
	private Long webContentId;
	
	@NotEmpty(message = "Description cant be empty or null")
	private String message;

	@NotEmpty(message = "Image cant be empty or null")
	private String quote;

	public Long getWebContentId() {
		return webContentId;
	}

	public void setWebContentId(Long webContentId) {
		this.webContentId = webContentId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}
	
	
}
