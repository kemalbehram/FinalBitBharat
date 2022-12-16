package com.mobiloitte.microservice.wallet.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.WalletTransactionHistoryManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class TransactionHistoryController.
 * 
 * @author Ankush Mohapatra
 */
@RestController
@RequestMapping(value = "/admin/transaction-history")
@Api(value = "Transaction History APIs")
public class TransactionHistoryController implements OtherConstants {

	/** The transaction history service. */
	@Autowired
	private WalletTransactionHistoryManagementService transactionHistoryService;

	/**
	 * Gets the transaction history.
	 *
	 * @param coinName the coin name
	 * @param txnType  the txn type
	 * @param fromDate the from date
	 * @param toDate   the to date
	 * @param page     the page
	 * @param pageSize the page size
	 * @return the transaction history
	 */
	@ApiOperation(value = "API to get all transaction history")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "transaction history fetched successfully"),
			@ApiResponse(code = 205, message = "Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-all-transaction-history")
	public Response<Map<String, Object>> getTransactionHistory(@RequestParam(required = false) String coinName,
			@RequestParam(required = false) String txnType, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate, @RequestParam(required = false) Long fkUserId,
			@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) String txnHash,
			@RequestParam(required = false) String status, @RequestParam(required = false) BigDecimal amount,
			@RequestParam(required = false) String userName, @RequestParam(required = false) String userEmail) {
		return transactionHistoryService.getTransactionHistory(coinName, txnType, fromDate, toDate, fkUserId, page,
				pageSize, txnHash, status, amount, userName, userEmail);
	}

	/**
	 * Gets the user transaction history.
	 *
	 * @param userId the user id
	 * @return the user transaction history
	 */
	@ApiOperation(value = "API to get all transaction history of a User")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "user transaction history fetched succuessfully"),
			@ApiResponse(code = 205, message = "No transaction history found of User / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-user-transaction-history")
	public Response<List<CoinDepositWithdrawal>> getUserTransactionHistory(@RequestParam(required = true) Long userId) {
		if (userId != null && userId != 0) {
			return transactionHistoryService.getTransactionHistoryOfUser(userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}
	


	/**
	 * Gets the transaction details.
	 *
	 * @param txnId the txn id
	 * @return the transaction details
	 */
	@ApiOperation(value = "API to get transaction details of a Transaction History")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "transaction details for a transaction fetched successfully"),
			@ApiResponse(code = 205, message = "No transaction history found of txnId / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-transaction-details")
	public Response<CoinDepositWithdrawal> getTransactionDetails(@RequestParam(required = true) Long txnId) {
		if (txnId != null && txnId != 0) {
			return transactionHistoryService.getDetailsOfTransaction(txnId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

}
