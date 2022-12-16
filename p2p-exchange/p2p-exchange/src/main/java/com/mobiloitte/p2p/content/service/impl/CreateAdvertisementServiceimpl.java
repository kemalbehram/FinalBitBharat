package com.mobiloitte.p2p.content.service.impl;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mobiloitte.p2p.content.constants.AddConstants;
import com.mobiloitte.p2p.content.constants.EmailConstants;
import com.mobiloitte.p2p.content.dao.AdminChargeDao;
import com.mobiloitte.p2p.content.dao.AdminDao;
import com.mobiloitte.p2p.content.dao.CreateAdvertisementDao;
import com.mobiloitte.p2p.content.dao.EscrowDao;
import com.mobiloitte.p2p.content.dao.MarketPriceDao;
import com.mobiloitte.p2p.content.dao.TradingDao;
import com.mobiloitte.p2p.content.dto.EmailDto;
import com.mobiloitte.p2p.content.dto.GetBalanceResponseDto;
import com.mobiloitte.p2p.content.dto.MarketPriceDto;
import com.mobiloitte.p2p.content.dto.P2PAdvertisementDto;
import com.mobiloitte.p2p.content.dto.SearchAdvertisementDto;
import com.mobiloitte.p2p.content.dto.SearchTradeDto;
import com.mobiloitte.p2p.content.dto.TradeCountDto;
import com.mobiloitte.p2p.content.dto.UserEmailAndNameDto;
import com.mobiloitte.p2p.content.enums.DisputeStatus;
import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.KycStatus;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.StatusType;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.enums.TransactionStatus;
import com.mobiloitte.p2p.content.enums.TwoFaType;
import com.mobiloitte.p2p.content.exception.OrderTypeNotFoundException;
import com.mobiloitte.p2p.content.exception.StatusTypeNotFoundException;
import com.mobiloitte.p2p.content.feign.NotificationClient;
import com.mobiloitte.p2p.content.feign.UserClient;
import com.mobiloitte.p2p.content.feign.WalletClient;
import com.mobiloitte.p2p.content.model.AdminCharge;
import com.mobiloitte.p2p.content.model.Escrow;
import com.mobiloitte.p2p.content.model.MarketPrice;
import com.mobiloitte.p2p.content.model.P2PAdvertisement;
import com.mobiloitte.p2p.content.model.Response;
import com.mobiloitte.p2p.content.model.Trading;
import com.mobiloitte.p2p.content.service.CreateAdvertisementService;

/**
 * @author Kumar Arjun NOTIFICATION_REQUEST-FOR-P2P { "userId":"1",
 *         "notificationUserType":"USER" }
 */
@Service
@Component
public class CreateAdvertisementServiceimpl extends AddConstants implements CreateAdvertisementService {
	private static final Logger LOGGER = LogManager.getLogger(CreateAdvertisementServiceimpl.class);
	private static final Gson GSON = new Gson();
	@Autowired
	CreateAdvertisementDao createAdvertisementDao;
	@Autowired
	private EntityManager em;

	@Value("${service.url}")
	private String url;

	@Autowired
	private NotificationClient notificationClient;

	@Autowired
	private WalletClient walletClient;
	@Autowired
	private UserClient userClient;
	@Autowired
	private TradingDao tradingDao;
	@Autowired
	private EscrowDao escrowDao;

	
	@Autowired
	private AdminChargeDao adminChargeDao;
	@Autowired
	private MarketPriceDao marketPriceDao;
	@Value("${spring.project.name}")
	private String projectName;

	@Override
	public Response<Object> createAdvertisement(P2PAdvertisementDto p2PAdvertisementDto, Long userId) {
		try {

			P2PAdvertisement p2pBuySellModel = new P2PAdvertisement();

			Response<UserEmailAndNameDto> data = userClient.getEmailAndName(userId);
			p2pBuySellModel.setOrderType(p2PAdvertisementDto.getOrderType());
			p2pBuySellModel.setCryptoCoin(p2PAdvertisementDto.getCryptoCoin());
			p2pBuySellModel.setPaymentType(p2PAdvertisementDto.getPaymentType());
			p2pBuySellModel.setFiatCoin(p2PAdvertisementDto.getFiatCoin());
			p2pBuySellModel.setMargin(p2PAdvertisementDto.getMargin());
			p2pBuySellModel.setMaxValue(p2PAdvertisementDto.getMaxValue());
			p2pBuySellModel.setMinValue(p2PAdvertisementDto.getMinValue());
			p2pBuySellModel.setExchangeStatusType(p2PAdvertisementDto.getOrderStatus());
			p2pBuySellModel.setTermsOfTrade(p2PAdvertisementDto.getTermsOfTrade());
			p2pBuySellModel.setPaymentWindow(p2PAdvertisementDto.getPaymentWindow());
			p2pBuySellModel.setFkUserId(userId);
			p2pBuySellModel.setPrice(p2PAdvertisementDto.getPrice());
			p2pBuySellModel.setUserName(data.getData().getName());
			p2pBuySellModel.setEmail(data.getData().getEmail());
			p2pBuySellModel.setIsDeleted(false);
			p2pBuySellModel.setFiatCoin(p2PAdvertisementDto.getFiatCoin());
			p2pBuySellModel.setCountry(p2PAdvertisementDto.getCountry());
			p2pBuySellModel.setRestrictAmount(p2PAdvertisementDto.getRestrictAmount());
			p2pBuySellModel.setIsKycAccepted(Boolean.FALSE);
			p2pBuySellModel.setIsIdentifiedPeople(Boolean.TRUE);
			p2pBuySellModel.setTwpfaType(p2PAdvertisementDto.getTwpfaType());
			p2pBuySellModel.setPaymentDetail(p2PAdvertisementDto.getPaymentDetail());
			p2pBuySellModel.setPrice(p2PAdvertisementDto.getPrice());
			p2pBuySellModel.setBankName(p2PAdvertisementDto.getBankName());
			p2pBuySellModel.setLocation(p2PAdvertisementDto.getLocation());
			if (p2PAdvertisementDto.getMaxValue() == null)
				return new Response<>(MAX_VALUE_NOT_FOUND, ENTER_THE_MAX_VALUE);

			if (p2PAdvertisementDto.getUsername() == null)
				return new Response<>(786, "ENTER_USERNAME");
			if (p2PAdvertisementDto.getMargin() == null)
				return new Response<>(MARGIN_NOT_FOUND, ENTER_THE_MARGIN);

			if (p2PAdvertisementDto.getMinValue() == null)
				return new Response<>(MIN_VALUE_NOT_FOUND, ENTER_THE_MIN_VALUE);

			if (p2PAdvertisementDto.getTermsOfTrade() == null)
				return new Response<>(TERMS_OF_TRADE_NOT_FOUND, ENTER_TERMS_OF_TRADE);

			if (p2PAdvertisementDto.getMaxValue().compareTo(p2PAdvertisementDto.getMinValue()) < 0) {
				return new Response<>(MISMATCHED_CODE, MAX_SHOULD_BE_GRATTER_THAN_MIN_VALUE);
			} else {
				createAdvertisementDao.save(p2pBuySellModel);
				return new Response<>(SUCCESS_CODE, ADDVERTISEMENT_PLACED_SUCCESSFULLY, p2pBuySellModel);
			}
		}

		catch (Exception e) {

			LOGGER.catching(e);
		}
		return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
	}

