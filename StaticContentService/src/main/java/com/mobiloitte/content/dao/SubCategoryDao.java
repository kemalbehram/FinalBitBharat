package com.mobiloitte.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.SubCategory;

public interface SubCategoryDao extends JpaRepository<SubCategory, Long> {

	List<SubCategory> findBySubTeamId(Long subTeamId);

	Optional<SubCategory> findBySubTeamName(String subTeamName);

}
