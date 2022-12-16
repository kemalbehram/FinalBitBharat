package com.mobiloitte.notification.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.notification.model.NotificationTemplateContent;

public interface NotificationTemplateContentDao extends JpaRepository<NotificationTemplateContent, Long> {

	List<NotificationTemplateContent> findByActivityType(String key);

}
