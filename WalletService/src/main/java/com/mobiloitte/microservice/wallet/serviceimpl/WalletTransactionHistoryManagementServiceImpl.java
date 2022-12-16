package com.mobiloitte.microservice.wallet.serviceimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.dao.CoinDepositWithdrawalDao;
import com.mobiloitte.microservice.wallet.dto.TransactionListDto;
import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.exception.TransactionHistoryNotFoundException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.WalletTransactionHistoryManagementService;

/**
 * The Class WalletTransactionHistoryManagementServiceImpl.
 * 
 * @author Ankush Mohapatra
 */
@Service("WalletTransactionHistoryManagementService")
public class WalletTransactionHistoryManagementServiceImpl
		implements WalletTransactionHistoryManagementService, OtherConstants, WalletConstants {

	/** The em. */
	@Autowired
	private EntityManager em;

	/** The coin deposit withdrawal dao. */
	@Autowired
	private CoinDepositWithdrawalDao coinDepositWithdrawalDao;
	@SuppressWarnings("unchecked")
	@Override
	public Response<Map<String, Object>> getTransactionHistory(String coinName, String txnType, Long fromDate,
			Long toDate, Long fkUserId, Integer page, Integer pageSize, String txnHash, String status,
			BigDecimal amount, String userName, String userEmail) {
		StringBuilder query = new StringBuilder(
				"select c.txnId, c.coinType, c.status, c.txnHash, c.units, c.txnTime, c.txnType, c.userName, c.userEmail,c.address from CoinDepositWithdrawal c");

		List<String> conditions = new ArrayList<>();

		if (fkUserId != null && !fkUserId.equals(BLANK)) {
			conditions.add(" c.fkUserId=:fkUserId ");
		}
		if (coinName != null && !coinName.equals(BLANK)) {
			conditions.add(" c.coinType=:cointype ");
		}
		if (txnType != null && !txnType.equals(BLANK)) {
			conditions.add(" c.txnType=:txntype ");
		}
		if (fromDate != null && !String.valueOf(fromDate).equals(BLANK)) {
			conditions.add(" c.txnTime >= :fromDate ");
		}
		if (toDate != null && !String.valueOf(toDate).equals(BLANK)) {
			conditions.add(" c.txnTime <= :toDate ");
		}
		if (txnHash != null && !txnHash.equals(BLANK)) {
			conditions.add("c.txnHash=:txnHash");
		}
		if (status != null && !status.equals(BLANK)) {
			conditions.add("c.status=:status");
		}
		if (amount != null && !amount.equals(BLANK)) {
			conditions.add("c.units=:units");
		}
		if (userName != null && !userName.equals(BLANK)) {
			conditions.add("c.userName=:userName");
		}
		if (userEmail != null && !userEmail.equals(BLANK)) {
			conditions.add("c.userEmail=:userEmail");
		}
		if (!conditions.isEmpty()) {
			query.append(" where ");
			query.append(String.join(" and ", conditions.toArray(new String[0])));
		}
		query.append(" order by c.txnId desc");
		Query createQuery = em.createQuery(query.toString());
		if (fkUserId != null && !fkUserId.equals(BLANK))
			createQuery.setParameter("fkUserId", fkUserId);

		if (coinName != null && !coinName.equals(BLANK))
			createQuery.setParameter("cointype", coinName);
		if (txnType != null && !txnType.equals(BLANK))
			createQuery.setParameter("txntype", txnType);
		if (fromDate != null && !String.valueOf(fromDate).equals(BLANK))
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null && !String.valueOf(toDate).equals(BLANK))
			createQuery.setParameter("toDate", new Date(toDate));
		if (txnHash != null && !txnHash.equals(BLANK)) {
			createQuery.setParameter("txnHash", txnHash);
		}
		if (status != null && !status.equals(BLANK)) {
			createQuery.setParameter("status", status);
		}
		if (amount != null && !amount.equals(BLANK)) {
			createQuery.setParameter("units", amount);
		}
		if (userName != null && !userName.equals(BLANK)) {
			createQuery.setParameter("userName", userName);
		}
		if (userEmail != null && !userEmail.equals(BLANK)) {
			createQuery.setParameter("userEmail", userEmail);
		}
		int filteredResultCount = createQuery.getResultList().size();
		createQuery.setFirstResult(page * pageSize);
		createQuery.setMaxResults(pageSize);
		List<Object[]> list = createQuery.getResultList();
		List<TransactionListDto> response = list.parallelStream().map(o -> {
			TransactionListDto dto = new TransactionListDto();
			dto.setTxnId((Long) o[0]);
			dto.setCoinType((String) o[1]);
			dto.setStatus((String) o[2]);
			dto.setTxnHash((String) o[3]);
			dto.setAmount((BigDecimal) o[4]);
			dto.setTxnTime((Date) o[5]);
			dto.setTxnType((String) o[6]);
			dto.setUserName((String) o[7]);
			dto.setUserEmail((String) o[8]);
			dto.setAddress((String) o[9]);
			return dto;
		}).collect(Collectors.toList());
		Map<String, Object> data = new HashMap<>();
		data.put(RESULT_LIST, response);
		data.put(TOTAL_COUNT, filteredResultCount);

		return new Response<>(SUCCESS_CODE, SUCCESS, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.
	 * WalletTransactionHistoryManagementService
	 * #getTransactionHistoryOfUser(java.lang.Long)
	 */
	@Override
	public Response<List<CoinDepositWithdrawal>> getTransactionHistoryOfUser(Long userId) {
		List<CoinDepositWithdrawal> getTransactionHistoryListOfUser = coinDepositWithdrawalDao.findByFkUserId(userId);
		if (getTransactionHistoryListOfUser != null && !getTransactionHistoryListOfUser.isEmpty()) {
			return new Response<>(SUCCESS_CODE, USER_TXN_HISTORY_FETCHED_SUCCUESSFULLY,
					getTransactionHistoryListOfUser);
		} else {
			throw new TransactionHistoryNotFoundException("No transaction history found for userId: " + userId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.
	 * WalletTransactionHistoryManagementService
	 * #getDetailsOfTransaction(java.lang.Long)
	 */
	@Override
	public Response<CoinDepositWithdrawal> getDetailsOfTransaction(Long txnId) {
		Optional<CoinDepositWithdrawal> getTransactionDetails = coinDepositWithdrawalDao.findById(txnId);
		if (getTransactionDetails.isPresent()) {
			return new Response<>(SUCCESS_CODE, TXN_DETAILS_FETCHED_FOR_A_TRANSACTION, getTransactionDetails.get());
		} else {
			throw new TransactionHistoryNotFoundException("No transaction history found for txnId: " + txnId);
		}
	}

}
