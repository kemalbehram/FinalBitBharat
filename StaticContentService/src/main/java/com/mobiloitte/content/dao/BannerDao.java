package com.mobiloitte.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Banner;
import com.mobiloitte.content.enums.BannerStatus;

public interface BannerDao extends JpaRepository<Banner, Long> {

	Optional<Banner> findByBannerId(Long bannerId);

	List<Banner> findByBannerStatusOrderByCreationTimeDesc(BannerStatus active);

	Long countByBannerStatus(BannerStatus active);

	List<Banner> findByOrderByCreationTimeDesc(Pageable of);

	Optional<Banner> findByImageUrl(String imageUrl);
	
}
