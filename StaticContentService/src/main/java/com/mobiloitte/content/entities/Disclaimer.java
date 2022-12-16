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

import com.mobiloitte.content.enums.DisclaimerStatus;

@Entity
@Table
public class Disclaimer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long disclaimerID;

	@Lob
	@Column(length = 20971520)
	private String disclaimer;

	private Date creationDate;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Enumerated(EnumType.STRING)
	private DisclaimerStatus disclaimerStatus;

	public DisclaimerStatus getDisclaimerStatus() {
		return disclaimerStatus;
	}

	public void setDisclaimerStatus(DisclaimerStatus disclaimerStatus) {
		this.disclaimerStatus = disclaimerStatus;
	}

	public Long getDisclaimerID() {
		return disclaimerID;
	}

	public void setDisclaimerID(Long disclaimerID) {
		this.disclaimerID = disclaimerID;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

}
