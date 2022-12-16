package com.mobiloitte.microservice.wallet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.ThresholdTransferHistory;

public interface ThresholdTransferHistoryDao extends JpaRepository<ThresholdTransferHistory, Long>{

}
