package com.mobiloitte.microservice.wallet.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.DepositInrAmount;

public interface AmountDao extends JpaRepository<DepositInrAmount, Long> {

	Optional<DepositInrAmount> findByUserId(Long userId);

}
