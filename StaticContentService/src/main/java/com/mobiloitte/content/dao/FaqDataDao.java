package com.mobiloitte.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.FaqData;

public interface FaqDataDao extends JpaRepository<FaqData, Long> {

	Optional<FaqData> findByFaqDataId(Long faqId);

	List<FaqData> findByTopicKey(String topicKey);

	List<FaqData> findByFkFaqIdOrderByFkFaqIdAsc(Long faqId, Pageable of);

	Optional<FaqData> findByFkFaqId(Long faqId);

	List<FaqData> findByTopicKeyOrderByFaqDataIdAsc(String topicName, Pageable of);

	List<FaqData> findByFkFaqIdOrderByFaqDataIdAsc(Long faqId);

	List<FaqData> findByIsDeletedFalse(Pageable of);

	List<FaqData> findByIsDeletedFalse();

//	List<FaqData> findByQuestion(String question, Pageable pageRequest);

	List<FaqData> findByQuestion(String question, Pageable of);

//	List<FaqData> findAll(String question, Pageable of);

}
