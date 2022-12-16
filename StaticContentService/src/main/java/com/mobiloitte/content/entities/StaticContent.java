package com.mobiloitte.content.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * The Class StaticContent.
 * @author Ankush Mohapatra
 */
@Entity
@Table
public class StaticContent {
	
	/** The static content id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long staticContentId;
	
	/** The page key. */
	private String pageKey;
	
	/** The page data. */
	@Lob
	@Column(length = 20971520)
	private String pageData;
	
	/** The is deleted. */
	private Boolean isDeleted;

	/**
	 * Gets the static content id.
	 *
	 * @return the static content id
	 */
	public Long getStaticContentId() {
		return staticContentId;
	}

	/**
	 * Sets the static content id.
	 *
	 * @param staticContentId the new static content id
	 */
	public void setStaticContentId(Long staticContentId) {
		this.staticContentId = staticContentId;
	}

	/**
	 * Gets the page key.
	 *
	 * @return the page key
	 */
	public String getPageKey() {
		return pageKey;
	}

	/**
	 * Sets the page key.
	 *
	 * @param pageKey the new page key
	 */
	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
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

	/**
	 * Gets the checks if is deleted.
	 *
	 * @return the checks if is deleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the checks if is deleted.
	 *
	 * @param isDeleted the new checks if is deleted
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
