package com.mobiloitte.usermanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.ReferalComission;

public interface ReferalComissionDao extends JpaRepository<ReferalComission, Long> {

	List<ReferalComission> findByReferedId(Long userId);

	List<ReferalComission> findByReferedIdAndTypeNull(Long userId);

	List<ReferalComission> findByReferedIdAndTypeAndCoinName(Long userId, String string, String string2);

}
