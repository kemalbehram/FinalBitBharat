package com.mobiloitte.microservice.wallet.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class CoinPair.
 * 
 * @author Ankush Mohapatra
 */
@Entity
@Table(name = "coin_pair")
public class CoinPair {

	/** the coinpair id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coin_pair_id")
	private Long coinPairId;

	/** the base coin. */
	@Column(name = "base_coin")
	private String baseCoin;

	/** the executable coin. */
	@Column(name = "executable_coin")
	private String executableCoin;

	private Boolean visible;
	private Boolean isFavourite;

	/**
	 * get coin pair id.
	 *
	 * @return the coin pair id
	 */
	public Long getCoinPairId() {
		return coinPairId;
	}

	/**
	 * sets coin pair id.
	 *
	 * @param coinPairId the new coin pair id
	 */
	public void setCoinPairId(Long coinPairId) {
		this.coinPairId = coinPairId;
	}

	/**
	 * gets base coin.
	 *
	 * @return the base coin
	 */
	public String getBaseCoin() {
		return baseCoin;
	}

	/**
	 * sets base coin.
	 *
	 * @param baseCoin the new base coin
	 */
	public void setBaseCoin(String baseCoin) {
		this.baseCoin = baseCoin;
	}

	/**
	 * gets executable coin.
	 *
	 * @return the executable coin
	 */
	public String getExecutableCoin() {
		return executableCoin;
	}

	/**
	 * sets executable coin.
	 *
	 * @param executableCoin the new executable coin
	 */
	public void setExecutableCoin(String executableCoin) {
		this.executableCoin = executableCoin;
	}

	/**
	 * @return the visible
	 */
	public Boolean getVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(Boolean isFavourite) {
		this.isFavourite = isFavourite;
	}

}
