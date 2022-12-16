package com.mobiloitte.content.dto;

import javax.validation.constraints.NotEmpty;

public class WebContentDto {

	@NotEmpty(message = "Description cant be empty or null")
	private String message;

	@NotEmpty(message = "Image cant be empty or null")
	private String quote;

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
