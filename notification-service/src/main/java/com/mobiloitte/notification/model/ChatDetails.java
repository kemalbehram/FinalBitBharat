package com.mobiloitte.notification.model;

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
import org.hibernate.annotations.UpdateTimestamp;

import com.mobiloitte.notification.enums.MessageFormat;
import com.mobiloitte.notification.enums.TradeStatus;

@Entity
@Table
public class ChatDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;
	private Long toUserId;
	private Long fromUserId;
	@Lob
	@Column( length = 20971520)
	private String message;
	@Enumerated(EnumType.STRING)
	private MessageFormat messageFormat;
	private String fileUrl;
	private Boolean isSeen;
	private Long roleId;
	private String role;
	private String tradeId;
	@Enumerated(EnumType.STRING)
	private TradeStatus tradeStatus;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updationTime;
	private String sender;
	private String reciever;
	private String profileImageOfBuyer;
	private String profileImageOfSeller;
	private int timerForEvidence;
	private int timeLeft;
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public Long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageFormat getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(MessageFormat messageFormat) {
		this.messageFormat = messageFormat;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Boolean getIsSeen() {
		return isSeen;
	}

	public void setIsSeen(Boolean isSeen) {
		this.isSeen = isSeen;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public TradeStatus getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUpdationTime() {
		return updationTime;
	}

	public void setUpdationTime(Date updationTime) {
		this.updationTime = updationTime;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public String getProfileImageOfBuyer() {
		return profileImageOfBuyer;
	}

	public void setProfileImageOfBuyer(String profileImageOfBuyer) {
		this.profileImageOfBuyer = profileImageOfBuyer;
	}

	public String getProfileImageOfSeller() {
		return profileImageOfSeller;
	}

	public void setProfileImageOfSeller(String profileImageOfSeller) {
		this.profileImageOfSeller = profileImageOfSeller;
	}

	public int getTimerForEvidence() {
		return timerForEvidence;
	}

	public void setTimerForEvidence(int timerForEvidence) {
		this.timerForEvidence = timerForEvidence;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

}
