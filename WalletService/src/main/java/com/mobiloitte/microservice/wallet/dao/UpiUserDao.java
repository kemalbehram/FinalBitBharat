package com.mobiloitte.microservice.wallet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.UpiUser;

public interface UpiUserDao extends JpaRepository<UpiUser, Long> {

	List<UpiUser> findByUserId(Long userId);

	Optional<UpiUser> findByUpiIdAndUserId(String upiId, Long userId);

	List<UpiUser> findByNameOrUpiId(Pageable of, String name, String upiId);

	Optional<UpiUser> findByUserUpiIdAndUserId(Long upiId, Long userId);

}
