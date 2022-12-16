package com.mobiloitte.microservice.wallet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobiloitte.microservice.wallet.model.BulkPurchaseRequest;
@Repository
public interface BulkPurchaseDao extends JpaRepository<BulkPurchaseRequest, Long> {

}
