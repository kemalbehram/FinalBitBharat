package com.mobiloitte.notification.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.notification.model.P2pExchangeNotification;

public interface P2pExchangeNotificationDao extends JpaRepository<P2pExchangeNotification, Long> {

	List<P2pExchangeNotification> findByToUserId(Long userId);

}
