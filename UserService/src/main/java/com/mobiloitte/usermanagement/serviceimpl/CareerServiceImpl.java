package com.mobiloitte.usermanagement.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mobiloitte.usermanagement.dao.CareerDao;
import com.mobiloitte.usermanagement.dto.CareerDto;
import com.mobiloitte.usermanagement.enums.CareerStatus;
import com.mobiloitte.usermanagement.model.Career;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.CareerService;

@Service
public class CareerServiceImpl implements CareerService {
	
	@Autowired
	private CareerDao careerDao;

	@Override
	public Response<Object> addCareer(CareerDto careerDto, Long userId) {
		Career career =new Career();
		Optional<Career> careeerExists= careerDao.findByUserId(userId);
		
		if (!careeerExists.isPresent()) {
			career.setUserId(userId);
			career.setCareerEmail(careerDto.getCareerEmail());
			career.setCareerMessage(careerDto.getCareerMessage());
			career.setCareerTitle(careerDto.getCareerTitle());
			career.setCareerName(careerDto.getCareerName());
			career.setCareerStatus(careerDto.getCareerStatus());
			careerDao.save(career);
			return new Response<>(200,"CAREERS_ADD_SUCCESSFULLY");
		}
		else {
			return new Response<>(205,"CAREERS_ALREADY_EXIST");
		}	
		
	}

	@Override
	public Response<Object> getAllCareerList() {
		List<Career> isCareeerExists=careerDao.findAll();
		if (!isCareeerExists.isEmpty()) {
			return new Response<Object>(200, "CAREERS_LIST_FETCHED", isCareeerExists);
		} else {
			return new Response<>(205, "CAREERS_LIST_NOT_FOUND");
		}
	}

	@Override
	public Response<Object> getListById(Long careerId) {
		Optional<Career> isCareeerIDExists=careerDao.findById(careerId);
		if (isCareeerIDExists.isPresent()) {
			CareerDto careerDto =new CareerDto();
			careerDto.setCareerEmail(isCareeerIDExists.get().getCareerEmail());
			careerDto.setCareerName(isCareeerIDExists.get().getCareerName());
			careerDto.setCareerMessage(isCareeerIDExists.get().getCareerMessage());
			careerDto.setCareerTitle(isCareeerIDExists.get().getCareerTitle());
			careerDto.setCareerStatus(isCareeerIDExists.get().getCareerStatus());
			return new Response<>(200,"CAREERS_FETCHED_SUCCESSFULLY", careerDto);
		} else {
			return new Response<>(205,"CAREERS_DETAILS_DOES_NOT_EXISTS");
		}
	}

	@Override
	public Response<Object> getListByuserId(Long userId) {
		Optional<Career> isuserIDExists=careerDao.findByUserId(userId);
		if (isuserIDExists.isPresent()) {
			return new Response<Object>(200,"CAREERS_FETCHED_SUCCESSFULLY", isuserIDExists);
		} else {
			return new Response<>(205,"CAREERS_DETAILS_DOES_NOT_EXISTS");
		}
	}

	@Override
	public Response<Object> deleteCareerByCareerId(Long careerId) {
		Optional<Career> isCareerIdExists = careerDao.findById(careerId);
		if(isCareerIdExists.isPresent()) {
			isCareerIdExists.get().setCareerStatus(CareerStatus.DELETED);
			careerDao.save(isCareerIdExists.get());
			return new Response<>(200, "CAREER_DETAILS_DELETE_SUCCESSFULLY");
		}else {
			return new Response<>(205, "CAREER_DETAILS_DOES_NOT_EXISTS");
		}
		
	}
	
	
}
