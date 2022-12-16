package com.mobiloitte.microservice.wallet.controller;

import java.math.BigDecimal;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.dto.CalculateFeesRequestDto;
import com.mobiloitte.microservice.wallet.dto.MarketPriceDto;
import com.mobiloitte.microservice.wallet.dto.TakerMakerFeesRequestDto;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.FeesAndAmountManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class FeesAndAmountController.
 * @author Ankush Mohapatra
 */
@RestController
@RequestMapping(value = "/admin/fee-management")
@Api(value="Fees and Amount Management APIs")
public class FeesAndAmountController implements OtherConstants{

	/** The fees and amount management service. */
	@Autowired
	private FeesAndAmountManagementService feesAndAmountManagementService;
	
	/**
	 * Gets the update withdraw fee.
	 *
	 * @param coinName the coin name
	 * @param withdrawFee the withdraw fee
	 * @return the update withdraw fee
	 */
	@ApiOperation(value = "API to set coin withdraw fee")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "withdraw fee updated successfully"),
			@ApiResponse(code = 205, message = "withdraw fee updation failed / coin not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/set-withdrawal-fee")
	public Response<String> getUpdateWithdrawFee(@RequestParam("coinName") String coinName, @RequestParam("withdrawalFee") BigDecimal withdrawFee) {
		if(coinName != null && withdrawFee != null) {
		return feesAndAmountManagementService.setWithdrawlFee(coinName, withdrawFee);
		}
		else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}
	
	@PostMapping(value = "/set-transfer-fee")
	public Response<String> settransferFee( @RequestParam BigDecimal transferFee,@RequestParam BigDecimal minimumFee) {
		if(transferFee != null && minimumFee != null) {
		return feesAndAmountManagementService.settransferFee(transferFee, minimumFee);
		}
		else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the update minimum withdrawal amount.
	 *
	 * @param coinName the coin name
	 * @param withdrawalAmount the withdrawal amount
	 * @return the update minimum withdrawal amount
	 */
	@ApiOperation(value = "API to set coin minimum withdrawal amount")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "withdraw amount updated successfully"),
			@ApiResponse(code = 205, message = "updation failed / coin not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/set-minimum-withdrawal-amount")
	public Response<String> getUpdateMinimumWithdrawalAmount(@RequestParam("coinName") String coinName, @RequestParam(required = false) BigDecimal withdrawalAmount, @RequestParam(required = false) BigDecimal withdrawalAmountMax) {
		if(coinName != null && withdrawalAmount != null) {
		return feesAndAmountManagementService.setMinimumCoinWithdrawlAmount(coinName, withdrawalAmount,withdrawalAmountMax);
		}
		else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}
	
	@ApiOperation(value = "API to set coin minimum deposit amount")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "deposit amount updated successfully"),
			@ApiResponse(code = 205, message = "updation failed / coin not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/set-minimum-deposit-amount")
	public Response<String> getUpdateMinimumdepositeAmount(@RequestParam("coinName") String coinName, @RequestParam(required = false) BigDecimal depositeAmount) {
		if(coinName != null && depositeAmount != null) {
		return feesAndAmountManagementService.getUpdateMinimumdepositeAmount(coinName, depositeAmount);
		}
		else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}
	
	/**
	 * Gets the update taker maker fee.
	 *
	 * @param takerMakerFeesRequestDto the taker maker fees request dto
	 * @return the update taker maker fee
	 */
	@ApiOperation(value = "API to set coin taker maker fee")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "taker maker fee updated successfully"),
			@ApiResponse(code = 205, message = "updation failed / coin not found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/set-taker-maker-fee")
	public Response<String> getUpdateTakerMakerFee(@Valid @RequestBody TakerMakerFeesRequestDto takerMakerFeesRequestDto) {
		if(takerMakerFeesRequestDto != null) {
		return feesAndAmountManagementService.setTakerMakeeFee(takerMakerFeesRequestDto);
		}
		else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Gets the calculated profit fees.
	 *
	 * @param calculateFeesRequestDto the calculate fees request dto
	 * @return the calculated profit fees
	 */
	@ApiOperation(value = "API to get profit fees od various fee type")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "fees profit calculated successfully"),
			@ApiResponse(code = 205, message = "No such history found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/get-fees-profit")
	public Response<Map<String, Object>> getCalculatedProfitFees(@Valid @RequestBody CalculateFeesRequestDto calculateFeesRequestDto) {
		if(calculateFeesRequestDto != null) {
		return feesAndAmountManagementService.getProfitFees(calculateFeesRequestDto);
		}
		else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}
	@GetMapping(value = "/set-trade-fee")
	public Response<MarketPriceDto> setUpdateTradeFee(@RequestParam("coinName") String coinName,
			@RequestParam BigDecimal tradeFee){
		return feesAndAmountManagementService.setUpdateTradeFee(coinName, tradeFee);
		
	}
}
