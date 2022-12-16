package com.mobiloitte.microservice.wallet.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.model.Response;

/**
 * The Interface WalletTransactionHistoryManagementService.
 * 
 * @author Ankush Mohapatra
 */
public interface WalletTransactionHistoryManagementService {

	/**
	 * Gets the transaction history.
	 *
	 * @param coinName  the coin name
	 * @param txnType   the txn type
	 * @param fromDate  the from date
	 * @param toDate    the to date
	 * @param fkUserId
	 * @param page      the page
	 * @param pageSize  the page size
	 * @param userEmail
	 * @param userName
	 * @return the transaction history
	 */
	Response<Map<String, Object>> getTransactionHistory(String coinName, String txnType, Long fromDate, Long toDate,
			Long fkUserId, Integer page, Integer pageSize, String txnHash, String status, BigDecimal amount,
			String userName, String userEmail);

	/**
	 * Gets the transaction history of user.
	 *
	 * @param userId the user id
	 * @return the transaction history of user
	 */
	Response<List<CoinDepositWithdrawal>> getTransactionHistoryOfUser(Long userId);

	/**
	 * Gets the details of transaction.
	 *
	 * @param txnId the txn id
	 * @return the details of transaction
	 */
	Response<CoinDepositWithdrawal> getDetailsOfTransaction(Long txnId);

}
