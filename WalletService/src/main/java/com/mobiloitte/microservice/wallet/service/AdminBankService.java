package com.mobiloitte.microservice.wallet.service;

import com.mobiloitte.microservice.wallet.dto.SearchAndFilterBankDto;
import com.mobiloitte.microservice.wallet.enums.BankStatus;
import com.mobiloitte.microservice.wallet.model.Response;

public interface AdminBankService {

	Response<Object> getBankAccountDetails(Long userId, Long bankDetailsId);

	Response<Object> searchAndFilterBankAccountList(Long userId, SearchAndFilterBankDto searchAndFilterBankDto);

	Response<Object> approveOrRejectBank(Long userId, Long bankDetailId, BankStatus bankStatus);

}