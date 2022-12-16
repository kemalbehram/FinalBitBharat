package com.mobiloitte.usermanagement.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.enums.FeedbackEnum;
import com.mobiloitte.usermanagement.model.FeedbackModel;

public interface Feedbackstaticdao extends JpaRepository<FeedbackModel, Long> {

	List<FeedbackModel> findByUserId(Long userId);

	List<FeedbackModel> findByFeedbackEnum(Pageable of, FeedbackEnum active);

}
