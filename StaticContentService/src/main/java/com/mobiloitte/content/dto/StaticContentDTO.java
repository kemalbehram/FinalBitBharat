package com.mobiloitte.content.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class StaticContentDTO {

	@ApiModelProperty(value = "Page Name")
	@NotEmpty(message = "Page name can't be blank")
	@Size(min = 3, message = "Please enter at least 3 character")
	@Size(max = 100, message = "Please enter less than 100 character")
	private String pageKey;

	@ApiModelProperty(value = "Page Body Content")
	@NotEmpty(message = "Page Content can't be blank")
	private String pageData;

	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	public String getPageData() {
		return pageData;
	}

	public void setPageData(String pageData) {
		this.pageData = pageData;
	}

}
