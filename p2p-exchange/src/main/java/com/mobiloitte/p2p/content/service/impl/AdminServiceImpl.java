package com.mobiloitte.p2p.content.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mobiloitte.p2p.content.constants.AddConstants;
import com.mobiloitte.p2p.content.constants.EmailConstants;
import com.mobiloitte.p2p.content.dao.AdminChargeDao;
import com.mobiloitte.p2p.content.dao.AdminDao;
import com.mobiloitte.p2p.content.dao.AdminTradeDao;
import com.mobiloitte.p2p.content.dao.CreateAdvertisementDao;
import com.mobiloitte.p2p.content.dao.EscrowDao;
import com.mobiloitte.p2p.content.dao.PaymentDao;
import com.mobiloitte.p2p.content.dao.TradingDao;
import com.mobiloitte.p2p.content.dto.AdminChargeDto;
import com.mobiloitte.p2p.content.dto.DisputeDto;
import com.mobiloitte.p2p.content.dto.EmailDto;
import com.mobiloitte.p2p.content.dto.GetBalanceResponseDto;
import com.mobiloitte.p2p.content.dto.MarketPriceDto;
import com.mobiloitte.p2p.content.dto.P2PAdvertisementDto;
import com.mobiloitte.p2p.content.dto.SearchAdvertisementDto;
import com.mobiloitte.p2p.content.dto.SearchAndFilterDto;
import com.mobiloitte.p2p.content.dto.SearchTradeDto;
import com.mobiloitte.p2p.content.dto.TradingListDto;
import com.mobiloitte.p2p.content.dto.UserEmailAndNameDto;
import com.mobiloitte.p2p.content.enums.DisputeStatus;
import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.StatusType;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.enums.TransactionStatus;
import com.mobiloitte.p2p.content.exception.OrderTypeNotFoundException;
import com.mobiloitte.p2p.content.exception.StatusTypeNotFoundException;
import com.mobiloitte.p2p.content.feign.NotificationClient;
import com.mobiloitte.p2p.content.feign.UserClient;
import com.mobiloitte.p2p.content.feign.WalletClient;
import com.mobiloitte.p2p.content.model.AdminCharge;
import com.mobiloitte.p2p.content.model.Escrow;
import com.mobiloitte.p2p.content.model.P2PAdvertisement;
import com.mobiloitte.p2p.content.model.PaymentTypeMethod;
import com.mobiloitte.p2p.content.model.Response;
import com.mobiloitte.p2p.content.model.Trading;
import com.mobiloitte.p2p.content.service.AdminService;

@Service
public class AdminServiceImpl extends AddConstants implements AdminService {
	private static final Logger LOGGER = LogManager.getLogger(AdminServiceImpl.class);
	private static final Gson GSON = new Gson();
	public static final String ORDER_BY_DESC = " order by u.tradingId desc ";

	public static final String USER_STATUS_AND_STATUS = " (u.disputeStatus =:status) ";
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private EntityManager em;

	@Autowired
	private AdminDao adminDao;
	@Autowired
	private TradingDao tradingDao;

	@Autowired
	AdminChargeDao adminChargeDao;
	@Autowired
	private AdminTradeDao adminTradeDao;
	@Autowired
	CreateAdvertisementDao createAdvertisementDao;

	@Value("${service.url}")
	private String url;

	@Autowired
	NotificationClient notificationClient;

	@Autowired
	UserClient userClient;

	@Autowired
	EscrowDao escrowDao;
	@Autowired
	WalletClient walletClient;

