package com.mobiloitte.p2p.content.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.Role;
import com.mobiloitte.p2p.content.enums.StatusType;
import com.mobiloitte.p2p.content.model.P2PAdvertisement;

@Repository
public interface CreateAdvertisementDao extends JpaRepository<P2PAdvertisement, Long> {

	Page<P2PAdvertisement> findByOrderType(OrderType orderType, Boolean isDeleted, Pageable pageable);

	Long countByFkUserIdNot(Long userId);

	List<P2PAdvertisement> findByOrderType(OrderType valueOf);

	List<P2PAdvertisement> findAllByRoleAndOrderType(Role role, OrderType orderType, Pageable pageable);

	Long countByOrderType(StatusType statusType);

	Optional<P2PAdvertisement> findByFkUserId(Long userId);

	Long countByMaxValueLessThanEqual(BigDecimal amount);

	Page<P2PAdvertisement> findByMaxValueLessThanEqualAndPaymentType(BigDecimal amount, PaymentType paymentType,
			Pageable pageable);

	Page<P2PAdvertisement> findAllByFkUserIdNot(Long userId, Pageable pageable);

	Page<P2PAdvertisement> findAllByIsDeletedAndFkUserIdNot(Long userId, Boolean isDeleted, Pageable pageable);

	Long countByIsDeletedAndFkUserIdNot(Long userId, Boolean isDeleted);

	Page<P2PAdvertisement> findAllByFkUserId(Long userId, Pageable pageable);

	Long countByFkUserId(Long userId);

	List<P2PAdvertisement> findByOrderType(OrderType orderType, Pageable pageable);

	List<P2PAdvertisement> findByEmail(String email);

	Long countByEmail(String email);

	Page<P2PAdvertisement> findByMaxValueLessThanEqualAndPaymentTypeAndFiatCoinAndCountry(BigDecimal amount,
			PaymentType paymentType, String currency, String country, Pageable of);

	List<P2PAdvertisement> findByFkUserIdAndIsDeletedFalse(Long userId);

	List<P2PAdvertisement> findByFkUserIdAndIsDeletedFalseAndOrderType(Long userId, OrderType orderType);

	Page<P2PAdvertisement> findByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNot(OrderType orderType,
			boolean b, ExchangeStatusType enable, Long userId, Pageable of);

	Long countByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNot(OrderType orderType, boolean b,
			ExchangeStatusType enable, Long userId);

	
	Page<P2PAdvertisement> findByIsDeletedAndExchangeStatusTypeAndFkUserIdNot(boolean b, ExchangeStatusType enable,
			Long userId, Pageable of);

	Long countByIsDeletedAndExchangeStatusTypeAndFkUserIdNot(boolean b, ExchangeStatusType enable, Long userId);

	Page<P2PAdvertisement> findByOrderTypeAndIsDeletedFalseAndExchangeStatusType(OrderType orderType,
			ExchangeStatusType enable, Pageable of);

	Long countByOrderTypeAndIsDeletedFalseAndExchangeStatusType(OrderType orderType, ExchangeStatusType enable);





	List<P2PAdvertisement> findAllByFkUserId(Long userId);

	List<P2PAdvertisement> findByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNot(OrderType orderType,
			boolean b, ExchangeStatusType enabled, Long userId);

	List<P2PAdvertisement> findByIsDeletedAndExchangeStatusTypeAndFkUserIdNot(boolean b, ExchangeStatusType enabled,
			Long userId);

	Optional<P2PAdvertisement> findByPeerToPeerExchangeIdAndIsDeletedFalse(Long peerToPeerExchangeId);

	long countByIsDeletedFalse();

	long countByStatusType(StatusType open);
	
	


	
	
	
	
	
	
	


	

	



}
