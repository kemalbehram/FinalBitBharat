package com.mobiloitte.content.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.XinidiaHoldingList;

public interface HoldingListDao extends JpaRepository<XinidiaHoldingList, Long> {

	Optional<XinidiaHoldingList> findByHoldingId(Long holdingxindiaId);

}
