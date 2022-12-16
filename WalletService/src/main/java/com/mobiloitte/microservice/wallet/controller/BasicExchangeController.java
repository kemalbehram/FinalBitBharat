package com.mobiloitte.microservice.wallet.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.dto.ExchangeRequestDto;
import com.mobiloitte.microservice.wallet.enums.OrderType;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.serviceimpl.BasicExchangeServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class ExchangeController.
 * 
 * @author Ankush Mohapatra
 */
@RestController
@RequestMapping(value = "/basic-exchange")
@Api(value = "Basic Exchange Management APIs")
public class BasicExchangeController implements OtherConstants{

	/** The exchange management service. */
	@Autowired
	private BasicExchangeServiceImpl exchangeManagementService;
	
	/**
	 * Request place buy order.
	 *
	 * @param exchangeRequestDto the exchange request dto
	 * @param userId the user id
	 * @return the response
	 */
	@ApiOperation(value = "API to place BUY order")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Buy order placed and executed successfully"),
			@ApiResponse(code = 205, message = "Order placed failed / Insufficient wallet balance / No such coin found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/place-buy-order")
	public Response<String> requestPlaceBuyOrder(@Valid @RequestBody ExchangeRequestDto buyOrderDto, @RequestHeader Long userId, @RequestHeader String username) {
		if(userId !=null && userId!=0 && buyOrderDto != null) {
			return exchangeManagementService.placeBuyOrderFromWallet(buyOrderDto, userId, username);
		}
		else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}	
	
	/**
	 * Request place sell order.
	 *
	 * @param exchangeRequestDto the exchange request dto
	 * @param userId the user id
	 * @return the response
	 */
	@ApiOperation(value = "API to place SELL order")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sell order placed successfully"),
			@ApiResponse(code = 205, message = "Order placed failed / Insufficient wallet balance / No such coin found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/place-sell-order")
	public Response<String> requestPlaceSellOrder(@Valid @RequestBody ExchangeRequestDto sellOrderDto, @RequestHeader Long userId, @RequestHeader String username) {
		if(userId !=null && userId!=0 && sellOrderDto != null) {
			return exchangeManagementService.placeSellOrderFromWallet(sellOrderDto, userId, username);
		}
		else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}
	
	/**
	 * Gets the user order history.
	 *
	 * @param paymentType the payment type
	 * @param page the page
	 * @param pageSize the page size
	 * @param userId the user id
	 * @return the user order history
	 */
	@ApiOperation(value = "API to get user's order history")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User order history fetched successfully / No records found"),
			@ApiResponse(code = 205, message = "Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-user-order-history")
	public Response<Map<String, Object>> getUserOrderHistory(@RequestParam(required = true) OrderType orderType, @RequestParam("exeCoin") String exeCoin, @RequestParam(required = true, value="page") Integer page, 
			@RequestParam(required = true, value="pageSize") Integer pageSize, @RequestHeader Long userId) {
		if(userId !=null && userId!=0){
			return exchangeManagementService.getExchangeHistory(userId, orderType, exeCoin, page, pageSize);
		}else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}
}
