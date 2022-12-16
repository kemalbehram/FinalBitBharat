package com.mobiloitte.usermanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.Countries;

public interface CountriesDao extends JpaRepository<Countries, Long> {

	List<Countries> findAllByOrderByCountryAsc();

}