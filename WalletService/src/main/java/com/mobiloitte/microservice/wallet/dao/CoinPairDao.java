package com.mobiloitte.microservice.wallet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mobiloitte.microservice.wallet.entities.CoinPair;

/**
 * The Interface CoinDao.
 * 
 * @author Ankush Mohapatra
 */
public interface CoinPairDao extends JpaRepository<CoinPair, Long> {

	/**
	 * Find by base coin name.
	 *
	 * @param baseCoin the base coin name
	 * @return the optional
	 */
	List<CoinPair> findByBaseCoin(String baseCoin);

	/**
	 * Find by baseCoin And Executable.
	 *
	 * @param baseCoin       the base coin
	 * @param executableCoin the executable coin
	 * @return the list
	 */
	Optional<CoinPair> findByBaseCoinAndExecutableCoin(String baseCoin, String executableCoin);

	/**
	 * Find by executable coin.
	 *
	 * @param executable_coin the executable coin
	 * @return the list
	 */
	List<CoinPair> findByExecutableCoin(String executable_coin);

	@Query(value = "SELECT distinct base_coin FROM bit_bharat_wallet.coin_pair", nativeQuery = true)
	List<String> getDistinctByBaseCoin();

	@Query(value = "SELECT  executable_coin FROM bit_bharat_wallet.coin_pair where base_coin='getDistinctByBaseCoin'", nativeQuery = true)
	List<String> getExecutableCoin();

	@Query(value = "SELECT  executable_coin FROM bit_bharat_wallet.coin_pair where base_coin=?", nativeQuery = true)
	List<String> findBycoin(String baseCoin);

	List<CoinPair> findByBaseCoinAndVisible(String coin, Boolean true1);

	Optional<CoinPair> findByBaseCoinAndExecutableCoinAndIsFavouriteFalse(String baseCoin, String executableCoin);

	Optional<CoinPair> findByBaseCoinAndExecutableCoinAndIsFavouriteTrue(String baseCoin, String executableCoin);

	List<CoinPair> findByIsFavouriteTrue();
	

}
