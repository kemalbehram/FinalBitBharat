package com.mobiloitte.microservice.wallet.service;

import com.mobiloitte.microservice.wallet.dto.BankDetailsDto;
import com.mobiloitte.microservice.wallet.model.Response;

public interface BankService {

	Response<Object> addUserBankAccount(Long userId, BankDetailsDto bankDetailsDto);

	Response<Object> deleteUserBankAccount(Long userId, Long bankDetailId);

	Response<Object> getUserBankAccountDetail(Long userId, Long bankDetailId);

	Response<Object> getUserBankAccountList(Long userId, Integer page, Integer pageSize);

	Response<Object> getUserBankAccountListWithoutPagination(Long userId);

}