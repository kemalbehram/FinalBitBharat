package com.mobiloitte.content.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiloitte.content.enums.ListingEnum;

@Entity
@Table
public class ListingProjectTeam {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long listingProjectTeamId;

	private String representativeName;

	private String representativeEmail;

	private String representativeContactNumber;

	private String representativeTelegramLink;

	private String companyName;

	private String companyRegisteredAddress;

	@Lob
	@Column(length = 20971520)
	private String projectTeamEmail;

	private String CoinTokenName;

	private String coinImageUrl;

	private String websiteUrl;

	private String contactAddress;

	private String ticker;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Lob
	@Column(length = 20971520)
	private String projetDescription;

	private String tokenSupply;

	private String gitHubRepoName;

	private String twitter;

	private String telegram;

	private String medium;

	private String discord;

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	@Enumerated(EnumType.STRING)
	private ListingEnum listingEnum;

	public Long getListingProjectTeamId() {
		return listingProjectTeamId;
	}

	public void setListingProjectTeamId(Long listingProjectTeamId) {
		this.listingProjectTeamId = listingProjectTeamId;
	}

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

}
