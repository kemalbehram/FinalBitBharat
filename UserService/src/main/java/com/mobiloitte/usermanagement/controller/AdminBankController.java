package com.mobiloitte.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.dto.SearchAndFilterBankDto;
import com.mobiloitte.usermanagement.enums.BankStatus;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.AdminBankService;

@RestController
public class AdminBankController {

	@Autowired
	private AdminBankService adminBankService;

	@GetMapping("/admin/get-bank-account-detail")
	public Response<Object> getBankAccountDetail(@RequestHeader Long userId, @RequestParam Long bankDetailsId) {
		return adminBankService.getBankAccountDetails(userId, bankDetailsId);
	}

	@PostMapping("/admin/search-and-filter-bank-account-list")
	public Response<Object> searchAndFilterBankList(@RequestHeader Long userId,
			@RequestBody SearchAndFilterBankDto searchAndFilterBankDto) {
		return adminBankService.searchAndFilterBankAccountList(userId, searchAndFilterBankDto);
	}

	@PostMapping("/admin/approve-or-reject-bank-account")
	public Response<Object> approveOrRejectBankAccount(@RequestHeader Long userId, @RequestParam Long bankDetailId,
			@RequestParam BankStatus bankStatus) {
		return adminBankService.approveOrRejectBank(userId, bankDetailId, bankStatus);
	}
	
	@GetMapping("/list-view")
	public Response<Object> getView(@RequestParam Long userId,@RequestParam String bankName){
		return adminBankService.getView(userId,bankName);
	}

}