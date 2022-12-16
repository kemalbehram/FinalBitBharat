package com.mobiloitte.usermanagement.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiloitte.usermanagement.dao.CountriesDao;
import com.mobiloitte.usermanagement.dao.RoleDao;
import com.mobiloitte.usermanagement.dao.StateDao;
import com.mobiloitte.usermanagement.dao.UserDao;
import com.mobiloitte.usermanagement.dto.Country;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.feign.WalletClient;
import com.mobiloitte.usermanagement.model.Countries;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.Role;
import com.mobiloitte.usermanagement.model.StateList;
import com.mobiloitte.usermanagement.model.States;
import com.mobiloitte.usermanagement.model.User;

@Configuration
@EnableScheduling
public class InitializeUserService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private StateDao stateDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private WalletClient walletClient;

	@Bean
	public InitializingBean initializeRoleTable() {
		return () -> {
			if (roleDao.findAll().isEmpty()) {
				RoleStatus[] roleStatus = RoleStatus.values();
				List<Role> roleList = new ArrayList<>();
				for (int i = 0; i < roleStatus.length; i++) {
					Role role = new Role();
					role.setRole(roleStatus[i]);
					roleList.add(role);
				}
				roleDao.saveAll(roleList);
			}
		};
	}

	@Bean
	public InitializingBean initializeStateTable(CountriesDao countriesDao) {
		return () -> {
			if (countriesDao.findAll().isEmpty()) {
				String[] locales = Locale.getISOCountries();
				for (String countryCode : locales) {
					Countries countries = new Countries();
					Locale obj = new Locale("", countryCode);
					countries.setCountry(obj.getDisplayCountry());
					countries.setCountryCode(obj.getCountry());
					countriesDao.save(countries);
				}
			}
			if (stateDao.findAll().isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				TypeReference<StateList> mapType = new TypeReference<StateList>() {
				};
				InputStream is = TypeReference.class.getResourceAsStream("/json/state-list.json");
				StateList stateList = mapper.readValue(is, mapType);
				List<Countries> countryList = countriesDao.findAll();
				if (!stateList.getCountries().isEmpty()) {
					for (Countries countries : countryList) {
						saveState(stateList, countries);
					}
				}
			}
		};
	}

	public void saveState(StateList stateList, Countries countries) {
		for (Country country : stateList.getCountries()) {
			if (country.getCountry().equalsIgnoreCase(countries.getCountry()) && !country.getStates().isEmpty()) {
				for (String stateName : country.getStates()) {
					States state = new States();
					Countries countriess = new Countries();
					countriess.setCountriesId(countries.getCountriesId());
					state.setStateName(stateName);
					state.setCountries(countriess);
					stateDao.save(state);
				}
			}
		}
	}

}
