package com.mobiloitte.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Link;
import com.mobiloitte.content.enums.Status;

public interface LinkDao extends JpaRepository<Link, Long> {

//	List<Link> findByContactUsId(Long linkId);

	List<Link> findByLinkId(Long linkId);

	Optional<Link> findByLinkName(String linkName);

	boolean existsByStatus(Status block);

}
