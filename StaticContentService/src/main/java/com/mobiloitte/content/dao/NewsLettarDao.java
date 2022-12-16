package com.mobiloitte.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.NewsLettar;
import com.mobiloitte.content.enums.NewsLettarStatus;

public interface NewsLettarDao extends JpaRepository<NewsLettar, Long> {

//
//	Optional<NewsLettar> findByEmail(String email);
//
//	Optional<NewsLettar> findByEmailId(Long emailId);
//
//	List<NewsLettar> findByNewsLettarStatus(NewsLettarStatus active);

	List<NewsLettar> findByOrderBySentDateTimeDesc(Pageable of);

	Optional<NewsLettar> findByNewsLetterId(Long newsLetterId);
	
	

}
