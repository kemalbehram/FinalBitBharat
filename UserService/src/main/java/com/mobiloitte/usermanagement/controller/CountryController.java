package com.mobiloitte.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.model.Countries;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.CountryService;

@RestController
public class CountryController {

	@Autowired
	private CountryService countryService;

	@GetMapping("/save-country-list")
	public Response<Countries> getCountryList() {
		return countryService.getCountryList();
	}

	@GetMapping("/get-country-list")
	public Response<Object> getCountryListOne() {
		return countryService.getCountryListOne();
	}
	
	@GetMapping("/get-state-country-wise")
	public Response<Object> getStateCountryWise(@RequestParam String countryCode) {
		return countryService.getStateCountryWise(countryCode);
	}
}
