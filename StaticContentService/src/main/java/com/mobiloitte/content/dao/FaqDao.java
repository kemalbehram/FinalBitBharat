package com.mobiloitte.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.FAQ;

public interface FaqDao extends JpaRepository<FAQ, Long> {

	Optional<FAQ> findByTopicKey(String topicName);


	List<FAQ> findByIsDeletedFalse();


	List<FAQ> findByIsDeletedFalse(PageRequest of);


}
