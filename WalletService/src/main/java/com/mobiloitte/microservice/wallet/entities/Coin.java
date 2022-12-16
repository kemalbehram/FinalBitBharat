package com.mobiloitte.microservice.wallet.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobiloitte.microservice.wallet.enums.Network;

/**
 * Entity Class : Coin.
 *
 * @author Ankush Mohapatra
 */
@Entity
@Table(name = "coin")
public class Coin {

	/** The coin id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coin_id")
	private Long coinId;

	/** The coin full name. */
	@Column(name = "coin_full_name")
	private String coinFullName;

	/** The coin image. */
	@Column(name = "coin_image")
	private String coinImage;

	/** The coin short name. */
	@Column(name = "coin_short_name")
	private String coinShortName;

	/** The wallet image url. */
	@Column(name = "wallet_image_url")
	private String walletImageUrl;

	/** The wallet background url. */
	@Column(name = "wallet_background_url")
	private String walletBackgroundUrl;

	/** The wallet dot url. */
	@Column(name = "wallet_dot_url")
	private String walletDotUrl;

	@Column(name = "basicex_bg_url")
	private String basicexBgUrl;

	/** The color code. */
	@Column(name = "color_code")
	private String colorCode;

	@Column(name = "rgb")
	private String rgb;

	/** The coin type. */
	@Column(name = "coin_type")
	private String coinType;

	private String category;
	/** The withdrawl fee. */
	@Column(name = "withdrawl_fee", scale = 6, precision = 15)
	private BigDecimal withdrawlFee;
	private BigDecimal minimumdepositeAmount;
	/** The withdrawal amount. */
	@Column(name = "withdrawal_amount", scale = 6, precision = 15)
	private BigDecimal withdrawalAmount;
	@Column(scale = 6, precision = 15)
	private BigDecimal internalTransferFee;
	private BigDecimal withdrawalAmountMax;
	private BigDecimal minimunInternalTransfer;
	@CreationTimestamp
	private Date createDate;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getMinimunInternalTransfer() {
		return minimunInternalTransfer;
	}

	public void setMinimunInternalTransfer(BigDecimal minimunInternalTransfer) {
		this.minimunInternalTransfer = minimunInternalTransfer;
	}

	public BigDecimal getInternalTransferFee() {
		return internalTransferFee;
	}

	public void setInternalTransferFee(BigDecimal internalTransferFee) {
		this.internalTransferFee = internalTransferFee;
	}

	@Enumerated(EnumType.STRING)
	private Network network;

	public BigDecimal getMinimumdepositeAmount() {
		return minimumdepositeAmount;
	}

