package com.mobiloitte.microservice.wallet.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.dto.BlockBalanceDto;
import com.mobiloitte.microservice.wallet.dto.TransferBalanceDto;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.OrderManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class OrderController.
 * 
 * @author Ankush Mohapatra
 */
@RestController
@RequestMapping(value = "/orders")
@Api(value = "Orders Management APIs")
public class OrderController implements OtherConstants {

	/** The order management service. */
	@Autowired
	private OrderManagementService orderManagementService;

	/**
	 * Update wallet and block balance of user.
	 *
	 * @param orderRequestDto the order request dto
	 * @return the response
	 */
	@ApiOperation(value = "API to update user balance and block balance on cancel order and return block balance")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "updation successful and return block balance"),
			@ApiResponse(code = 205, message = "updation failed / Insufficient Block Balance / No wallet found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/return-block-balance")
	public Response<String> returnBlockBalance(@Valid @RequestBody BlockBalanceDto orderRequestDto) {
		if (orderRequestDto != null) {
			return orderManagementService.updateUserWalletAndBlockBalanceOnOrderCancellation(orderRequestDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Update wallet and block balance of user on order execution.
	 *
	 * @param orderRequestDto the order request dto
	 * @return the response
	 */
	@ApiOperation(value = "API to update user balance and block balance on order execution")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "updation successful"),
			@ApiResponse(code = 205, message = "updation failed / Insufficient Wallet Balance / No wallet found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/block-balance")
	public Response<String> verifyAndBlockBalance(@Valid @RequestBody BlockBalanceDto orderRequestDto) {
		if (orderRequestDto != null) {
			return orderManagementService.updateWalletBalanceOnOrderExecution(orderRequestDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Transfer balance.
	 *
	 * @param dto the dto
	 * @return the response
	 */
	@ApiOperation(value = "API to transfer balance from one user to other on Order Execution")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Transfer successful"),
			@ApiResponse(code = 205, message = "Transfer failed / No wallet found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping("/transfer-balance")
	Response<String> transferBalance(@Valid @RequestBody TransferBalanceDto dto) {
		if (dto != null) {
			return orderManagementService.transferBalanceOnOrderExecution(dto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

}
