package com.mobiloitte.content.dto;

import javax.validation.constraints.Positive;

/**
 * The Class DeleteContentRequestDto.
 * @author Ankush Mohapatra
 */
public class DeleteContentRequestDto {

	/** The content id. */
	@Positive(message="contentId cannot be negative or 0, must be greater than 0")
	private Long contentId;

	/**
	 * Gets the content id.
	 *
	 * @return the content id
	 */
	public Long getContentId() {
		return contentId;
	}

	/**
	 * Sets the content id.
	 *
	 * @param contentId the new content id
	 */
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	
	
}
