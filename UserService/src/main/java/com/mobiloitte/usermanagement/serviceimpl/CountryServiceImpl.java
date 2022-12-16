package com.mobiloitte.usermanagement.serviceimpl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.mobiloitte.usermanagement.dao.CountriesDao;
import com.mobiloitte.usermanagement.dao.StateDao;
import com.mobiloitte.usermanagement.model.Countries;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.States;
import com.mobiloitte.usermanagement.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountriesDao countryDao;

	@Autowired
	private StateDao stateDao;

	@Autowired
	private MessageSource messageSource;

	@Override
	public Response<Countries> getCountryList() {
		String[] locales = Locale.getISOCountries();

		for (String countryCode : locales) {
			Countries country = new Countries();
			Locale obj = new Locale("", countryCode);
			country.setCountry(obj.getDisplayCountry());
			country.setCountryCode(obj.getCountry());
			countryDao.save(country);

		}
		return new Response<Countries>(200, "country save");
	}

	@Override
	public Response<Object> getCountryListOne() {
		List<Countries> list = countryDao.findAllByOrderByCountryAsc();
		if (list.isEmpty())
			return new Response<>(201, "Country list not found");
		else
			return new Response<>(200, "Country list found", list);
	}

	@Override
	public Response<Object> getStateCountryWise(String countryCode) {
		List<States> list = stateDao.findByCountriesCountryCode(countryCode);
		if (!list.isEmpty()) {
			return new Response<>(200, messageSource.getMessage(
					"usermanagement.get.state.list.country.wise.successfully", new Object[0], Locale.US), list);
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US));
		}
	}
}
