package com.mobiloitte.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.order.model.PercentageChange;

public interface PercentageChangeRepo extends JpaRepository<PercentageChange, Long> {

}
