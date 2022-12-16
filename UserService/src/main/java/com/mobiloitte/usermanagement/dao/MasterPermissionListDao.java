package com.mobiloitte.usermanagement.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.MasterPermissionList;

public interface MasterPermissionListDao extends JpaRepository<MasterPermissionList, Long> {

	Optional<MasterPermissionList> findByMasterPermissionListId(Long valueOf);

}
