package com.mobiloitte.content.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.SubCategoryFormNew;

public interface SubCategoryFormDao extends JpaRepository<SubCategoryFormNew, Long>{

	List<SubCategoryFormNew> findBySubCategoryFormId(Long subCategoryFormId);

}