	public void setMinimumdepositeAmount(BigDecimal minimumdepositeAmount) {
		this.minimumdepositeAmount = minimumdepositeAmount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getWithdrawalAmountMax() {
		return withdrawalAmountMax;
	}

	public void setWithdrawalAmountMax(BigDecimal withdrawalAmountMax) {
		this.withdrawalAmountMax = withdrawalAmountMax;
	}

	/** The taker fee. */
	@Column(name = "taker_fee", scale = 6, precision = 15)
	private BigDecimal takerFee;

	/** The maker fee. */
	@Column(name = "maker_fee", scale = 6, precision = 15)
	private BigDecimal makerFee;

	/** The coin description. */
	@Lob
	@Column(name = "coin_description", length = 20971520)
	private String coinDescription;

	/** The deposit fee. */
	@Column(name = "basic_buy_fee", scale = 6, precision = 15)
	private BigDecimal basicBuyFee;

	/** The basic sell fee. */
	@Column(name = "basic_sell_fee", scale = 6, precision = 15)
	private BigDecimal basicSellFee;

	/** The market price in usd. */
	@Column(name = "market_price_in_usd", scale = 6, precision = 15)
	private BigDecimal marketPriceInUsd;
	@Column(name = "market_price_in_inr", scale = 6, precision = 15)
	private BigDecimal marketPriceInInr;

	@Column(name = "market_price_in_eur", scale = 6, precision = 15)
	private BigDecimal marketPriceInEur;

	@Column(name = "threshold_price", scale = 6, precision = 15)
	private BigDecimal thresholdPrice;

	private Boolean isDeposit;

	public Boolean getIsDeposit() {
		return isDeposit;
	}

	public void setIsDeposit(Boolean isDeposit) {
		this.isDeposit = isDeposit;
	}

	// bi-directional many-to-one association to Wallet
	@JsonIgnore
	@OneToMany(mappedBy = "coin")
	private List<Wallet> wallets;

	private String contractAddress;

	/** The storage details. */
	// bi-directional many-to-one association to StorageDetail
	@JsonIgnore
	@OneToMany(mappedBy = "coin")
	private List<StorageDetail> storageDetails;

	private Boolean isVisible;
	private Boolean isWithdrawl;
	private Boolean isDeposite;
	private String remark;
	private int confermation;
	private BigDecimal tradeFee;

	private Boolean isFavourite;

	public Boolean getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(Boolean isFavourite) {
		this.isFavourite = isFavourite;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Boolean getIsWithdrawl() {
		return isWithdrawl;
	}

	public void setIsWithdrawl(Boolean isWithdrawl) {
		this.isWithdrawl = isWithdrawl;
	}

	public Boolean getIsDeposite() {
		return isDeposite;
	}

	public void setIsDeposite(Boolean isDeposite) {
		this.isDeposite = isDeposite;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getConfermation() {
		return confermation;
	}

	public void setConfermation(int confermation) {
		this.confermation = confermation;
	}

	/**
	 * Gets the coin id.
	 *
	 * @return the coin id
	 */

	public Long getCoinId() {
		return coinId;
	}

	/**
	 * Sets the coin id.
	 *
	 * @param coinId the new coin id
	 */
	public void setCoinId(Long coinId) {
		this.coinId = coinId;
	}

	/**
	 * Gets the coin full name.
	 *
	 * @return the coin full name
	 */
	public String getCoinFullName() {
		return coinFullName;
	}

	/**
	 * Sets the coin full name.
	 *
	 * @param coinFullName the new coin full name
	 */
	public void setCoinFullName(String coinFullName) {
		this.coinFullName = coinFullName;
	}

	/**
	 * Gets the coin image.
	 *
	 * @return the coin image
	 */
	public String getCoinImage() {
		return coinImage;
	}

	/**
	 * Sets the coin image.
	 *
	 * @param coinImage the new coin image
	 */
	public void setCoinImage(String coinImage) {
		this.coinImage = coinImage;
	}

	/**
	 * Gets the coin short name.
	 *
	 * @return the coin short name
	 */
	public String getCoinShortName() {
		return coinShortName;
	}

	/**
	 * Sets the coin short name.
	 *
	 * @param coinShortName the new coin short name
	 */
	public void setCoinShortName(String coinShortName) {
		this.coinShortName = coinShortName;
	}

	/**
	 * Gets the coin type.
	 *
	 * @return the coin type
	 */
	public String getCoinType() {
		return coinType;
	}

	/**
	 * Sets the coin type.
	 *
	 * @param coinType the new coin type
	 */
	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}

	/**
	 * Gets the withdrawl fee.
	 *
	 * @return the withdrawl fee
	 */
	public BigDecimal getWithdrawlFee() {
		return withdrawlFee;
	}

	/**
	 * Sets the withdrawl fee.
	 *
	 * @param withdrawlFee the new withdrawl fee
	 */
	public void setWithdrawlFee(BigDecimal withdrawlFee) {
		this.withdrawlFee = withdrawlFee;
	}

	/**
	 * Gets the coin description.
	 *
	 * @return the coin description
	 */
	public String getCoinDescription() {
		return coinDescription;
	}

	/**
	 * Sets the coin description.
	 *
	 * @param coinDescription the new coin description
	 */
	public void setCoinDescription(String coinDescription) {
		this.coinDescription = coinDescription;
	}

	/**
	 * Gets the wallets.
	 *
	 * @return the wallets
	 */
	public List<Wallet> getWallets() {
		return wallets;
	}

	/**
	 * Sets the wallets.
	 *
	 * @param wallets the new wallets
	 */
	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}

	/**
	 * Gets the storage details.
	 *
	 * @return the storage details
	 */
	public List<StorageDetail> getStorageDetails() {
		return storageDetails;
	}

	/**
	 * Sets the storage details.
	 *
	 * @param storageDetails the new storage details
	 */
	public void setStorageDetails(List<StorageDetail> storageDetails) {
		this.storageDetails = storageDetails;
	}

	/**
	 * Gets the withdrawal amount.
	 *
	 * @return the withdrawal amount
	 */
	public BigDecimal getWithdrawalAmount() {
		return withdrawalAmount;
	}

	/**
	 * Sets the withdrawal amount.
	 *
	 * @param withdrawalAmount the new withdrawal amount
	 */
	public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
	}

	/**
	 * Gets the taker fee.
	 *
	 * @return the taker fee
	 */
	public BigDecimal getTakerFee() {
		return takerFee;
	}