	@Override
	public Response<Map<String, Object>> getAdvertisementForAdmin(OrderType orderType, Integer page, Integer pageSize) {
		try {
			Page<P2PAdvertisement> getBuyOrderList;
			Long getTotalCount;
			if (orderType != null) {

				getBuyOrderList = adminDao.findByOrderType(orderType,
						PageRequest.of(page, pageSize, Direction.DESC, "peerToPeerExchangeId"));
				getTotalCount = adminDao.countByOrderType(orderType);
			} else {
				getBuyOrderList = adminDao
						.findAll(PageRequest.of(page, pageSize, Direction.DESC, "peerToPeerExchangeId"));
				getTotalCount = adminDao.count();
			}
			if (getBuyOrderList.hasContent()) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put(RESULT_LIST, getBuyOrderList.getContent());
				responseMap.put(TOTAL_COUNT, getTotalCount);
				return new Response<>(SUCCESS_CODE, ADDVERTISEMENT_DATA_GET_SUCCESSFULLY, responseMap);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {
			throw new OrderTypeNotFoundException(
					String.format("OrderType not found for getting details with the orderType : %s", orderType), e);
		}

	}

	@Override
	public Response<Map<String, Object>> getTradeHistory(TradeStatus tradeStatus, Integer page, Integer pageSize) {
		try {
			Page<Trading> getTradeList;
			Long getTotalCount;
			if (tradeStatus != null) {
				getTradeList = adminTradeDao.findByTradeStatus(tradeStatus,
						PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
				getTotalCount = adminTradeDao.countByTradeStatus(tradeStatus);
			} else {
				getTradeList = adminTradeDao.findAll(PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
				getTotalCount = adminTradeDao.count();
			}
			if (getTradeList.hasContent()) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put(RESULT_LIST, getTradeList.getContent());
				responseMap.put(TOTAL_COUNT, getTotalCount);
				return new Response<>(SUCCESS_CODE, "Trade Data Get SuccessFully", responseMap);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {
			throw new StatusTypeNotFoundException(
					String.format("tradeStatus not found for getting details with the orderType : %s", tradeStatus), e);
		}
	}

	@Override
	public Response<Map<String, Object>> getAdvertisementForAdminByTradingId(String tradeId) {
		try {
			Optional<Trading> tradeData = tradingDao.findByTradeId(tradeId);
			Long count = createAdvertisementDao.count();
			if (tradeData != null) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put(RESULT_LIST, tradeData);
				responseMap.put(TOTAL_COUNT, count);
				return new Response<>(SUCCESS_CODE, "Trade Data Get SuccessFully", responseMap);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {

			throw new StatusTypeNotFoundException(
					String.format("tradingId not found for getting details with the tradingId : %s", tradeId), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> filterTradeDetails(TradeStatus tradeStatus, Long fromDate, Long toDate, String search,
			Integer page, Integer pageSize) {

		StringBuilder query = new StringBuilder(
				"select t.tradingId,t.tradeId,t.totalPrice,t.creationTime,t.tradeStatus,t.buyer,t.seller,t.type from Trading t");

		if (tradeStatus != null) {
			LOGGER.debug("filtering with trade status {}", tradeStatus);
			query.append(" where t.trade_Status=" + tradeStatus);
		}
		if (fromDate != null) {
			LOGGER.debug("filtering with from date {}", new Date());
			query.append(" where t.creation_Time=" + fromDate);
		}
		if (toDate != null) {
			LOGGER.debug("filtering with to date {}", new Date());
			query.append(" and t.creation_Time=" + toDate);
		}

		Query createQuery = em.createQuery(query.toString());

		if (tradeStatus != null)
			createQuery.setParameter("status", tradeStatus);
		if (fromDate != null)
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null)
			createQuery.setParameter("toDate", new Date(toDate));
		if (search != null)
			createQuery.setParameter("search", '%' + search + '%');
		LOGGER.debug("Total Results {}", search);
		int filteredResultCount = createQuery.getResultList().size();
		LOGGER.debug("Total Results {}", filteredResultCount);
		if (page != null && pageSize != null) {
			createQuery.setFirstResult(page * pageSize);
			createQuery.setMaxResults(pageSize);
		}

		List<Object[]> list = createQuery.getResultList();
		List<TradingListDto> response = list.parallelStream().map(o -> {
			TradingListDto dto = new TradingListDto();

			dto.setTradingId((Long) o[0]);
			dto.setTradeId((String) o[1]);
			dto.setTotalPrice((BigDecimal) o[2]);
			dto.setCreationTime((Date) o[3]);
			dto.setTradeStatus((TradeStatus) o[4]);
			dto.setBuyer((String) o[5]);
			dto.setSeller((String) o[6]);

			return dto;
		}).collect(Collectors.toList());
		Collections.sort(response, (a, b) -> b.getTradingId().compareTo(a.getTradingId()));
		Map<String, Object> data = new HashMap<>();
		data.put("list", response);
		data.put("totalCount", filteredResultCount);

		return new Response<>(200, "SUCCESS", data);

	}

	@Override
	public Response<Map<String, Object>> getTradeHistoryFromDate(Long fromDate, Long toDate, Integer page,
			Integer pageSize) {
		try {
			Date l1 = new Date(fromDate);
			Date l2 = new Date(toDate);
			Page<Trading> getTradeList;
			Long getTotalCount;
			if (fromDate != null && toDate != null) {
				getTradeList = adminTradeDao.findByCreationTimeBetween(l1, l2,
						PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
				getTotalCount = adminTradeDao.count();
			} else {
				getTradeList = adminTradeDao.findAll(PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
				getTotalCount = adminTradeDao.count();
			}
			if (getTradeList.hasContent()) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put(RESULT_LIST, getTradeList.getContent());
				responseMap.put(TOTAL_COUNT, getTotalCount);
				return new Response<>(SUCCESS_CODE, "Trade Data Get SuccessFully", responseMap);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {

			throw new StatusTypeNotFoundException(
					String.format("CreationTime not found for getting details with the orderType : %s", fromDate), e);
		}
	}

	@Override
	public Response<Object> getDetailsByEmail(String email) {
		List<P2PAdvertisement> data = createAdvertisementDao.findByEmail(email);
		Map<String, Object> map = new HashMap<>();
		Long count = createAdvertisementDao.countByEmail(email);
		if (!data.isEmpty()) {
			map.put("Count", count);
			map.put("Data", data);
			map.put("StatusCode", 200);
			return new Response<>(SUCCESS_CODE, "SUCCESS", map);
		} else {
			return new Response<>(203, DATA_NOT_FOUND);

		}

	}

	@Override
	public Response<Object> editAdvertisement(Long userId, P2PAdvertisementDto p2PAdvertisementDto,
			Long peerToPeerExchangeId) {
		try {
			if (peerToPeerExchangeId != null) {
				Optional<P2PAdvertisement> details = createAdvertisementDao.findById(peerToPeerExchangeId);
				if (!details.isPresent()) {
					return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
				} else {
					Response<MarketPriceDto> value2 = walletClient.getMarketPrice(p2PAdvertisementDto.getCryptoCoin());
					if (value2 == null) {
						return new Response<>(700, "Market Price  Not Found For This Coin");

					}
					P2PAdvertisement p2pBuySellModel = details.get();

					BigDecimal priceInInr = value2.getData().getPriceInInr();
					BigDecimal priceInUsd = value2.getData().getPriceInUsd();

					BigDecimal b = new BigDecimal(100);

					if (p2PAdvertisementDto.getFiatCoin().equals("USD")) {
						BigDecimal pricePerCoin = priceInUsd
								.add(priceInUsd.multiply(p2PAdvertisementDto.getMargin().divide(b)));
						p2pBuySellModel.setPrice(pricePerCoin);

					} else {
						BigDecimal pricePerCoin = priceInInr
								.add(priceInInr.multiply(p2PAdvertisementDto.getMargin().divide(b)));
						p2pBuySellModel.setPrice(pricePerCoin);
					}

					p2pBuySellModel.setCryptoCoin(p2PAdvertisementDto.getCryptoCoin());
					p2pBuySellModel.setRole(p2PAdvertisementDto.getRole());
					p2pBuySellModel.setFiatCoin(p2PAdvertisementDto.getFiatCoin());
					p2pBuySellModel.setMargin(p2PAdvertisementDto.getMargin());
					p2pBuySellModel.setMaxValue(p2PAdvertisementDto.getMaxValue());
					p2pBuySellModel.setMinValue(p2PAdvertisementDto.getMinValue());
					p2pBuySellModel.setTermsOfTrade(p2PAdvertisementDto.getTermsOfTrade());
					p2pBuySellModel.setCountry(p2PAdvertisementDto.getCountry());
					p2pBuySellModel.setRestrictAmount(p2PAdvertisementDto.getRestrictAmount());
					p2pBuySellModel.setPaymentWindow(p2PAdvertisementDto.getPaymentWindow());
					p2pBuySellModel.setFkUserId(userId);

					createAdvertisementDao.save(p2pBuySellModel);
					return new Response<>(SUCCESS_CODE, ADDVERTISEMENT_UPDATE_SUCCESSFULLY);
				}

			}

			else
				return new Response<>(P2PID_NOT_FOUND_CODE, CHECKYOUR_PEER_TO_PEER_EXCHANGE_ID);
		} catch (

		Exception e) {
			LOGGER.catching(e);
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public Response<AdminCharge> setBasicTradeFees(AdminChargeDto adminChargeDto) {

		Optional<AdminCharge> data = adminChargeDao.findByCoinName(adminChargeDto.getCoinName());
		if (data.isPresent()) {

			data.get().setCoinName(adminChargeDto.getCoinName());
			data.get().setFees(adminChargeDto.getFees());
			walletClient.setUpdateTradeFee(adminChargeDto.getCoinName(), adminChargeDto.getFees());
			adminChargeDao.save(data.get());
			return new Response<>(SUCCESS_CODE, "SUCCESS");

		}

		else {
			AdminCharge adminCharge = new AdminCharge();
			adminCharge.setCoinName(adminChargeDto.getCoinName());
			if (adminChargeDto.getFees() != null)
				adminCharge.setFees(adminChargeDto.getFees());
			else
				adminCharge.setFees(BigDecimal.ZERO);

			walletClient.setUpdateTradeFee(adminChargeDto.getCoinName(), adminChargeDto.getFees());
			adminChargeDao.save(adminCharge);
		}
		return new Response<>(SUCCESS_CODE, "SUCCESS");
	}

	@Override
	public Response<Object> getAdvertisementSearch(Long userId, Long userId2, Long peerToPeerExchangeId,
			String userName, ExchangeStatusType orderStatus, String country, String currency, Long amount,
			PaymentType paymentType, OrderType orderType, Long fromDate, Long toDate, Integer page, Integer pageSize) {

		StringBuilder query = new StringBuilder(
				"select c.peerToPeerExchangeId, c.userName, c.exchangeStatusType, c.country, c.paymentType, c.orderType, c.creationTime,c.fiatCoin,c.maxValue,c.fkUserId,c.minValue,c.price from P2PAdvertisement c");
		List<String> conditions = new ArrayList<>();
		if (peerToPeerExchangeId != null) {
			conditions.add("((c.peerToPeerExchangeId like :peerToPeerExchangeId))");
		}
		if (userName != null) {
			conditions.add("((c.userName like :userName))");
		}
		if (userId2 != null) {
			conditions.add("((c.fkUserId like :userId2))");
		}
		if (orderStatus != null) {
			conditions.add("((c.exchangeStatusType like :orderStatus))");
		}
		if (currency != null) {
			conditions.add("((c.fiatCoin like :currency))");
		}
		if (amount != null) {

			conditions.add("((c.maxValue like :amount))");
		}
		if (country != null) {
			conditions.add("((c.country like :country))");
		}
		if (paymentType != null) {
			conditions.add("((c.paymentType like :paymentType))");
		}
		if (orderType != null) {
			conditions.add("((c.orderType like :orderType))");
		}
		if (fromDate != null) {
			LOGGER.debug("filtering with from date {}", new Date());

			conditions.add(" (c.creationTime >=:fromDate) ");
		}
		if (toDate != null) {
			LOGGER.debug("filtering with to date {}", new Date());

			conditions.add(" (c.creationTime <=:toDate) ");
		}

		if (!conditions.isEmpty()) {
			query.append(" where ");
			query.append(String.join(" and ", conditions.toArray(new String[0])));
		}
		query.append(" order by c.peerToPeerExchangeId Desc ");
		Query createQuery = em.createQuery(query.toString());
		if (peerToPeerExchangeId != null) {
			createQuery.setParameter("peerToPeerExchangeId", peerToPeerExchangeId);
		}
		if (orderStatus != null) {
			createQuery.setParameter("orderStatus", orderStatus);
		}
		if (country != null) {
			createQuery.setParameter("country", country);
		}
		if (currency != null) {
			createQuery.setParameter("currency", currency);
		}
		if (userId2 != null) {
			createQuery.setParameter("userId2", userId2);
		}
		if (amount != null) {
			BigDecimal am = new BigDecimal(String.format("%.6f", (double) amount));

			createQuery.setParameter("amount", am);
		}
		if (userName != null) {
			createQuery.setParameter("userName", '%' + userName + '%');
		}
		if (paymentType != null) {
			createQuery.setParameter("paymentType", paymentType);
		}
		if (orderType != null) {
			createQuery.setParameter("orderType", orderType);
		}
		if (fromDate != null)
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null)
			createQuery.setParameter("toDate", new Date(toDate));
		int filteredResultCount = createQuery.getResultList().size();
		if (page != null && pageSize != null) {
			createQuery.setFirstResult(page * pageSize);
			createQuery.setMaxResults(pageSize);
		}
		List<Object[]> list = createQuery.getResultList();
		List<SearchAdvertisementDto> response = list.parallelStream().map(o -> {
			SearchAdvertisementDto dto = new SearchAdvertisementDto();
			dto.setPeerToPeerExchangeId((Long) o[0]);
			dto.setUserName((String) o[1]);
			dto.setOrderStatus((ExchangeStatusType) o[2]);
			dto.setCountry((String) o[3]);

			dto.setPaymentType((PaymentType) o[4]);
			dto.setOrderType((OrderType) o[5]);
			dto.setCreationTime((Date) o[6]);
			dto.setFiatCoin((String) o[7]);
			dto.setMaxValue((BigDecimal) o[8]);
			dto.setUserId((Long) o[9]);
			BigDecimal ret = null;
			BigDecimal rot = null;
			if (o[10] instanceof BigDecimal) {
				ret = (BigDecimal) o[10];
			}
			if (o[11] instanceof BigDecimal) {
				rot = (BigDecimal) o[11];
			}
			dto.setMinValue(ret);
			dto.setPrice(rot);

			return dto;
		}).collect(Collectors.toList());
		Collections.sort(response, (a, b) -> b.getPeerToPeerExchangeId().compareTo(a.getPeerToPeerExchangeId()));
		if (!response.isEmpty()) {
			List<String> list4 = new ArrayList<>();
			Map<String, Object> data = new HashMap<>();
			Map<String, Object> map = new HashMap<>();
			for (SearchAdvertisementDto method : response) {
				List<PaymentTypeMethod> list2 = paymentDao
						.findByp2pAdvertisementPeerToPeerExchangeId(method.getPeerToPeerExchangeId());
				if (!list.isEmpty()) {
					for (PaymentTypeMethod list3 : list2) {
						map.put("p2pId", list3.getP2pAdvertisement().getPeerToPeerExchangeId());
						map.put("paymentType", list3.getTypeName());
						list4.add(map.toString());
					}

				}
			}
//			Map<String, Object> data = new HashMap<>();
			data.put("list", response);
			data.put("totalCount", filteredResultCount);
			data.put("data", list4);
			return new Response<>(200, "success", data);
		} else {
			return new Response<>(201, "no data found according to search");
		}
	}

	@Override
	public Response<Object> getTradingSearch(Long userId, String search, TradeStatus tradeStatus, String country,
			PaymentType paymentType, OrderType type, Long fromDate, Long toDate, DisputeStatus disputeStatus,
			Integer page, Integer pageSize) {
		StringBuilder query = new StringBuilder(
				"select c.tradingId,c.tradeId, c.buyer, c.seller, c.tradeStatus, c.type,c.paymentType, c.creationTime,c.tradeAmount,c.totalBTC,c.tradeFee,c.fkUserId,c.partnerId,c.disputeId,c.disputeStatus,c.disputer,c.disputeDate,c.fiatCoin ,c.staffId,c.staffName from Trading c");
		List<String> conditions = new ArrayList<>();

		if (tradeStatus != null) {
			conditions.add("((c.tradeStatus like :tradeStatus))");
		}
		if (disputeStatus != null) {
			conditions.add("((c.disputeStatus like :disputeStatus))");
		}

		if (country != null) {
			conditions.add("((c.country like :country))");
		}
		if (paymentType != null) {
			conditions.add("((c.paymentType like :paymentType))");
		}
		if (type != null) {
			conditions.add("((c.type like :type))");
		}
		if (search != null) {
			LOGGER.debug("filtering with name {}", search);
			conditions.add(
					"((c.buyer like :search) or (c.seller like :search) or (c.fkUserId like :search) or (c.partnerId like :search))");

		}
		if (fromDate != null) {
			LOGGER.debug("filtering with from date {}", new Date());

			conditions.add(" c.creationTime >= :fromDate ");
		}
		if (toDate != null) {
			LOGGER.debug("filtering with to date {}", new Date());

			conditions.add(" c.creationTime <= :toDate ");

		}

		if (!conditions.isEmpty()) {
			query.append(" where ");
			query.append(String.join(" and ", conditions.toArray(new String[0])));
		}
		query.append(" order by c.tradingId Desc ");

		Query createQuery = em.createQuery(query.toString());

		if (country != null) {
			createQuery.setParameter("country", country);
		}
		if (tradeStatus != null) {
			createQuery.setParameter("tradeStatus", tradeStatus);
		}
		if (disputeStatus != null) {
			createQuery.setParameter("disputeStatus", disputeStatus);
		}
		if (paymentType != null) {
			createQuery.setParameter("paymentType", paymentType);
		}
		if (type != null) {
			createQuery.setParameter("type", type);
		}
		if (search != null)
			createQuery.setParameter("search", '%' + search + '%');
		LOGGER.debug("Total Results {}", search);
		if (fromDate != null)
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null)
			createQuery.setParameter("toDate", new Date(toDate));
		int filteredResultCount = createQuery.getResultList().size();
		if (page != null && pageSize != null) {
			createQuery.setFirstResult(page * pageSize);
			createQuery.setMaxResults(pageSize);
		}

		List<Object[]> list = createQuery.getResultList();
		List<SearchTradeDto> response = list.parallelStream().map(o -> {
			SearchTradeDto dto = new SearchTradeDto();
			dto.setTradingId((Long) o[0]);
			dto.setTradeId((String) o[1]);
			dto.setBuyer((String) o[2]);
			dto.setSeller((String) o[3]);
			dto.setTradeStatus((TradeStatus) o[4]);
			dto.setType((OrderType) o[5]);
			dto.setPaymentType((PaymentType) o[6]);
			dto.setCreationTime((Date) o[7]);
			dto.setTradeAmount((BigDecimal) o[8]);
			dto.setTotalBTC((BigDecimal) o[9]);
			dto.setTradeFee((BigDecimal) o[10]);
			dto.setUserId((Long) o[11]);
			dto.setPartnerId((Long) o[12]);
			dto.setDisputeId((String) o[13]);
			dto.setDisputeStatus((DisputeStatus) o[14]);
			dto.setDisputer((String) o[15]);
			dto.setDisputeDate((Date) o[16]);
			dto.setFiatCoin((String) o[17]);
			dto.setStaffId((String) o[18]);
			dto.setStaffName((String) o[19]);
			return dto;
		}).collect(Collectors.toList());
		Collections.sort(response, (a, b) -> b.getTradingId().compareTo(a.getTradingId()));
		if (!response.isEmpty()) {
			Map<String, Object> data = new HashMap<>();
			List<Trading> data2 = tradingDao.findByTradeStatus(TradeStatus.COMPLETE);
			BigDecimal fees = BigDecimal.ZERO;
			if (!data2.isEmpty()) {
				for (Trading t : data2) {
					fees = fees.add(t.getTradeFee());
				}

			}
			data.put("feeColletion", fees);
			data.put("list", response);
			data.put("totalCount", filteredResultCount);

			LOGGER.debug("Total Results {}", data);

			return new Response<>(200, "success", data);
		} else {
			return new Response<>(201, "no data found according to search");
		}
	}

	@Override
	public Response<Object> deleteAdvertisement(Long userId, Long peerToPeerExchangeId) {
		Optional<P2PAdvertisement> data = createAdvertisementDao
				.findByPeerToPeerExchangeIdAndIsDeletedFalse(peerToPeerExchangeId);
		if (data.isPresent()) {

			data.get().setIsDeleted(Boolean.TRUE);
			data.get().setFkUserId(userId);
			createAdvertisementDao.save(data.get());
			return new Response<>(200, "Advertisement Delete SuccessFully");

		} else

			return new Response<>(200, "No Advertisement data Found", data);
	}

	@Override
	public Response<Object> forReleaseBitcoinsByAdmin(String tradeId, Long userId) {
		try {
			Response<UserEmailAndNameDto> emailAndName = userClient.getEmailAndName(userId);

			Map<String, Object> map1 = new HashMap<>();
			Optional<Escrow> data = escrowDao.findByTradeId(tradeId);
			if (!data.isPresent())
				return new Response<>(201, ESCROW_NOT_FOUND);
			Optional<Trading> data1 = tradingDao.findByTradeId(tradeId);
			Optional<P2PAdvertisement> data3 = createAdvertisementDao.findById(data1.get().getPeerToPeerExchangeId());
			if (!data3.isPresent())
				return new Response<>(201, ADDVERTISEMENT_NOT_FOUND);
			if (!data1.isPresent())
				return new Response<>(201, TRADE_NOT_FOUND);

			if (data1.get().getTradeStatus() == TradeStatus.COMPLETE)
				return new Response<>(201, "RELEASED_COINS_ALLREADY", data1.get().getTradeStatus());

			Response<UserEmailAndNameDto> emailAndName2 = userClient.getEmailAndName(data1.get().getFkUserId());
			Response<GetBalanceResponseDto> value5 = walletClient.getBalance(data1.get().getCryptoCoin(),
					data1.get().getPartnerId());
			BigDecimal BallanceOnWallet2 = value5.getData().getWalletBalance();
			BigDecimal releaseAmount3 = BallanceOnWallet2.add(data1.get().getTotalBTC());
			if (data1.get().getTransactionStatus() == TransactionStatus.PAID
					&& data1.get().getTradeStatus() == TradeStatus.DISPUTE) {

				walletClient.updateWallet(releaseAmount3, data1.get().getPartnerId(), data1.get().getCryptoCoin());

				data.get().setBlockedBalance(BigDecimal.ZERO);
				data1.get().setTradeStatus(TradeStatus.COMPLETE);

				data1.get().setDisputeStatus(DisputeStatus.DISPUTE_RESOLVED_BY_ADMIN);
				data1.get().setIsDeleted(false);

				escrowDao.save(data.get());
				tradingDao.save(data1.get());
				map1.put("tradeStatus", TradeStatus.COMPLETE);
				map1.put("fromUserId", userId);
				map1.put("isSeen", false);
				map1.put("toUserId", data.get().getFkUserId());
				map1.put("tradeId", tradeId);
				map1.put("tradePartner", emailAndName.getData().getName());
				map1.put("notificationStatus", "Notification Send For Release Coins");
				Map<String, String> setData = new HashMap<>();

				if (data3.get().getOrderType() == OrderType.BUY) {
					setData.put(EmailConstants.EMAIL_TOKEN, emailAndName2.getData().getEmail());
					setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(data1.get().getTotalBTC()));
					setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
					String jsonStr = GSON.toJson(map1);
					setData.put(EmailConstants.DATA_TOKEN, jsonStr);
					EmailDto emailDto = new EmailDto(data1.get().getFkUserId(), "release_coin",
							emailAndName2.getData().getEmail(), setData);
					notificationClient.sendNotification(emailDto);

				} else {
					setData.put(EmailConstants.EMAIL_TOKEN, emailAndName.getData().getEmail());
					setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(data1.get().getTotalBTC()));
					setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
					String jsonStr = GSON.toJson(map1);
					setData.put(EmailConstants.DATA_TOKEN, jsonStr);
					EmailDto emailDto = new EmailDto(data1.get().getPartnerId(), "release_coin",
							emailAndName.getData().getEmail(), setData);
					notificationClient.sendNotification(emailDto);
				}
				return new Response<>(SUCCESS_CODE, BITCOIN_RELEASE_SUCCESSFULLY);
			} else {
				return new Response<>(COINS_NOT_FOUND_CODE, FIRST_PAID_ANYWAY);
			}

		} catch (Exception e) {
			LOGGER.catching(e);
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> setEnableDisable(OrderType orderType, ExchangeStatusType statusType, Long userId,
			Long p2pId) {
		Optional<P2PAdvertisement> data = createAdvertisementDao.findByPeerToPeerExchangeIdAndIsDeletedFalse(p2pId);

		if (data.isPresent()) {
			data.get().setExchangeStatusType(statusType);
			createAdvertisementDao.save(data.get());

			return new Response<>(200, "Advertisement Status Updated SuccessFully");
		} else
			return new Response<>(201, "ADDVERTISEMENT_NOT_FOUND");

	}

	@Override
	public Response<Object> getTotalP2pCount() {
		Map<String, Long> userCountByStatus = new HashMap<>();
		long totalAdvertisementCount = createAdvertisementDao.countByIsDeletedFalse();
		long totalOpenAdvertisementCount = createAdvertisementDao.countByStatusType(StatusType.OPEN);
		long totalCloseAdvertisementCount = createAdvertisementDao.countByStatusType(StatusType.CLOSED);
		long totalCanceledAdvertisementCount = tradingDao.countByTradeStatus(TradeStatus.CANCEL);
		long totalDisputeCount = tradingDao.countByTradeStatus(TradeStatus.DISPUTE);
		long totalOpenDisputeCount = tradingDao.countByDisputeStatus(DisputeStatus.Raised);
		long totalClosedDisputeCount = tradingDao.countByDisputeStatus(DisputeStatus.DISPUTE_RESOLVED_BY_ADMIN);
		List<Trading> data = tradingDao.findByTradeStatus(TradeStatus.COMPLETE);
		BigDecimal fees = BigDecimal.ZERO;
		if (!data.isEmpty()) {
			for (Trading t : data) {
				fees = fees.add(t.getTradeFee());
			}
			userCountByStatus.put("totalAdvertisementFeeCollectionCount", fees.longValue());

		}
		userCountByStatus.put("totalAdvertisementCount", totalAdvertisementCount);
		userCountByStatus.put("totalOpenAdvertisementCount", totalOpenAdvertisementCount);
		userCountByStatus.put("totalCloseAdvertisementCount", totalCloseAdvertisementCount);
		userCountByStatus.put("totalCanceledAdvertisementCount", totalCanceledAdvertisementCount);
		userCountByStatus.put("totalDisputeCount", totalDisputeCount);
		userCountByStatus.put("totalOpenDisputeCount", totalOpenDisputeCount);
		userCountByStatus.put("totalClosedDisputeCount", totalClosedDisputeCount);

		return new Response<>(200, "Count Get SuccessFully", userCountByStatus);
	}

	@Override
	public Response<Object> assignStaffForDispute(Long userId, String staffId, String disputeId) {

		Optional<Trading> kycDataById = tradingDao.findByDisputeId(disputeId);
		if (kycDataById.isPresent()) {
			Response<UserEmailAndNameDto> staffData = userClient.getEmailAndName(Long.parseLong(staffId));
			if (staffData.getStatus() == 200) {

				kycDataById.get().setStaffName(staffData.getData().getName());
				kycDataById.get().setStaffId(staffId);
				tradingDao.save(kycDataById.get());
			}

			return new Response<>(200, "This " + disputeId + " DisputeId");
		}
		return new Response<>(205, "Data not found");
	}

	@Override
	public Response<Object> getBasicTradeFees(String coinName) {

		Optional<AdminCharge> data = adminChargeDao.findByCoinName(coinName);
		if (data.isPresent()) {
			return new Response<>(200, "Trade Fee Get SuccessFully", data);

		} else
			return new Response<>(205, "Data not found");

	}

	@Override
	public Response<Object> recommendation(Long userId, String disputeId, String recommendation) {
		Optional<Trading> kycDataById = tradingDao.findByDisputeId(disputeId);
		if (kycDataById.isPresent()) {
			kycDataById.get().setRecommendation(recommendation);

			tradingDao.save(kycDataById.get());
			return new Response<>(200, "recommendation successfully ");
		}
		return new Response<>(205, "Data not found");
	}

	@Override
	public Response<Map<String, Object>> getDisputeHistory(Long buyerId, Long sellerId, Integer page,
			Integer pageSize) {
		try {

			List<Trading> data = tradingDao.findByFkUserIdInAndPartnerIdInAndDisputeStatusIn(
					Arrays.asList(buyerId, sellerId), Arrays.asList(sellerId, buyerId),
					Arrays.asList(DisputeStatus.Pending_for_Release, DisputeStatus.Raised, DisputeStatus.WIP),
					PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
			Long count = tradingDao.countByFkUserIdInAndPartnerIdInAndDisputeStatusIn(Arrays.asList(buyerId, sellerId),
					Arrays.asList(sellerId, buyerId),
					Arrays.asList(DisputeStatus.Pending_for_Release, DisputeStatus.Raised, DisputeStatus.WIP));
			if (!data.isEmpty()) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("disputeHistory", data);
				responseMap.put("count", count);
				return new Response<>(SUCCESS_CODE, "Trade Data Get SuccessFully", responseMap);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	@Override
	public Response<Map<String, Object>> getTradeHistoryWithChat(Long buyerId, Long sellerId, Integer page,
			Integer pageSize) {
		try {
			List<Trading> tradeData = tradingDao.findByFkUserIdInAndPartnerIdIn(Arrays.asList(buyerId, sellerId),
					Arrays.asList(buyerId, sellerId), PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
			Long count = tradingDao.countByFkUserIdInAndPartnerIdIn(Arrays.asList(buyerId, sellerId),
					Arrays.asList(buyerId, sellerId));
			if (!tradeData.isEmpty()) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("tradeHistory", tradeData);
				responseMap.put("count", count);
				return new Response<>(SUCCESS_CODE, "Trade Data Get SuccessFully", responseMap);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> searchAndFilterAdmin(Long userId, SearchAndFilterDto searchAndFiltersDto) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder query = new StringBuilder(
				"select u.tradingId,u.tradeId,u.buyer,u.seller,u.tradeStatus,u.type,u.paymentType,u.creationTime,u.tradeAmount,u.totalBTC,u.tradeFee,u.fkUserId,u.partnerId,u.disputeId,u.disputeStatus,u.disputer,u.disputeDate,u.fiatCoin,u.staffId,u.staffName,u.country from Trading u "
						+ "where u.disputeStatus in ('Raised','Pending_for_Release','WIP','DISPUTE_RESOLVED_BY_ADMIN','OPEN')");
		List<String> conditions = new ArrayList<>();
		if (searchAndFiltersDto.getSearch() != null) {
			conditions.add(
					" ((d.firstName like :search) or (d.lastName like :search) or (u.email like :search) or (r.role like :search) or (u.country like :search) or (u.paymentType like :search) or (u.disputeStatus like :search) or (u.buyer like :search) or (u.seller like :search) or (u.type like :search)) ");
		}
		StringBuilder updatedQuery = insertQueryConditions(searchAndFiltersDto, conditions, query);
		updatedQuery.append(ORDER_BY_DESC);
		Query createQuery = em.createQuery(String.valueOf(updatedQuery));
		if (searchAndFiltersDto.getSearch() != null)
			createQuery.setParameter("search", '%' + searchAndFiltersDto.getSearch() + '%');
		if (searchAndFiltersDto.getStatus() != null)
			createQuery.setParameter("status", DisputeStatus.valueOf(searchAndFiltersDto.getStatus()));
//		if (searchAndFiltersDto.getPaymentMethod() != null)
//			createQuery.setParameter("paymentMethod", PaymentType.valueOf(searchAndFiltersDto.getPaymentMethod()));
//		if (searchAndFiltersDto.getCountry() != null)
//			createQuery.setParameter("country", searchAndFiltersDto.getCountry());
		if (searchAndFiltersDto.getFromDate() != null)
			createQuery.setParameter("fromDate", new Date(Long.parseLong(searchAndFiltersDto.getFromDate())));
		if (searchAndFiltersDto.getToDate() != null)
			createQuery.setParameter("toDate", new Date(Long.parseLong(searchAndFiltersDto.getToDate())));
		int filteredResultCount = createQuery.getResultList().size();
		createQuery.setFirstResult(
				Integer.parseInt(searchAndFiltersDto.getPage()) * Integer.parseInt(searchAndFiltersDto.getPageSize()));
		createQuery.setMaxResults(Integer.parseInt(searchAndFiltersDto.getPageSize()));
		List<Object[]> list = createQuery.getResultList();
		List<DisputeDto> response = returnAdminDetailList(list);
		if (!response.isEmpty()) {
			map.put("size", filteredResultCount);
			map.put("list", response);
			return new Response<>(201, "dispute History Fetch successfully", map);
		} else {
			return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		}
	}

	private StringBuilder insertQueryConditions(SearchAndFilterDto searchAndFiltersDto, List<String> conditions,
			StringBuilder query) {
		if (searchAndFiltersDto.getStatus() != null) {
			conditions.add(USER_STATUS_AND_STATUS);
		}

		if (searchAndFiltersDto.getFromDate() != null) {
			conditions.add(" (u.creationTime >=:fromDate) ");
		}


		if (searchAndFiltersDto.getToDate() != null) {
			conditions.add(" (u.creationTime <=:toDate) ");
		}
		if (!conditions.isEmpty()) {
			query.append("and");
			query.append(String.join("and", conditions.toArray(new String[0])));
		}

		return query;
	}

	private List<DisputeDto> returnAdminDetailList(List<Object[]> list) {
		return list.parallelStream().map(o -> {
			DisputeDto dto = new DisputeDto();
			dto.setTradingId((Long) o[0]);
			dto.setTradeId((String) o[1]);
			dto.setBuyer((String) o[2]);
			dto.setSeller((String) o[3]);
			dto.setTradeStatus((TradeStatus) o[4]);
			dto.setType((OrderType) o[5]);
			dto.setPaymentType((PaymentType) o[6]);
			dto.setCreationTime((Date) o[7]);
			dto.setTradeAmount((BigDecimal) o[8]);
			dto.setTotalBTC((BigDecimal) o[9]);
			dto.setTradeFee((BigDecimal) o[10]);
			dto.setUserId((Long) o[11]);
			dto.setPartnerId((Long) o[12]);
			dto.setDisputeId((String) o[13]);
			dto.setDisputeStatus((DisputeStatus) o[14]);
			dto.setDisputer((String) o[15]);
			dto.setDisputeDate((Date) o[16]);
			dto.setFiatCoin((String) o[17]);
			dto.setStaffId((String) o[18]);
			dto.setStaffName((String) o[19]);
			dto.setCountry((String) o[20]);
			return dto;
		}).collect(Collectors.toList());

	}

	@Override
	public Response<Object> escrowList(Long userId, Integer page, Integer pageSize, String tradingPartner,
			StatusType statustype, String coinName, TransactionStatus transactionStatus) {

		if (page != null && pageSize != null) {
			Map<String, Object> map = new HashMap<>();
			if ((statustype == null) && (tradingPartner == null) && (coinName == null) && (transactionStatus == null)) {
				Page<Escrow> isdataExits = escrowDao
						.findAll(PageRequest.of(page, pageSize, Direction.DESC, "escrowId"));
				if (!isdataExits.isEmpty()) {
					map.put("Data", isdataExits);
					map.put("Count", isdataExits.getSize());
					return new Response<>(200, "Escrow List Fetched Successfully", map);
				}
			}
			if ((statustype != null) || (tradingPartner != null) || (coinName != null) || (transactionStatus != null)) {
				List<Escrow> isDataPresent = escrowDao
						.findByStatusType(PageRequest.of(page, pageSize, Direction.DESC, "escrowId"), statustype);
				if (!isDataPresent.isEmpty()) {
					map.put("Data", isDataPresent);
					map.put("Count", isDataPresent.size());
					return new Response<>(200, "Escrow List Fetched Successfully", map);
				}
				List<Escrow> isDataPresent1 = escrowDao.findByTradingPartner(
						PageRequest.of(page, pageSize, Direction.DESC, "escrowId"), tradingPartner);
				if (!isDataPresent1.isEmpty()) {
					map.put("Data", isDataPresent1);
					map.put("Count", isDataPresent1.size());
					return new Response<>(200, "Escrow List Fetched Successfully", map);
				}
				List<Escrow> isDataPresent2 = escrowDao
						.findByCoinName(PageRequest.of(page, pageSize, Direction.DESC, "escrowId"), coinName);
				if (!isDataPresent2.isEmpty()) {
					map.put("Data", isDataPresent2);
					map.put("Count", isDataPresent2.size());
					return new Response<>(200, "Escrow List Fetched Successfully", map);
				}
				List<Escrow> isDataPresent3 = escrowDao.findByTransactionStatus(
						PageRequest.of(page, pageSize, Direction.DESC, "escrowId"), transactionStatus);
				if (!isDataPresent3.isEmpty()) {
					map.put("Data", isDataPresent3);
					map.put("Count", isDataPresent3.size());
					return new Response<>(200, "Escrow List Fetched Successfully", map);
				}
				List<Escrow> isDataPresent4 = escrowDao.findByTransactionStatusOrStatusTypeOrCoinNameOrTradingPartner(
						PageRequest.of(page, pageSize, Direction.DESC, "escrowId"), transactionStatus,statustype,coinName,tradingPartner);
				if (!isDataPresent4.isEmpty()) {
					map.put("Data", isDataPresent4);
					map.put("Count", isDataPresent4.size());
					return new Response<>(200, "Escrow List Fetched Successfully", map);
				}
			}
			return new Response<>(205, "Escrow List Not Present");

		}
		throw new RuntimeException();

	}

}
