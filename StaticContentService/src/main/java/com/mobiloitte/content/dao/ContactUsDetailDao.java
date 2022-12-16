package com.mobiloitte.content.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.ContactUs;
//import com.mobiloitte.content.entities.ContactUsDetails;

public interface ContactUsDetailDao extends JpaRepository<ContactUs, Long> {

//	List<ContactUs> OrderByCreationTimeDesc(Pageable of);

	List<ContactUs> findByOrderByCreationTimeDesc(Pageable of);

//	List<ContactUs> findByOrderByCreationTimeDesc(Long fromDate, Long toDate, PageRequest of);

//	List<ContactUs> findByCreationTimeBetween(Long fromDate, Long toDate, Pageable of);

	List<ContactUs> findByCreationTimeBetween(Date date, Date date2, Pageable of);

//	Page<ContactUs> OrderByCreationTimeDesc(PageRequest of);

//	Page<ContactUs> findAll(Pageable of, String string);

}
