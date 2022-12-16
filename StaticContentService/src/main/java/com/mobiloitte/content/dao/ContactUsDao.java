package com.mobiloitte.content.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.ContactUs;

public interface ContactUsDao extends JpaRepository<ContactUs, Long>{

	List<ContactUs> findByContactUsId(Long contactUsId);

}
