package com.mobiloitte.microservice.wallet.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.dto.UserToAdminTransferDto;
import com.mobiloitte.microservice.wallet.entities.InternalTransferAmount;
import com.mobiloitte.microservice.wallet.entities.UserToAdminTransfer;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.WalletManagementService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/wallet")
public class InternalTransferController implements OtherConstants {

	@Autowired
	private WalletManagementService walletManagementService;

	@ApiOperation(value = "API to transfer internal amount")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Amount transferessuccessfully"),
			@ApiResponse(code = 205, message = "No withdraw found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/transfer-internal-amount")
	public Response<Map<String, Object>> saveInternalAmountTransfer(@RequestHeader Long userId,
			@RequestParam String randomId, @RequestParam BigDecimal amount, @RequestParam String coinName) {
		if (coinName != null && amount != null && randomId != null) {
			return walletManagementService.saveInternalAmountTransfer(userId, randomId, amount, coinName);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "history for transfer-internal-amount of perticular ")
	@GetMapping("/get-transfer-internal-for-user")
	public Response<Object> historyOfTransferAmountPerticularUser(@RequestHeader Long userId,
			@RequestParam(required = false) BigDecimal amount, @RequestParam(required = false) String coinName, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Long fromdate, @RequestParam(required = false) Long toDate) {
		return walletManagementService.historyOfTransferAmountPerticularUser(userId, amount, coinName, page, pageSize,
				fromdate, toDate);
	}

	@ApiOperation(value = "user one send to amount user two")
	@PostMapping("/user-to-admin-transfer")
	public Response<Object> UserToAdminTransfer(@RequestHeader Long userId,
			@RequestBody UserToAdminTransferDto userToAdminTransferDto) {

		return walletManagementService.UserToAdminTransfer(userId, userToAdminTransferDto);

	}

	@ApiOperation(value = "API to transfer internal amount(pass status= SEND ,RECEIVE)")
	@GetMapping("/getData-ESCROW-transfer-api")
	public Response<List<UserToAdminTransfer>> escrowTransferAmount(@RequestHeader Long userId,
			@RequestParam String status, @RequestParam(required = false) Long userToAdminTransferId) {
		if (status.equals("SEND") || status.equals("RECEIVE")) {
			return walletManagementService.escrowTransferAmount(userId, status, userToAdminTransferId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to transfer internal amount(pass status= SEND ,RECEIVE)")
	@GetMapping("/allgetData-ESCROW-transfer-api")
	public Response<Map<String, Object>> allEscrowTransferAmount(@RequestHeader Long userId) {
		if (userId != null && userId != 0) {
			return walletManagementService.allEscrowTransferAmount(userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

	@GetMapping("/request-cancel-ESCROW")
	public Response<Object> requestCancel(@RequestHeader Long userId, @RequestParam Long userToAdminTransferId) {

		return walletManagementService.requestCancel(userId, userToAdminTransferId);
	}

	@PostMapping("/request-send-to-user1-from-user2")
	public Response<Object> requestSendToUser1FromUser2(@RequestHeader Long userId,
			@RequestParam Long userToAdminTransferId) {

		return walletManagementService.requestSendToUser1FromUser2(userId, userToAdminTransferId);
	}

	@PostMapping("/approvedBy-user1")
	Response<Object> adminToUser2Transfer(@RequestHeader Long userId, @RequestParam Long referenceId) {
		return walletManagementService.adminToUser2Transfer(referenceId);
	}

	@PostMapping("/dispute-by-user")
	Response<Object> disputeTrade(@RequestHeader Long UserId, @RequestParam Long referenceId) {
		return walletManagementService.disputeTrade(UserId, referenceId);
	}

}
