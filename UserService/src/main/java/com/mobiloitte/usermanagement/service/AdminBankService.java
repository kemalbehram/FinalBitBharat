package com.mobiloitte.usermanagement.service;

import com.mobiloitte.usermanagement.dto.SearchAndFilterBankDto;
import com.mobiloitte.usermanagement.enums.BankStatus;
import com.mobiloitte.usermanagement.model.Response;

public interface AdminBankService {

	Response<Object> getBankAccountDetails(Long userId, Long bankDetailsId);

	Response<Object> searchAndFilterBankAccountList(Long userId, SearchAndFilterBankDto searchAndFilterBankDto);

	Response<Object> approveOrRejectBank(Long userId, Long bankDetailId, BankStatus bankStatus);

	Response<Object> getView(Long userId, String bankName);

}