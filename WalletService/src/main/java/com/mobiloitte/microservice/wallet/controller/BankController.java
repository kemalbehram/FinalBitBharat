package com.mobiloitte.microservice.wallet.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.dto.BankDetailsDto;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.BankService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Bank API")
public class BankController {

	@Autowired
	private BankService bankService;

	@ApiOperation(value = "API to Create Bank Account")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Account Created Successfully"),
			@ApiResponse(code = 201, message = "Failed to Create Account"),
			@ApiResponse(code = 400, message = "Invalid User Id") })
	@PostMapping("/add-user-bank-account")
	public Response<Object> createAccount(@RequestHeader Long userId,
			@Valid @RequestBody BankDetailsDto bankDetailsDto) {
		if (userId != null && userId != 0) {
			return bankService.addUserBankAccount(userId, bankDetailsDto);
		} else {
			throw new BadRequestException("Invalid User Id");
		}
	}

	@ApiOperation(value = "API to Delete Bank Account")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Account Deleted Successfully"),
			@ApiResponse(code = 400, message = "Invalid Bank Acoount Id") })
	@DeleteMapping("/delete-user-bank-account")
	public Response<Object> deleteUserBankAccount(@RequestHeader Long userId, @RequestParam Long bankDetailId) {
		if (userId != null && userId != 0) {
			if (bankDetailId != null && bankDetailId != 0) {
				return bankService.deleteUserBankAccount(userId, bankDetailId);
			} else {
				throw new BadRequestException("Invalid Bank Acoount Id");
			}
		} else {
			throw new BadRequestException("Invalid User Id");
		}
	}

	@ApiOperation(value = "API to Get Bank Account Detail")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Account Detail Fetched Successfully"),
			@ApiResponse(code = 201, message = "Account Detail Not Found") })
	@GetMapping("/get-user-account-detail")
	public Response<Object> getUserBankAccountDetail(@RequestHeader Long userId, @RequestParam Long bankDetailId) {
		if (userId != null && userId != 0) {
			if (bankDetailId != null && bankDetailId != 0) {
				return bankService.getUserBankAccountDetail(userId, bankDetailId);
			} else {
				throw new BadRequestException("Invalid Bank Acoount Id");
			}
		} else {
			throw new BadRequestException("Invalid User Id");
		}
	}

	@ApiOperation(value = "API to Get Bank Account List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Account List Fetched Successfully"),
			@ApiResponse(code = 201, message = "Account List Not Found") })
	@GetMapping("/get-user-account-list")
	public Response<Object> getUserBankAccountList(@RequestHeader Long userId,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
		if (userId != null && userId != 0) {
			if (pageSize != null && pageSize != 0) {
				return bankService.getUserBankAccountList(userId, page, pageSize);
			} else if (pageSize == null && page == null) {
				return bankService.getUserBankAccountListWithoutPagination(userId);
			} else {
				throw new BadRequestException("Invalid Page Size");
			}
		} else {
			throw new BadRequestException("Invalid User Id");
		}
	}
}