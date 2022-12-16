package com.mobiloitte.p2p.content.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	Page<P2PAdvertisement> findByOrderTypeAndIsDeletedFalseAndExchangeStatusTypeAndCryptoCoin(OrderType orderType,
			ExchangeStatusType enable,String cryptoCoin,Pageable of);

	Long countByOrderTypeAndIsDeletedFalseAndExchangeStatusTypeAndCryptoCoin(OrderType orderType, ExchangeStatusType enable,String cryptoCoin);

	List<P2PAdvertisement> findAllByFkUserId(Long userId);

	List<P2PAdvertisement> findByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNot(OrderType orderType,
			boolean b, ExchangeStatusType enabled, Long userId);

	List<P2PAdvertisement> findByIsDeletedAndExchangeStatusTypeAndFkUserIdNot(boolean b, ExchangeStatusType enabled,
			Long userId);

	Optional<P2PAdvertisement> findByPeerToPeerExchangeIdAndIsDeletedFalse(Long peerToPeerExchangeId);

	long countByIsDeletedFalse();

	long countByStatusType(StatusType open);

	List<P2PAdvertisement> findByFkUserIdAndIsDeletedFalseAndOrderTypeAndPeerToPeerExchangeId(Long userId,
			Long peerToPeerExchangeId, OrderType orderType);

	List<P2PAdvertisement> findByPeerToPeerExchangeId(Long peerToPeerExchangeId);

	Optional<P2PAdvertisement> findByPeerToPeerExchangeIdAndFkUserId(Long peerToPeerExchangeId, Long userId);

	Optional<P2PAdvertisement> findByOrderTypeAndExchangeStatusType(ExchangeStatusType statusType, OrderType orderType);

//	Optional<P2PAdvertisement> findByPeerToPeerExchangeIdAndFkUserIdAndIsDeletedFalse(Long peerToPeerExchangeId,
//			Long fkUserId);

//	Optional<P2PAdvertisement> findByPeerToPeerExchangeIdIsDeletedFalseAndFkUserId(Long peerToPeerExchangeId,
//			Long fkUserId);

//	Optional<P2PAdvertisement> findByPeerToPeerExchangeIdAndIsDeletedFalseAndFkUserId(Long peerToPeerExchangeId,
//			Long fkUserId);

//	List<P2PAdvertisement> findByFkUserIdAndPeerToPeerExchangeId(Long userId, Long peerToPeerExchangeId);

//	Optional<P2PAdvertisement> findByFkUserIdAndPeerToPeerExchangeIdAndOrderTypeAndExchangeStatusType(Long userId,
//			Long peerToPeerExchangeId, ExchangeStatusType statusType, OrderType orderType);

//	Optional<P2PAdvertisement> findByFkUserIdAndpeerToPeerExchangeIdAndOrderTypeAndExchangeStatusType(Long userId,
//			Long peerToPeerExchangeId, ExchangeStatusType statusType, OrderType orderType);

//	Optional<P2PAdvertisement> findByFkUserIdAndPeerToPeerExchangeIdAndOrderTypeAndExchangeStatusType(Long userId,
//			Long peerToPeerExchangeId, ExchangeStatusType statusType, OrderType orderType);

//	Optional<P2PAdvertisement> findByPeerToPeerExchangeIdAndOrderTypeAndExchangeStatusType(Long peerToPeerExchangeId,
//			ExchangeStatusType statusType, OrderType orderType);

//	Optional<P2PAdvertisement> findByFkUserIdAndPeerToPeerExchangeIdAndOrderTypeAndExchangeStatusType(Long userId,
//			Long peerToPeerExchangeId, ExchangeStatusType statusType, OrderType orderType);

//	Optional<P2PAdvertisement> findByFkUserIdAndPeerToPeerExchangeId(Long userId, Long peerToPeerExchangeId);

//	Optional<P2PAdvertisement> findByFkUserIdAndPeerToPeerExchangeIdOrExchangeStatusTypeOrOrderType(Long userId,
//			Long peerToPeerExchangeId, ExchangeStatusType statusType, OrderType orderType);

	Optional<P2PAdvertisement> findById(Long peerToPeerExchangeId);

	Optional<P2PAdvertisement> findByExchangeStatusTypeAndOrderType(ExchangeStatusType enabled, OrderType buy);

	Optional<P2PAdvertisement> findByExchangeStatusTypeAndOrderTypeAndPeerToPeerExchangeIdAndFkUserId(
			ExchangeStatusType enabled, OrderType buy, Long peerToPeerExchangeId, Long userId);

	Long countByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserId(OrderType orderType, boolean b,
			ExchangeStatusType enabled, Long userId);

	List<P2PAdvertisement> findByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserId(OrderType orderType, boolean b,
			ExchangeStatusType enabled, Long userId);

	List<P2PAdvertisement> findByIsDeletedAndExchangeStatusTypeAndFkUserId(boolean b, ExchangeStatusType enabled,
			Long userId);

	Long countByIsDeletedAndExchangeStatusTypeAndFkUserId(boolean b, ExchangeStatusType enabled, Long userId);

	Page<P2PAdvertisement> findByIsDeletedAndExchangeStatusTypeAndFkUserId(boolean b, ExchangeStatusType enabled,
			Long userId, Pageable of);

	Page<P2PAdvertisement> findByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserId(OrderType orderType, boolean b,
			ExchangeStatusType enabled, Long userId, Pageable of);

	List<P2PAdvertisement> findByFkUserIdAndExchangeStatusTypeAndStatusType(Long userId,
			ExchangeStatusType disabled, StatusType closed);

	List<P2PAdvertisement> findByFkUserIdAndExchangeStatusTypeAndStatusTypeAndOrderType(Long userId,
			ExchangeStatusType disabled, StatusType closed, OrderType buy);

