package com.mobiloitte.order.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mobiloitte.order.model.LastPrice;
import com.mobiloitte.order.repo.ExeLastPriceRepo;

@Configuration
public class InitializeLastPrice {

	@Bean
	public InitializingBean initializePrice(ExeLastPriceRepo lastPriceRepo) {
		return () -> {
			List<LastPrice> getLastPrice = lastPriceRepo.findAll();
			if(getLastPrice.isEmpty())
			{
				lastPriceRepo.save(new LastPrice(BigDecimal.ZERO));
			}
		};
	}
}
