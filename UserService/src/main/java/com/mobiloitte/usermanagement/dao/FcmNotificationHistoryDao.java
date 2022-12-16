package com.mobiloitte.usermanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.FcmNotificationHistory;

public interface FcmNotificationHistoryDao extends JpaRepository<FcmNotificationHistory, Long> {

	Long countByStatus(String status);

	List<FcmNotificationHistory> findByUserNameAndUserEmail(String userName, String userEmail);

	List<FcmNotificationHistory> findByUserName(String userName);

	List<FcmNotificationHistory> findByUserEmail(String userEmail);

	List<FcmNotificationHistory> findByUserNameOrUserEmailAndStatus(String userName, String userEmail, String status);

	List<FcmNotificationHistory> findByStatusAndUserNameContainingOrStatusAndUserEmailContaining(String status,
			String search, String status2, String search2);

}
