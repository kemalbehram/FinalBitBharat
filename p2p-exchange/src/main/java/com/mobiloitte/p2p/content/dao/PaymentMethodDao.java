package com.mobiloitte.p2p.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.model.PaymentMethod;
import com.mobiloitte.p2p.content.model.PaymentTypeMethod;

public interface PaymentMethodDao extends JpaRepository<PaymentMethod, Long> {

	Optional<PaymentMethod> findByUserIdAndPaymentType(Long userId, PaymentType paymentType);

	Optional<PaymentMethod> findByUserIdAndPaymentDetailId(Long userId, Long paymentDetailId);

	Optional<PaymentMethod> findByUserIdAndPaymentDetailId(Long fkUserId, String typeName);

	Optional<PaymentMethod> findByUserIdAndPaymentType(Long userId, String typeName);

	List<PaymentMethod> findByUserId(Long userId);

	List<PaymentMethod> findPaymentMethodByUserIdAndPaymentType(Long userId, String typeName);

//	List<PaymentTypeMethod> findByP2PAdvertisementPeerToPeerExchangeId(Long peerToPeerExchangeId);

//	List<PaymentTypeMethod> findByp2pAdvertisementPeerToPeerExchangeId(Long peerToPeerExchangeId);

}
