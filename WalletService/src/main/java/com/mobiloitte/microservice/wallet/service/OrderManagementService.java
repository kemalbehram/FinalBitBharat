package com.mobiloitte.microservice.wallet.service;

import com.mobiloitte.microservice.wallet.dto.BlockBalanceDto;
import com.mobiloitte.microservice.wallet.dto.TransferBalanceDto;
import com.mobiloitte.microservice.wallet.model.Response;

/**
 * The Interface OrderManagementService.
 * @author Ankush Mohapatra
 */
public interface OrderManagementService {
	
	/**
	 * Update user wallet and block balance.
	 *
	 * @param orderRequestDto the order request dto
	 * @return the response
	 */
	Response<String> updateUserWalletAndBlockBalanceOnOrderCancellation(BlockBalanceDto orderRequestDto);
	
	/**
	 * Update wallet balance on order execution.
	 *
	 * @param orderRequestDto the order request dto
	 * @return the response
	 */
	Response<String> updateWalletBalanceOnOrderExecution(BlockBalanceDto orderRequestDto);
	
	/**
	 * Transfet balance on order execution.
	 *
	 * @param dto the dto
	 * @return the response
	 */
	Response<String> transferBalanceOnOrderExecution(TransferBalanceDto dto);

}
