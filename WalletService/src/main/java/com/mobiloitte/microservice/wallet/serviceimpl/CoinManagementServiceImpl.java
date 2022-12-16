package com.mobiloitte.microservice.wallet.serviceimpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.CoinDepositWithdrawalDao;
import com.mobiloitte.microservice.wallet.dao.CoinPairDao;
import com.mobiloitte.microservice.wallet.dao.FiatDao;
import com.mobiloitte.microservice.wallet.dao.MarketPriceDao;
import com.mobiloitte.microservice.wallet.dao.WalletDao;
import com.mobiloitte.microservice.wallet.dto.CoinDto;
import com.mobiloitte.microservice.wallet.dto.CoinPairDto;
import com.mobiloitte.microservice.wallet.dto.CoinPairReqDto;
import com.mobiloitte.microservice.wallet.dto.CoinStatusDto;
import com.mobiloitte.microservice.wallet.dto.CoinUpdateDto;
import com.mobiloitte.microservice.wallet.dto.MarketPriceDto;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.CoinPair;
import com.mobiloitte.microservice.wallet.entities.MarketPrice;
import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.enums.FiatStatus;
import com.mobiloitte.microservice.wallet.enums.Network;
import com.mobiloitte.microservice.wallet.enums.TransactionType;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.CoinManagementService;
import com.mobiloitte.microservice.wallet.utils.MarketData;

/**
 * The Class CoinManagementServiceImpl.
 * 
 * @author Ankush Mohapatra
 */
@Service("CoinManagementService")
public class CoinManagementServiceImpl implements CoinManagementService, WalletConstants, OtherConstants {

	@Autowired
	MarketData javaExample;
	// DAO Objects
	/** The coin dao. */
	@Autowired
	private CoinDao coinDao;

	/** The coin pair dao. */
	@Autowired
	private CoinPairDao coinPairDao;
	@Autowired
	private FiatDao fiatDao;
	@Autowired
	private WalletDao walletDao;
	@Autowired
	private CoinDepositWithdrawalDao coinDepositWithdrawalDao;
	@Autowired
	private MarketPriceDao marketPriceDao;

	ModelMapper modelMapper = new ModelMapper();

