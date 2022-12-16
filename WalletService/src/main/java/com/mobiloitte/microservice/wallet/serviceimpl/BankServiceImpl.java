package com.mobiloitte.microservice.wallet.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mobiloitte.microservice.wallet.dao.BankDao;
import com.mobiloitte.microservice.wallet.dto.BankDetailsDto;
import com.mobiloitte.microservice.wallet.entities.BankDetails;
import com.mobiloitte.microservice.wallet.enums.BankStatus;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.BankService;

@Service
public class BankServiceImpl implements BankService {

	private static final Logger LOGGER = LogManager.getLogger(BankServiceImpl.class);

	@Autowired
	private BankDao bankDao;

	@Override
	public Response<Object> addUserBankAccount(Long userId, BankDetailsDto bankDetailsDto) {
		try {
			Optional<BankDetails> bankExist = bankDao.findByAccountNoAndIsDeletedFalse(bankDetailsDto.getAccountNo());
			if (!bankExist.isPresent()) {
				BankDetails bankDetails = new BankDetails();
				bankDetails.setAccountHolderName(bankDetailsDto.getAccountHolderName());
				bankDetails.setAccountNo(bankDetailsDto.getAccountNo());
				bankDetails.setBankName(bankDetailsDto.getBankName());
				bankDetails.setContactNo(bankDetailsDto.getContactNo());
				bankDetails.setSwiftNo(bankDetailsDto.getSwiftNo());
				bankDetails.setIbanNo(bankDetailsDto.getIbanNo());
				bankDetails.setBankStatus(BankStatus.PENDING);
				bankDetails.setImageUrl(bankDetailsDto.getImageUrl());
				bankDetails.setIfsc(bankDetailsDto.getIfsc());
				bankDetails.setAccountType(bankDetailsDto.getAccountType());

				bankDetails.setUserId(userId);
				bankDetails.setIsDeleted(Boolean.FALSE);
				bankDao.save(bankDetails);
				return new Response<>(200, "User Bank Account Added Successfully");
			} else {
				return new Response<>(201, "User Bank Account Already Exist");
			}
		} catch (Exception e) {
			return new Response<>(203, "User Bank Account Not Added");
		}
	}

	@Override
	public Response<Object> deleteUserBankAccount(Long userId, Long bankDetailId) {
		try {
			Optional<BankDetails> accountExist = bankDao.findByBankDetailIdAndUserIdAndIsDeletedFalse(bankDetailId,
					userId);
			if (accountExist.isPresent()) {
				BankDetails bankDetails = accountExist.get();
				bankDetails.setIsDeleted(Boolean.TRUE);
				bankDao.save(bankDetails);
				LOGGER.info("Deleted Bank Account Id :{}", bankDetailId);
				return new Response<>(200, "User Bank Account Deleted Successfully");
			} else {
				return new Response<>(201, "User Bank Account Does Not Exist");
			}
		} catch (EmptyResultDataAccessException e) {
			return new Response<>(203, "User Bank Account Not Deleted");
		}
	}

	@Override
	public Response<Object> getUserBankAccountDetail(Long userId, Long bankDetailId) {
		try {
			Optional<BankDetails> accountDetail = bankDao.findByBankDetailIdAndUserIdAndIsDeletedFalse(bankDetailId,
					userId);
			if (accountDetail.isPresent()) {
				return new Response<>(200, "User Bank Account Detail Fetch Successfully", accountDetail);
			} else {
				return new Response<>(201, "User Bank Account Does Not exist");
			}
		} catch (Exception e) {
			return new Response<>(201, "User Bank Account Does Not exist");
		}
	}

	@Override
	public Response<Object> getUserBankAccountList(Long userId, Integer page, Integer pageSize) {
		try {
			Long getTotalCount;
			List<BankDetails> userBankList = bankDao.findByUserIdAndIsDeletedFalse(userId,
					PageRequest.of(page, pageSize, Direction.DESC, "bankDetailId"));
			if (!userBankList.isEmpty()) {
				getTotalCount = bankDao.countByUserIdAndIsDeletedFalse(userId);
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("RESULT_LIST", userBankList);
				responseMap.put("TOTAL_COUNT", getTotalCount);
				return new Response<>(200, "User Bank Account List Fetched Successfully", responseMap);
			} else {
				return new Response<>(201, "User Bank Account List Not Found");
			}
		} catch (Exception e) {
			return new Response<>(201, "User Bank Account List Not Found");
		}
	}

	@Override
	public Response<Object> getUserBankAccountListWithoutPagination(Long userId) {
		try {
			List<BankDetails> userBankList = bankDao.findByUserIdAndIsDeletedFalse(userId);
			if (!userBankList.isEmpty()) {
				return new Response<>(200, "User Bank Account List Fetched Successfully", userBankList);
			} else {
				return new Response<>(201, "User Bank Account List Not Found");
			}
		} catch (Exception e) {
			return new Response<>(201, "User Bank Account List Not Found");
		}
	}

}