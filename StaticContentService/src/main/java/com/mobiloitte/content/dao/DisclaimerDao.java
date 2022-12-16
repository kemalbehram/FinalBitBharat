package com.mobiloitte.content.dao;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Announcement;
import com.mobiloitte.content.entities.Disclaimer;

public interface DisclaimerDao extends JpaRepository<Disclaimer, Long> {

	List<Announcement> findByOrderByCreationDateDesc(PageRequest of);

}
