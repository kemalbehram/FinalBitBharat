package com.mobiloitte.usermanagement.service;

import com.mobiloitte.usermanagement.model.Countries;
import com.mobiloitte.usermanagement.model.Response;

public interface CountryService {

	Response<Object> getCountryListOne();

	Response<Countries> getCountryList();

	Response<Object> getStateCountryWise(String country);

}
