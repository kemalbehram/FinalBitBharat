package com.mobiloitte.microservice.wallet.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.mobiloitte.microservice.wallet.dto.CoinDto;
import com.mobiloitte.microservice.wallet.dto.CoinPairReqDto;
import com.mobiloitte.microservice.wallet.dto.CoinStatusDto;
import com.mobiloitte.microservice.wallet.dto.CoinUpdateDto;
import com.mobiloitte.microservice.wallet.dto.MarketPriceDto;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.CoinPair;
import com.mobiloitte.microservice.wallet.enums.Network;
import com.mobiloitte.microservice.wallet.model.Response;

/**
 * The Interface CoinManagementService.
 * 
 * @author Ankush Mohapatra
 */
public interface CoinManagementService {

	/**
	 * Gets the all coin list.
	 *
	 * @return the all coin list
	 */
	// Response<Map<String, Object>> getAllCoinList();

	/**
	 * Gets the coin pair list.
	 *
	 * @param baseCoin the base coin
	 * @return the coin pair list
	 */
	Response<List<Coin>> getCoinPairList(String baseCoin);

	/**
	 * Gets the all coin pair list.
	 *
	 * @return the all coin pair list
	 */
	Response<List<Map<String, Object>>> getAllCoinPairList();

	/**
	 * Gets the particular coin details.
	 *
	 * @param coinName the coin name
	 * @return the particular coin details
	 */
	Response<Coin> getParticularCoinDetailsByCoinName(String coinName);

	/**
	 * Gets the coin pair symbol list.
	 *
	 * @param base_coin       the base coin
	 * @param executable_coin the executable coin
	 * @return the coin pair symbol list
	 */
	Response<List<CoinPair>> getCoinPairSymbolList(String base_coin, String executable_coin);

	Response<String> updateMarketPrice(String coinName);

	Response<MarketPriceDto> getMarketPriceFromCoinName(String coinName);

	String getMarketPriceDetails() throws URISyntaxException, IOException;

//	Response<List<Coin>> getAllCoinList(String coinShortName, Integer page, Integer pageSize);

	Response<List<Object>> getCoinPairSymbolListOhlc(String baseCoin, String executableCoin);

	Response<List<Object>> getAllCoinPairListOhlc();

	Response<Map<String, Object>> getDepositeAndCoinCoin();

	Response<Object> getCoinStatus(String coin);

	Response<Object> setCoinStatus(String coin, CoinStatusDto coinStatusDto);

	Response<CoinPair> getCoinPairList();

	Response<CoinPair> setVisibility(CoinPairReqDto coinPairReqDto);

	Response<Object> getCOinMobileApps(String coinName);

	Response<?> getCoinPairSymbolList2(String baseCoin, String exeCoin);

	Response<?> getAllCoinPairList2();

	Response<Object> getAllCoinList();

	Response<List<Coin>> getFiatCoinList(String coinType);

	Response<Object> setCoinFavourite(Long coinId, Long coinId2);

	Response<List<Coin>> getCoinListfavourite(Long userId);

	Response<Object> setCoinUnfavourite(String coinName, Long userId);

	Response<Object> setCoinVisble(String coinName, Long userId, Boolean isVisible, Boolean iswithdraw, Boolean isDeposit, String remark);

	Response<Object> getCoinListAmin();

	Response<Object> addCoin(CoinDto coinDto, Network network);

	Response<Coin> setAVTPriceInUsd(String coin, BigDecimal priseInUsd);

	Response<Map<String, BigDecimal>> getAVTPriceInUsd(String coin);

	Response<Object> coinUpdate(Long coinId, Network network, CoinUpdateDto coinUpdateDto);

	Response<Object> coinDelete(Long coinId);

	Response<Object> coincategory(String category);

	Response<CoinPair> setCoinFavouriteUnFavourite(String baseCoin, String executableCoin);

	Response<List<CoinPair>> getCoinPairListfavourite(Long userId);

	

}
