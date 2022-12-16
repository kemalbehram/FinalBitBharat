package com.mobiloitte.notification.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.notification.model.NotificationData;

public interface NotificationDataDao extends JpaRepository<NotificationData, Long> {

	List<NotificationData> findByFkUserId(Long b);

	List<NotificationData> findByRoleId(Long roleId);

	List<NotificationData> findByFkUserIdAndIsSeen(Long userId, boolean b);

	List<NotificationData> findByFkUserIdAndIsSeenFalse(Long userId);

	List<NotificationData> findByRoleIdAndFkUserId(Long roleId, Long userId);

	Boolean existsByFkUserIdAndMessage(Long fkUserId, String message);

	Boolean existsByFkUserIdAndMessageAndCreatedAt(Long fkUserId, String message, Date createdAt);

	void deleteByFkUserId(Long userId);

	List<NotificationData> findByRoleIdAndFkUserIdAndIsSeenFalse(Long roleId, Long userId);

	List<NotificationData> findAllByFkUserId(Long userId);

//	List<NotificationData> findByRoleIdAndFkUserIdAndOrderByCreatedAt(Long data, Long userId);

	List<NotificationData> findByRoleIdAndFkUserIdOrderByCreatedAt(Long data, Long userId);

	List<NotificationData> findByRoleIdAndFkUserIdOrderByCreatedAtDesc(Long data, Long userId);


}
