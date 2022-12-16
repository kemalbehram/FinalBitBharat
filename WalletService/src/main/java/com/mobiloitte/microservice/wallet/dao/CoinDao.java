package com.mobiloitte.microservice.wallet.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.enums.Network;

/**
 * The Interface CoinDao.
 * 
 * @author Ankush Mohapatra
 */
public interface CoinDao extends JpaRepository<Coin, Long> {

	/**
	 * Find by coin short name.
	 *
	 * @param coinShortName the coin short name
	 * @return the optional
	 */
	Optional<Coin> findByCoinShortName(String coinShortName);

	/**
	 * Find by coin short name not.
	 *
	 * @param coinShortName the coin short name
	 * @return the list
	 */
	List<Coin> findByCoinShortNameNot(String coinShortName);

	/**
	 * Find by coin short name and coin type.
	 *
	 * @param coinShortName the coin short name
	 * @param coinType      the coin type
	 * @return the optional
	 */
	Optional<Coin> findByCoinShortNameAndCoinType(String coinShortName, String coinType);

	Long countByCoinType(String cryptoCurrency);

	List<Coin> findByCoinShortName(String coinShortName, Pageable of);

	List<Coin> findByCoinType(String coinType);

	Optional<Coin> findByCoinId(Long coinId);

	List<Coin> findAllByIsFavouriteTrue();

	Optional<Coin> findByCoinShortName(Long coinName);

	List<Coin> findAllByIsVisibleTrue();

	Optional<Coin> findByCoinShortNameAndNetwork(String coinName, Network network);

	Optional<Coin> findByCoinShortName(Object duplicate);

	Optional<Coin> findByCoinShortNameOrNetwork(String element1, Network bep20);

	Optional<Coin> findByNetwork(boolean equals);

	Optional<Coin> findByCoinShortNameAndNetwork(String element1, boolean equals);

	Optional<Coin> findByCoinShortNameOrNetwork(String element1, boolean equals);

	List<Coin> findAllByIsVisibleTrueAndNetwork(Network bep20);

	List<Coin> findAllByNetwork(Network bep20);

	void deleteByCoinId(Long coinId);

	List<Coin> findByCategory(String category);


}
