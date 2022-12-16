package com.mobiloitte.order.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mobiloitte.order.model.PercentageChange;
import com.mobiloitte.order.repo.PercentageChangeRepo;

@Configuration
public class InitializePercentageChange {

	@Bean
	public InitializingBean initializeChange(PercentageChangeRepo   lastPriceRepo) {
		return () -> {
			List<PercentageChange> getLastPrice = lastPriceRepo.findAll();
			if(getLastPrice.isEmpty())
			{
				lastPriceRepo.save(new PercentageChange(BigDecimal.ZERO));
			}
		};
	}
}
