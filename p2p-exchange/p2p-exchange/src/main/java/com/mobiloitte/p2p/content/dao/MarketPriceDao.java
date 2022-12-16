package com.mobiloitte.p2p.content.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.p2p.content.model.MarketPrice;


public interface MarketPriceDao extends JpaRepository<MarketPrice, Long> {

	Optional<MarketPrice> findByCoinName(String xbt);

	Optional<MarketPrice> findByMarketPriceId(Long marketPriceId);

	Optional<MarketPrice> save(Optional<MarketPrice> marketPrice);



}
