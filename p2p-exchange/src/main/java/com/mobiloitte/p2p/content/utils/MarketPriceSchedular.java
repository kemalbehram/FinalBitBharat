
package com.mobiloitte.p2p.content.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mobiloitte.p2p.content.dao.MarketPriceDao;
import com.mobiloitte.p2p.content.model.MarketPrice;


@Component
public class MarketPriceSchedular {

	@Autowired
	MarketPriceDao marketPriceDao;

	@SuppressWarnings("unchecked")

	/* @Scheduled(fixedRate = 2000) */
	public void scheduleTaskWithFixedRate() {

		final String uri = "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD";
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> responses = new HashMap<String, Object>();
		responses = restTemplate.getForObject(uri, Map.class);
		String keys = "";
		for (String string : responses.keySet()) {
			keys = string;
		}
		BigDecimal valueOfff = BigDecimal.valueOf(Double.valueOf(String.valueOf(responses.get(keys))));
		Long j = 1L;
		Optional<MarketPrice> marketPrice = marketPriceDao.findByMarketPriceId(j);
		marketPrice.get().setPriceInUsd(valueOfff);
	//	System.out.println(valueOfff);
		marketPriceDao.save(marketPrice.get());
		//marketPriceDao.save(valueOfff);

	}
}
