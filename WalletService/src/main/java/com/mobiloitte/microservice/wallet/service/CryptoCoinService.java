package com.mobiloitte.microservice.wallet.service;

import java.math.BigDecimal;

import com.mobiloitte.microservice.wallet.model.CryptoRequestModel;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;

/**
 * The Interface CryptoCoinService.
 * @author Ankush Mohapatra
 */
public interface CryptoCoinService {
	
	/**
	 * Gets the address API.
	 *
	 * @param account the account
	 * @return the address API
	 */
	CryptoResponseModel getAddressAPI(String account);
	
	/**
	 * Gets the balance API.
	 *
	 * @param address the address
	 * @return the balance API
	 */
	BigDecimal getBalanceAPI(String address);
	
	/**
	 * Internal transfer API.
	 *
	 * @param coinRequest the coin request
	 * @return the crypto response model
	 */
	CryptoResponseModel internalTransferAPI(CryptoRequestModel coinRequest);
	
	/**
	 * Withdraw API.
	 *
	 * @param coinRequest the coin request
	 * @return the crypto response model
	 */
	CryptoResponseModel withdrawAPI(CryptoRequestModel coinRequest);

}
