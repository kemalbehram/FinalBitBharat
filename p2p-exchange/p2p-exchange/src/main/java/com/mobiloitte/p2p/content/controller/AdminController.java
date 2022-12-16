package com.mobiloitte.p2p.content.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.p2p.content.constants.OtherConstants;
import com.mobiloitte.p2p.content.dto.AdminChargeDto;
import com.mobiloitte.p2p.content.dto.P2PAdvertisementDto;
import com.mobiloitte.p2p.content.dto.SearchAndFilterDto;
import com.mobiloitte.p2p.content.enums.DisputeStatus;
import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.exception.BadRequestException;
import com.mobiloitte.p2p.content.model.AdminCharge;
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
@RequestMapping("admin")
public class AdminController implements OtherConstants {

	@Autowired
	AdminService adminService;
	@Autowired
	CreateAdvertisementService createAdvertisementService;

	@ApiOperation(value = "API for Get Advertisment For Buy and Sell For Admin")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Advertisment Details For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/find-all-buy-sell-for-admin1")
	public Response<Map<String, Object>> getOpenTradeDetails(@RequestParam(required = true) OrderType orderType,
			@RequestParam(required = true, value = "page") Integer page,
			@RequestParam(required = true, value = "pageSize") Integer pageSize) {

		return adminService.getAdvertisementForAdmin(orderType, page, pageSize);

	}

