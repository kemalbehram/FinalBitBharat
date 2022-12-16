package com.mobiloitte.microservice.wallet.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.dto.CoinDto;
import com.mobiloitte.microservice.wallet.dto.CoinPairReqDto;
import com.mobiloitte.microservice.wallet.dto.CoinUpdateDto;
import com.mobiloitte.microservice.wallet.dto.MarketPriceDto;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.CoinPair;
import com.mobiloitte.microservice.wallet.enums.Network;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.CoinManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class CoinController.
 * 
 * @author Ankush Mohapatra
 */
@RestController
@RequestMapping(value = "/coin")
@Api(value = "Coin Management APIs")
public class CoinController implements OtherConstants {

	/** The coin management service. */
	@Autowired
	private CoinManagementService coinManagementService;

	/**
	 * Gets the coin list.
	 *
	 * @return the coin list
	 */
	@ApiOperation(value = "API to get all coins list")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "coin list fetched successfully"),
			@ApiResponse(code = 205, message = "coin not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@GetMapping(value = "/get-coin-list")
	public Response<Object> getCoinList() {
		return coinManagementService.getAllCoinList();
	}

	@PostMapping("/admin/add-Coin")
	public Response<Object> addCoin(@RequestBody CoinDto coinDto, @RequestParam Network network) {
		return coinManagementService.addCoin(coinDto, network);
	}

	@GetMapping(value = "/get-coin-list-Admin")
	public Response<Object> getCoinListAmin() {
		return coinManagementService.getCoinListAmin();
	}

	@PostMapping("/set-coin-favourite-true")
	public Response<Object> setCoinFavourite(@RequestHeader Long userId, @RequestParam Long coinId) {
		return coinManagementService.setCoinFavourite(userId, coinId);
	}

	@PostMapping("/set-coin-unfavourite")
	public Response<Object> setCoinUnfavourite(@RequestHeader Long userId, @RequestParam String coinName) {
		return coinManagementService.setCoinUnfavourite(coinName, userId);
	}

	@GetMapping(value = "/get-coin-list-by-favourite")
	public Response<List<Coin>> getCoinListfavourite(@RequestHeader Long userId) {
		return coinManagementService.getCoinListfavourite(userId);
	}

	@GetMapping(value = "/get-fiatcoin-list")
	public Response<List<Coin>> getFiatCoinList(String coinType) {
		return coinManagementService.getFiatCoinList(coinType);
	}

	/**
	 * Gets the coin pair list.
	 *
	 * @param baseCoin the base coin
	 * @return the coin pair list
	 */
	@ApiOperation(value = "API to get coin pair list of a given Base Coin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "coinpair list fetched successfully"),
			@ApiResponse(code = 205, message = "coin not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-coin-pair-list")
	public Response<List<Coin>> getCoinPairList(@RequestParam("baseCoin") String baseCoin) {
		if (baseCoin != null) {
			return coinManagementService.getCoinPairList(baseCoin);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the particular coin details.
	 *
	 * @param coinName the coin name
	 * @return the particular coin details
	 */
	@ApiOperation(value = "API to get a coin details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "coin details for a coin fetched successfully"),
			@ApiResponse(code = 205, message = "coin not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-coin-details")
	public Response<Coin> getParticularCoinDetails(@RequestParam("coinName") String coinName) {
		if (coinName != null) {
			return coinManagementService.getParticularCoinDetailsByCoinName(coinName);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the coin pair symbol list.
	 *
	 * @param baseCoin the base coin
	 * @param exeCoin  the exe coin
	 * @return the coin pair symbol list
	 */
	@ApiOperation(value = "API to get coinpair symbol list by base coin or all coin pair list")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "coinpair symbol list fetched successdully"),
			@ApiResponse(code = 205, message = "coin not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping(value = "/get-symbol-list")
	public Response<?> getCoinPairSymbolList(@RequestParam(value = "baseCoin", required = false) String baseCoin,
			@RequestParam(value = "exeCoin", required = false) String exeCoin) {
		if ((baseCoin != null && exeCoin == null && !baseCoin.equals(BLANK))
				|| (exeCoin != null && baseCoin == null && !exeCoin.equals(BLANK))) {
			return coinManagementService.getCoinPairSymbolList(baseCoin, exeCoin);
		} else {
			return coinManagementService.getAllCoinPairList();
		}
	}

	@ApiOperation(value = "API to update Market Price")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "market price updated successfully"),
			@ApiResponse(code = 205, message = "coin not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/update-market-price")
	public Response<String> getUpdatedMarketPrice(@RequestParam("coinName") String coinName) {
		if (coinName != null) {
			return coinManagementService.updateMarketPrice(coinName);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to fetch Market Price")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "market price fetched successfully"),
			@ApiResponse(code = 205, message = "Failure / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-market-price")
	public Response<MarketPriceDto> getMarketPrice(@RequestParam("coinName") String coinName) {
		if (coinName != null) {
			return coinManagementService.getMarketPriceFromCoinName(coinName);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to fetch Market Price-details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "market price fetched successfully"),
			@ApiResponse(code = 205, message = "Failure / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-market-price-details")
	public String getMarketPriceDetails() throws URISyntaxException, IOException {

		return coinManagementService.getMarketPriceDetails();

	}

	@GetMapping(value = "/get-symbol-list-for-ohlc")
	public Response<List<Object>> getCoinPairSymbolListOhlc(
			@RequestParam(value = "baseCoin", required = false) String baseCoin,
			@RequestParam(value = "exeCoin", required = false) String exeCoin) {
		if ((baseCoin != null && exeCoin == null && !baseCoin.equals(""))
				|| (exeCoin != null && baseCoin == null && !exeCoin.equals(""))) {
			return coinManagementService.getCoinPairSymbolListOhlc(baseCoin, exeCoin);
		} else {
			return coinManagementService.getAllCoinPairListOhlc();
		}
	}

	@PostMapping("/Set-coinPair-visibility")
	public Response<CoinPair> setVisibility(@RequestBody CoinPairReqDto coinPairReqDto) {
		return coinManagementService.setVisibility(coinPairReqDto);
	}

	@PostMapping("/get-coinPair-list")
	public Response<CoinPair> getCoinPairList() {
		return coinManagementService.getCoinPairList();
	}

	@GetMapping(value = "/get-coin-mobile-apps")
	public Response<Object> getCOinMobileApps(@RequestParam String coinName) {
		return coinManagementService.getCOinMobileApps(coinName);
	}

	@ApiOperation(value = "API to get coinpair symbol list by base coin or all coin pair list")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "coinpair symbol list fetched successdully"),
			@ApiResponse(code = 205, message = "coin not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping(value = "/get-symbol-list-2")
	public Response<?> getCoinPairSymbolList2(@RequestParam(value = "baseCoin", required = false) String baseCoin,
			@RequestParam(value = "exeCoin", required = false) String exeCoin) {
		if ((baseCoin != null && exeCoin == null && !baseCoin.equals(BLANK))
				|| (exeCoin != null && baseCoin == null && !exeCoin.equals(BLANK))) {
			return coinManagementService.getCoinPairSymbolList2(baseCoin, exeCoin);
		} else {
			return coinManagementService.getAllCoinPairList2();
		}
	}

	@PostMapping("/set-Coin-Visible")
	public Response<Object> setCoinVisble(@RequestParam String coinName, @RequestHeader Long userId,
			@RequestParam(required = false) Boolean isVisible, @RequestParam(required = false) Boolean iswithdraw,
			@RequestParam(required = false) Boolean isDeposit, @RequestParam(required = false) String remark) {
		return coinManagementService.setCoinVisble(coinName, userId, isVisible, iswithdraw, isDeposit, remark);
	}

	@PostMapping("/coin-data-update")
	public Response<Object> coinUpdate(@RequestParam Long coinId, @RequestParam(required = false) Network network,
			@RequestBody(required = false) CoinUpdateDto coinUpdateDto) {
		return coinManagementService.coinUpdate(coinId, network, coinUpdateDto);
	}

	@DeleteMapping("/coin-delete")
	public Response<Object> coinDelete(@RequestParam Long coinId) {
		return coinManagementService.coinDelete(coinId);
	}

	@GetMapping(value = "/get-coin-by-category")
	public Response<Object> coincategory(@RequestParam String category) {
		return coinManagementService.coincategory(category);
	}

	@PostMapping("/set-coinPair-favourite-unfavourite-true")
	public Response<CoinPair> setCoinFavouriteUnFavourite(@RequestParam String baseCoin,
			@RequestParam String executableCoin) {
		return coinManagementService.setCoinFavouriteUnFavourite(baseCoin, executableCoin);
	}
	
	@GetMapping(value = "/get-coinPair-list-by-favourite")
	public Response<List<CoinPair>> getCoinPairListfavourite(@RequestHeader Long userId) {
		return coinManagementService.getCoinPairListfavourite(userId);
	}
}
