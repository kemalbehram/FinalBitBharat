package com.mobiloitte.usermanagement.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.PhoneSms;

public interface PhoneSmaDao extends JpaRepository<PhoneSms, Long> {

	Optional<PhoneSms> findByPhoneNo(String phoneNo);

	Optional<PhoneSms> findByOtp(Integer otp);

}
