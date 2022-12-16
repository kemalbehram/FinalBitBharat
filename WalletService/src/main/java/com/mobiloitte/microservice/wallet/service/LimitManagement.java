package com.mobiloitte.microservice.wallet.service;

import java.util.List;

import com.mobiloitte.microservice.wallet.dto.LimitRequestDto;
import com.mobiloitte.microservice.wallet.entities.LimitData;
import com.mobiloitte.microservice.wallet.model.Response;

public interface LimitManagement {

	Response<List<LimitData>> getAllLimitData();
	
	Response<LimitData> getLimitDataDetails(Long limitId);
	
	Response<Boolean> saveLimitData(LimitRequestDto limitRequestDto);
}
