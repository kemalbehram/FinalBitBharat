package com.mobiloitte.content.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mobiloitte.content.enums.TeamStatus;

@Entity
@Table
public class Team {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private Long projectTeamId;
	private String team;
	private String teamName;
	private String imageUrl;
	private String proffessionType;
	private String educationalDetails;
	private String instagram;
	private String twitter;
	private String telegram;
	@Enumerated(EnumType.STRING)
	private TeamStatus teamStatus;
	
	
	public TeamStatus getTeamStatus() {
		return teamStatus;
	}
	public void setTeamStatus(TeamStatus teamStatus) {
		this.teamStatus = teamStatus;
	}
	public Long getProjectTeamId() {
		return projectTeamId;
	}
	public void setProjectTeamId(Long projectTeamId) {
		this.projectTeamId = projectTeamId;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getProffessionType() {
		return proffessionType;
	}
	public void setProffessionType(String proffessionType) {
		this.proffessionType = proffessionType;
	}
	public String getEducationalDetails() {
		return educationalDetails;
	}
	public void setEducationalDetails(String educationalDetails) {
		this.educationalDetails = educationalDetails;
	}
	public String getInstagram() {
		return instagram;
	}
	public void setInstagram(String instagram) {
		this.instagram = instagram;
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
	
	
	

}
