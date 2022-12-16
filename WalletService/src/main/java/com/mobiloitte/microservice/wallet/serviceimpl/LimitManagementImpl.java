package com.mobiloitte.microservice.wallet.serviceimpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.microservice.wallet.dao.LimitDataDao;
import com.mobiloitte.microservice.wallet.dto.LimitRequestDto;
import com.mobiloitte.microservice.wallet.entities.LimitData;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.LimitManagement;

@Service("LimitManagement")
public class LimitManagementImpl implements LimitManagement{
	
	@Autowired
	private LimitDataDao limitDataDao;

	@Override
	public Response<List<LimitData>> getAllLimitData() {
		List<LimitData> getLimitData = limitDataDao.findAll();
		if(getLimitData.isEmpty())
			return new Response<>(205, "No limit data found", Collections.emptyList());
		else
			return new Response<>(205, "success", getLimitData);
	}

	@Override
	public Response<LimitData> getLimitDataDetails(Long limitId) {
		Optional<LimitData> getDataDetails = limitDataDao.findById(limitId);
		if(getDataDetails.isPresent())
			return new Response<>(200, "success", getDataDetails.get());
		else
			return new Response<>(205, "No details found...");
	}

	@Override
	@Transactional
	public Response<Boolean> saveLimitData(LimitRequestDto limitRequestDto) {
		try {
			Optional<LimitData> getDataDetails = limitDataDao.findById(limitRequestDto.getLimitId());
			if(getDataDetails.isPresent())
			{
				getDataDetails.get().setLimitPrice(limitRequestDto.getLimitAmount());
				limitDataDao.save(getDataDetails.get());
				
				return new Response<>(200, "Limit data updated successfully", true);
			}
			else
				return new Response<>(205, "No limit data found...", false);
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong !!!");
		}
	}

}
