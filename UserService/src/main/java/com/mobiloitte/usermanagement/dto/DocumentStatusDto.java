package com.mobiloitte.usermanagement.dto;

import com.mobiloitte.usermanagement.enums.DocumentStatus;

public class DocumentStatusDto {

	private long kycId;
	private long documentId;
	private DocumentStatus status;
	private String reason;

	public long getKycId() {
		return kycId;
	}

	public void setKycId(long kycId) {
		this.kycId = kycId;
	}

	public long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	public DocumentStatus getStatus() {
		return status;
	}

	public void setStatus(DocumentStatus status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
