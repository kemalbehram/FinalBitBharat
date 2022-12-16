package com.mobiloitte.ohlc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.ohlc.enums.OrderSide;
import com.mobiloitte.ohlc.model.Response;
import com.mobiloitte.ohlc.service.TradeHistoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Jha Shubham
 *
 */
@RequestMapping(value = "trade-history")
@RestController
public class TradeHistoryController {

	@Autowired
	private TradeHistoryService tradeHistoryService;

	/**
	 * Api to get transaction history.
	 *
	 * @param baseCoin
	 * @param userId
	 * @param type
	 * @param from
	 * @param to
	 * @param page
	 * @param pageSize
	 * @return tradeHistory list
	 */
	@ApiOperation(value = "Api creation to get the transaction history.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "List fectched successfully."),
			@ApiResponse(code = 401, message = "Coin not found.") })
	@GetMapping(value = "get-list")
	public Response<Object> getTranscationHistory(@RequestParam(required = false) String executableCoin,
			@RequestParam(required = false) Long userId, @RequestParam(required = false) OrderSide type,
			@RequestParam(required = false) Long from, @RequestParam(required = false) Long to,
			@RequestParam Integer page, @RequestParam Integer pageSize) {
		return tradeHistoryService.getTradeHistory(executableCoin, userId, type, from, to, page, pageSize);
	}

	/**
	 * Get trade detail
	 * 
	 * @param exeCoin
	 * @param baseCoin
	 * @param transactionId
	 * @return transaction details.
	 */
	
	      
	@ApiOperation(value = "Api creation to get the transaction history detail.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Data fethed successfully."),
			@ApiResponse(code = 401, message = "Data not found.") })
	@GetMapping(value = "get-detail")
	public Response<Object> getTransactionDetail(@RequestParam String exeCoin, @RequestParam String baseCoin,
			@RequestParam Long transactionId) {
		return tradeHistoryService.getTradeDetail(exeCoin, baseCoin, transactionId);
	}
}
