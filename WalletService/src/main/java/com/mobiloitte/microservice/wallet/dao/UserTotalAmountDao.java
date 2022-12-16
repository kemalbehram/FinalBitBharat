package com.mobiloitte.microservice.wallet.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.UserTotalAmount;


public interface UserTotalAmountDao extends JpaRepository<UserTotalAmount, Long> {

	Optional<UserTotalAmount> findByUserId(Long userId1);



}
