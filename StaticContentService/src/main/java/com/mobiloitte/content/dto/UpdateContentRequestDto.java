package com.mobiloitte.content.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import io.swagger.annotations.ApiModelProperty;

/**
 * The Class UpdateContentRequestDto.
 * @author Ankush Mohapatra
 */
public class UpdateContentRequestDto {
	
	/** The content id. */
	@Positive(message="contentId cannot be negative or 0, must be greater than 0")
	private Long contentId;
	
	/** The page data. */
	@ApiModelProperty(value = "Page Body Content")
	@NotEmpty(message = "Page Content can't be blank")
	private String pageData;

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

	/**
	 * Gets the page data.
	 *
	 * @return the page data
	 */
	public String getPageData() {
		return pageData;
	}

	/**
	 * Sets the page data.
	 *
	 * @param pageData the new page data
	 */
	public void setPageData(String pageData) {
		this.pageData = pageData;
	}

}