	/**
	 * Sets the taker fee.
	 *
	 * @param takerFee the new taker fee
	 */
	public void setTakerFee(BigDecimal takerFee) {
		this.takerFee = takerFee;
	}

	/**
	 * Gets the maker fee.
	 *
	 * @return the maker fee
	 */
	public BigDecimal getMakerFee() {
		return makerFee;
	}

	/**
	 * Sets the maker fee.
	 *
	 * @param makerFee the new maker fee
	 */
	public void setMakerFee(BigDecimal makerFee) {
		this.makerFee = makerFee;
	}

	/**
	 * Gets the basic buy fee.
	 *
	 * @return the basic buy fee
	 */
	public BigDecimal getBasicBuyFee() {
		return basicBuyFee;
	}

	/**
	 * Sets the basic buy fee.
	 *
	 * @param basicBuyFee the new basic buy fee
	 */
	public void setBasicBuyFee(BigDecimal basicBuyFee) {
		this.basicBuyFee = basicBuyFee;
	}

	/**
	 * Gets the basic sell fee.
	 *
	 * @return the basic sell fee
	 */
	public BigDecimal getBasicSellFee() {
		return basicSellFee;
	}

	/**
	 * Sets the basic sell fee.
	 *
	 * @param basicSellFee the new basic sell fee
	 */
	public void setBasicSellFee(BigDecimal basicSellFee) {
		this.basicSellFee = basicSellFee;
	}

	/**
	 * Gets the market price in usd.
	 *
	 * @return the market price in usd
	 */
	public BigDecimal getMarketPriceInUsd() {
		return marketPriceInUsd;
	}

	/**
	 * Sets the market price in usd.
	 *
	 * @param marketPriceInUsd the new market price in usd
	 */
	public void setMarketPriceInUsd(BigDecimal marketPriceInUsd) {
		this.marketPriceInUsd = marketPriceInUsd;
	}

	/**
	 * Gets the wallet image url.
	 *
	 * @return the wallet image url
	 */
	public String getWalletImageUrl() {
		return walletImageUrl;
	}

	/**
	 * Sets the wallet image url.
	 *
	 * @param walletImageUrl the new wallet image url
	 */
	public void setWalletImageUrl(String walletImageUrl) {
		this.walletImageUrl = walletImageUrl;
	}

	/**
	 * Gets the wallet background url.
	 *
	 * @return the wallet background url
	 */
	public String getWalletBackgroundUrl() {
		return walletBackgroundUrl;
	}

	/**
	 * Sets the wallet background url.
	 *
	 * @param walletBackgroundUrl the new wallet background url
	 */
	public void setWalletBackgroundUrl(String walletBackgroundUrl) {
		this.walletBackgroundUrl = walletBackgroundUrl;
	}

	/**
	 * Gets the wallet dot url.
	 *
	 * @return the wallet dot url
	 */
	public String getWalletDotUrl() {
		return walletDotUrl;
	}

	/**
	 * Sets the wallet dot url.
	 *
	 * @param walletDotUrl the new wallet dot url
	 */
	public void setWalletDotUrl(String walletDotUrl) {
		this.walletDotUrl = walletDotUrl;
	}

	/**
	 * Gets the color code.
	 *
	 * @return the color code
	 */
	public String getColorCode() {
		return colorCode;
	}

	/**
	 * Sets the color code.
	 *
	 * @param colorCode the new color code
	 */
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getBasicexBgUrl() {
		return basicexBgUrl;
	}

	public void setBasicexBgUrl(String basicexBgUrl) {
		this.basicexBgUrl = basicexBgUrl;
	}

	public String getRgb() {
		return rgb;
	}

	public void setRgb(String rgb) {
		this.rgb = rgb;
	}

	public BigDecimal getThresholdPrice() {
		return thresholdPrice;
	}

	public void setThresholdPrice(BigDecimal thresholdPrice) {
		this.thresholdPrice = thresholdPrice;
	}

	public BigDecimal getMarketPriceInInr() {
		return marketPriceInInr;
	}

	public void setMarketPriceInInr(BigDecimal marketPriceInInr) {
		this.marketPriceInInr = marketPriceInInr;
	}

	public BigDecimal getMarketPriceInEur() {
		return marketPriceInEur;
	}

	public void setMarketPriceInEur(BigDecimal marketPriceInEur) {
		this.marketPriceInEur = marketPriceInEur;
	}

	public BigDecimal getTradeFee() {
		return tradeFee;
	}

	public void setTradeFee(BigDecimal tradeFee) {
		this.tradeFee = tradeFee;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

}
