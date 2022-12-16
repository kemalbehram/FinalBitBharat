/*
 * package com.mobiloitte.microservice.wallet.utils;
 * 
 * import java.math.BigDecimal; import java.util.List; import java.util.Map;
 * 
 * import org.apache.logging.log4j.LogManager; import
 * org.apache.logging.log4j.Logger; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.scheduling.annotation.Scheduled; import
 * org.springframework.stereotype.Component; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import com.mobiloitte.microservice.wallet.constants.WalletConstants; import
 * com.mobiloitte.microservice.wallet.dao.MarketPriceDao; import
 * com.mobiloitte.microservice.wallet.entities.MarketPrice;
 * 
 *//**
	 * The Class RealTimePriceScheduler.
	 * 
	 * @author Ankush Mohapatra
	 */
/*
 * @Component public class RealTimePriceSchedulerForMarket implements
 * WalletConstants {
 * 
 *//** The market price dao. */
/*
 * @Autowired private MarketPriceDao marketPriceDao;
 * 
 *//** The Constant LOGGER. */
/*
 * private static final Logger LOGGER =
 * LogManager.getLogger(RealTimePriceSchedulerForMarket.class);
 * 
 *//**
	 * Update real time price.
	 *//*
		 * @Scheduled(fixedRateString =
		 * "${coinmarketcap.marketprice.scheduler.fixedrate}")
		 * 
		 * @Transactional public void updateRealTimePrice() { List<MarketPrice>
		 * getAllList = marketPriceDao.findAll(); if (!getAllList.isEmpty()) { for
		 * (MarketPrice marketPrice : getAllList) { Map<String, BigDecimal>
		 * getRealTimePriceData = MarketPriceUtil
		 * .getMarketPrice(marketPrice.getCoinName());
		 * System.out.println(getRealTimePriceData); if (getRealTimePriceData != null) {
		 * marketPrice.setPriceInUsd(getRealTimePriceData.get(USD));
		 * 
		 * marketPrice.setPrice_1Hour(getRealTimePriceData.get(percent_change_1h));
		 * marketPrice.setPrice_24Hour(getRealTimePriceData.get(percent_change_24h));
		 * marketPrice.setPrice_24Hour(getRealTimePriceData.get(percent_change_7d));
		 * 
		 * marketPriceDao.save(marketPrice); LOGGER.info("Price updated for coin: " +
		 * marketPrice.getCoinName()); } else {
		 * LOGGER.error("Price not updated for coin: " + marketPrice.getCoinName()); } }
		 * } else { LOGGER.info("No coins found, table is empty"); } } }
		 */