	@ApiOperation(value = "API For Release Bitcoins")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "API for BITCOIN_RELEASE_SUCCESSFULLY"),
			@ApiResponse(code = 205, message = "Data not found/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })

	@PostMapping("/release-bitcoins-by-admin")
	public Response<Object> forReleaseBitcoinsByAdmin(@RequestParam String tradeId, @RequestHeader Long userId) {
		if (tradeId != null && userId != null && userId != 0) {
			return adminService.forReleaseBitcoinsByAdmin(tradeId, userId);

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
	public Response<Map<String, Object>> getOpenTradeDetails(@RequestParam Long userId,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "pageSize") Integer pageSize) {

		return createAdvertisementService.getOpenTradeDetails(userId, page, pageSize);

	}

	@ApiOperation(value = "API for Get Dispute History and trade History")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Advertisment Details For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/get-trade-history")
	public Response<Map<String, Object>> getTradeHistoryWithChat(@RequestParam Long buyerId,
			@RequestParam Long sellerId, @RequestParam Integer page, @RequestParam Integer pageSize) {
		if (buyerId != null && sellerId != null) {
			return adminService.getTradeHistoryWithChat(buyerId, sellerId, page, pageSize);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}

	}

	@ApiOperation(value = "API for Get Dispute History  History")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Advertisment Details For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/get-dispute-history")
	public Response<Map<String, Object>> getDisputeHistory(@RequestParam Long buyerId, @RequestParam Long sellerId,
			@RequestParam(required = true, value = "page") Integer page,
			@RequestParam(required = true, value = "pageSize") Integer pageSize) {
		if (buyerId != null && sellerId != null) {
			return adminService.getDisputeHistory(buyerId, sellerId, page, pageSize);
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
	@GetMapping("/get-trade-history-for-admin")
	public Response<Map<String, Object>> getTradeHistory(@RequestParam(required = false) TradeStatus tradeStatus,
			@RequestParam(required = true, value = "page") Integer page,
			@RequestParam(required = true, value = "pageSize") Integer pageSize) {
		if (page != null && pageSize != null) {

			return adminService.getTradeHistory(tradeStatus, page, pageSize);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@ApiOperation(value = "API for Get Advertisment By TradeId For Admin")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Advertisment Details For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/get-trade-details-by-tradeid")
	public Response<Map<String, Object>> getAdvertisementForAdminByTradingId(@RequestParam String tradeId) {

		return adminService.getAdvertisementForAdminByTradingId(tradeId);

	}

	@ApiOperation(value = "API to get all trade  details using filter")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/filter-trade-details")
	public Response<Object> filterUserDetails(@RequestParam(required = false) TradeStatus tradeStatus,
			@RequestParam(required = false) Long fromDate, @RequestParam(required = false) Long toDate,
			@RequestParam(required = false) String search, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		return adminService.filterTradeDetails(tradeStatus, fromDate, toDate, search, page, pageSize);
	}

	@ApiOperation(value = "API for Get Trade Details For Admin")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "API for Get Advertisment Details For Buy and Sell successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/get-trade-history-for-admin-from-date")
	public Response<Map<String, Object>> getTradeHistory(@RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate, @RequestParam(required = true, value = "page") Integer page,
			@RequestParam(required = true, value = "pageSize") Integer pageSize) {

		return adminService.getTradeHistoryFromDate(fromDate, toDate, page, pageSize);
	}

	@GetMapping("/get-details-by-email")
	public Response<Object> getAddDetailsByEmail(@RequestParam String email) {
		if (email != null) {
			return adminService.getDetailsByEmail(email);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@GetMapping("/delete-addvertisement")
	public Response<Object> deleteAdvertisement(@RequestHeader Long userId, @RequestParam Long peerToPeerExchangeId) {
		if (userId != null) {
			return adminService.deleteAdvertisement(userId, peerToPeerExchangeId);
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
	@PostMapping("/edit-advertisment-by-admin")
	public Response<Object> editAdvertisement(@RequestHeader Long userId,
			@RequestBody P2PAdvertisementDto p2pBuySellDto, @RequestParam Long peerToPeerExchangeId) {
		if (p2pBuySellDto != null && peerToPeerExchangeId != null && peerToPeerExchangeId != 0 && userId != null) {
			return adminService.editAdvertisement(userId, p2pBuySellDto, peerToPeerExchangeId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@PostMapping("/set-enable-disable-all-advertisements")
	public Response<Object> setEnableDisable(@RequestParam OrderType orderType,
			@RequestParam ExchangeStatusType statusType, @RequestHeader Long userId,
			@RequestParam(required = false) Long p2pId) {
		if (userId != null && userId != 0 && statusType != null) {
			return adminService.setEnableDisable(orderType, statusType, userId, p2pId);

		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@PostMapping("/add-admin-basic-trade-fees")
	public Response<AdminCharge> setFees(@RequestBody AdminChargeDto adminChargeDto) {
		if (adminChargeDto != null) {
			return adminService.setBasicTradeFees(adminChargeDto);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@GetMapping("/get-admin-basic-trade-fees")
	public Response<Object> getFees(@RequestParam String coinName, @RequestHeader Long userId) {
		if (coinName != null) {
			return adminService.getBasicTradeFees(coinName);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@GetMapping("/search-and-filters-advertisement")
	public Response<Object> getAdvertisementSearch(@RequestHeader Long userId,
			@RequestParam(required = false) Long userId2, @RequestParam(required = false) Long peerToPeerExchangeId,
			@RequestParam(required = false) String userName,
			@RequestParam(required = false) ExchangeStatusType orderStatus,
			@RequestParam(required = false) String country, @RequestParam(required = false) String currency,
			@RequestParam(required = false) Long amount, @RequestParam(required = false) PaymentType paymentType,
			@RequestParam(required = false) OrderType orderType, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate, @RequestParam Integer page, @RequestParam Integer pageSize) {

		return adminService.getAdvertisementSearch(userId, userId2, peerToPeerExchangeId, userName, orderStatus,
				country, currency, amount, paymentType, orderType, fromDate, toDate, page, pageSize);

	}

	@GetMapping("/search-and-filters-trade-list")
	public Response<Object> getTradingSearch(@RequestHeader Long userId, @RequestParam(required = false) String search,
			@RequestParam(required = false) TradeStatus tradeStatus, @RequestParam(required = false) String country,
			@RequestParam(required = false) PaymentType paymentType, @RequestParam(required = false) OrderType type,
			@RequestParam(required = false) Long fromDate, @RequestParam(required = false) Long toDate,
			@RequestParam(required = false) DisputeStatus disputeStatus, @RequestParam Integer page,
			@RequestParam Integer pageSize) {
		return adminService.getTradingSearch(userId, search, tradeStatus, country, paymentType, type, fromDate, toDate,
				disputeStatus, page, pageSize);

	}
	@ApiOperation(value = "API to search-and-filter-dispute-trade")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/search-and-filter-dispute")
	public Response<Object> searchAndFilterAdminApi(@RequestHeader Long userId,
			@RequestBody SearchAndFilterDto searchAndFilterDto) {
		return adminService.searchAndFilterAdmin(userId, searchAndFilterDto);
	}

	@ApiOperation(value = "API to get-staff-user-profile")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/assign-staff-for-dispute")
	public Response<Object> assignStaffForDispute(@RequestHeader Long userId, @RequestParam String staffId,
			@RequestParam String disputeId) {
		if (staffId != null && disputeId != null) {
			return adminService.assignStaffForDispute(userId, staffId, disputeId);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

	@ApiOperation(value = "API to get p2p count ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@GetMapping("/get-p2p-count-for-dashboard")
	public Response<Object> getUserCountByStatus() {
		return adminService.getTotalP2pCount();
	}

	@GetMapping("/recommendation")
	public Response<Object> recommendation(@RequestHeader Long userId, @RequestParam String disputeId,
			@RequestParam String recommendation) {
		if (recommendation != null) {
			return adminService.recommendation(userId, disputeId, recommendation);
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);

		}
	}

}
