package com.mobiloitte.microservice.wallet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.enums.OrderType;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.serviceimpl.AdminBasicExchangeServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/admin-basic-exchange")
@Api(value = "Admin Basic Exchange Management APIs")
public class AdminBasicExchangeController implements OtherConstants{

	@Autowired
	private AdminBasicExchangeServiceImpl  adminBasicExchangeService;

	@ApiOperation(value = "API to getAllExchangeHistory")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User order history fetched successfully / No records found"),
			@ApiResponse(code = 205, message = "Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-all-exchange-history")
	public Response<Map<String, Object>> getUserOrderHistory(@RequestParam(required = true) OrderType orderType, @RequestParam(required = true, value="page") Integer page, 
			@RequestParam(required = true, value="pageSize") Integer pageSize) {
		if(orderType !=null ){
			return adminBasicExchangeService.getAllExchangeHistory(orderType, page, pageSize);
		}else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}

	}

}




