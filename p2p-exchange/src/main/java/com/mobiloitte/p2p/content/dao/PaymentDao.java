package com.mobiloitte.p2p.content.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.p2p.content.model.PaymentTypeMethod;

public interface PaymentDao extends JpaRepository<PaymentTypeMethod, Long> {

	List<PaymentTypeMethod> findByp2pAdvertisementPeerToPeerExchangeId(Long peerToPeerExchangeId);

}
