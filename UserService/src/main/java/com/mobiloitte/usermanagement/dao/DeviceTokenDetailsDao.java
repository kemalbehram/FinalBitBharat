package com.mobiloitte.usermanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.DeviceTokenDetails;

public interface DeviceTokenDetailsDao extends JpaRepository<DeviceTokenDetails, Long> {

	List<DeviceTokenDetails> findByUserUserId(Long userId);

	Optional<DeviceTokenDetails> findByUserUserIdAndDeviceToken(Long userId, String deviceToken);
}
