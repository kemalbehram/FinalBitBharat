package com.mobiloitte.content.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

/**
 * The Class Listing.
 * 
 * @author Priyank Mishra
 */

@Entity
@Table
public class Listing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long listingId;

	private String Email;

	private String tokenName;

	private String ticker;

	@CreationTimestamp
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private String coinImage;

	private String whitepaperLink;

	@Lob
	@Column(length = 20971520)
	private String description;

	private String website;

	public Long getListingId() {
		return listingId;
	}

	public void setListingId(Long listingId) {
		this.listingId = listingId;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public String getCoinImage() {
		return coinImage;
	}

	public void setCoinImage(String coinImage) {
		this.coinImage = coinImage;
	}

	public String getWhitepaperLink() {
		return whitepaperLink;
	}

	public void setWhitepaperLink(String whitepaperLink) {
		this.whitepaperLink = whitepaperLink;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

}
