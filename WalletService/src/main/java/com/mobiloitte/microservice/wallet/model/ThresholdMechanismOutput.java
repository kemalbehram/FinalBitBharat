package com.mobiloitte.microservice.wallet.model;

public class ThresholdMechanismOutput {

	private Boolean isApproved;
	
	private Boolean showMessage;
	
	private String keyMessage;

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getKeyMessage() {
		return keyMessage;
	}

	public void setKeyMessage(String keyMessage) {
		this.keyMessage = keyMessage;
	}

	public ThresholdMechanismOutput(Boolean isApproved, String keyMessage, Boolean showMessage) {
		super();
		this.isApproved = isApproved;
		this.keyMessage = keyMessage;
		this.showMessage = showMessage;
	}

	public Boolean getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(Boolean showMessage) {
		this.showMessage = showMessage;
	}
	
	
}
