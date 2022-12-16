package com.mobiloitte.usermanagement.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
public class BlockedP2pUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long blockedId;

	private Boolean isBlocked;

	private Long blockedUserId;

	private Long blockedBy;

	@CreationTimestamp
	private Date creationTime;

	@UpdateTimestamp
	private Date updationTime;

	public Long getBlockedId() {
		return blockedId;
	}

	public void setBlockedId(Long blockedId) {
		this.blockedId = blockedId;
	}

	public Boolean getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public Long getBlockedUserId() {
		return blockedUserId;
	}

	public void setBlockedUserId(Long blockedUserId) {
		this.blockedUserId = blockedUserId;
	}

	public Long getBlockedBy() {
		return blockedBy;
	}

	public void setBlockedBy(Long blockedBy) {
		this.blockedBy = blockedBy;
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

}