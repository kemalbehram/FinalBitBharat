package com.mobiloitte.content.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.WebsiteContent;

public interface WebsiteContentDao extends JpaRepository<WebsiteContent, Long> {

	Optional<WebsiteContent> findByPageName(String pageName);

	// Optional<WebsiteContent> findByWebsiteContentId(Long websiteContentId);
}
