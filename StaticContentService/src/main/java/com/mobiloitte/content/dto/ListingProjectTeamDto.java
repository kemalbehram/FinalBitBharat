package com.mobiloitte.content.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiloitte.content.enums.ListingEnum;

public class ListingProjectTeamDto {

	private String representativeName;

	private String representativeEmail;

	private String representativeContactNumber;

	private String representativeTelegramLink;

	private String companyName;

	private String companyRegisteredAddress;

	private String projectTeamEmail;

	private String CoinTokenName;

	private String coinImageUrl;

	private String websiteUrl;
	private String ticker;
	private String contactAddress;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String projetDescription;

	private String tokenSupply;

	private String gitHubRepoName;

	private String twitter;

	private String telegram;

	private String medium;

	private String discord;

	private ListingEnum listingEnum;

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getRepresentativeEmail() {
		return representativeEmail;
	}

	public void setRepresentativeEmail(String representativeEmail) {
		this.representativeEmail = representativeEmail;
	}

	public String getRepresentativeContactNumber() {
		return representativeContactNumber;
	}

	public void setRepresentativeContactNumber(String representativeContactNumber) {
		this.representativeContactNumber = representativeContactNumber;
	}

	public String getRepresentativeTelegramLink() {
		return representativeTelegramLink;
	}

	public void setRepresentativeTelegramLink(String representativeTelegramLink) {
		this.representativeTelegramLink = representativeTelegramLink;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyRegisteredAddress() {
		return companyRegisteredAddress;
	}

	public void setCompanyRegisteredAddress(String companyRegisteredAddress) {
		this.companyRegisteredAddress = companyRegisteredAddress;
	}

	public String getProjectTeamEmail() {
		return projectTeamEmail;
	}

	public void setProjectTeamEmail(String projectTeamEmail) {
		this.projectTeamEmail = projectTeamEmail;
	}

	public String getCoinTokenName() {
		return CoinTokenName;
	}

	public void setCoinTokenName(String coinTokenName) {
		CoinTokenName = coinTokenName;
	}

	public String getCoinImageUrl() {
		return coinImageUrl;
	}

	public void setCoinImageUrl(String coinImageUrl) {
		this.coinImageUrl = coinImageUrl;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getProjetDescription() {
		return projetDescription;
	}

	public void setProjetDescription(String projetDescription) {
		this.projetDescription = projetDescription;
	}

	public String getTokenSupply() {
		return tokenSupply;
	}

	public void setTokenSupply(String tokenSupply) {
		this.tokenSupply = tokenSupply;
	}

	public String getGitHubRepoName() {
		return gitHubRepoName;
	}

	public void setGitHubRepoName(String gitHubRepoName) {
		this.gitHubRepoName = gitHubRepoName;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getTelegram() {
		return telegram;
	}

	public void setTelegram(String telegram) {
		this.telegram = telegram;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getDiscord() {
		return discord;
	}

	public void setDiscord(String discord) {
		this.discord = discord;
	}

	public ListingEnum getListingEnum() {
		return listingEnum;
	}

	public void setListingEnum(ListingEnum listingEnum) {
		this.listingEnum = listingEnum;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

}
