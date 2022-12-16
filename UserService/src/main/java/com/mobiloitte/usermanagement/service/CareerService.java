package com.mobiloitte.usermanagement.service;

import com.mobiloitte.usermanagement.dto.CareerDto;
import com.mobiloitte.usermanagement.model.Response;

public interface CareerService {

	Response<Object> addCareer(CareerDto careerDto, Long userId);

	Response<Object> getAllCareerList();

	Response<Object> getListById(Long careerId);

	Response<Object> getListByuserId(Long userId);

	Response<Object> deleteCareerByCareerId(Long careerId);

}
