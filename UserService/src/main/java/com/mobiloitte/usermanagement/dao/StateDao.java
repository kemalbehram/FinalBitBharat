package com.mobiloitte.usermanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.States;

public interface StateDao extends JpaRepository<States, Long> {

	List<States> findByCountriesCountriesId(Long countryId);

	List<States> findByCountriesCountriesIdAndStatusTrue(Long castCountryId);

	List<States> findByCountriesCountryCode(String countryCode);

}
