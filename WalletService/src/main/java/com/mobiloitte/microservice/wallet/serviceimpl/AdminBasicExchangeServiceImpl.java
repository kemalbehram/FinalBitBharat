package com.mobiloitte.microservice.wallet.serviceimpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.dao.BasicTradeHistoryDao;
import com.mobiloitte.microservice.wallet.entities.BasicTradeHistory;
import com.mobiloitte.microservice.wallet.enums.OrderType;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.AdminBasicExchangeService;

/**
 * The Class AdminBasicExchangeServiceImpl.
 */
@Service("AdminBasicExchangeService")
public class AdminBasicExchangeServiceImpl implements AdminBasicExchangeService, WalletConstants, OtherConstants{ 
	
	/** The basic trade history dao. */
	@Autowired
	private BasicTradeHistoryDao basicTradeHistoryDao;

	/**
	 * Gets the all exchange history.
	 *
	 * @param orderType the order type
	 * @param page the page
	 * @param pageSize the page size
	 * @return the all exchange history
	 */
	@Override
	public Response<Map<String, Object>> getAllExchangeHistory(OrderType orderType, Integer page, Integer pageSize) {
		List<BasicTradeHistory> getUserTradeHistory = basicTradeHistoryDao.findByOrderType(orderType,
				PageRequest.of(page, pageSize, Direction.DESC, "basicTradeHistoryId"));
       Long getTotalCount = basicTradeHistoryDao.countByOrderType(orderType);
       Map<String, Object> responseMap = new HashMap<>();
       if(getUserTradeHistory != null && !getUserTradeHistory.isEmpty() )
       {
    	   responseMap.put(RESULT_LIST, getUserTradeHistory);
    	   responseMap.put(TOTAL_COUNT, getTotalCount);
    	   return new Response<>(SUCCESS_CODE, USER_ORDER_HISTORY_FETCHED_SUCCESSFULLY, responseMap);
       }else {
			responseMap.put(RESULT_LIST, Collections.emptyList());
			responseMap.put(TOTAL_COUNT, getTotalCount);
			return new Response<>(SUCCESS_CODE, NO_RECORDS_FOUND, responseMap);


       }
	}
	
	
}



