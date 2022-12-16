package com.mobiloitte.microservice.wallet.service;

import java.util.Map;

import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.model.Response;

/**
 * The Interface WalletManagementForCoinType2Service.
 * @author Ankush Mohapatra
 */
public interface WalletManagementForCoinType2Service {
	
	/**
	 * Gets the wallet address.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the wallet address
	 */
	Response<Wallet> getWalletAddress(String coinName, Long fkUserId);
	
	/**
	 * Deposit list for XRP.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the response
	 */
	Response<Map<String, Object>> depositListForXRPXLM(String coinName, Long fkUserId, Integer page, Integer pageSize, String email);
	
	/**
	 * Deposit list for ERC 20 tokens.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the response
	 */
	Response<Map<String, Object>> depositListForERC20Tokens(String coinName, Long fkUserId, Integer page, Integer pageSize, String email);
	
	Response<Map<String, Object>> depositListForUSDT(String coinName, Long fkUserId, Integer page, Integer pageSize, String email);
	
	/**
	 * Async transfer.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the response
	 */
	Response<String> asyncTransfer(String coinName, Long fkUserId);

}
