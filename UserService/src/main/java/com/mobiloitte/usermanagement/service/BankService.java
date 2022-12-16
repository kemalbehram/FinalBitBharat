package com.mobiloitte.usermanagement.service;

import com.mobiloitte.usermanagement.dto.BankDetailsDto;
import com.mobiloitte.usermanagement.model.Response;

public interface BankService {

	Response<Object> addUserBankAccount(Long userId, BankDetailsDto bankDetailsDto);

	Response<Object> deleteUserBankAccount(Long userId, Long bankDetailId);

	Response<Object> getUserBankAccountDetail(Long userId, Long bankDetailId);

	Response<Object> getUserBankAccountList(Long userId, Integer page, Integer pageSize);

	Response<Object> getUserBankAccountListWithoutPagination(Long userId);

	Response<Object> getBankData(String bankName, Long userId);

}