package com.mobiloitte.microservice.wallet.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mobiloitte.microservice.wallet.dao.MarketPriceDao;
import com.mobiloitte.microservice.wallet.entities.MarketPrice;
import com.mobiloitte.microservice.wallet.enums.MarketCoins;


@Configuration
public class InitializeMarketPrice {

	/**
	 * Initialize market price table.
	 *
	 * @param marketPriceDao the market price dao
	 * @return the initializing bean
	 */
	@Bean
	public InitializingBean initializeMarketPriceTable(MarketPriceDao marketPriceDao) {
		return () -> {
			if (marketPriceDao.findAll().isEmpty()) {
				MarketCoins[] totalCoins = MarketCoins.values();
				List<MarketPrice> marketPriceList = new ArrayList<>();
				for (int i = 0; i < totalCoins.length; i++) {
					MarketPrice marketPrice = new MarketPrice();
					marketPrice.setCoinName(totalCoins[i].name());
					marketPrice.setPriceInUsd(BigDecimal.TEN);
					marketPrice.setPriceInBtc(BigDecimal.TEN);
					marketPrice.setPriceInInr(BigDecimal.TEN);
					marketPrice.setPriceInEur(BigDecimal.TEN);
					marketPriceList.add(marketPrice);
				}
				marketPriceDao.saveAll(marketPriceList);

			}
		};
	}
}
