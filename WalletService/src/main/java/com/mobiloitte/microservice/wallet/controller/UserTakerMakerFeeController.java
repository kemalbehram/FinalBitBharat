package com.mobiloitte.microservice.wallet.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.enums.FiatStatus;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.FeesAndAmountManagementService;
import com.mobiloitte.microservice.wallet.service.HotColdManagementService;
import com.mobiloitte.microservice.wallet.service.WalletManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Fees and Amount Management APIs for user")
public class UserTakerMakerFeeController implements OtherConstants {

	@Autowired
	private FeesAndAmountManagementService feesAndAmountManagementService;
	@Autowired
	private WalletManagementService walletManagementService;
	@Autowired
	private HotColdManagementService hotColdManagementService;

	@ApiOperation(value = "API to set coin taker maker fee")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "taker maker fee updated successfully"),
			@ApiResponse(code = 205, message = "updation failed / coin not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-taker-maker-fee")
	public Response<Map<String, BigDecimal>> getTakerMakerFee(@RequestParam String coinName) {
		if (coinName != null) {
			return feesAndAmountManagementService.getTakerMakerFee(coinName);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to get all transaction history")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "transaction history fetched successfully"),
			@ApiResponse(code = 205, message = "Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-all-transaction-history-USER")
	public Response<Map<String, Object>> getTransactionHistoryNew(@RequestParam(required = false) String coinName,
			@RequestParam(required = false) String txnType, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate, @RequestHeader Long userId, @RequestParam Integer page,
			@RequestParam Integer pageSize, @RequestParam(required = false) String txnHash,
			@RequestParam(required = false) String status, @RequestParam(required = false) BigDecimal amount,
			@RequestParam(required = false) String userName, @RequestParam(required = false) String userEmail) {
		return hotColdManagementService.getTransactionHistoryNew(coinName, txnType, fromDate, toDate, userId, page,
				pageSize, txnHash, status, amount, userName, userEmail);
	}
	
	@GetMapping("/Deposit-Inr-List")
	public Response<Object> getListDepositAdmin(@RequestParam(required = false) String utrNo,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) FiatStatus fiatStatus) {
		return walletManagementService.getListDepositAdmin(utrNo, page, pageSize, fiatStatus);
	}

	@GetMapping("/withdraw-Inr-List")
	public Response<Object> getListwithdrawAdmin(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) FiatStatus fiatStatus
			) {
		return walletManagementService.getListwithdrawAdmin(page, pageSize, fiatStatus);
	}

}
