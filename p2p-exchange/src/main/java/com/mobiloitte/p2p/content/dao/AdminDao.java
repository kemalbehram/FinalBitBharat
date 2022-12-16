package com.mobiloitte.p2p.content.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.model.P2PAdvertisement;
@Repository
public interface AdminDao  extends JpaRepository<P2PAdvertisement, Long>{



	Long countByOrderType(OrderType orderType);

	Page<P2PAdvertisement> findByOrderType(OrderType orderType, Pageable of);


	


	
	

	
	
}
