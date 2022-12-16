/*
 * package com.mobiloitte.microservice.wallet.utils;
 * 
 * import java.math.BigDecimal; import java.util.List; import java.util.Map;
 * 
 * import org.apache.logging.log4j.LogManager; import
 * org.apache.logging.log4j.Logger; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.scheduling.annotation.Scheduled; import
 * org.springframework.stereotype.Component;
 * 
 * import com.mobiloitte.microservice.wallet.dao.MarketPriceDao; import
 * com.mobiloitte.microservice.wallet.entities.MarketPrice;
 * 
 *//**
	 * The Class RealTimePriceScheduler.
	 */
/*
 * @Component public class RealTimePriceScheduler {
 * 
 * @Autowired private CoinDao coinDao;
 * 
 * 
 *//** The market price dao. */
/*
 * @Autowired private MarketPriceDao marketPriceDao;
 * 
 *//** The crypto fiat converter. */
/*
 * @Autowired private CryptoFiatConverter cryptoFiatConverter;
 * 
 *//** The Constant LOGGER. */
/*
 * private static final Logger LOGGER =
 * LogManager.getLogger(RealTimePriceScheduler.class);
 * 
 *//**
	 * Update market price.
	 *//*
		 * @Scheduled(fixedRateString = "${marketprice.scheduler.fixedrate}") public
		 * void updateMarketPrice() { try { List<MarketPrice> getAllList =
		 * marketPriceDao.findAll(); if (!getAllList.isEmpty()) { for (MarketPrice
		 * marketPrice : getAllList) { Map<String, BigDecimal> currentPrice =
		 * cryptoFiatConverter.marketPriceApi(marketPrice.getCoinName(),
		 * CryptoFiatConverter.USD, CryptoFiatConverter.INR); if (currentPrice != null)
		 * { marketPrice.setPriceInUsd(currentPrice.get(CryptoFiatConverter.USD));
		 * marketPrice.setPriceInInr(currentPrice.get(CryptoFiatConverter.INR));
		 * marketPrice.setPriceInBtc(currentPrice.get(CryptoFiatConverter.BTC));
		 * marketPrice.setPriceInEur(currentPrice.get(CryptoFiatConverter.EUR));
		 * marketPriceDao.save(marketPrice); LOGGER.info("Price set for pair " +
		 * marketPrice.getCoinName() + "/USD is: " +
		 * currentPrice.get(CryptoFiatConverter.USD) + " and " +
		 * marketPrice.getCoinName() + "/BTC is: " +
		 * currentPrice.get(CryptoFiatConverter.BTC)); } else {
		 * LOGGER.info("API service went wrong!!!"); } } } } catch (Exception e) {
		 * LOGGER.info("Something went wrong"); }
		 * 
		 * }
		 * 
		 * 
		 * @Scheduled(fixedRateString = "${marketprice.scheduler.fixedrate1}") public
		 * void updateMarketPriceForLastSeek() { try { List<MarketPrice> getAllList =
		 * marketPriceDao.findAll(); List<Coin> coinList = coinDao.findAll(); if
		 * (!getAllList.isEmpty()) { for (MarketPrice marketPrice : getAllList) {
		 * BigDecimal currentPrice =
		 * cryptoFiatConverter.marketPriceApi(marketPrice.getCoinName(),
		 * CryptoFiatConverter.USD); if (currentPrice != null) { for (Coin coin :
		 * coinList) {
		 * 
		 * // Coin coin = new Coin(); coin.setMarketPriceInUsdAWeekBefore(currentPrice);
		 * 
		 * coinDao.save(coin); } LOGGER.info("Price set for pair " +
		 * marketPrice.getCoinName() + "/USD is: " + currentPrice); } else {
		 * LOGGER.info("API service went wrong!!!"); } } } } catch (Exception e) {
		 * LOGGER.info("Something went wrong"); }
		 * 
		 * }
		 * 
		 * }
		 */