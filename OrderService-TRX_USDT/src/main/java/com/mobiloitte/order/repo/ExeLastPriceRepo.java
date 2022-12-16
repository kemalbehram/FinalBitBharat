package com.mobiloitte.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.order.model.LastPrice;

public interface ExeLastPriceRepo extends JpaRepository<LastPrice, Long> {

}
