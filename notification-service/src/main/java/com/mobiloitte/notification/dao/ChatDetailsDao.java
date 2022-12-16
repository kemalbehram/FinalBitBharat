package com.mobiloitte.notification.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.notification.model.ChatData;

public interface ChatDetailsDao extends JpaRepository<ChatData, Long> {

	List<ChatData> findByFromEmail(String fromEmail);

	List<ChatData> findByFromEmailAndIsSeenFalse(String fromEmail);

	List<ChatData> findByTopicAndIsSeenFalse(String string);

	List<ChatData> findByTopicAndFromEmailAndIsSeenFalse(String string, String fromEmail);

	List<ChatData> findByTopicAndToEmailAndIsSeenFalse(String string, String fromEmail);

	List<ChatData> findByFromEmailOrToEmailAndIsSeenTrue(String username, String username2);

	List<ChatData> findByFromEmailInAndIsSeenTrue(List<String> usernames);

	List<ChatData> findByFromEmailInOrToEmailInAndIsSeenTrue(List<String> usernames, List<String> toUsernames);

	List<ChatData> findByFromEmailAndToEmailAndIsSeenTrue(String username, String toUsername);

	List<ChatData> findByFromEmailInAndToEmailInAndIsSeenTrueOrderByMessageIdAsc(List<String> usernames,
			List<String> usernames2);

	List<ChatData> findByFromEmailInAndToEmailInAndIsSeenTrueOrderByMessageIdDesc(List<String> usernames,
			List<String> usernames2);

}
