package com.mobiloitte.microservice.wallet.service;

import java.util.Map;

import com.mobiloitte.microservice.wallet.dto.BulkPurchaseDto;
import com.mobiloitte.microservice.wallet.model.Response;

public interface BulkPurchaseService {

	Response<String> setPurchaseRequest(BulkPurchaseDto bulkPurchaseDto);

	Response<Object> getPurchaseRequestDetails(Long requestId);

	Response<Map<String, Object>> getPurchaseRequestHistory(Integer page, Integer pageSize);

	Response<String> setPurchaseRequestResolved(Long requestId, Boolean isResolved);
	
	
	
	

}
