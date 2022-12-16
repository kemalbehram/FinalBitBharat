package com.mobiloitte.content.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * The Class ContactUsDto.
 */
public class ContactUsDto {
	
	/** The from email. */
	@NotNull(message = "Email cannot be NULL")
	@Email(message = "should be according to standard EMAIL format")
	private String fromEmail;
	
	@NotEmpty(message = "Name cannot be empty")
	private String name;
	
	/** The description. */
	@NotEmpty(message = "Name cannot be empty")
	private String description;
	
	@NotEmpty(message = "Name cannot be empty")
	private String issue;
	

	/**
	 * Gets the from email.
	 *
	 * @return the from email
	 */
	public String getFromEmail() {
		return fromEmail;
	}

	/**
	 * Sets the from email.
	 *
	 * @param fromEmail the new from email
	 */
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	
}
