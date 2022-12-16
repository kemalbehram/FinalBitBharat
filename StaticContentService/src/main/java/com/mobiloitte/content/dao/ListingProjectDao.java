package com.mobiloitte.content.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.ListingProjectTeam;

public interface ListingProjectDao extends JpaRepository<ListingProjectTeam, Long> {

}
