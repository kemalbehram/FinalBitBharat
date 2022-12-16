package com.mobiloitte.microservice.wallet.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.dto.LimitRequestDto;
import com.mobiloitte.microservice.wallet.entities.LimitData;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.LimitManagement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/admin/limit-management")
@Api(value="Withdrawal Limit Management APIs")
public class LimitController implements OtherConstants {

	@Autowired
	private LimitManagement limitManagement;
	
	@ApiOperation(value = "API to get all limits")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-all")
	public Response<List<LimitData>> getAll() {
		return limitManagement.getAllLimitData();
	}
	
	@ApiOperation(value = "API to get limit details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-limit-details")
	public Response<LimitData> getDetails(@RequestParam("limitId") Long limitId) {
		return limitManagement.getLimitDataDetails(limitId);
	}
	
	@ApiOperation(value = "API to update limit details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/update-limit")
	public Response<Boolean> getDetails(@Valid @RequestBody LimitRequestDto limitRequestDto) {
		return limitManagement.saveLimitData(limitRequestDto);
	}
}
