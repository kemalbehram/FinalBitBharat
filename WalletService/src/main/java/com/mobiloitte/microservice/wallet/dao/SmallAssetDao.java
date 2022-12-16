package com.mobiloitte.microservice.wallet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.SmallAssetHistory;

public interface SmallAssetDao extends JpaRepository<SmallAssetHistory, Long>{

}
