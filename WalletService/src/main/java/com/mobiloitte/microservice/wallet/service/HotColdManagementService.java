package com.mobiloitte.microservice.wallet.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.json.JsonParseException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.mobiloitte.microservice.wallet.dto.ApproveWithdrawRequestDto;
import com.mobiloitte.microservice.wallet.dto.FiatWithdrawRequestDto;
import com.mobiloitte.microservice.wallet.dto.HotToColdTransferDto;
import com.mobiloitte.microservice.wallet.dto.SearchAndFilterCoinWithdrawlDepositDto;
import com.mobiloitte.microservice.wallet.dto.WithdrawRequestDto;
import com.mobiloitte.microservice.wallet.dto.WithdrawRequestDtoUpdated;
import com.mobiloitte.microservice.wallet.enums.Network;
import com.mobiloitte.microservice.wallet.model.Response;

/**
 * The Interface HotColdManagementService.
 * 
 * @author Ankush Mohapatra
 */
public interface HotColdManagementService {

	/**
	 * Deposit.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the response
	 */
	Response<Map<String, Object>> deposit(String coinName, Long fkUserId, Integer page, Integer pageSize, String email);

	/**
	 * Withdraw balance.
	 *
	 * @param withdrawRequest the withdraw request
	 * @param fkUserId        the fk user id
	 * @return the response
	 */
	Response<String> withdrawBalance(WithdrawRequestDto withdrawRequest, Long fkUserId);

	/**
	 * Withdraw list.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the response
	 */
	Response<Map<String, Object>> withdrawList(String coinName, Long fkUserId, Integer page, Integer pageSize);

	/**
	 * Approve withdraw.
	 *
	 * @param approveWithdrawRequestDto the approve withdraw request dto
	 * @param fkUserId                  the fk user id
	 * @return the response
	 */
	Response<String> approveWithdraw(ApproveWithdrawRequestDto approveWithdrawRequestDto);

	/**
	 * Reject withdraw.
	 *
	 * @param withdrawToken the withdraw token
	 * @param fkUserId      the fk user id
	 * @return the response
	 */
	Response<String> rejectWithdraw(String withdrawToken);

	/**
	 * Withdraw fiat balance.
	 *
	 * @param fiatWithdrawRequest the fiat withdraw request
	 * @param fkUserId            the fk user id
	 * @return the response
	 */
	Response<String> withdrawFiatBalance(FiatWithdrawRequestDto fiatWithdrawRequest, Long fkUserId);

	Response<String> transferFromHotToCold(HotToColdTransferDto hotToColdTransferDto, Long fkUserId);

	Response<Map<String, Object>> withdrawListFilter(Long userId,
			SearchAndFilterCoinWithdrawlDepositDto serAndFilterCoinWithdrawlDepositDto);

	Response<Map<String, Object>> getDepositeFilter(Long userId,
			SearchAndFilterCoinWithdrawlDepositDto serAndFilterCoinWithdrawlDepositDto);

	BigDecimal getPrice(String coinName) throws JsonParseException, JsonMappingException, IOException, JSONException;

	Response<Object> getAdminAddress(String coinType);

	Response<Map<String, Object>> getTransactionHistoryNew(String coinName, String txnType, Long fromDate, Long toDate,
			Long fkUserId, Integer page, Integer pageSize, String txnHash, String status, BigDecimal amount,
			String userName, String userEmail);

	Response<Map<String, Object>> getDepositsErcBep(String coinName, Long userId, Integer page, Integer pageSize,
			String username, Network network, String email);

	Response<String> getApproveWithdrawBepErc(@Valid ApproveWithdrawRequestDto approveWithdrawRequestDto,
			Network network);

	Response<String> withdrawBalanceErcBep(@Valid WithdrawRequestDtoUpdated withdrawRequest, Long userId);

	Response<Map<String, Object>> referalAmountDeductNew(String coinName, Long fkUserId, BigDecimal amount);

	Response<Object> usdAmountConvert( Long userId) throws JsonParseException, JsonMappingException, IOException, JSONException;

	BigDecimal getPriceUsd(String coinName) throws JsonParseException, JsonMappingException, IOException, JSONException;

	Response<Object> usdAmountList();

	Response<Object> usdAmountConvertParticularUser(List<String> coinName,Long userId);

	Response<Object> inrAmountConvert(Long userId) throws JsonParseException, JsonMappingException, IOException, JSONException;

	BigDecimal getPriceUSD(String coinName) throws JsonParseException, JsonMappingException, IOException, JSONException;

	Response<Object> fullData(Long userId);

	Response<Object> getLivePrice(String coinName);

}
