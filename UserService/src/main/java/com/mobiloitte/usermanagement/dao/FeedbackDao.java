package com.mobiloitte.usermanagement.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.enums.FeedbackStatus;
import com.mobiloitte.usermanagement.model.Feedback;

public interface FeedbackDao extends JpaRepository<Feedback, Long> {

	Long countByFeedbackStatus(FeedbackStatus positive);

	Long countByFeedbackStatusAndUserUserId(FeedbackStatus positive, Long userId);

	List<Feedback> findByUserUserId(Long feedbackUserId);

	List<Feedback> findBySubmittedByOrUserUserId(Long feedbackUserId, Long feedbackUserId2, Pageable of);

	List<Feedback> findBySubmittedByAndFeedbackStatus(Long feedbackUserId, FeedbackStatus feedbackStatus, Pageable of);

	List<Feedback> findByUserUserIdAndFeedbackStatus(Long feedbackUserId, FeedbackStatus feedbackStatus, Pageable of);

}