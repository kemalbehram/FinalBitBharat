package com.mobiloitte.microservice.wallet.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.MarketPrice;

/**
 * The Interface MarketPriceDao.
 * @author Ankush Mohapatra
 */
public interface MarketPriceDao extends JpaRepository<MarketPrice, Long>{

	/**
	 * Find by coin name.
	 *
	 * @param coinName the coin name
	 * @return the optional
	 */
	Optional<MarketPrice> findByCoinName(String coinName);
}
