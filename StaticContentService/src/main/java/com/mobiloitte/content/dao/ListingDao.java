package com.mobiloitte.content.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Listing;

public interface ListingDao extends JpaRepository<Listing, Long> {

	List<Listing> findByListingId(Long listingId);

}
