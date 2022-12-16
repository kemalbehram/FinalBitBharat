package com.mobiloitte.ohlc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.ohlc.constants.OhlcConstants;
import com.mobiloitte.ohlc.model.ErrorResponse;
import com.mobiloitte.ohlc.model.Response;
import com.mobiloitte.ohlc.service.TradingViewExchangeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping(value = "exchange-feed")
@RestController
public class TradingViewExchageController implements OhlcConstants {

	@Autowired
	TradingViewExchangeService tradingViewExchangeService;

	/**
	 * Api to get trading history.
	 * 
	 * @param symbol
	 * @param resolution
	 * @param from
	 * @param to
	 * @return
	 */
	@ApiOperation(value = "Api to get trading history.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 401, message = "List not found") })
	@GetMapping(value = "history")
	public Map<String, Object> getHistory(@RequestParam String symbol, @RequestParam String resolution,
			@RequestParam long from, @RequestParam long to) {
		return tradingViewExchangeService.getOHLCData(symbol, resolution, from, to);
	}

	/**
	 * Api to get data feed config.
	 * 
	 * @return
	 */
	@ApiOperation(value = "Api to get data feed config.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), })
	@GetMapping(value = "config")
	public Map<String, Object> dataFeedConfig() {
		Map<String, Object> data = new HashMap<>();
		data.put("supports_search", true);
		data.put("supports_group_request", true);
		data.put("supports_marks", false);
		data.put("supports_time", true);
		ArrayList<String> resolutions = new ArrayList<>();
		resolutions.add("1");
		resolutions.add("5");
		resolutions.add("15");
		resolutions.add("30");
		resolutions.add("60");
		resolutions.add("180");
		resolutions.add("360");
		resolutions.add("720");
		resolutions.add("D");
		resolutions.add("W");
		data.put("supported_resolutions", resolutions);
		ArrayList<HashMap<String, String>> symbolTypes = new ArrayList<>();
		HashMap<String, String> allTypes = new HashMap<>();
		allTypes.put("name", "All Types");
		allTypes.put("value", "");
		symbolTypes.add(allTypes);
		HashMap<String, String> stock = new HashMap<>();
		stock.put("name", "Exchange");
		stock.put("value", "exchange");
		symbolTypes.add(stock);
		data.put("symbols_types", symbolTypes);
		ArrayList<HashMap<String, String>> exchanges = new ArrayList<>();
		HashMap<String, String> exchange = new HashMap<>();
		exchange.put("value", "");
		exchange.put("name", "Exchange");
		exchange.put("desc", "Exchange");
		exchanges.add(exchange);
		data.put("exchanges", exchanges);
		return data;
	}

	/**
	 * Api to get the current time stamp.
	 * 
	 * @return
	 */
	@ApiOperation(value = "Api to get the current timestamp.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), })
	@GetMapping(value = "time")
	public @ResponseBody long time() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * Api to get coin symbols.
	 * 
	 * @param symbol
	 * @return
	 */
	@ApiOperation(value = "Api to get coin symbols.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping(value = "symbols")
	public Map<String, Object> getSymbols(@RequestParam("symbol") String symbol) {
		return tradingViewExchangeService.getSymbol(symbol);
	}

	/**
	 * Api for search.
	 * 
	 * @param coin
	 * @return
	 */
	@ApiOperation(value = "Api for search.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@GetMapping(value = "search")
	public Map<String, Object> search(@RequestParam("coin") String coin) {
		return tradingViewExchangeService.getSearchResults(coin);
	}

	/**
	 * Api to get symbol info.
	 * 
	 * @param symbols
	 * @return
	 */
	@ApiOperation(value = "Api to get symbol info.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), })
	@GetMapping(value = "symbol_info")
	public Map<String, Object> dataFeedConfigs() {
		return tradingViewExchangeService.getDataFeedConfig();
	}

	/**
	 * Gets depth chart data.
	 * 
	 * @param currency
	 * @param exchangeCurrency
	 * @return
	 */
	@ApiOperation(value = "Api to get depth chart data.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), })
	@GetMapping(value = "depth-chart")
	public Response<Object> getDepthChartDataExchange(@RequestParam("currency") String currency,
			@RequestParam("exchangeCurrency") String exchangeCurrency) {
		return tradingViewExchangeService.getDepthChartDataExchange(currency, exchangeCurrency);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage(), "Bad Request"));
	}
	
	@ApiOperation(value = "Api to get trading history.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 401, message = "List not found") })
	@GetMapping(value = "ohlc-data")
	public Response<List<Map<String, Object>>> getHistoryType2(@RequestParam String symbol, @RequestParam String resolution, @RequestParam Long to) {
		return tradingViewExchangeService.getOHLCData2(symbol, resolution, to);
	}

}
