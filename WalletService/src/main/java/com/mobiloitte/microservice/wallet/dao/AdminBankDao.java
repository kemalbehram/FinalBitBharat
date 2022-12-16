package com.mobiloitte.microservice.wallet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.AdminBank;

public interface AdminBankDao extends JpaRepository<AdminBank, Long> {

	List<AdminBank> findByIsVisibleTrue();

	Optional<AdminBank> findById(Long Id);

	Optional<AdminBank> findByBankId(Long bankId);

}
