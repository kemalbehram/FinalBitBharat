package com.mobiloitte.usermanagement.emailservice;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Arvind Kumar
 *
 */
public class Mail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String smtpHost;

	private String[] toAddresses;
	private String fromAddress;
	private String mailServerSecureFlag;
	private String mailServerPort;
	private String fromPassword;
	private String[] ccAddresses;
	private String[] bccAddresses;
	private String subject;
	private String bodyText;
	private String mailFormat;
	private String signature;
	private String attachmentpath;
	private Date sentTime;

	public Mail() {
	}

	public Mail(MailConfigModel mailConfigVO, String fromAddress, String[] toAddress, String[] ccAddresses,
			String[] bccAddresses, String bodyText, String signature, String attachment, Date sentTime,
			String subject) {
		this.toAddresses = toAddress;
		this.fromAddress = fromAddress;
		this.fromPassword = mailConfigVO.getPassword();
		this.ccAddresses = ccAddresses;
		this.bccAddresses = bccAddresses;
		this.subject = subject;
		this.bodyText = bodyText;
		this.mailFormat = mailConfigVO.getContentType();
		this.signature = signature;
		this.attachmentpath = attachment;
		this.sentTime = sentTime;
		this.smtpHost = mailConfigVO.getHost();
		this.mailServerPort = mailConfigVO.getPort();
		this.mailServerSecureFlag = mailConfigVO.getChannel();
	}

	public String[] getToAddress() {
		return toAddresses;
	}

	public void setToAddress(String[] toAddress) {
		this.toAddresses = toAddress;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String[] getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses(String[] ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public String[] getBccAddresses() {
		return bccAddresses;
	}

	public void setBccAddresses(String[] bccAddresses) {
		this.bccAddresses = bccAddresses;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBodyText() {
		return bodyText;
	}

	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}

	public String getMailFormat() {
		return mailFormat;
	}

	public void setMailFormat(String mailFormat) {
		this.mailFormat = mailFormat;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAttachmentpath() {
		return attachmentpath;
	}

	public void setAttachmentpath(String attachmentpath) {
		this.attachmentpath = attachmentpath;
	}

	public String getFromPassword() {
		return fromPassword;
	}

	public void setFromPassword(String fromPassword) {
		this.fromPassword = fromPassword;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getMailServerSecureFlag() {
		return mailServerSecureFlag;
	}

	public void setMailServerSecureFlag(String mailServerSecureFlag) {
		this.mailServerSecureFlag = mailServerSecureFlag;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}
}