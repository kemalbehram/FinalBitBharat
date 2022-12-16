package com.mobiloitte.usermanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.enums.KycStatus;
import com.mobiloitte.usermanagement.model.KYC;

public interface KycDao extends JpaRepository<KYC, Long> {

	Optional<KYC> findByUserUserId(Long userId);

	Optional<KYC> findByKycId(Long kycId);

	List<KYC> findByKycStatus(KycStatus submitted);

	int countByKycStatus(KycStatus submitted);

	List<KYC> findByUserUserIdAndKycStatusNot(Long userId, KycStatus rejected);

	void deleteByKycId(Long kycId);

	Boolean existsByUserUserIdAndKycStatusNot(Long userId, KycStatus rejected);

	Optional<KYC> findTopByUserUserIdAndKycStatusOrderByKycIdDesc(Long p2pUserId, KycStatus accepted);

	Optional<KYC> findByKycStatusAndUserUserId(KycStatus accepted, Long userId);

}
