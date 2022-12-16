package com.mobiloitte.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.NewsLetterStatic;
import com.mobiloitte.content.enums.NewsLettarStatus;

public interface NewsLetterStaticDao extends JpaRepository<NewsLetterStatic, Long> {

	Optional<NewsLetterStatic> findByTitle(String title);


	Optional<NewsLetterStatic> findByTitleAndNewsLettarStatus(String title, Object a);

	List<NewsLetterStatic> findByOrderByCreationDateDesc(Pageable of);


	List<NewsLetterStatic> findByNewsLettarStatus(NewsLettarStatus active);


	Long countByNewsLettarStatus(NewsLettarStatus active);




	List<NewsLetterStatic> findByNewsLettarStatusOrderByCreationDateDesc(NewsLettarStatus active);

}
