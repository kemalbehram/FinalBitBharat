package com.mobiloitte.microservice.wallet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.AdminUpi;

public interface AdminUpidao extends JpaRepository<AdminUpi, Long>{

	Optional<AdminUpi> findByUpiId(String upiId);

	List<AdminUpi> findByIsVisibleTrue();

}