//	List<P2PAdvertisement> findFirst3OrderByCreationTime();

	List<P2PAdvertisement> findFirst30ByOrderByCreationTimeDesc();

//	Page<P2PAdvertisement> findAllByFkUserIdOrCryptoCoinOrOrderTypeOrCreationTimeBetween(Long userId, String coin,
//			OrderType orderType, Date fromDate, Pageable of);

	Page<P2PAdvertisement> findByFkUserIdOrCryptoCoinOrOrderTypeOrCreationTimeBetween(Long userId, String coin,
			OrderType orderType, Date fromDate, Pageable of);

	Optional<P2PAdvertisement> findP2PAdvertisementByPeerToPeerExchangeId(Long peerToPeerExchangeId);

	List<P2PAdvertisement> findP2PAdvertisementByFkUserId(Long userId);

	Page<P2PAdvertisement> findByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNotAndCryptoCoin(
			OrderType orderType, boolean b, ExchangeStatusType enabled, Long userId, String cryptoCoin, Pageable of);

	Long countByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNotAndCryptoCoin(OrderType orderType, boolean b,
			ExchangeStatusType enabled, Long userId, String cryptoCoin);

	Page<P2PAdvertisement> findByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNotAndCryptoCoinAndCountryAndFiatCoinAndPriceAndCreationTimeBetween(
			OrderType orderType, boolean b, ExchangeStatusType enabled, Long userId, String cryptoCoin, String country,
			String currency, Long amount, Date date, Date date2, Pageable of);

	Long countByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNotAndCryptoCoinAndCountryAndFiatCoinAndPriceAndCreationTimeBetween(
			OrderType orderType, boolean b, ExchangeStatusType enabled, Long userId, String cryptoCoin, String country,
			String currency, Long amount, Date date, Date date2);

	Page<P2PAdvertisement> findByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNotOrCryptoCoinOrCountryOrFiatCoinOrPriceOrCreationTimeBetween(
			OrderType orderType, boolean b, ExchangeStatusType enabled, Long userId, String cryptoCoin, String country,
			String currency, Long amount, Date date, Date date2, Pageable of);

	Long countByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNotOrCryptoCoinOrCountryOrFiatCoinOrPriceOrCreationTimeBetween(
			OrderType orderType, boolean b, ExchangeStatusType enabled, Long userId, String cryptoCoin, String country,
			String currency, Long amount, Date date, Date date2);

	Long countByOrderTypeOrIsDeletedOrExchangeStatusTypeOrFkUserIdNotOrCryptoCoinOrCountryOrFiatCoinOrPriceOrCreationTimeBetween(
			OrderType orderType, boolean b, ExchangeStatusType enabled, Long userId, String cryptoCoin, String country,
			String currency, Long amount, Date date, Date date2);

	Page<P2PAdvertisement> findByOrderTypeOrIsDeletedOrExchangeStatusTypeOrFkUserIdNotOrCryptoCoinOrCountryOrFiatCoinOrPriceOrCreationTimeBetween(
			OrderType orderType, boolean b, ExchangeStatusType enabled, Long userId, String cryptoCoin, String country,
			String currency, Long amount, Date date, Date date2, Pageable of);

	Page<P2PAdvertisement> findByOrderTypeAndIsDeletedFalseAndExchangeStatusType(OrderType orderType,
			ExchangeStatusType enabled, Pageable of);

//	Long countByOrderTypeAndIsDeletedFalseAndExchangeStatusType(OrderType orderType, ExchangeStatusType enabled,
//			String cryptoCoin);

	Long countByOrderTypeAndIsDeletedFalseAndExchangeStatusType(OrderType orderType, ExchangeStatusType enabled);

//	List<P2PAdvertisement> findByFkUserIdAndIsDeletedFalseAndOrderType(Long userId, Long peerToPeerExchangeId,
//			OrderType orderType);
	
}