	@Override
	public Response<Object> getAllCoinList() {

		List<Coin> getAllCoinsList = coinDao.findAllByIsVisibleTrue();
		if (!getAllCoinsList.isEmpty()) {
			return new Response<>(200, "List Fetched", getAllCoinsList);
		} else {
			return new Response<>(205, "Data not Present");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.CoinManagementService#
	 * getCoinPairList(java.lang.String)
	 */
	@Override
	public Response<List<Coin>> getCoinPairList(String baseCoin) {
		List<Coin> getAllCoinsList = coinDao.findByCoinShortNameNot(baseCoin);
		if (getAllCoinsList != null && !getAllCoinsList.isEmpty()) {
			return new Response<>(SUCCESS_CODE, COIN_LIST_FETCHED_SUCCESSFULLY, getAllCoinsList);
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.CoinManagementService#
	 * getParticularCoinDetails(java.lang.String)
	 */
	@Override
	public Response<Coin> getParticularCoinDetailsByCoinName(String coinName) {
		Optional<Coin> getCoin = coinDao.findByCoinShortName(coinName);
		
		if (getCoin.isPresent()) {
			
			return new Response<>(SUCCESS_CODE, COIN_DETAILS_FETCHED_SUCCESSFULLY, getCoin.get());
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.CoinManagementService#
	 * getCoinPairSymbolList(java.lang.String, java.lang.String)
	 */
	@Override
	public Response<List<CoinPair>> getCoinPairSymbolList(String base_coin, String executable_coin) {
		List<CoinPair> getAllCoinsList = null;
		if (base_coin != null && executable_coin == null) {
			getAllCoinsList = coinPairDao.findByBaseCoin(base_coin);

		} else if (base_coin == null && executable_coin != null) {
			getAllCoinsList = coinPairDao.findByExecutableCoin(executable_coin);
		}
		if (getAllCoinsList != null && !getAllCoinsList.isEmpty()) {

			return new Response<>(SUCCESS_CODE, COIN_LIST_FETCHED_SUCCESSFULLY, getAllCoinsList);
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND);
		}
	}

	@Override
	public Response<List<Map<String, Object>>> getAllCoinPairList() {

		List<Map<String, Object>> responseListObj = new ArrayList<>();

		List<String> getAllCoinPairList = coinPairDao.getDistinctByBaseCoin();
		if (!getAllCoinPairList.isEmpty()) {
			for (String coin : getAllCoinPairList) {
				Map<String, Object> pairObj = new HashMap<>();
				List<String> respExecCoins = new ArrayList<>();
				List<String> executableCoins = coinPairDao.findBycoin(coin);
				if (!executableCoins.isEmpty()) {
					respExecCoins.addAll(executableCoins);
					pairObj.put("baseCoin", coin);
					pairObj.put("execCoin", respExecCoins);

				}
				responseListObj.add(pairObj);
				Collections.reverse(responseListObj);
			}
		}

		if (!responseListObj.isEmpty()) {
			return new Response<>(SUCCESS_CODE, ALL_COINPAIR_FETCHED_SUCCESSFULLY, responseListObj);
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND, Collections.emptyList());
		}
	}

	@Override
	@Transactional
	public Response<String> updateMarketPrice(String coinName) {
		Optional<Coin> getCoin = coinDao.findByCoinShortName(coinName);
		if (getCoin.isPresent()) {
			Optional<MarketPrice> getMarketPrice = marketPriceDao.findByCoinName(coinName);
			if (getMarketPrice.isPresent()) {

				getCoin.get().setMarketPriceInUsd(getMarketPrice.get().getPriceInUsd());
				getCoin.get().setMarketPriceInInr(getMarketPrice.get().getPriceInInr());
				getCoin.get().setMarketPriceInEur(getMarketPrice.get().getPriceInEur());
				coinDao.save(getCoin.get());
				return new Response<>(SUCCESS_CODE, MARKET_PRICE_UPDATED_SUCCESSFULLY + coinName);
			} else {
				return new Response<>(FAILURE_CODE, FAILURE);
			}
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND);
		}
	}

	@Override
	public String getMarketPriceDetails() throws URISyntaxException, IOException {
		String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
		List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
		paratmers.add(new BasicNameValuePair("start", "1"));
		paratmers.add(new BasicNameValuePair("limit", "10"));
		paratmers.add(new BasicNameValuePair("convert", "BTC"));
		String result = javaExample.makeAPICall(uri, paratmers);

		if (result != null) {
			return result;

		} else {
			return "URL Not Working";
		}
	}

	@Override
	public Response<List<Object>> getAllCoinPairListOhlc() {
		List<CoinPair> getAllCoinPairList = coinPairDao.findAll();

		List<Object> cptd = new ArrayList<>();
		if (!getAllCoinPairList.isEmpty()) {
			for (int i = 0; i < getAllCoinPairList.size(); i++) {
				CoinPairDto dto = modelMapper.map(getAllCoinPairList.get(i), CoinPairDto.class);
				cptd.add(dto);
			}
		}

		if (!cptd.isEmpty()) {
			return new Response<List<Object>>(200, "ALL_COINPAIR_FETCHED_SUCCESSFULLY", cptd);
		} else {
			return new Response<>(201, COIN_NOT_FOUND, Collections.emptyList());
		}
	}

	@Override

	public Response<List<Object>> getCoinPairSymbolListOhlc(String baseCoin, String executableCoin) {
		List<CoinPair> getAllCoinsList = Collections.emptyList();

		if (baseCoin != null && executableCoin == null) {
			getAllCoinsList = coinPairDao.findByBaseCoin(baseCoin);
		} else if (baseCoin == null && executableCoin != null) {
			getAllCoinsList = coinPairDao.findByExecutableCoin(executableCoin);
		}

		List<Object> cptd = new ArrayList<>();
		if (!getAllCoinsList.isEmpty()) {
			getAllCoinsList.parallelStream().forEachOrdered(p -> {
				CoinPairDto dto = modelMapper.map(p, CoinPairDto.class);
				cptd.add(dto);
			});
		}
		if (!getAllCoinsList.isEmpty()) {
			return new Response<List<Object>>(200, COIN_LIST_FETCHED_SUCCESSFULLY, cptd);
		} else {
			return new Response<>(201, COIN_NOT_FOUND);
		}
	}

	@Override
	public Response<Map<String, Object>> getDepositeAndCoinCoin() {

		Long coinCount = coinDao.countByCoinType(CRYPTO_CURRENCY);
		Long depositCount = coinDepositWithdrawalDao.countByTxnType(DEPOSIT);
		Long withdrawCount = coinDepositWithdrawalDao.countByTxnTypeAndStatus(WITHDRAW, "PENDING");
		Long withdrawCountComplete = coinDepositWithdrawalDao.countByTxnTypeAndStatus(WITHDRAW, "CONFIRM");
		Long fiatDeposit = fiatDao.countByFiatStatusAndTransactionType(FiatStatus.PENDING, TransactionType.DEPOSIT);
		Long fiatDeposit2 = fiatDao.countByFiatStatusAndTransactionType(FiatStatus.PENDING, TransactionType.WITHDRAW);
		Long fiatDeposit3 = fiatDao.countByFiatStatusAndTransactionType(FiatStatus.COMPLETED, TransactionType.DEPOSIT);
		Long fiatDeposit4 = fiatDao.countByFiatStatusAndTransactionType(FiatStatus.COMPLETED, TransactionType.WITHDRAW);
		Long fiatDeposit5 = fiatDao.countByFiatStatusAndTransactionType(FiatStatus.REJECTED, TransactionType.WITHDRAW);
		Long fiatDeposit6 = fiatDao.countByFiatStatusAndTransactionType(FiatStatus.REJECTED, TransactionType.DEPOSIT);
		Map<String, Object> count = new HashMap<String, Object>();
		count.put("coinCount", coinCount);
		count.put("depositCount", depositCount);
		count.put("withdrawCountPending", withdrawCount);
		count.put("withdrawCountComplete", withdrawCountComplete);
		count.put("fiatDepositPending", fiatDeposit);
		count.put("fiatWithdrawPending", fiatDeposit2);
		count.put("fiatDepositComplete", fiatDeposit3);
		count.put("fiatWithdrawComplete", fiatDeposit4);
		count.put("fiatWithdarwRejected", fiatDeposit5);
		count.put("fiatDepositRejected", fiatDeposit6);
		return new Response<>(SUCCESS_CODE, "Deposit and coin count", count);
	}

	@Override
	public Response<Object> getCoinStatus(String coin) {
		if (coin != null) {
			Optional<Coin> coinStatus = coinDao.findByCoinShortName(coin);
			CoinStatusDto CoinStatusDto = new CoinStatusDto();

			CoinStatusDto.setConfermation(coinStatus.get().getConfermation());
			CoinStatusDto.setIsDeposite(coinStatus.get().getIsDeposite());
			CoinStatusDto.setIsWithdrawl(coinStatus.get().getIsWithdrawl());
			CoinStatusDto.setRemark(coinStatus.get().getRemark());
			CoinStatusDto.setIsDeposit(coinStatus.get().getIsDeposit());
			CoinStatusDto.setIsVisible(coinStatus.get().getIsVisible());
			CoinStatusDto.setCoinName(coinStatus.get().getCoinShortName());

			return new Response<>(SUCCESS_CODE, "Coin status fatch succussfully", CoinStatusDto);
		} else {
			List<Coin> coinStatusDetails = coinDao.findAll();
			List<CoinStatusDto> CoinStatusList = new ArrayList<>();
			List<CoinStatusDto> CoinStatusAllList = new ArrayList<>();

			for (int i = 0; i < coinStatusDetails.size(); i++) {
				CoinStatusDto CoinStatusDto = new CoinStatusDto();
				CoinStatusDto.setConfermation(coinStatusDetails.get(i).getConfermation());
				CoinStatusDto.setIsDeposite(coinStatusDetails.get(i).getIsDeposite());
				CoinStatusDto.setIsWithdrawl(coinStatusDetails.get(i).getIsWithdrawl());
				CoinStatusDto.setRemark(coinStatusDetails.get(i).getRemark());
				CoinStatusDto.setIsVisible(coinStatusDetails.get(i).getIsVisible());
				CoinStatusDto.setIsDeposit(coinStatusDetails.get(i).getIsDeposit());
				CoinStatusDto.setCoinName(coinStatusDetails.get(i).getCoinShortName());
				CoinStatusList.add(CoinStatusDto);

			}
			CoinStatusAllList.addAll(CoinStatusList);
			return new Response<>(SUCCESS_CODE, "Coin status fatch succussfully", CoinStatusAllList);
		}

	}

	@Override
	public Response<Object> setCoinStatus(String coin, CoinStatusDto coinStatusDto) {
		try {
			Optional<Coin> coinStatus = coinDao.findByCoinShortName(coin);
			if (coinStatus.isPresent()) {

				Coin data = coinStatus.get();
				data.setConfermation(coinStatusDto.getConfermation());
				data.setIsDeposite(coinStatusDto.getIsDeposite());
				data.setIsVisible(coinStatusDto.getIsVisible());
				data.setIsWithdrawl(coinStatusDto.getIsWithdrawl());
				data.setRemark(coinStatusDto.getRemark());

				coinDao.save(data);
				return new Response<>(SUCCESS_CODE, "Coin details update successfully.");

			} else {

				return new Response<>(205, "Coin is not presnet.");
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return new Response<>(FAILURE_CODE, "Something went wrong.", e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response<MarketPriceDto> getMarketPriceFromCoinName(String coinName) {
		Optional<MarketPrice> getMarketPrice = marketPriceDao.findByCoinName(coinName);
		if (getMarketPrice.isPresent()) {
			MarketPriceDto dto = new MarketPriceDto();
			dto.setPriceInUsd(getMarketPrice.get().getPriceInUsd());
			dto.setPriceInInr(getMarketPrice.get().getPriceInInr());
			dto.setPriceInEur(getMarketPrice.get().getPriceInEur());
			return new Response<>(SUCCESS_CODE, "Price fetched successfully", dto);

		} else
			return new Response<>(FAILURE_CODE, FAILURE);
	}

	@Override
	public Response<CoinPair> setVisibility(CoinPairReqDto coinPairReqDto) {
		Optional<CoinPair> coinStatus = coinPairDao.findByBaseCoinAndExecutableCoin(coinPairReqDto.getBaseCoin(),
				coinPairReqDto.getExecutableCoin());

		if (coinPairReqDto.getVisible() == null) {
			return new Response<>(201, "Please add visible ");
		} else if (!coinStatus.isPresent()) {
			return new Response<>(201, "Please enter valid Coin");

		} else {

			CoinPair data = coinStatus.get();
			data.setBaseCoin(coinPairReqDto.getBaseCoin());
			data.setExecutableCoin(coinPairReqDto.getExecutableCoin());
			data.setVisible(coinPairReqDto.getVisible());
			coinPairDao.save(data);

			return new Response<>(200, "Visiblity status Changed ", data);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response<CoinPair> getCoinPairList() {
		List<CoinPair> coinList = coinPairDao.findAll();
		if (coinList.isEmpty()) {
			return new Response(201, " No data Found");

		} else {
			return new Response(200, " Success", coinList);
		}
	}

	@Override
	public Response<Object> getCOinMobileApps(String coinName) {
		List<Coin> coinpairdata1 = new ArrayList<>();
		List<Coin> coinpairdata = new ArrayList<>();
		List<CoinPair> getAllCoinsList = null;
		getAllCoinsList = coinPairDao.findByBaseCoin(coinName);
		if (getAllCoinsList.isEmpty()) {
			return new Response<Object>(201, " No data Found");
		}

		for (CoinPair c : getAllCoinsList) {
			Optional<Coin> getCoin = coinDao.findByCoinShortName(c.getExecutableCoin());

			coinpairdata.add(getCoin.get());

		}
		coinpairdata1.addAll(coinpairdata);
		return new Response<>(200, "coin pair data found successfully", coinpairdata1);
	}

	@Override
	public Response<?> getCoinPairSymbolList2(String base_coin, String exeCoin) {
		List<CoinPair> getAllCoinsList = null;
		if (base_coin != null && exeCoin == null) {
			getAllCoinsList = coinPairDao.findByBaseCoin(base_coin);
		} else if (base_coin == null && exeCoin != null) {
			getAllCoinsList = coinPairDao.findByExecutableCoin(exeCoin);
		}
		if (getAllCoinsList != null && !getAllCoinsList.isEmpty()) {
			return new Response<>(SUCCESS_CODE, COIN_LIST_FETCHED_SUCCESSFULLY, getAllCoinsList);
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND);
		}
	}

	@Override
	public Response<?> getAllCoinPairList2() {
		List<Map<String, Object>> responseListObj = new ArrayList<>();
		List<String> getAllCoinPairList = coinPairDao.getDistinctByBaseCoin();
		if (!getAllCoinPairList.isEmpty()) {
			for (String coin : getAllCoinPairList) {
				Map<String, Object> pairObj = new HashMap<>();
				List<String> respExecCoins = new ArrayList<>();
				List<CoinPair> executableCoins = coinPairDao.findByBaseCoinAndVisible(coin, Boolean.TRUE);
				List<String> list = new ArrayList<>();
				for (CoinPair p : executableCoins) {
					list.add(p.getExecutableCoin());
				}
				if (!executableCoins.isEmpty()) {
					respExecCoins.addAll(list);
					pairObj.put("baseCoin", coin);
					pairObj.put("execCoin", respExecCoins);
				}
				responseListObj.add(pairObj);
			}
		}

		if (!responseListObj.isEmpty()) {
			return new Response<>(SUCCESS_CODE, ALL_COINPAIR_FETCHED_SUCCESSFULLY, responseListObj);
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND, Collections.emptyList());
		}
	}

	@Override
	public Response<List<Coin>> getFiatCoinList(String coinType) {
		List<Coin> getAllCoinsList = coinDao.findByCoinType(coinType);
		if (!getAllCoinsList.isEmpty()) {
			return new Response<>(SUCCESS_CODE, COIN_LIST_FETCHED_SUCCESSFULLY, getAllCoinsList);
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND);
		}
	}

	@Override
	public Response<Object> setCoinFavourite(Long userId, Long coinId) {
		Optional<Coin> isCoinExists = coinDao.findByCoinId(coinId);
		if (isCoinExists.isPresent()) {
			isCoinExists.get().setIsFavourite(true);
			coinDao.save(isCoinExists.get());
			return new Response<>(200, "Coin Favourite Successfully");
		}
		return new Response<>(205, "Coin not found");
	}

	@Override
	public Response<List<Coin>> getCoinListfavourite(Long userId) {
		List<Coin> isListExists = coinDao.findAllByIsFavouriteTrue();
		if (!isListExists.isEmpty()) {
			return new Response<>(200, "List Fetched Successfully", isListExists);
		}
		return new Response<>(205, "Data Not Found");
	}

	@Override
	public Response<Object> setCoinUnfavourite(String coinName, Long userId) {

		Optional<Coin> isCoinExists1 = coinDao.findByCoinShortName(coinName);
		if (isCoinExists1.isPresent()) {
			isCoinExists1.get().setIsFavourite(false);
			coinDao.save(isCoinExists1.get());
			return new Response<>(200, "Coin UnFavourite Successfully");
		}
		return new Response<>(205, "Coin not found");
	}

	@Override
	public Response<Object> setCoinVisble(String coinName, Long userId, Boolean isVisible, Boolean iswithdraw,
			Boolean isDeposit, String remark) {
		Optional<Coin> coinVisible = coinDao.findByCoinShortName(coinName);
		if (coinVisible.isPresent()) {
			coinVisible.get().setIsVisible(isVisible);
			coinVisible.get().setIsWithdrawl(iswithdraw);
			coinVisible.get().setIsDeposit(isDeposit);
			coinVisible.get().setRemark(remark);
			coinDao.save(coinVisible.get());

			Map<String, Object> map = new HashMap<>();
			map.put("CoinName", coinVisible.get().getCoinShortName());
			map.put("Visibilty", coinVisible.get().getIsVisible());
			map.put("Withdraw", coinVisible.get().getIsWithdrawl());
			map.put("Deposit", coinVisible.get().getIsDeposit());
			map.put("Remark", coinVisible.get().getRemark());
			return new Response<>(200, "Coin Status changed", map);
		}
		return new Response<>(205, "Coin Not Found");
	}

	@Override
	public Response<Object> getCoinListAmin() {
		List<Coin> coins = coinDao.findAll();

		if (!coins.isEmpty()) {
			return new Response<>(SUCCESS_CODE, COIN_LIST_FETCHED_SUCCESSFULLY, coins);
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND);
		}
	}

	@Override
	public Response<Object> addCoin(CoinDto coinDto, Network network) {
		Coin coin = new Coin();
		coin.setCoinFullName(coinDto.getCoinFullName());
		coin.setContractAddress(coinDto.getContractAddress());
		coin.setCoinImage(coinDto.getCoinImage());
		coin.setCoinShortName(coinDto.getCoinShortName());
		coin.setNetwork(network);
		coin.setMakerFee(BigDecimal.ZERO);
		coin.setTakerFee(BigDecimal.ZERO);
		coin.setBasicBuyFee(BigDecimal.ZERO);
		coin.setBasicSellFee(BigDecimal.ZERO);
		coin.setMarketPriceInUsd(BigDecimal.TEN);
		coin.setWithdrawalAmount(BigDecimal.TEN);
		coin.setWithdrawlFee(BigDecimal.ZERO);
		coin.setCategory(coinDto.getCategory());
		coin.setIsFavourite(false);
		coin.setCreateDate(new Date());
		coin.setCoinType(coinDto.getCoinType());

		coin.setThresholdPrice(BigDecimal.valueOf(50));

		coin.setThresholdPrice(BigDecimal.ZERO);
		coinDao.save(coin);
		return new Response<>(200, "Coin Added Successfully");
	}

	@Override
	public Response<Map<String, BigDecimal>> getAVTPriceInUsd(String coin) {
		Optional<Coin> coinStatus = coinDao.findByCoinShortName(coin);
		if (coinStatus.isPresent()) {

			BigDecimal avtPriceInUsd = coinStatus.get().getMarketPriceInUsd();
			Map<String, BigDecimal> price = new HashMap<>();
			price.put("priceInUsd", avtPriceInUsd);
			return new Response<>(SUCCESS_CODE, "Price get successfully", price);
		}
		return new Response<>(FAILURE_CODE, "Coin not found");
	}

	@Override
	public Response<Coin> setAVTPriceInUsd(String coin, BigDecimal priseInUsd) {
		Optional<Coin> coinStatus = coinDao.findByCoinShortName(coin);
		Optional<MarketPrice> marketCoin = marketPriceDao.findByCoinName(coin);
		if (marketCoin.isPresent()) {

			marketCoin.get().setPriceInUsd(priseInUsd);
			marketPriceDao.save(marketCoin.get());
		} else {
			MarketPrice MarketPrice = new MarketPrice();
			MarketPrice.setCoinName(coin);
			MarketPrice.setPriceInUsd(priseInUsd);
			marketPriceDao.save(MarketPrice);
		}
		if (coinStatus.isPresent()) {
			Coin avtPrice = new Coin();
			coinStatus.get().setMarketPriceInUsd(priseInUsd);

			Coin avtPriceInUsd = coinDao.save(coinStatus.get());
			return new Response<>(SUCCESS_CODE, "Price set successfully", avtPriceInUsd);
		}
		return new Response<>(FAILURE_CODE, "Coin not found");
	}

	@Override
	public Response<Object> coinUpdate(Long coinId, Network network, CoinUpdateDto coinUpdateDto) {
		Optional<Coin> dataPresent = coinDao.findByCoinId(coinId);
		if (dataPresent.isPresent()) {
			dataPresent.get().setContractAddress(coinUpdateDto.getContractAddress());
			dataPresent.get().setCoinFullName(coinUpdateDto.getCoinFullName());
			dataPresent.get().setCoinShortName(coinUpdateDto.getCoinShortName());
			dataPresent.get().setCoinType(coinUpdateDto.getCoinType());
			dataPresent.get().setNetwork(network);
			dataPresent.get().setCoinImage(coinUpdateDto.getCoinImage());
			dataPresent.get().setCategory(coinUpdateDto.getCategory());
//			dataPresent.get().setCreateDate(new Date());
			coinDao.save(dataPresent.get());
			return new Response<>(200, "Coin Data Updated Successfully");
		}
		return new Response<>(205, "Coin Not Found");
	}

	@Override
	public Response<Object> coinDelete(Long coinId) {
		Optional<Coin> dataPresent = coinDao.findByCoinId(coinId);
		if (dataPresent.isPresent()) {
			coinDao.deleteById(coinId);
			return new Response<>(200, "Coin Deleted");
		}
		return new Response<>(205, "Coin Not Found");
	}

	@Override
	public Response<Object> coincategory(String category) {
		List<Coin> dataList = coinDao.findByCategory(category);
		if (!dataList.isEmpty()) {
			return new Response<>(200, "Coin list by category", dataList);
		}

		return new Response<>(205, "No coin by category");

	}

	@Override
	public Response<CoinPair> setCoinFavouriteUnFavourite(String baseCoin, String executableCoin) {
		Optional<CoinPair> isCoinExists = coinPairDao.findByBaseCoinAndExecutableCoinAndIsFavouriteFalse(baseCoin,
				executableCoin);
		if (isCoinExists.isPresent()) {
			isCoinExists.get().setIsFavourite(true);
			coinPairDao.save(isCoinExists.get());
			return new Response<>(200, "Coin Pair is Favourite Successfully");
		}
		Optional<CoinPair> isCoinExists1 = coinPairDao.findByBaseCoinAndExecutableCoinAndIsFavouriteTrue(baseCoin,
				executableCoin);
		if (isCoinExists1.isPresent()) {
			isCoinExists1.get().setIsFavourite(false);
			coinPairDao.save(isCoinExists1.get());
			return new Response<>(200, "Coin Pair is UnFavourite Successfully");
		}
		return new Response<>(201, "Coin Pair not found");
	}

	@Override
	public Response<List<CoinPair>> getCoinPairListfavourite(Long userId) {

		List<CoinPair> isListExists = coinPairDao.findByIsFavouriteTrue();
		if (!isListExists.isEmpty()) {
			return new Response<>(200, "List Fetched Successfully", isListExists);
		}
		return new Response<>(205, "Data Not Found");
	}
}
