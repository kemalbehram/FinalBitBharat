package com.mobiloitte.content.dto;

import com.mobiloitte.content.enums.DisclaimerStatus;

public class DisclaimerDto {

	private String disclaimer;

	private DisclaimerStatus disclaimerStatus;

	public DisclaimerStatus getDisclaimerStatus() {
		return disclaimerStatus;
	}

	public void setDisclaimerStatus(DisclaimerStatus disclaimerStatus) {
		this.disclaimerStatus = disclaimerStatus;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

}
