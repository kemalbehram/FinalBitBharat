package com.mobiloitte.microservice.wallet.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.LimitData;

public interface LimitDataDao extends JpaRepository<LimitData, Long>{

	Optional<LimitData> findByLimitName(String limitName);
}
