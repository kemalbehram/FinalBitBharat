package com.mobiloitte.usermanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.AdminActionDetails;

public interface AdminActionDao extends JpaRepository<AdminActionDetails, Long> {

}
