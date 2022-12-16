package com.mobiloitte.microservice.wallet.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mobiloitte.microservice.wallet.dto.AdminBankDto;
import com.mobiloitte.microservice.wallet.dto.AdminUpiDto;
import com.mobiloitte.microservice.wallet.dto.DepositInrDto;
import com.mobiloitte.microservice.wallet.dto.FiatDto;
import com.mobiloitte.microservice.wallet.dto.FundUserDto;
import com.mobiloitte.microservice.wallet.dto.GetBalanceResponseDto;
import com.mobiloitte.microservice.wallet.dto.P2pHistoryDto;
import com.mobiloitte.microservice.wallet.dto.UpiUserDto;
import com.mobiloitte.microservice.wallet.dto.UserToAdminTransferDto;
import com.mobiloitte.microservice.wallet.dto.WithdrawDtoInr;
import com.mobiloitte.microservice.wallet.entities.UserToAdminTransfer;
import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.enums.FiatStatus;
import com.mobiloitte.microservice.wallet.model.Response;

/**
 * The Interface WalletManagementService.
 * 
 * @author Ankush Mohapatra
 */
public interface WalletManagementService {

	/**
	 * Creates the wallet.
	 *
	 * @param userId the user id
	 * @param randomId 
	 * @return the response
	 */
	Response<String> createWallet(Long userId, String randomId);

	/**
	 * Gets the wallet address.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the wallet address
	 */
	Response<Wallet> getWalletAddress(String coinName, Long fkUserId);

	/**
	 * Gets the balance.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the balance
	 */
	Response<GetBalanceResponseDto> getBalance(String coinName, Long fkUserId);

	/**
	 * Gets the wallet details by coin name and user.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the wallet details by coin name and user
	 */
	Response<Wallet> getWalletDetailsByCoinNameAndUser(String coinName, Long fkUserId);

	/**
	 * Gets the user all balance with coin list.
	 *
	 * @param userId the user id
	 * @return the user all balance with coin list
	 */
	Response<Map<String, Object>> getUserAllBalanceWithCoinList(Long userId);

	/**
	 * Gets the all user address.
	 *
	 * @param userId the user id
	 * @return the all user address
	 */
	Response<List<Map<String, Object>>> getAllUserAddress(Long userId);

	Response<String> updateWallet(BigDecimal walletBalance, Long userId, String coinName);

	Response<Map<String, Object>> getAllUserBalanceAndCoinListWithName(Long userId);

	Response<String> getAddressforERC20Tokens(String coinName, Long fkUserId);


	Response<Object> UserToAdminTransfer(Long userId, UserToAdminTransferDto userToAdminTransferDto);

	Response<List<com.mobiloitte.microservice.wallet.entities.UserToAdminTransfer>> getUserToAdminSendAmountDetails(
			Long page, Long pageSize);

	Response<Object> adminToUser2Transfer(Long referenceId);

	Response<java.util.Date> getLastTransactionOfUser(Long userId);

	Response<Map<String,BigDecimal>> getBalanceInUsd(String coinName, String converterCoin, Long fkUserId);

	Response<Map<String, Object>> saveInternalAmountTransfer(Long userId, String toCoinAddress, BigDecimal amount,
			String coinName);

	Response<Object> historyOfTransferAmountPerticularUser(Long userId,
			BigDecimal amount, String coinName, Integer page, Integer pageSize, Long fromdate, Long toDate);

	Response<List<UserToAdminTransfer>> escrowTransferAmount(Long userId, String status, Long userToAdminTransferId);

	Response<Object> requestSendToUser1FromUser2(Long userId, Long userToAdminTransferId);

	Response<Object> getInternaltransferHistory();

	

	Response<Object> requestCancel(Long userId, Long userToAdminTransferId);

	Response<Map<String, Object>> allEscrowTransferAmount(
			Long userId);

	Response<Object> disputeTrade(Long userId, Long referenceId);

	Response<Object> updateId(Long userId, String oldRandomId, String newRandomId);

	Response<Object> getEscrowList(Integer page, Integer pageSize);

	Response<Object> adminToUser2TransferByAdmin(Long referenceId);

	Response<Object> totalbalance(Long fkUserId);

	Response<Object> totalblockedbalance(Long fkUserId);

	Response<Object> totalbalanceSubtractedFromtotalblockedbalance(Long fkUserId);

	Response<Object> percentage(Long userId);

	Response<Object> getReferalTier1(String myReferralCode, Long userId);

	Response<Object> depositInr(Long userId, FiatDto fiatDto);

	Response<Object> getListDeposit(Long userId, Long fromDate, Long toDate, String email, String name, String utrNo,
			Integer page, Integer pageSize, BigDecimal amount);

	Response<Object> getListDepositAdmin(String utrNo, Integer page, Integer pageSize, FiatStatus fiatStatus);

	Response<Object> addDeposit(DepositInrDto depositInrDto, Long userId);

	Response<Object> addbank(AdminBankDto adminBankDto);

	Response<Object> addUpi(AdminUpiDto adminUpiDto);

	Response<Object> changestatus(FiatStatus fiatStatus, String utrNo, Long userId);

	Response<Object> getListWithdraw(Long userId, WithdrawDtoInr withdrawDtoInr);

	Response<Object> getListWithdrawInr(Long userId, Long fromDate, Long toDate, String email, String name, Integer page, Integer pageSize, BigDecimal amount);

	Response<Object> getListwithdrawAdmin(Integer page, Integer pageSize, FiatStatus fiatStatus);

	Response<Object> changestatuswithdraw(FiatStatus fiatStatus, Long userId, Long userId1, BigDecimal amount);

	Response<Object> addUpiUser(UpiUserDto upiUserDto, Long userId);

	Response<Object> userListUpi(Long userId);

	Response<Object> fundUser(Long userId);

	Response<Object> listUpi();

	Response<Object> bankList();

	Response<Object> getAmount();

	Response<Object> addUpiStatus(Boolean isVisible, String upiId);

	Response<Object> bankListUser();

	Response<Object> listUpiUser();

	Response<Object> addBankStatus(Boolean isVisible, Long bankId);

	Response<Object> bankListuser();

	Response<Object> listUpiuser();

	Response<Object> getView(Long userId, String upiId);

	Response<Object> getFullList(Integer page, Integer pageSize, String name, String upiId);

	Response<Wallet> getWalletAddressNew();

	Response<Object> addDepositFee(BigDecimal deposite, BigDecimal withdraw, Long userId);

	Response<String> updateWalletNew(BigDecimal walletBalance, Long userId, String coinName);

	Response<Object> addMinimumDeposit(BigDecimal minimumdeposite, BigDecimal minimumwithdraw, Long userId);

	Response<Object> getdepositview(Long fiatid);

	Response<Object> getwithdarwList(Long fiatid);

	Response<Object> updateINR(Long userId);

	Response<Object> updateFund(Long userId, BigDecimal walletBalance);

	Response<FundUserDto> fundUserParam(Long userId);

	Response<Object> getAveragePrice(Long userId);

	Response<Object> nomineeComision(BigDecimal walletBalance);

	Response<String> updateBlocked(BigDecimal blockedBalance, Long userId, String coinName);

	Response<Object> getAdminCommisssionAmount();

	Response<String> updateP2pData(P2pHistoryDto p2pHistoryDto, Long userId);

	Response<Object> updateLocked(Long userId, BigDecimal walletBalance);

	Response<Object> deleteUserBankAccount(Long userId, Long upiId);

	Response<Object> getSmallAssetHistory(String coinName, Long userId, Integer page, Integer pageSize, Long fromDate, Long toDate, BigDecimal amount);
	
}
