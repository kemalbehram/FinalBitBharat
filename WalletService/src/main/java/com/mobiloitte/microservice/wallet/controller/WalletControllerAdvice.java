package com.mobiloitte.microservice.wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.exception.CoinNotFoundException;
import com.mobiloitte.microservice.wallet.exception.StorageDetailsNotFoundException;
import com.mobiloitte.microservice.wallet.exception.TransactionHistoryNotFoundException;
import com.mobiloitte.microservice.wallet.exception.WalletHistoryNotFoundException;
import com.mobiloitte.microservice.wallet.exception.WalletNotFoundException;
import com.mobiloitte.microservice.wallet.model.ErrorResponse;

/**
 * The Class WalletControllerAdvice.
 * 
 * @author Ankush Mohapatra
 */
@ControllerAdvice
public class WalletControllerAdvice implements WalletConstants {
	/**
	 * Coin not found exception.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(CoinNotFoundException.class)
	public ResponseEntity<ErrorResponse> coinNotFoundException(CoinNotFoundException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(FAILURE_CODE, e.getMessage(), COIN_NOT_FOUND));
	}

	/**
	 * Wallet not found exception.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(WalletNotFoundException.class)
	public ResponseEntity<ErrorResponse> walletNotFoundException(WalletNotFoundException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(FAILURE_CODE, e.getMessage(), WALLET_NOT_FOUND));
	}

	/**
	 * Storage not found exception.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(StorageDetailsNotFoundException.class)
	public ResponseEntity<ErrorResponse> storageNotFoundException(StorageDetailsNotFoundException e) {
		return ResponseEntity.badRequest()
				.body(new ErrorResponse(FAILURE_CODE, e.getMessage(), NO_STORAGE_DETAILS_FOUND));
	}

	/**
	 * Bad request exception.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> badRequestException(BadRequestException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(BAD_REQUEST_CODE, e.getMessage(), BAD_REQUEST));
	}
	
	/**
	 * Transaction history not found exception.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(TransactionHistoryNotFoundException.class)
	public ResponseEntity<ErrorResponse> transactionHistoryNotFoundException(TransactionHistoryNotFoundException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(FAILURE_CODE, e.getMessage(), BAD_REQUEST));
	}
	
	/**
	 * Wallet history not found exception.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(WalletHistoryNotFoundException.class)
	public ResponseEntity<ErrorResponse> walletHistoryNotFoundException(WalletHistoryNotFoundException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(FAILURE_CODE, e.getMessage(), BAD_REQUEST));
	}
}
