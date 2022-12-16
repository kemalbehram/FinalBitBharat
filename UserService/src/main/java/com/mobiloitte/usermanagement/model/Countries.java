package com.mobiloitte.usermanagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Countries {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long countriesId;

	private String countryCode;

	private String country;

	public Long getCountriesId() {
		return countriesId;
	}

	public void setCountriesId(Long countriesId) {
		this.countriesId = countriesId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
