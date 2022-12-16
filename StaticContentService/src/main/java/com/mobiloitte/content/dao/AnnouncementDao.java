package com.mobiloitte.content.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Announcement;
import com.mobiloitte.content.enums.AnnouncementStatus;

public interface AnnouncementDao extends JpaRepository<Announcement, Long> {

	Optional<Announcement> findByTitle(String title);

	List<Announcement> findByAnnouncementStatus(AnnouncementStatus active);

	Long countByAnnouncementStatus(AnnouncementStatus active);

	List<Announcement> findByOrderByCreationDateDesc(Pageable of);

//	List<Announcement> findByAnnouncementStatusOrderByCreationDateDesc(Pageable of, AnnouncementStatus active);

	List<Announcement> findByAnnouncementStatusOrderByCreationDateDesc(AnnouncementStatus active, Pageable of);

	List<Announcement> findByTitleAndAnnouncementStatusOrderByCreationDateDesc(String title, AnnouncementStatus active,
			Pageable of);

	List<Announcement> findByTitleContainsAndAnnouncementStatusOrderByCreationDateDesc(String title,
			AnnouncementStatus active, Pageable of);

	List<Announcement> findByAnnouncementStatusAndCreationDateBetweenOrderByCreationDateDesc(AnnouncementStatus active,
			Date date, Date date2, Pageable of);

	List<Announcement> findByAnnouncementStatusAndCreationDateBetweenOrderByCreationDateDesc(AnnouncementStatus active,
			Date date, Pageable of);

	List<Announcement> findByAnnouncementStatusAndCreationDateOrderByCreationDateDesc(AnnouncementStatus active,
			Date date, Pageable of);

//	List<Announcement> findByContentContainsAndAnnouncementStatusOrderByCreationDateDesc(String title,
//			AnnouncementStatus active, PageRequest of);

//	List<Announcement> findByAnnouncementStatusOrderByCreationDateDesc(AnnouncementStatus active, PageRequest pageRequest);
//
////	List<Announcement> findByAnnouncementStatus(Pageable of, AnnouncementStatus active);
//
//	List<Announcement> findByAnnouncementStatusOrderByCreationDateDesc(Pageable of, AnnouncementStatus active);

//	List<Announcement> findByOrderByCreationDateDesc(Pageable of);

}