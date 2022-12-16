package com.mobiloitte.usermanagement.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobiloitte.usermanagement.enums.UserSecurityStatus;

@Entity
@Table
public class UserSecurityDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userSecurityDetailsId;

	@CreationTimestamp
	private Date createTime;

	private String source;

	@Enumerated(EnumType.STRING)
	private UserSecurityStatus status;

	private String ipAddess;

	private String acitvityMessage;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_USER_ID")
	private User user;

	public Long getUserSecurityDetailsId() {
		return userSecurityDetailsId;
	}

	public void setUserSecurityDetailsId(Long userSecurityDetailsId) {
		this.userSecurityDetailsId = userSecurityDetailsId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public UserSecurityStatus getStatus() {
		return status;
	}

	public void setStatus(UserSecurityStatus status) {
		this.status = status;
	}

	public String getIpAddess() {
		return ipAddess;
	}

	public void setIpAddess(String ipAddess) {
		this.ipAddess = ipAddess;
	}

	public String getAcitvityMessage() {
		return acitvityMessage;
	}

	public void setAcitvityMessage(String acitvityMessage) {
		this.acitvityMessage = acitvityMessage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
