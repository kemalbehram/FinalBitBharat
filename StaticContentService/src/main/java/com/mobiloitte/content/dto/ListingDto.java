package com.mobiloitte.content.dto;

import javax.persistence.Lob;

public class ListingDto {

	private Long listingId;

	private String Email;

	private String tokenName;

	private String coinImage;

	private String whitepaperLink;
	private String ticker;
	@Lob
	private String description;

	private String website;

	public Long getListingId() {
		return listingId;
	}

	public void setListingId(Long listingId) {
		this.listingId = listingId;
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

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

}
