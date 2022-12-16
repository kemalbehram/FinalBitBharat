package com.mobiloitte.p2p.content.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.p2p.content.constants.OtherConstants;
import com.mobiloitte.p2p.content.dto.MarketPriceDto;
import com.mobiloitte.p2p.content.dto.P2PAdvertisementDto;
import com.mobiloitte.p2p.content.dto.PaymentMethodDto;
import com.mobiloitte.p2p.content.dto.PaymentMethodUpdateDto;
import com.mobiloitte.p2p.content.dto.SearchAndFilterDto;
import com.mobiloitte.p2p.content.dto.TradeCountDto;
import com.mobiloitte.p2p.content.enums.DisputeStatus;
import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.exception.BadRequestException;
import com.mobiloitte.p2p.content.model.P2PAdvertisement;
import com.mobiloitte.p2p.content.model.Response;
import com.mobiloitte.p2p.content.service.AdminService;
import com.mobiloitte.p2p.content.service.CreateAdvertisementService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Kumar Arjun
 *
 */

@RestController
public class CreateAdvertisementController implements OtherConstants {
	@Autowired
	CreateAdvertisementService createAdvertisementService;
	@Autowired
	AdminService adminService;

	@ApiOperation(value = "API for Create Advertisment For Buy and Sell")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Create Advertisment For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping("/add-advertisment")
	public Response<Object> placeordeForbuySell(@RequestBody P2PAdvertisementDto p2PAdvertisementDto,
			@RequestHeader Long userId) {
		if (userId != null && userId != 0) {
			return createAdvertisementService.createAdvertisement(p2PAdvertisementDto, userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API for Get Advertisment For Buy and Sell")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Advertisment Details For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/get-advertisement-list")
	public Response<Map<String, Object>> getBuySEllAdvertisementDetails(

			@RequestParam(required = false) OrderType orderType, @RequestHeader Long userId,

			@RequestParam(required = true, value = "page") Integer page,

			@RequestParam(required = true, value = "pageSize") Integer pageSize) {
		if (page != null && pageSize != null) {
			return createAdvertisementService.getBuySEllAdvertisementDetails(orderType, userId, page, pageSize);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}

	}

	@GetMapping("/get-advertisement-list-user")
	public Response<Map<String, Object>> getBuySEllAdvertisementDetailsUser(

			@RequestParam(required = false) OrderType orderType, @RequestParam Long userId,

			@RequestParam(required = true, value = "page") Integer page,

			@RequestParam(required = true, value = "pageSize") Integer pageSize) {
		if (page != null && pageSize != null) {
			return createAdvertisementService.getBuySEllAdvertisementDetailsNew(orderType, userId, page, pageSize);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}

	}

	@GetMapping("/get-advertisement-list-for-mobile-app")
	public Response<Map<String, Object>> getPostTradeListForApp(

			@RequestParam(required = false) OrderType orderType, @RequestHeader Long userId) {

		return createAdvertisementService.getPostTradeListForApp(orderType, userId);

	}

	@ApiOperation(value = "API for Get Advertisment For Buy and Sell")

	@ApiResponses(value = {

			@ApiResponse(code = 200, message = "API for Get Advertisment Details For Buy and Sell successfully"),

			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),

			@ApiResponse(code = 400, message = "Bad Request"),

			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@GetMapping("/get-advertisement-list-globally")
	public Response<Map<String, Object>> getBuySEllAdvertisementList(

			@RequestParam(required = false) OrderType orderType,

			@RequestParam(required = false) String cryptoCoin,

			@RequestParam(required = true, value = "page") Integer page,

			@RequestParam(required = true, value = "pageSize") Integer pageSize) {
		if (page != null && pageSize != null) {

			return createAdvertisementService.getBuySEllAdvertisementList(orderType, page, pageSize, cryptoCoin);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@ApiOperation(value = "API for Get Advertisment For Buy and Sell For User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Advertisment Details For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/get-open-trade-list")
	public Response<Map<String, Object>> getOpenTradeDetails(@RequestHeader Long userId,
			@RequestParam(required = false, value = "page") Integer page, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) String coin, @RequestParam(required = false) OrderType orderType,
			@RequestParam(required = false, value = "pageSize") Integer pageSize,
			@RequestParam(required = false) Long toDate) {
		if (page != null && pageSize != null) {
			return createAdvertisementService.getOpenTradeDetails(userId, page, fromDate, coin, orderType, pageSize,
					toDate);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@ApiOperation(value = "API for Edit Advertisment For Buy and Sell")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Edit Advertisment For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Edited/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping("/edit-advertisment")
	public Response<Object> editAdvertisement(@RequestHeader Long userId,
			@RequestBody P2PAdvertisementDto p2pBuySellDto, @RequestParam Long peerToPeerExchangeId) {
		if (p2pBuySellDto != null && peerToPeerExchangeId != null && peerToPeerExchangeId != 0 && userId != null) {
			return createAdvertisementService.editAdvertisement(userId, p2pBuySellDto, peerToPeerExchangeId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API for Get All Details Of Buy-Sell Through OrderType For admin")

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get All Details Of Buy-Sell Through OrderType For admin successfully"),
			@ApiResponse(code = 205, message = "Data not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@GetMapping("/find-all-buy-sell-for-admin")
	public Response<List<P2PAdvertisement>> getAllBuyDetailsForAdmin(@RequestParam OrderType orderType,
			@RequestParam(required = true) Integer page, @RequestParam(required = true) Integer pageSize) {
		if (orderType != null) {
			return createAdvertisementService.getAllBuyAdvertismentForAdmin(orderType, page, pageSize);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API for Get Details After press Buy Button")

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Details After press Buy Button successfully"),
			@ApiResponse(code = 205, message = "Data not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@GetMapping("/get-details-after-press-buy-button")
	public Response<Object> getCoinDetailsAfterBuying(@RequestParam Long peerToPeerExchangeId) {
		if (peerToPeerExchangeId != null && peerToPeerExchangeId != 0) {
			return createAdvertisementService.getDetailsOfBuyByfkId(peerToPeerExchangeId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API for Get Details After press Sell Button")

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Details After press Sell Button successfully"),
			@ApiResponse(code = 205, message = "Data not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@GetMapping("/get-details-after-press-sell-button")
	public Response<Object> getCoinDetailsAfterSelling(@RequestParam Long peerToPeerExchangeId) {
		if (peerToPeerExchangeId != null && peerToPeerExchangeId != 0) {
			return createAdvertisementService.getDetailsOfSellByfkId(peerToPeerExchangeId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API for Get Values Of Total Input BitCoins")

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Values Of Total Input BitCoins successfully"),
			@ApiResponse(code = 205, message = "Data not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@GetMapping("/converter")
	public Response<Object> getBitCoinConvertor(@RequestParam(required = false) BigDecimal amount,
			@RequestParam Long peerToPeerExchangeId, @RequestParam(required = false) BigDecimal noOfCoins) {
		if (peerToPeerExchangeId != null && peerToPeerExchangeId != 0) {
			return createAdvertisementService.getBitCoinInUSD(amount, peerToPeerExchangeId, noOfCoins);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API for Save Details and Perform Trading")

	@ApiResponses(value = { @ApiResponse(code = 200, message = "API for Save Details and Perform Trading successfully"),
			@ApiResponse(code = 205, message = "Data not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping("/send-trade-request")
	public Response<Object> sendTradeRequest(@RequestHeader Long userId, @RequestParam Long peerToPeerExchangeId,
			@RequestParam(required = false) BigDecimal amountInRange,
			@RequestParam(required = false) BigDecimal restrictAmount) {
		if (userId != null && peerToPeerExchangeId != null && amountInRange != null) {
			return createAdvertisementService.sendTradeRequest(userId, peerToPeerExchangeId, amountInRange,
					restrictAmount);
		} else {
			return createAdvertisementService.sendTradeRequestUsingRestrictAmount(userId, peerToPeerExchangeId,
					restrictAmount);
		}
	}

	@ApiOperation(value = "API for Cancel Buy-sell Trade")

	@ApiResponses(value = { @ApiResponse(code = 200, message = "API for Cancel Buy-sell Trade successfully"),
			@ApiResponse(code = 205, message = "Data not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@PostMapping("/send-message-after-cancel-trade-button")
	public Response<Object> sendMessageAfterPressCancelButton(@RequestHeader Long userId, @RequestParam String tradeId,
			@RequestParam(required = false) String cancelReason) {
		if (tradeId != null && userId != null && userId != 0) {
			return createAdvertisementService.sendMessageAfterPressCancelButton(userId, tradeId, cancelReason);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API For Send Message After Paid")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "API for Cancel Buy-sell Trade successfully"),
			@ApiResponse(code = 205, message = "Data not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@PostMapping("/send-message-after-paid-button")
	public Response<Object> sendMessageAfterPressPaidButton(@RequestParam String tradeId, @RequestHeader Long userId) {
		if (tradeId != null && userId != null && userId != 0) {
			return createAdvertisementService.sendMessageAfterPressPaidButton(tradeId, userId);// UserId Of Trading //
																								// Partner
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@PostMapping("/marketprice")
	public Response<Object> setMarketPrice(@RequestBody MarketPriceDto marketpricedto) {
		if (marketpricedto != null) {
			return createAdvertisementService.setMarketPrice(marketpricedto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API For Release Bitcoins")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "API for BITCOIN_RELEASE_SUCCESSFULLY"),
			@ApiResponse(code = 205, message = "Data not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@PostMapping("/release-bitcoins")
	public Response<Object> forReleaseBitcoins(@RequestParam String tradeId, @RequestHeader Long userId) {
		if (tradeId != null && userId != null && userId != 0) {
			return createAdvertisementService.forReleaseBitcoins(tradeId, userId);

		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@PostMapping("/set-enable-disable-all-advertisements")
	public Response<Object> setEnableDisable(@RequestParam OrderType orderType,
			@RequestParam ExchangeStatusType statusType, @RequestHeader Long userId) {
		if (userId != null && userId != 0 && statusType != null) {
			return createAdvertisementService.setEnableDisable(orderType, statusType, userId);

		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@PostMapping("/set-enable-disable-advertisements")
	public Response<Object> setEnableDisableParticular(@RequestHeader Long userId,
			@RequestParam Long peerToPeerExchangeId,
			@RequestParam(required = false) ExchangeStatusType exchangeStatusType,
			@RequestParam(required = false) OrderType orderType) {

		return createAdvertisementService.setEnableDisableParticular(userId, peerToPeerExchangeId, exchangeStatusType,
				orderType);

	}

	@ApiOperation(value = "API for Get Trading-details-list")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "API for Get Trading list  successfully"),
			@ApiResponse(code = 205, message = "Data Not Found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/get-trading-details-list")
	public Response<Map<String, Object>> getAllTradeDetailsList(@RequestParam(required = false) TradeStatus tradeStatus,
			@RequestHeader Long userId, @RequestParam(required = true, value = "page") Integer page,
			@RequestParam(required = true, value = "pageSize") Integer pageSize) {
		return createAdvertisementService.getAllTradeDetailsList(tradeStatus, userId, page, pageSize);

	}

	@ApiOperation(value = "API for Get Trading-details-list")

	@ApiResponses(value = { @ApiResponse(code = 200, message = "API for Get Trading list  successfully"),

			@ApiResponse(code = 205, message = "Data Not Found/ Exception Occured Internally"),

			@ApiResponse(code = 400, message = "Bad Request"),

			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@GetMapping("/get-trading-details-list-between-paymentWindow")
	public Response<Object> getAllTradeDetailsListForPaymentWindow(@RequestHeader Long userId) {
		return createAdvertisementService.getAllTradeDetailsListForPaymentWindow(userId);

	}

	@ApiOperation(value = "API for Filter Addvertisement list Through Amount")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Filter Addvertisement list   successfully"),
			@ApiResponse(code = 205, message = "Data Not Found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@GetMapping("/filter-addvertisement-list-through-amount")
	public Response<Map<String, Object>> getAddvertisementListThroughAmount(
			@RequestParam(required = false) BigDecimal amount, @RequestParam PaymentType paymentType,
			@RequestParam String currency, @RequestParam String country,
			@RequestParam(required = true, value = "page") Integer page,
			@RequestParam(required = true, value = "pageSize") Integer pageSize) {
		return createAdvertisementService.getAddvertisementListThroughAmount(amount, paymentType, currency, country,
				page, pageSize);

	}

	@PostMapping("/trade-status-check")
	public Response<Object> forStatusChecking(@RequestParam Long peerToPeerExchangeId, @RequestParam String tradeId) {
		if (peerToPeerExchangeId != null) {
			return createAdvertisementService.forStatusChecking(peerToPeerExchangeId, tradeId);

		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@GetMapping("/after-press-dispute-button")
	public Response<Object> forStatusDisputed(@RequestHeader Long userId, @RequestParam String tradeId,
			@RequestParam DisputeStatus disputeStatus) {
		if (tradeId != null && userId != null && disputeStatus != null) {
			return createAdvertisementService.forStatusDisputed(userId, tradeId, disputeStatus);

		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@GetMapping("/search-and-filters-advertisement")
	public Response<Object> getAdvertisementSearch(@RequestHeader(required = false) Long userId,
			@RequestParam(required = false) Long userId2, @RequestParam(required = false) Long peerToPeerExchangeId,
			@RequestParam(required = false) String userName,
			@RequestParam(required = false) ExchangeStatusType orderStatus,
			@RequestParam(required = false) String country, @RequestParam(required = false) String currency,
			@RequestParam(required = false) Long amount, @RequestParam(required = false) PaymentType paymentType,
			@RequestParam(required = false) OrderType orderType, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String cryptoCoin,
			@RequestParam(required = false) BigDecimal margin, @RequestParam(required = false) Integer paymentWindow) {

		return createAdvertisementService.getAdvertisementSearch(userId, userId2, peerToPeerExchangeId, userName,
				orderStatus, country, currency, amount, paymentType, orderType, fromDate, toDate, page, pageSize,
				cryptoCoin, margin, paymentWindow);

	}

	@GetMapping("/search-and-filters-trade-list")
	public Response<Object> getTradingSearch(@RequestHeader(required = false) Long userId,
			@RequestParam(required = false) String search, @RequestParam(required = false) TradeStatus tradeStatus,
			@RequestParam(required = false) String country, @RequestParam(required = false) PaymentType paymentType,
			@RequestParam(required = false) OrderType type, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate, @RequestParam(required = false) DisputeStatus disputeStatus,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Long userId2) {
		return createAdvertisementService.getTradingSearch(userId, search, tradeStatus, country, paymentType, type,
				fromDate, toDate, disputeStatus, page, pageSize, userId2);

	}

	@ApiOperation(value = "API for get Trade Details  by userId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/get-trade-count-by-userId")
	public Response<TradeCountDto> getTradeCount(@RequestParam Long userId) {
		if (userId != null && userId != 0) {
			return createAdvertisementService.getTradeCount(userId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@GetMapping("/get-trade-status")
	public Response<Object> getTradeStataus(@RequestParam String tradeId) {
		return createAdvertisementService.getTradeStatus(tradeId);
	}

	@GetMapping("/get-advertisements-status")
	public Response<Object> getAdvertisementStataus(@RequestParam OrderType orderType, @RequestHeader Long userId) {
		return createAdvertisementService.getAdvertisementStataus(orderType, userId);
	}

	@GetMapping("/get-bank-details-status")
	public Response<Object> getBankDetails(@RequestParam Long userId, @RequestParam Long p2pId) {
		return createAdvertisementService.getBankDetails(userId, p2pId);
	}

	@GetMapping("/find-block-balance")
	public BigDecimal getBlockBalance(@RequestParam Long userId, @RequestParam String coinName) {

		return createAdvertisementService.getBlockBalance(userId, coinName);

	}

	@ApiOperation(value = "API for Get Advertisment For Buy and Sell For User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Advertisment Details For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/get-completed-trade-list")
	public Response<Map<String, Object>> getCompletedTradeDetails(@RequestHeader Long userId,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "pageSize") Integer pageSize) {
		if (page != null && pageSize != null) {
			return createAdvertisementService.getCompletedTradeDetails(userId, page, pageSize);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@DeleteMapping("/Delete-advertisement")
	public Response<Object> deleteAdvertisement(@RequestHeader Long userId, @RequestParam Long peerToPeerExchangeId) {
		return createAdvertisementService.deleteAdvertisement(userId, peerToPeerExchangeId);
	}

	@PostMapping("/add-payment-method")
	public Response<Object> addPaymentMethod(@RequestHeader Long userId, @RequestBody PaymentMethodDto paymentMethodDto,
			@RequestParam String paymentType) {
		return createAdvertisementService.addPaymentMethod(userId, paymentMethodDto, paymentType);
	}

	@PostMapping("/edit-payment-method")
	public Response<Object> editPaymentMethod(@RequestHeader Long userId,
			@RequestBody PaymentMethodUpdateDto paymentMethodUpdateDto, @RequestParam String paymentType) {
		return createAdvertisementService.editPaymentMethod(userId, paymentMethodUpdateDto, paymentType);
	}

	@DeleteMapping("/delete-payment-method")
	public Response<Object> deletePaymentMethod(@RequestHeader Long userId, @RequestParam Long paymentDetailId) {
		return createAdvertisementService.deletePaymentMethod(userId, paymentDetailId);
	}

	@GetMapping("/get-payment-method-list")
	public Response<Object> getpaymentList(@RequestHeader Long userId) {

		return createAdvertisementService.getpaymentList(userId);

	}

	@GetMapping("/get-payment-method-details")
	public Response<Object> getpaymentdetails(@RequestHeader Long userId, @RequestParam Long paymentDetailId) {

		return createAdvertisementService.getpaymentdetails(userId, paymentDetailId);

	}

	@GetMapping("/get-payment-method")
	public Response<Object> getpaymentdetailsDetail(@RequestParam Long userId, @RequestParam Long paymentDetailId) {

		return createAdvertisementService.getpaymentdetails(userId, paymentDetailId);

	}

	@GetMapping("/get-user-detail-details")
	public Response<Object> getuserdetails(@RequestParam Long userId) {

		return createAdvertisementService.getuserdetails(userId);

	}

	@GetMapping("/get-p2pData-details")
	public Response<Object> getp2pDetails(@RequestParam Long p2pId) {

		return createAdvertisementService.getp2pDetails(p2pId);

	}

	@GetMapping("/get-trading-details-by-p2pId")
	public Response<Object> gettradedetail(@RequestParam Long p2pId,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "pageSize") Integer pageSize) {

		return createAdvertisementService.gettradedetail(p2pId, page, pageSize);

	}

//	n

	@PostMapping("/cancel-dispute")
	public Response<Object> cancelDispute(@RequestHeader Long userId, @RequestParam String tradeId) {
		return createAdvertisementService.cancelDispute(userId, tradeId);
	}

	

}