package com.mobiloitte.usermanagement.dto;

import java.util.List;

public class Country {

	private String country;

	private List<String> states;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<String> getStates() {
		return states;
	}

	public void setStates(List<String> states) {
		this.states = states;
	}

}
