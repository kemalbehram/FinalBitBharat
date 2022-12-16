package com.mobiloitte.content.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table
public class Xindia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long xindiaId;

	private String coinImage;

	private String coinName;
	@Lob
	@Column(length = 20971520)
	private String title;
	@Lob
	@Column(length = 20971520)
	private String title2;

	@Lob
	@Column(length = 20971520)
	private String about;
	@Lob
	@Column(length = 20971520)
	private String buy;
	@Lob
	@Column(length = 20971520)
	private String footer;

	public Long getXindiaId() {
		return xindiaId;
	}

	public void setXindiaId(Long xindiaId) {
		this.xindiaId = xindiaId;
	}

	public String getCoinImage() {
		return coinImage;
	}

	public void setCoinImage(String coinImage) {
		this.coinImage = coinImage;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getBuy() {
		return buy;
	}

	public void setBuy(String buy) {
		this.buy = buy;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

}
