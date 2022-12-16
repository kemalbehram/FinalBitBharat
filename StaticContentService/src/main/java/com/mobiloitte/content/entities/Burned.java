package com.mobiloitte.content.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Burned {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long burnedId;
	private String pendindBurned;
	private String burned;
	private String circulatingSupply;

	public Long getBurnedId() {
		return burnedId;
	}

	public void setBurnedId(Long burnedId) {
		this.burnedId = burnedId;
	}

	public String getPendindBurned() {
		return pendindBurned;
	}

	public void setPendindBurned(String pendindBurned) {
		this.pendindBurned = pendindBurned;
	}

	public String getBurned() {
		return burned;
	}

	public void setBurned(String burned) {
		this.burned = burned;
	}

	public String getCirculatingSupply() {
		return circulatingSupply;
	}

	public void setCirculatingSupply(String circulatingSupply) {
		this.circulatingSupply = circulatingSupply;
	}

}
