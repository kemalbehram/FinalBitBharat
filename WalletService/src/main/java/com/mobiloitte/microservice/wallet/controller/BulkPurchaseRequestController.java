package com.mobiloitte.microservice.wallet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.dto.BulkPurchaseDto;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.BulkPurchaseService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class BulkPurchaseRequestController implements OtherConstants {
	@Autowired
	private BulkPurchaseService bulkPurchaseService;

	@PostMapping(value = "/bulk-purchase-request")
	public Response<String> setPurchaseRequest(@RequestBody BulkPurchaseDto bulkPurchaseDto) {

		return bulkPurchaseService.setPurchaseRequest(bulkPurchaseDto);

	}

	@GetMapping("get-bulk-purchase-request-details")
	public Response<Object> getPurchaseRequestDetails(@RequestParam Long requestId) {
		if (requestId != null) {

			return bulkPurchaseService.getPurchaseRequestDetails(requestId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API for Get Trade Details For Admin")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Advertisment Details For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("get-bulk-purchase-request-list")
	public Response<Map<String, Object>> getPurchaseRequestHistory(
			@RequestParam(required = true, value = "page") Integer page,
			@RequestParam(required = true, value = "pageSize") Integer pageSize) {

		return bulkPurchaseService.getPurchaseRequestHistory(page, pageSize);
	}

	@GetMapping(value = "/bulk-purchase-request-set-resolved")
	public Response<String> setPurchaseRequestResolved(@RequestParam Long requestId, @RequestParam Boolean isResolved) {
		if (requestId != null) {
			return bulkPurchaseService.setPurchaseRequestResolved(requestId, isResolved);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

}
