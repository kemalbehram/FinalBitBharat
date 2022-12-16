package com.mobiloitte.usermanagement.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.SmsDetails;

public interface SmsDetailsDao extends JpaRepository<SmsDetails, Long>{

	Optional<SmsDetails> findByUserId(Long userId);
	
	Boolean existsByOtp(Integer otp);

	Optional<SmsDetails> findByPhoneNo(String mobileNo);

	Optional<SmsDetails> findByOtp(Integer otp);
}