	@Override
	public Response<Map<String, Object>> getBuySEllAdvertisementDetails(OrderType orderType, Long userId, Integer page,
			Integer pageSize) {
		try {
			Page<P2PAdvertisement> getBuyOrderList;
			Long getTotalCount;
			if (orderType != null) {
				getBuyOrderList = createAdvertisementDao.findByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNot(
						orderType, false, ExchangeStatusType.ENABLED, userId,
						PageRequest.of(page, pageSize, Direction.DESC, "peerToPeerExchangeId"));
				getTotalCount = createAdvertisementDao.countByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNot(
						orderType, false, ExchangeStatusType.ENABLED, userId);
			}

			else {
				getBuyOrderList = createAdvertisementDao.findByIsDeletedAndExchangeStatusTypeAndFkUserIdNot(false,
						ExchangeStatusType.ENABLED, userId,
						PageRequest.of(page, pageSize, Direction.DESC, "peerToPeerExchangeId"));
				getTotalCount = createAdvertisementDao.countByIsDeletedAndExchangeStatusTypeAndFkUserIdNot(false,
						ExchangeStatusType.ENABLED, userId);
			}
			if (getBuyOrderList.hasContent()) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put(RESULT_LIST, getBuyOrderList.getContent());
				responseMap.put(TOTAL_COUNT, getTotalCount);
				return new Response<>(SUCCESS_CODE, ADDVERTISEMENT_DATA_GET_SUCCESSFULLY, responseMap);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {
			LOGGER.catching(e);
			throw new OrderTypeNotFoundException(
					String.format("OrderType not found for getting details with the orderType : %s", orderType), e);
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
						return new Response<>(700, MARKET_PRICE_NOT_FOUND);

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

					Response<UserEmailAndNameDto> data = userClient.getEmailAndName(userId);
					Response<KycStatus> value = userClient.getKycStatus(userId);
					Boolean checkTest = value.getData().equals(KycStatus.ACCEPTED);
					if (value.getData() == KycStatus.ACCEPTED) {

						if (checkTest == p2PAdvertisementDto.getIsIdentifiedPeople()) {
							TwoFaType t = data.getData().getTwoFaType();
							TwoFaType p = p2PAdvertisementDto.getTwpfaType();
							/*
							 * if (data.getData().getTwoFaType() == TwoFaType.SMS) {
							 * 
							 * if (t == p) {
							 */
							p2pBuySellModel.setStatusType(StatusType.OPEN);

							p2pBuySellModel.setCryptoCoin(p2PAdvertisementDto.getCryptoCoin());
							p2pBuySellModel.setRole(p2PAdvertisementDto.getRole());
							p2pBuySellModel.setPaymentType(p2PAdvertisementDto.getPaymentType());
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

						} else
							return new Response<>(302, KYC_REQUIRED);
					} else
						return new Response<>(303, KYC_SHOULD_ACCEPTED);

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
	public Response<List<P2PAdvertisement>> getAllBuyAdvertismentForAdmin(OrderType orderType, Integer page,
			Integer pageSize) {
		try {
			List<P2PAdvertisement> listAllForAdmin = createAdvertisementDao.findByOrderType(orderType,
					PageRequest.of(page, pageSize, Direction.ASC, "peerToPeerExchangeId"));
			return new Response<>(SUCCESS_CODE, "success", listAllForAdmin);

		} catch (Exception e) {
			LOGGER.catching(e);
			throw new OrderTypeNotFoundException(
					String.format("OrderType not found for getting details with the orderType : %s", orderType), e);
		}
	}

	@Override
	public Response<Object> getDetailsOfBuyByfkId(Long peerToPeerExchangeId) {
		try {
			Optional<P2PAdvertisement> data = createAdvertisementDao.findById(peerToPeerExchangeId);
			if (data.isPresent()) {

				Map<String, Object> map = new HashMap<>();
				map.put("maxValue", data.get().getMaxValue());
				map.put("minValue", data.get().getMinValue());
				map.put("price", data.get().getPrice());

				map.put("coinType", data.get().getCryptoCoin());
				map.put("valueInCoinType", data.get().getFiatCoin());
				map.put("paymentMode", data.get().getPaymentType());
				map.put("paymentWindow", data.get().getPaymentWindow());
				map.put("userName", data.get().getUserName());
				map.put("termsOfTrade", data.get().getTermsOfTrade());
				return new Response<>(SUCCESS_CODE, SUCCESS, map);
			} else
				return new Response<>(203, DATA_NOT_FOUND);
		} catch (Exception e) {
			LOGGER.catching(e);
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> getDetailsOfSellByfkId(Long peerToPeerExchangeId) {
		try {
			Map<String, Object> responseMap = new HashMap<>();
			List<Map<String, Object>> list = new ArrayList<>();
			Optional<P2PAdvertisement> data = createAdvertisementDao.findById(peerToPeerExchangeId);
			if (data.isPresent()) {

				Map<String, Object> map = new HashMap<>();
				map.put("maxValue", data.get().getMaxValue());
				map.put("minValue", data.get().getMinValue());
				map.put("price", data.get().getPrice());
				map.put("coinType", data.get().getCryptoCoin());
				map.put("valueInCoinType", data.get().getFiatCoin());
				map.put("paymentMode", data.get().getPaymentType());
				map.put("paymentWindow", data.get().getPaymentWindow());
				map.put("userName", data.get().getUserName());
				map.put("termsOfTrade", data.get().getTermsOfTrade());
				list.add(map);
				return new Response<>(SUCCESS_CODE, ADDVERTISEMENT_DATA_GET_SUCCESSFULLY, map);
			} else {
				responseMap.put("statusCode", 203);
				responseMap.put("message", DATA_NOT_FOUND);
				responseMap.put("totaldata", null);
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
			}
		} catch (Exception e) {
			LOGGER.catching(e);
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> getBitCoinInUSD(BigDecimal amount, Long peerToPeerExchangeId, BigDecimal noOfCoins) {
		try {
			Optional<P2PAdvertisement> data = createAdvertisementDao.findById(peerToPeerExchangeId);
			if (data.isPresent()) {

				if (amount != null) {
					if (data.get().getMaxValue().compareTo(amount) >= 0
							&& data.get().getMinValue().compareTo(amount) <= 0) {

						return new Response<>(SUCCESS_CODE, CONVERTED_DATA_GET_SUCCESSFULLY);

					} else {
						return new Response<>(201, AMOUNT_SHOULD_IN_RANGE);
					}
				} else {

					return new Response<>(SUCCESS_CODE, CONVERTED_DATA_GET_SUCCESSFULLY);
				}
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);

		} catch (Exception e) {
			LOGGER.catching(e);
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> sendTradeRequest(Long userId, Long peerToPeerExchangeId, BigDecimal amountInRange) {

		Optional<P2PAdvertisement> data = createAdvertisementDao
				.findByPeerToPeerExchangeIdAndIsDeletedFalse(peerToPeerExchangeId);
		Optional<Trading> tradeData = tradingDao.findBypeerToPeerExchangeId(peerToPeerExchangeId);
		if (!data.isPresent())
			return new Response<>(201, ADDVERTISEMENT_NOT_FOUND);
		if (userId.equals(data.get().getFkUserId())) {
			return new Response<>(208, NOT_APPLICABLE_FOR_TRADE_REQUEST);

		}
		if (tradeData.isPresent())
			return new Response<>(201, SEND_TRADE_REQUEST_ALREADY, tradeData);
		Trading trading = new Trading();
		Response<UserEmailAndNameDto> emailAndName = userClient.getEmailAndName(userId);

		Response<KycStatus> value = userClient.getKycStatus(userId);
		if (value.getData() != null) {
			if (value.getData().equals(KycStatus.ACCEPTED)) {

				List<Map<String, Object>> list = new ArrayList<>();
				if (data.get().getMaxValue().compareTo(amountInRange) >= 0
						&& data.get().getMinValue().compareTo(amountInRange) <= 0) {
					BigDecimal d3 = amountInRange.divide(data.get().getPrice(), 40, RoundingMode.HALF_UP);
					trading.setStatusType(StatusType.PENDING);
					String randomToken = UUID.randomUUID().toString().replace("-", "");
					trading.setTradeId(randomToken);
					trading.setPaymentType(data.get().getPaymentType());
					Map<String, Object> map1 = new HashMap<>();
					map1.put("tradeId", randomToken);
					map1.put("tradingPrice", amountInRange);
					map1.put("timeStamp", data.get().getCreationTime());
					map1.put("noOfCoins", d3);
					map1.put("fiat", data.get().getFiatCoin());
					map1.put("price", data.get().getPrice());
					map1.put("tradeStatus", TradeStatus.PENDING);
					map1.put("notificationStatus", "Notification Send For Trading Request");
					map1.put("tradePartner", emailAndName.getData().getName());
					map1.put("fromUserId", userId);
					map1.put("toUserId", data.get().getFkUserId());
					map1.put("paymentWindow", data.get().getPaymentWindow());
					map1.put("isSeen", false);
					map1.put("peerToPeerExchangeId", peerToPeerExchangeId);
					list.add(map1);
					if (data.get().getOrderType() == OrderType.SELL) {
						trading.setBuyer(emailAndName.getData().getName());
						trading.setSeller(data.get().getUserName());
						trading.setFiatCoin(data.get().getFiatCoin());
						trading.setCountry(data.get().getCountry());
						trading.setTotalBTC(d3);
						trading.setTotalPrice(amountInRange);
						trading.setTradeAmount(amountInRange);
						trading.setType(data.get().getOrderType());
						trading.setPeerToPeerExchangeId(peerToPeerExchangeId);
						trading.setTradeStatus(TradeStatus.PENDING);
						trading.setPartnerId(userId);
						trading.setTradingPartner(emailAndName.getData().getName());
						trading.setTransactionStatus(TransactionStatus.PENDING);
						trading.setCryptoCoin(data.get().getCryptoCoin());
						trading.setFkUserId(data.get().getFkUserId());
						trading.setPaymentWindow(data.get().getPaymentWindow());
						trading.setBuyer(data.get().getUserName());
						map1.put("orderType", "BUY");
						Response<GetBalanceResponseDto> value8 = walletClient.getBalance(data.get().getCryptoCoin(),
								data.get().getFkUserId());

						if (value8.getData() != null) {
							GetBalanceResponseDto data4 = value8.getData();
							BigDecimal walletBalance = data4.getWalletBalance();
							Escrow escrow = new Escrow();
							if (walletBalance.compareTo(d3) > 0) {
								BigDecimal restWalletBalance = walletBalance.subtract(d3);
								walletClient.updateWallet(restWalletBalance, data.get().getFkUserId(),
										data.get().getCryptoCoin());
								escrow.setWalletBalance(restWalletBalance);
								BigDecimal c = getChargeFromAdmin(data.get().getCryptoCoin());
								if (d3.compareTo(c) > 0) {
									escrow.setBlockedBalance(d3);
									trading.setTradeFee(c);
									escrow.setTradeFees(c);
									trading.setPaybleAmount(amountInRange);
									escrow.setPaybleAmount(amountInRange);
									escrow.setCoinQuantity(d3);
									escrow.setPeerToPeerExchangeId(peerToPeerExchangeId);
									escrow.setFkUserId(userId);
									escrow.setStatusType(StatusType.PENDING);
									escrow.setTradeId(trading.getTradeId());
									escrow.setTradingPartner(trading.getTradingPartner());
									escrow.setTransactionStatus(trading.getTransactionStatus());
									escrow.setCoinName(data.get().getCryptoCoin());
									escrow.setBlockBalanceUserId(data.get().getFkUserId());
									tradingDao.save(trading);
									escrowDao.save(escrow);
									data.get().setExchangeStatusType(ExchangeStatusType.DISABLED);
									data.get().setStatusType(StatusType.CLOSED);
									createAdvertisementDao.save(data.get());
									Map<String, String> setData = new HashMap<>();
									setData.put(EmailConstants.EMAIL_TOKEN, data.get().getEmail());
									setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(d3));
									setData.put(EmailConstants.PAYMENT_WINDOW_TOKEN,
											String.valueOf(data.get().getPaymentWindow()));
									String jsonStr = GSON.toJson(map1);
									setData.put(EmailConstants.DATA_TOKEN, jsonStr);
									EmailDto emailDto = new EmailDto(data.get().getFkUserId(), "trade_request",
											data.get().getEmail(), setData);

									notificationClient.sendNotification(emailDto);

									return new Response<>(SUCCESS_CODE, SEND_TRADE_REQUEST_SUCCESSFULLY, map1);

								} else {
									return new Response<>(207, TRADE_FEE_LESS_THAN_BLOCKED_BALANCE);
								}
							} else {
								return new Response<>(207, AMOUNT_SHOULD_LESS_THAN_WALLET_BALANCE);

							}
						} else {
							return new Response<>(512, WALLET_BALANCE_SHOULD_PRESENT);
						}
					} else {
						trading.setSeller(emailAndName.getData().getName());
						trading.setBuyer(data.get().getUserName());
						trading.setSeller(data.get().getUserName());
						trading.setFiatCoin(data.get().getFiatCoin());
						trading.setTotalBTC(d3);
						trading.setCountry(data.get().getCountry());
						trading.setTotalPrice(amountInRange);
						trading.setTradeAmount(amountInRange);
						trading.setType(data.get().getOrderType());
						trading.setPeerToPeerExchangeId(peerToPeerExchangeId);
						trading.setTradeStatus(TradeStatus.PENDING);
						trading.setPartnerId(data.get().getFkUserId());
						trading.setPaymentWindow(data.get().getPaymentWindow());
						trading.setTradingPartner(emailAndName.getData().getName());
						trading.setTransactionStatus(TransactionStatus.PENDING);
						trading.setCryptoCoin(data.get().getCryptoCoin());
						trading.setFkUserId(userId);
						Response<GetBalanceResponseDto> value8 = walletClient.getBalance(data.get().getCryptoCoin(),
								userId);
						map1.put("orderType", "SELL");
						if (value8.getData() != null) {
							GetBalanceResponseDto data5 = value8.getData();
							BigDecimal walletBalance5 = data5.getWalletBalance();
							Escrow escrow5 = new Escrow();
							if (walletBalance5.compareTo(d3) > 0) {
								BigDecimal restWalletBalance5 = walletBalance5.subtract(d3);
								walletClient.updateWallet(restWalletBalance5, userId, data.get().getCryptoCoin());

								escrow5.setWalletBalance(restWalletBalance5);
								BigDecimal c = getChargeFromAdmin(data.get().getCryptoCoin());

								if (amountInRange.compareTo(c) > 0) {
									escrow5.setBlockedBalance(d3);
									trading.setTradeFee(c);
									escrow5.setTradeFees(c);
									trading.setPaybleAmount(amountInRange);
									escrow5.setPaybleAmount(amountInRange);
									escrow5.setCoinQuantity(d3);
									escrow5.setPeerToPeerExchangeId(peerToPeerExchangeId);
									escrow5.setFkUserId(userId);
									escrow5.setStatusType(StatusType.PENDING);
									escrow5.setTradeId(trading.getTradeId());
									escrow5.setTradingPartner(trading.getTradingPartner());
									escrow5.setTransactionStatus(trading.getTransactionStatus());
									escrow5.setCoinName(data.get().getCryptoCoin());
									escrow5.setBlockBalanceUserId(data.get().getFkUserId());
									tradingDao.save(trading);
									escrowDao.save(escrow5);
									data.get().setStatusType(StatusType.CLOSED);
									data.get().setExchangeStatusType(ExchangeStatusType.DISABLED);
									createAdvertisementDao.save(data.get());
									Map<String, String> setData = new HashMap<>();
									setData.put(EmailConstants.EMAIL_TOKEN, data.get().getEmail());
									setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(d3));
									setData.put(EmailConstants.PAYMENT_WINDOW_TOKEN,
											String.valueOf(data.get().getPaymentWindow()));
									String jsonStr = GSON.toJson(map1);
									setData.put(EmailConstants.DATA_TOKEN, jsonStr);
									EmailDto emailDto = new EmailDto(data.get().getFkUserId(), "trade_request",
											data.get().getEmail(), setData);
									notificationClient.sendNotification(emailDto);
									// notificationClient.sendNotificationForExchange(map1);
									return new Response<>(SUCCESS_CODE, SEND_TRADE_REQUEST_SUCCESSFULLY, map1);
								} else {
									return new Response<>(207, TRADE_FEE_LESS_THAN_BLOCKED_BALANCE);
								}
							} else {
								return new Response<>(207, AMOUNT_SHOULD_LESS_THAN_WALLET_BALANCE);
							}
						} else {
							return new Response<>(512, WALLET_BALANCE_SHOULD_PRESENT);
						}
					}
				} else {
					return new Response<>(513, AMOUNT_SHOULD_IN_RANGE);
				}
			} else {
				return new Response<>(514, CHECK_YOUR_KYC_STATUS);
			}
		}
		return new Response<>(515, KYC_NOT_FOUND);

	}

	@Override
	public Response<Object> sendMessageAfterPressCancelButton(Long userId, String tradeId, String cancelReason) {
		try {
			Optional<Escrow> data = escrowDao.findByTradeId(tradeId);
			Response<UserEmailAndNameDto> emailAndName = userClient.getEmailAndName(userId);
			Map<String, Object> map1 = new HashMap<>();
			LOGGER.debug(data);
			Optional<Trading> data1 = tradingDao.findByTradeId(tradeId);
			if (data1.isPresent()) {
				Response<UserEmailAndNameDto> fkEmailAndName = userClient.getEmailAndName(data1.get().getFkUserId());
				if (data1.get().getTradeStatus().equals(TradeStatus.CANCEL))
					return new Response<>(SUCCESS_CODE, "Trade Allready Cancel", data1.get().getTradeStatus());
				if (data.isPresent()) {
					Response<GetBalanceResponseDto> value4 = walletClient.getBalance(data1.get().getCryptoCoin(),
							data1.get().getFkUserId());
					GetBalanceResponseDto data4 = value4.getData();
					BigDecimal walletBalance = data4.getWalletBalance();
					BigDecimal returnBallance = walletBalance.add(data.get().getBlockedBalance());
					walletClient.updateWallet(returnBallance, data1.get().getFkUserId(), data1.get().getCryptoCoin());
					data.get().setBlockedBalance(BigDecimal.ZERO);
					data.get().setWalletBalance(returnBallance);
					data.get().setStatusType(StatusType.CANCEL);
					escrowDao.save(data.get());
					data1.get().setStatusType(StatusType.CANCEL);

					data1.get().setTradeStatus(TradeStatus.CANCEL);
					data1.get().setCancelReason(cancelReason);
					map1.put("tradeStatus", TradeStatus.CANCEL);
					map1.put("fromUserId", userId);
					map1.put("toUserId", data1.get().getFkUserId());
					map1.put("tradeId", data1.get().getTradeId());
					map1.put("notificationStatus", "Notification Send For Cancel Trade");
					map1.put("isSeen", false);

					map1.put("tradePartner", emailAndName.getData().getName());

					tradingDao.save(data1.get());
					Map<String, String> setData = new HashMap<>();
					setData.put(EmailConstants.EMAIL_TOKEN, fkEmailAndName.getData().getEmail());
					setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(data1.get().getTotalBTC()));
					setData.put(EmailConstants.LOGGED_IN_TOKEN, emailAndName.getData().getEmail());
					setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
					String jsonStr = GSON.toJson(map1);
					setData.put(EmailConstants.DATA_TOKEN, jsonStr);
					EmailDto emailDto = new EmailDto(data.get().getFkUserId(), "trade_request_cancel",
							fkEmailAndName.getData().getEmail(), setData);
					notificationClient.sendNotification(emailDto);

					// notificationClient.sendNotificationForExchange(map1);

					return new Response<>(SUCCESS_CODE, CANCEL_TRADE);
				} else
					return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, TRADE_DATA_NOT_FOUND);

		} catch (Exception e) {
			LOGGER.catching(e);
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> sendMessageAfterPressPaidButton(String tradeId, Long userId) {
		try {
			Response<UserEmailAndNameDto> emailAndName = userClient.getEmailAndName(userId);
			Map<String, Object> map1 = new HashMap<>();
			Optional<Trading> data1 = tradingDao.findByTradeId(tradeId);
			if (data1.isPresent()) {
				Response<UserEmailAndNameDto> fkEmailAndName = userClient.getEmailAndName(data1.get().getFkUserId());
				if (data1.get().getTransactionStatus() == TransactionStatus.PAID)
					return new Response<>(206, "Amount AllReady Paid", data1);
				data1.get().setTransactionStatus(TransactionStatus.PAID);
				data1.get().setTradeStatus(TradeStatus.PAID);
				tradingDao.save(data1.get());
				map1.put("tradeStatus", TradeStatus.PAID);
				map1.put("fromUserId", userId);
				map1.put("toUserId", data1.get().getFkUserId());
				map1.put("tradeId", data1.get().getTradeId());
				map1.put("isSeen", false);

				map1.put("tradePartner", emailAndName.getData().getName());
				map1.put("notificationStatus", "Notification Send For Paid Amount For Coins");

				Map<String, String> setData = new HashMap<>();
				setData.put(EmailConstants.EMAIL_TOKEN, fkEmailAndName.getData().getEmail());
				setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(data1.get().getTradeAmount()));
				setData.put(EmailConstants.CURRENCY_TOKEN, String.valueOf(data1.get().getFiatCoin()));
				setData.put(EmailConstants.LOGGED_IN_TOKEN, emailAndName.getData().getEmail());
				setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
				String jsonStr = GSON.toJson(map1);
				setData.put(EmailConstants.DATA_TOKEN, jsonStr);
				EmailDto emailDto = new EmailDto(data1.get().getFkUserId(), "trade_request_paid",
						fkEmailAndName.getData().getEmail(), setData);
				notificationClient.sendNotification(emailDto);

				// notificationClient.sendNotificationForExchange(map1);

				return new Response<>(SUCCESS_CODE, AMOUNT_PAID_SUCCESSFULLY);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {
			LOGGER.catching(e);
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> setMarketPrice(MarketPriceDto marketpricedto) {
		try {
			Optional<MarketPrice> marketPrice = marketPriceDao.findByMarketPriceId(1L);
			if (marketPrice.isPresent()) {
				marketPriceDao.save(marketPrice.get());
				P2PAdvertisement p = new P2PAdvertisement();
				createAdvertisementDao.save(p);
				return new Response<>(SUCCESS_CODE, SUCCESS, marketPrice.get().getPriceInUsd());
			} else {
				return new Response<>(201, "Failure");
			}
		} catch (Exception e) {
			LOGGER.catching(e);
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> forReleaseBitcoins(String tradeId, Long userId) {
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
			LOGGER.debug(data1);
			if (data1.get().getTradeStatus() == TradeStatus.COMPLETE)
				return new Response<>(201, "RELEASED_COINS_ALLREADY", data1.get().getTradeStatus());

			Response<UserEmailAndNameDto> emailAndName2 = userClient.getEmailAndName(data3.get().getFkUserId());

			Response<GetBalanceResponseDto> value5 = walletClient.getBalance(data1.get().getCryptoCoin(),
					data1.get().getPartnerId());
			BigDecimal BallanceOnWallet2 = value5.getData().getWalletBalance();
			BigDecimal releaseAmount3 = BallanceOnWallet2.add(data1.get().getTotalBTC());
			BigDecimal releaseAmountFinal = releaseAmount3.subtract(data.get().getTradeFees());
			// walletClient.getStorageDetailsCoinHotForP2p(data3.get().getCryptoCoin(),
			// "HOT", data.get().getTradeFees());

			if (data1.get().getTransactionStatus() == TransactionStatus.PAID
					&& data1.get().getTradeStatus() == TradeStatus.PAID) {
				walletClient.updateWallet(releaseAmountFinal, data1.get().getPartnerId(), data1.get().getCryptoCoin());
				// walletClient.getStorageDetailsCoinHotForP2p(data3.get().getCryptoCoin(),
				// "HOT",
				// data1.get().getTradeFee());
				data.get().setBlockedBalance(BigDecimal.ZERO);
				data1.get().setTradeStatus(TradeStatus.COMPLETE);
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
				return new Response<>(COINS_NOT_FOUND_CODE, "First Of All Buyer Has to Paid");
			}
		} catch (Exception e) {
			LOGGER.catching(e);
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Map<String, Object>> getAllTradeDetailsList(TradeStatus tradeStatus, Long userId, Integer page,
			Integer pageSize) {
		try {
			Page<Trading> getBuyStatusByUserIdList = null;
			Page<Trading> getBuyStatusByPartnerIdList = null;
			Page<Trading> getBuyStatusList = null;
			Map<String, Object> responseMap = new HashMap<>();
			Long getTotalCount = 0L;
			if (tradeStatus != null) {
				getBuyStatusByUserIdList = tradingDao.findByTradeStatusAndFkUserId(tradeStatus, userId,
						PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
				getBuyStatusByPartnerIdList = tradingDao.findByTradeStatusAndPartnerId(tradeStatus, userId,
						PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
				if (getBuyStatusByPartnerIdList.hasContent()) {
					for (Trading t : getBuyStatusByPartnerIdList) {
						getBuyStatusByPartnerIdList = tradingDao.findByTradeStatusAndFkUserIdAndPartnerId(tradeStatus,
								t.getFkUserId(), t.getPartnerId(),
								PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
					}
					getTotalCount = tradingDao.countByTradeStatusAndPartnerId(tradeStatus, userId);
				} else if (getBuyStatusByUserIdList.hasContent()) {
					for (Trading t : getBuyStatusByUserIdList) {
						getBuyStatusByUserIdList = tradingDao.findByTradeStatusAndFkUserIdAndPartnerId(tradeStatus,
								t.getFkUserId(), t.getPartnerId(),
								PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
					}
					getTotalCount = tradingDao.countByTradeStatusAndFkUserId(tradeStatus, userId);
				}
			} else {
				getBuyStatusList = tradingDao.findAllByFkUserIdOrPartnerId(userId, userId,
						PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
			}
			if (getBuyStatusByUserIdList != null) {
				if (getBuyStatusByUserIdList.hasContent()) {
					responseMap.put(RESULT_LIST, getBuyStatusByUserIdList.getContent());
				} else {
					responseMap.put(RESULT_LIST, getBuyStatusByPartnerIdList.getContent());
				}
			}
			if (getTotalCount > 0) {
				responseMap.put("totalCount", getTotalCount);
			} else {
				responseMap.put("totalCount", getBuyStatusList);
			}
			return new Response<>(SUCCESS_CODE, TRADING_DATA_GET_SUCCESSFULLY, responseMap);

		} catch (Exception e) {
			LOGGER.catching(e);
			throw new StatusTypeNotFoundException(
					String.format("StatusType not found for getting details with the StatusType : %s", tradeStatus), e);
		}
	}

	@Override
	public Response<Map<String, Object>> getAddvertisementListThroughAmount(BigDecimal amount, PaymentType paymentType,
			String currency, String country, Integer page, Integer pageSize) {
		try {
			Page<P2PAdvertisement> getByAmountList;
			Long getTotalCount;
			if (amount != null && paymentType != null) {
				getByAmountList = createAdvertisementDao.findByMaxValueLessThanEqualAndPaymentTypeAndFiatCoinAndCountry(
						amount, paymentType, currency, country,
						PageRequest.of(page, pageSize, Direction.DESC, "peerToPeerExchangeId"));
				getTotalCount = createAdvertisementDao.countByMaxValueLessThanEqual(amount);
			} else {
				getByAmountList = createAdvertisementDao
						.findAll(PageRequest.of(page, pageSize, Direction.DESC, "peerToPeerExchangeId"));
				getTotalCount = createAdvertisementDao.count();
			}
			if (getByAmountList.hasContent()) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put(RESULT_LIST, getByAmountList.getContent());
				responseMap.put(TOTAL_COUNT, getTotalCount);
				return new Response<>(SUCCESS_CODE, TRADING_DATA_GET_SUCCESSFULLY, responseMap);
			} else {
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
			}
		} catch (Exception e) {
			LOGGER.catching(e);
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> forStatusChecking(Long peerToPeerExchangeId) {
		try {
			Optional<Trading> data = tradingDao.findBypeerToPeerExchangeId(peerToPeerExchangeId);
			Optional<P2PAdvertisement> data2 = createAdvertisementDao.findById(peerToPeerExchangeId);
			Map<String, Object> map1 = new HashMap<>();
			if (data.isPresent() && data2.isPresent()) {
				DateTime createdTime = new DateTime(data.get().getCreationTime());
				Date time = createdTime.toDate();
				int timeDiff = Seconds.secondsBetween(createdTime, DateTime.now()).getSeconds();
				map1.put("paidStatus", data.get().getTransactionStatus());
				map1.put("coinReleaseStatus", data.get().getTradeStatus());
				if (timeDiff != 0) {
					data.get().setStatusType(StatusType.TRADE_RUNNING);
					data2.get().setTradeStatus(TradeStatus.TRADE_RUNNING);
					map1.put("timeStamp", time);
					map1.put("tradeId", data.get().getTradeId());
					map1.put("price", data2.get().getPrice());
					return new Response<>(200, "Order Is Running On", map1);
				} else
					return new Response<>(205, "You Can Excute This Trade", map1);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {
			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> forStatusDisputed(Long userId, String tradeId, DisputeStatus disputeStatus) {
		try {
			Response<UserEmailAndNameDto> emailAndName = userClient.getEmailAndName(userId);
			Map<String, Object> map1 = new HashMap<>();
			Optional<Trading> data = tradingDao.findByTradeId(tradeId);
			if (data.isPresent()) {
				Response<UserEmailAndNameDto> fkEmailAndName = userClient.getEmailAndName(data.get().getFkUserId());
				if (data.get().getTradeStatus() == TradeStatus.DISPUTE)
					return new Response<>(205, "ALREADY_DISPUTED", data);

				if (data.get().getTradeStatus() == TradeStatus.PAID
						&& data.get().getTransactionStatus() == TransactionStatus.PAID) {
					data.get().setTradeStatus(TradeStatus.DISPUTE);
					data.get().setDisputeStatus(disputeStatus);
					data.get().setDisputer(emailAndName.getData().getName());
					data.get().setDisputeDate(new Date());

					String randomToken = UUID.randomUUID().toString().replace("-", "");
					data.get().setDisputeId(randomToken);
					map1.put("tradeStatus", TradeStatus.DISPUTE);
					map1.put("fromUserId", userId);
					map1.put("toUserId", data.get().getFkUserId());
					map1.put("tradeId", tradeId);
					map1.put("tradePartner", emailAndName.getData().getName());
					map1.put("notificationStatus", "Notification Send For Dispute Trade");

					tradingDao.save(data.get());
					Map<String, String> setData = new HashMap<>();
					setData.put(EmailConstants.EMAIL_TOKEN, fkEmailAndName.getData().getEmail());
					setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(data.get().getTotalBTC()));
					setData.put(EmailConstants.LOGGED_IN_TOKEN, String.valueOf(emailAndName.getData().getEmail()));
					setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
					String jsonStr = GSON.toJson(map1);
					setData.put(EmailConstants.DATA_TOKEN, jsonStr);
					EmailDto emailDto = new EmailDto(data.get().getFkUserId(), "disputed_trade",
							fkEmailAndName.getData().getEmail(), setData);
					notificationClient.sendNotification(emailDto);
					// notificationClient.sendNotificationForExchange(map1);

					return new Response<>(200, TRADE_DISPUTED_SUCCESSFULLY, data);
				} else
					return new Response<>(203, TRADE_SHOULD_PAID_STATUS);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {

			return new Response<>(SOMETHING_WRONG_CODE, SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> getTradeStatus(String tradeId) {
		Optional<Trading> tradingStatus = tradingDao.findByTradeId(tradeId);
		return new Response<Object>(200, "Success", tradingStatus);
	}

	@Override
	public Response<Map<String, Object>> getOpenTradeDetails(Long userId, Integer page, Integer pageSize) {
		try {
			Page<P2PAdvertisement> getBuyOrderList;
			Long getTotalCount;
			if (page != null && pageSize != null) {
				getBuyOrderList = createAdvertisementDao.findAllByFkUserId(userId,
						PageRequest.of(page, pageSize, Direction.DESC, "peerToPeerExchangeId"));
				getTotalCount = createAdvertisementDao.countByFkUserId(userId);
				if (getBuyOrderList.hasContent()) {
					Map<String, Object> responseMap = new HashMap<>();
					responseMap.put(RESULT_LIST, getBuyOrderList.getContent());
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, ADDVERTISEMENT_DATA_GET_SUCCESSFULLY, responseMap);
				} else
					return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
			} else {
				List<P2PAdvertisement> getBuyOrderList2 = createAdvertisementDao.findAllByFkUserId(userId);
				getTotalCount = createAdvertisementDao.countByFkUserId(userId);
				if (!getBuyOrderList2.isEmpty()) {
					Map<String, Object> responseMap = new HashMap<>();
					responseMap.put(RESULT_LIST, getBuyOrderList2);
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, ADDVERTISEMENT_DATA_GET_SUCCESSFULLY, responseMap);
				} else
					return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
			}

		} catch (

		Exception e) {
			LOGGER.catching(e);
			throw new OrderTypeNotFoundException(
					String.format("OrderType not found for getting details with the UserId : %s", userId), e);
		}
	}

	@Override
	public Response<Object> sendTradeRequestUsingRestrictAmount(Long userId, Long peerToPeerExchangeId,
			BigDecimal restrictAmount) {
		Optional<P2PAdvertisement> data = createAdvertisementDao
				.findByPeerToPeerExchangeIdAndIsDeletedFalse(peerToPeerExchangeId);
		Optional<Trading> tradeData = tradingDao.findBypeerToPeerExchangeId(peerToPeerExchangeId);
		if (!data.isPresent())
			return new Response<>(201, ADDVERTISEMENT_NOT_FOUND);
		if (userId.equals(data.get().getFkUserId())) {

			return new Response<>(208, NOT_APPLICABLE_FOR_TRADE_REQUEST);

		}
		if (tradeData.isPresent())
			return new Response<>(201, SEND_TRADE_REQUEST_ALREADY, tradeData);
		Trading trading = new Trading();
		Response<UserEmailAndNameDto> emailAndName = userClient.getEmailAndName(userId);

		Response<KycStatus> value = userClient.getKycStatus(userId);
		if (value.getData() != null) {
			if (value.getData().equals(KycStatus.ACCEPTED)) {
				BigDecimal d3 = restrictAmount.divide(data.get().getPrice(), 40, RoundingMode.HALF_UP);
				List<Map<String, Object>> list = new ArrayList<>();
				trading.setStatusType(StatusType.PENDING);
				String randomToken = UUID.randomUUID().toString().replace("-", "");
				trading.setTradeId(randomToken);
				trading.setPaymentType(data.get().getPaymentType());
				Map<String, Object> map1 = new HashMap<>();
				map1.put("tradeId", randomToken);
				map1.put("tradingPrice", restrictAmount);
				map1.put("timeStamp", data.get().getCreationTime());
				map1.put("noOfCoins", d3);
				map1.put("price", data.get().getPrice());
				map1.put("tradeStatus", TradeStatus.PENDING);
				map1.put("notificationStatus", "Notification Send For Trading Request");
				map1.put("tradePartner", emailAndName.getData().getName());
				map1.put("fromUserId", userId);
				map1.put("toUserId", data.get().getFkUserId());
				map1.put("paymentWindow", data.get().getPaymentWindow());
				map1.put("isSeen", false);
				map1.put("peerToPeerExchangeId", peerToPeerExchangeId);

				list.add(map1);
				if (data.get().getOrderType() == OrderType.SELL) {
					trading.setBuyer(emailAndName.getData().getName());
					trading.setSeller(data.get().getUserName());
					trading.setFiatCoin(data.get().getFiatCoin());
					trading.setCountry(data.get().getCountry());
					trading.setTotalBTC(d3);
					trading.setTotalPrice(restrictAmount);
					trading.setTradeAmount(restrictAmount);
					trading.setType(data.get().getOrderType());
					trading.setPeerToPeerExchangeId(peerToPeerExchangeId);
					trading.setTradeStatus(TradeStatus.PENDING);
					trading.setPartnerId(userId);
					trading.setTradingPartner(emailAndName.getData().getName());
					trading.setTransactionStatus(TransactionStatus.PENDING);
					trading.setCryptoCoin(data.get().getCryptoCoin());
					trading.setFkUserId(data.get().getFkUserId());
					trading.setPaymentWindow(data.get().getPaymentWindow());
					trading.setBuyer(data.get().getUserName());
					map1.put("orderType", "BUY");
					Response<GetBalanceResponseDto> value8 = walletClient.getBalance(data.get().getCryptoCoin(),
							data.get().getFkUserId());

					if (value8.getData() != null) {
						GetBalanceResponseDto data4 = value8.getData();
						BigDecimal walletBalance = data4.getWalletBalance();
						Escrow escrow = new Escrow();
						if (walletBalance.compareTo(d3) > 0) {
							BigDecimal restWalletBalance = walletBalance.subtract(d3);
							walletClient.updateWallet(restWalletBalance, data.get().getFkUserId(),
									data.get().getCryptoCoin());
							escrow.setWalletBalance(restWalletBalance);
							BigDecimal c = getChargeFromAdmin(data.get().getCryptoCoin());
							if (d3.compareTo(c) > 0) {
								escrow.setBlockedBalance(d3);
								trading.setTradeFee(c);
								escrow.setTradeFees(c);
								trading.setPaybleAmount(restrictAmount);
								escrow.setPaybleAmount(restrictAmount);
								escrow.setCoinQuantity(d3);
								escrow.setPeerToPeerExchangeId(peerToPeerExchangeId);
								escrow.setFkUserId(userId);
								escrow.setStatusType(StatusType.PENDING);
								escrow.setTradeId(trading.getTradeId());
								escrow.setTradingPartner(trading.getTradingPartner());
								escrow.setTransactionStatus(trading.getTransactionStatus());
								tradingDao.save(trading);
								escrowDao.save(escrow);
								data.get().setExchangeStatusType(ExchangeStatusType.DISABLED);
								data.get().setStatusType(StatusType.CLOSED);
								createAdvertisementDao.save(data.get());
								Map<String, String> setData = new HashMap<>();
								setData.put(EmailConstants.EMAIL_TOKEN, data.get().getEmail());
								setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(d3));
								setData.put(EmailConstants.PAYMENT_WINDOW_TOKEN,
										String.valueOf(data.get().getPaymentWindow()));
								String jsonStr = GSON.toJson(map1);
								setData.put(EmailConstants.DATA_TOKEN, jsonStr);
								EmailDto emailDto = new EmailDto(data.get().getFkUserId(), "trade_request",
										data.get().getEmail(), setData);

								notificationClient.sendNotification(emailDto);

								return new Response<>(SUCCESS_CODE, SEND_TRADE_REQUEST_SUCCESSFULLY, map1);

							} else {
								return new Response<>(207, TRADE_FEE_LESS_THAN_BLOCKED_BALANCE);
							}
						} else {
							return new Response<>(207, AMOUNT_SHOULD_LESS_THAN_WALLET_BALANCE);

						}
					} else {
						return new Response<>(512, WALLET_BALANCE_SHOULD_PRESENT);
					}
				} else {
					trading.setSeller(emailAndName.getData().getName());
					trading.setBuyer(data.get().getUserName());
					trading.setSeller(data.get().getUserName());
					trading.setFiatCoin(data.get().getFiatCoin());
					trading.setTotalBTC(d3);
					trading.setCountry(data.get().getCountry());
					trading.setTotalPrice(restrictAmount);
					trading.setTradeAmount(restrictAmount);
					trading.setType(data.get().getOrderType());
					trading.setPeerToPeerExchangeId(peerToPeerExchangeId);
					trading.setTradeStatus(TradeStatus.PENDING);
					trading.setPartnerId(data.get().getFkUserId());
					trading.setPaymentWindow(data.get().getPaymentWindow());
					trading.setTradingPartner(emailAndName.getData().getName());
					trading.setTransactionStatus(TransactionStatus.PENDING);
					trading.setCryptoCoin(data.get().getCryptoCoin());
					trading.setFkUserId(userId);
					Response<GetBalanceResponseDto> value8 = walletClient.getBalance(data.get().getCryptoCoin(),
							userId);
					map1.put("orderType", "SELL");
					if (value8.getData() != null) {
						GetBalanceResponseDto data5 = value8.getData();
						BigDecimal walletBalance5 = data5.getWalletBalance();
						Escrow escrow5 = new Escrow();
						if (walletBalance5.compareTo(d3) > 0) {
							BigDecimal restWalletBalance5 = walletBalance5.subtract(d3);
							walletClient.updateWallet(restWalletBalance5, userId, data.get().getCryptoCoin());
							escrow5.setWalletBalance(restWalletBalance5);
							BigDecimal c = getChargeFromAdmin(data.get().getCryptoCoin());
							if (d3.compareTo(c) > 0) {
								escrow5.setBlockedBalance(d3);
								trading.setTradeFee(c);
								escrow5.setTradeFees(c);
								trading.setPaybleAmount(restrictAmount);
								escrow5.setPaybleAmount(restrictAmount);
								escrow5.setCoinQuantity(d3);
								escrow5.setPeerToPeerExchangeId(peerToPeerExchangeId);
								escrow5.setFkUserId(userId);
								escrow5.setStatusType(StatusType.PENDING);
								escrow5.setTradeId(trading.getTradeId());
								escrow5.setTradingPartner(trading.getTradingPartner());
								escrow5.setTransactionStatus(trading.getTransactionStatus());
								tradingDao.save(trading);
								escrowDao.save(escrow5);
								data.get().setStatusType(StatusType.CLOSED);
								data.get().setExchangeStatusType(ExchangeStatusType.DISABLED);
								createAdvertisementDao.save(data.get());
								Map<String, String> setData = new HashMap<>();
								setData.put(EmailConstants.EMAIL_TOKEN, data.get().getEmail());
								setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(d3));
								setData.put(EmailConstants.PAYMENT_WINDOW_TOKEN,
										String.valueOf(data.get().getPaymentWindow()));
								String jsonStr = GSON.toJson(map1);
								setData.put(EmailConstants.DATA_TOKEN, jsonStr);
								EmailDto emailDto = new EmailDto(data.get().getFkUserId(), "trade_request",
										data.get().getEmail(), setData);
								notificationClient.sendNotification(emailDto);
								// notificationClient.sendNotificationForExchange(map1);
								return new Response<>(SUCCESS_CODE, SEND_TRADE_REQUEST_SUCCESSFULLY, map1);
							} else {
								return new Response<>(207, TRADE_FEE_LESS_THAN_BLOCKED_BALANCE);
							}
						} else {
							return new Response<>(207, AMOUNT_SHOULD_LESS_THAN_WALLET_BALANCE);
						}
					} else {
						return new Response<>(512, WALLET_BALANCE_SHOULD_PRESENT);
					}
				}
				/*
				 * } else { return new Response<>(513, AMOUNT_SHOULD_IN_RANGE); }
				 */
			} else {
				return new Response<>(514, CHECK_YOUR_KYC_STATUS);
			}
		}
		return new Response<>(515, KYC_NOT_FOUND);
	}

	private BigDecimal getChargeFromAdmin(String coinName) {
		Optional<AdminCharge> chargeValue = adminChargeDao.findByCoinName(coinName);
		if (chargeValue.isPresent()) {
			return chargeValue.get().getFees();
		} else
			return null;

	}

	@Override
	public Response<Object> setEnableDisable(OrderType orderType, ExchangeStatusType statusType, Long userId) {
		List<P2PAdvertisement> list = createAdvertisementDao.findByFkUserIdAndIsDeletedFalseAndOrderType(userId,
				orderType);
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setExchangeStatusType(statusType);
				list.get(i).setFkUserId(userId);
				createAdvertisementDao.save(list.get(i));
			}
			return new Response<>(200, "Advertisement Status Updated SuccessFully");

		} else
			return new Response<>(512, ADVERTISEMENT_LIST_NOT_FOUND);
	}

	@Override
	public Response<Object> getAdvertisementStataus(OrderType orderType, Long userId) {
		List<P2PAdvertisement> list = createAdvertisementDao.findByFkUserIdAndIsDeletedFalseAndOrderType(userId,
				orderType);
		Map<String, Object> map1 = new HashMap<>();
		if (!list.isEmpty()) {
			map1.put("type", orderType);
			map1.put("status", list.get(0).getExchangeStatusType());
			return new Response<>(200, ADVERTISEMENT_STATUS_GET_SUCCESS, map1);

		} else
			return new Response<>(200, ADVERTISEMENT_LIST_NOT_FOUND);
	}

	@Override
	public Response<Map<String, Object>> getBuySEllAdvertisementList(OrderType orderType, Integer page,
			Integer pageSize) {
		try {
			Page<P2PAdvertisement> getBuyOrderList;
			Long getTotalCount;
			getBuyOrderList = createAdvertisementDao.findByOrderTypeAndIsDeletedFalseAndExchangeStatusType(orderType,
					ExchangeStatusType.ENABLED, PageRequest.of(page, pageSize, Direction.DESC, "peerToPeerExchangeId"));
			getTotalCount = createAdvertisementDao.countByOrderTypeAndIsDeletedFalseAndExchangeStatusType(orderType,
					ExchangeStatusType.ENABLED);
			if (getBuyOrderList.hasContent()) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put(RESULT_LIST, getBuyOrderList.getContent());
				responseMap.put(TOTAL_COUNT, getTotalCount);
				return new Response<>(SUCCESS_CODE, ADDVERTISEMENT_DATA_GET_SUCCESSFULLY, responseMap);
			} else
				return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		} catch (Exception e) {
			LOGGER.catching(e);
			throw new OrderTypeNotFoundException(
					String.format("OrderType not found for getting details with the orderType : %s", orderType), e);
		}
	}

	@Override
	public Response<TradeCountDto> getTradeCount(Long userId) {

		List<Trading> data5 = tradingDao.findByFkUserIdAndTradeStatusAndIsDeletedFalseOrderByCreationTimeAsc(userId,
				TradeStatus.COMPLETE);
		Long count = tradingDao.countByFkUserIdAndTradeStatusAndIsDeletedFalse(userId, TradeStatus.COMPLETE);

		List<Trading> data2 = tradingDao.findByFkUserIdAndTradeStatusAndIsDeletedFalse(userId, TradeStatus.COMPLETE);
		TradeCountDto tradeCountDto = new TradeCountDto();
		BigDecimal sum = new BigDecimal(0);

		if (!data2.isEmpty()) {
			for (int i = 0; i < data2.size() - 1; i++) {

				sum = sum.add(data2.get(i).getTradeAmount());

			}
		}
		tradeCountDto.setTradeValume(sum);
		tradeCountDto.setTradeCount(count);
		if (!data5.isEmpty()) {
			tradeCountDto.setFirstPurchase(data5.get(0).getCreationTime());
		}

		return new Response<>(SUCCESS_CODE, ADDVERTISEMENT_DATA_GET_SUCCESSFULLY, tradeCountDto);
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
			query.append(" where c.creationTime=" + fromDate);
		}
		if (toDate != null) {
			LOGGER.debug("filtering with to date {}", new Date());
			query.append(" and c.creationTime=" + toDate);
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
			createQuery.setParameter("userName", userName);
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
			Map<String, Object> data = new HashMap<>();
			data.put("list", response);
			data.put("totalCount", filteredResultCount);
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
				"select c.tradingId,c.tradeId, c.buyer, c.seller, c.tradeStatus, c.type,c.paymentType, c.creationTime,c.tradeAmount,c.totalBTC,c.tradeFee,c.fkUserId,c.partnerId,c.disputeId,c.disputeStatus,c.disputer,c.disputeDate,c.fiatCoin from Trading c");
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
			query.append(" where c.creationTime=" + fromDate);
		}
		if (toDate != null) {
			LOGGER.debug("filtering with to date {}", new Date());
			query.append(" and c.creationTime=" + toDate);
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
			return dto;
		}).collect(Collectors.toList());
		Collections.sort(response, (a, b) -> b.getTradingId().compareTo(a.getTradingId()));
		if (!response.isEmpty()) {
			Map<String, Object> data = new HashMap<>();
			data.put("list", response);
			data.put("totalCount", filteredResultCount);
			return new Response<>(200, "success", data);
		} else {
			return new Response<>(201, "no data found according to search");
		}
	}

	@Override
	public Response<Map<String, Object>> getPostTradeListForApp(OrderType orderType, Long userId) {
		Long getTotalCount;
		List<P2PAdvertisement> getBuyOrderList = null;
		if (orderType != null) {
			getBuyOrderList = createAdvertisementDao.findByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNot(
					orderType, false, ExchangeStatusType.ENABLED, userId);
			getTotalCount = createAdvertisementDao.countByOrderTypeAndIsDeletedAndExchangeStatusTypeAndFkUserIdNot(
					orderType, false, ExchangeStatusType.ENABLED, userId);
		}

		else {
			getBuyOrderList = createAdvertisementDao.findByIsDeletedAndExchangeStatusTypeAndFkUserIdNot(false,
					ExchangeStatusType.ENABLED, userId);
			getTotalCount = createAdvertisementDao.countByIsDeletedAndExchangeStatusTypeAndFkUserIdNot(false,
					ExchangeStatusType.ENABLED, userId);
		}
		if (getBuyOrderList != null) {
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put(RESULT_LIST, getBuyOrderList);
			responseMap.put(TOTAL_COUNT, getTotalCount);
			return new Response<>(SUCCESS_CODE, ADDVERTISEMENT_DATA_GET_SUCCESSFULLY, responseMap);
		} else {
			return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
		}
	}

	@Override
	public Response<Object> getAllTradeDetailsListForPaymentWindow(Long userId) {
		List<Trading> list = tradingDao.findByFkUserId(userId);
		List<Trading> data = new ArrayList<>();
		for (Trading p2pAdvertisement : list) {
			DateTime createdTime = new DateTime(p2pAdvertisement.getCreationTime());
			DateTime currentTime = new DateTime(System.currentTimeMillis());

			int paymentWindow = p2pAdvertisement.getPaymentWindow();

			int timeDiff = Minutes.minutesBetween(createdTime, currentTime).getMinutes();

			if (paymentWindow > timeDiff) {
				data.add(p2pAdvertisement);
			}
		}
		if (!data.isEmpty())
			return new Response<>(200, "trade  list", data);

		else {
			return new Response<>(204, "no data found according to search");

		}
	}

	@Override
	public BigDecimal getBlockBalance(Long userId, String coinName) {
		List<Escrow> data = escrowDao.findByBlockBalanceUserIdAndCoinName(userId, coinName);
		BigDecimal d = BigDecimal.ZERO;

		if (data.isEmpty()) {
			return d;
		} else {
			for (Escrow b : data) {
				d = d.add(b.getBlockedBalance());
			}
			return d;
		}

	}

	@Override
	public Response<Map<String, Object>> getCompletedTradeDetails(Long userId, Integer page, Integer pageSize) {
		try {
			Page<Trading> getBuyOrderList;
			Long getTotalCount;
			if (page != null && pageSize != null) {
				getBuyOrderList = tradingDao.findAllByFkUserId(userId,
						PageRequest.of(page, pageSize, Direction.DESC, "tradingId"));
				getTotalCount = tradingDao.countByFkUserId(userId);
				if (getBuyOrderList.hasContent()) {
					Map<String, Object> responseMap = new HashMap<>();
					responseMap.put(RESULT_LIST, getBuyOrderList.getContent());
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, TRADING_DATA_GET_SUCCESSFULLY, responseMap);
				} else
					return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
			} else {
				List<Trading> getBuyOrderList2 = tradingDao.findAllByFkUserId(userId);
				getTotalCount = tradingDao.countByFkUserId(userId);
				if (!getBuyOrderList2.isEmpty()) {
					Map<String, Object> responseMap = new HashMap<>();
					responseMap.put(RESULT_LIST, getBuyOrderList2);
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, TRADING_DATA_GET_SUCCESSFULLY, responseMap);
				} else
					return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
			}

		} catch (

		Exception e) {
			LOGGER.catching(e);
			throw new OrderTypeNotFoundException(
					String.format("OrderType not found for getting details with the UserId : %s", userId), e);
		}
	}

	

}
