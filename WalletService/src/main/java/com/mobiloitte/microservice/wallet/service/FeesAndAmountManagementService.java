package com.mobiloitte.microservice.wallet.service;

import java.math.BigDecimal;
import java.util.Map;

import com.mobiloitte.microservice.wallet.dto.CalculateFeesRequestDto;
import com.mobiloitte.microservice.wallet.dto.MarketPriceDto;
import com.mobiloitte.microservice.wallet.dto.TakerMakerFeesRequestDto;
import com.mobiloitte.microservice.wallet.model.Response;

/**
 * The Interface FeesAndAmountManagementService.
 * 
 * @author Ankush Mohapatra
 */
public interface FeesAndAmountManagementService {

	/**
	 * Sets the withdrawl fee.
	 *
	 * @param coinShortName the coin short name
	 * @param withdrawFee   the withdraw fee
	 * @return the response
	 */
	Response<String> setWithdrawlFee(String coinShortName, BigDecimal withdrawFee);

	/**
	 * Sets the minimum coin withdrawl amount.
	 *
	 * @param coinShortName      the coin short name
	 * @param minWithdrawlAmount the min withdrawl amount
	 * @param withdrawalAmountMax 
	 * @return the response
	 */
	Response<String> setMinimumCoinWithdrawlAmount(String coinShortName, BigDecimal minWithdrawlAmount, BigDecimal withdrawalAmountMax);

	/**
	 * Sets the taker makee fee.
	 *
	 * @param takerMakerFeesRequestDto the taker maker fees request dto
	 * @return the response
	 */
	Response<String> setTakerMakeeFee(TakerMakerFeesRequestDto takerMakerFeesRequestDto);

	/**
	 * Gets the profit fees.
	 *
	 * @param calculateFeesRequestDto the calculate fees request dto
	 * @return the profit fees
	 */
	Response<Map<String, Object>> getProfitFees(CalculateFeesRequestDto calculateFeesRequestDto);

	Response<Map<String, BigDecimal>> getTakerMakerFee(String coinName);

	Response<MarketPriceDto> setUpdateTradeFee(String coinName, BigDecimal tradeFee);

	Response<String> getUpdateMinimumdepositeAmount(String coinName, BigDecimal depositeAmount);

	Response<String> settransferFee(BigDecimal transferFee, BigDecimal minimumFee);
	

}
