package com.mobiloitte.content.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.constants.EmailConstants;
import com.mobiloitte.content.dao.CareerDao;
import com.mobiloitte.content.dao.SubCategoryDao;
import com.mobiloitte.content.dao.SubCategoryFormDao;
import com.mobiloitte.content.dto.CareerDto;
import com.mobiloitte.content.dto.CareerUpdateDto;
import com.mobiloitte.content.dto.SubCategoryDto;
import com.mobiloitte.content.dto.SubCategoryFormDto;
import com.mobiloitte.content.dto.SubCategoryUpdateDto;
import com.mobiloitte.content.entities.Career;
import com.mobiloitte.content.entities.SubCategory;
import com.mobiloitte.content.entities.SubCategoryFormNew;
import com.mobiloitte.content.enums.CareerStatus;
import com.mobiloitte.content.enums.SubCategoryStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.CareerService;
import com.mobiloitte.content.utils.MailSender;

@Service
public class CareerServiceImpl implements CareerService {

	@Autowired
	private CareerDao careerDao;

	@Autowired
	private SubCategoryDao subCategoryDao;

	@Autowired
	private SubCategoryFormDao categoryDao;

	@Autowired
	private MailSender mailSender;

	@Override
	public Response<Object> addCareer(Long userId, CareerDto careerDto) {

		Optional<Career> careeerExists = careerDao.findByTeamName(careerDto.getTeamName());

		if (!careeerExists.isPresent()) {
			Career career = new Career();

			career.setCreateTime(new Date());
			career.setTeamName(careerDto.getTeamName());
			career.setImage(careerDto.getImage());
			career.setCareerStatus(CareerStatus.ACTIVE);
			careerDao.save(career);
			return new Response<>(200, "Job category added successfully.");
		} else {
			return new Response<>(205, "Job category already exists.");
		}

	}

	@Override
	public Response<Object> getAllCareerList() {
		List<Career> isCareerExists = careerDao.findAll();
		if (!isCareerExists.isEmpty()) {
			return new Response<Object>(200, "List fetched successfully", isCareerExists);
		} else {
			return new Response<>(205, "No data found");

		}
	}

	@Override
	public Response<Object> careerDetailsUpdate(Long userId, CareerUpdateDto careerUpdateDto) {
		Optional<Career> career = careerDao.findByTeamName(careerUpdateDto.getTeamName());

		if (career.isPresent()) {
			career.get().setCreateTime(new Date());
			career.get().setTeamName(careerUpdateDto.getTeamName());
			career.get().setImage(careerUpdateDto.getImage());
			careerDao.save(career.get());
			return new Response<>(200, "Job category updated successfully");
		} else {
			return new Response<>(205, "No data found");
		}
	}

	@Override
	public Response<Object> deleteCareerByTeamId(Long userId, Long teamId) {
		Optional<Career> isTeamIdExists = careerDao.findById(teamId);
		if (isTeamIdExists.isPresent()) {
			careerDao.deleteById(isTeamIdExists.get().getTeamId());
			return new Response<>(200, "Job category deleted successfully");
		} else {
			return new Response<>(205, "No data found");
		}

	}

	@Override
	public Response<Object> deleteSubCategoryByID(Long userId, Long subTeamId) {
		Optional<SubCategory> isTeamIdExists = subCategoryDao.findById(subTeamId);
		if (isTeamIdExists.isPresent()) {
			subCategoryDao.deleteById(isTeamIdExists.get().getSubTeamId());
			return new Response<>(200, "Job sub-category deleted successfully");
		} else {
			return new Response<>(205, "No data found");
		}

	}

	@Override
	public Response<Object> getListByTeamId(Long teamId) {
		List<Career> isTeamIdExists = careerDao.findByTeamId(teamId);
		if (!isTeamIdExists.isEmpty()) {
			return new Response<Object>(200, "Data fetched successfully", isTeamIdExists);
		} else {
			return new Response<>(205, "No data found");
		}
	}

	@Override
	public Response<Object> getsubCategoryId(Long subTeamId) {
		List<SubCategory> issubTeamIdExists = subCategoryDao.findBySubTeamId(subTeamId);
		if (!issubTeamIdExists.isEmpty()) {
			return new Response<Object>(200, "Data fetched successfully", issubTeamIdExists);
		} else {
			return new Response<>(205, "No data found");
		}
	}

	@Override
	public Response<Object> addSubCategory(Long userId, SubCategoryDto subCategoryDto) {

		SubCategory subCategory = new SubCategory();
		subCategory.setCreateTime(new Date());
		subCategory.setCareerType(subCategoryDto.getCareerType());
		subCategory.setDescription(subCategoryDto.getDescription());
		subCategory.setRequirement(subCategoryDto.getRequirement());
		subCategory.setSubTeamName(subCategoryDto.getSubTeamName());
		subCategory.setCategory(subCategoryDto.getCategory());
		subCategory.setImageUrl(subCategoryDto.getImageUrl());
		subCategory.setSubCategoryStatus(SubCategoryStatus.ACTIVE);
		subCategoryDao.save(subCategory);
		return new Response<>(200, "Job sub-category added successfully");

	}

	@Override
	public Response<Object> getAllSubCategoryList() {
		List<SubCategory> issubCategoryExists = subCategoryDao.findAll();
		if (!issubCategoryExists.isEmpty()) {
			return new Response<Object>(200, "List fetched successfully", issubCategoryExists);
		} else {
			return new Response<>(205, "No data found");

		}
	}

	@Override
	public Response<Object> addSubCategoryForm(SubCategoryFormDto subCategoryFormDto) {
		SubCategoryFormNew subCategoryForm = new SubCategoryFormNew();
		subCategoryForm.setResume(subCategoryFormDto.getResume());
		subCategoryForm.setFullName(subCategoryFormDto.getFullName());
		subCategoryForm.setEmail(subCategoryFormDto.getEmail());
		subCategoryForm.setMobileNumber(subCategoryFormDto.getMobileNumber());
		subCategoryForm.setCurrentCompany(subCategoryFormDto.getCurrentCompany());
		subCategoryForm.setResumeUrl(subCategoryFormDto.getResumeurl());
		subCategoryForm.setAdditionalInformation(subCategoryFormDto.getAdditionalInformation());
		categoryDao.save(subCategoryForm);
		Map<String, Object> sendMailData = setEmailDataForCareerSubmitSuccess(subCategoryFormDto.getEmail(),
				subCategoryFormDto.getFullName());
		mailSender.sendMailToCareerSubmissionSuccess(sendMailData, "en");
		return new Response<>(200, "Form submitted successfully");

	}

	@Override
	public Response<Object> subCategoryDetailsUpdate(Long userId, SubCategoryUpdateDto subCategoryUpdateDto) {
		Optional<SubCategory> subCategory = subCategoryDao.findBySubTeamName(subCategoryUpdateDto.getSubTeamName());

		if (subCategory.isPresent()) {
			subCategory.get().setCareerType(subCategoryUpdateDto.getCareerType());
			subCategory.get().setCreateTime(new Date());
			subCategory.get().setDescription(subCategoryUpdateDto.getDescription());
			subCategory.get().setRequirement(subCategoryUpdateDto.getRequirement());
			subCategory.get().setCategory(subCategoryUpdateDto.getCategory());
			subCategory.get().setSubTeamName(subCategoryUpdateDto.getSubTeamName());
			subCategory.get().setImageUrl(subCategoryUpdateDto.getImageUrl());
			subCategoryDao.save(subCategory.get());
			return new Response<>(200, "Job sub-category updated successfully");
		} else {
			return new Response<>(205, "No data found");
		}

	}

	private Map<String, Object> setEmailDataForCareerSubmitSuccess(String email, String name) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, email);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.FORM_SUBMITTED_SUCCESSFULLY);
		sendMailData.put(EmailConstants.USER_NAME, name);
		return sendMailData;
	}

	@Override
	public Response<Object> blockOrUnBlockCareer(Long userId, Long teamId, CareerStatus careerStatus) {
		try {
			Optional<Career> careerDetail = careerDao.findById(teamId);
			if (careerDetail.isPresent()) {
				Career career = careerDetail.get();
				career.setCareerStatus(careerStatus);
				careerDao.save(career);
				return new Response<>(200, "Job category blocked successfully");
			} else {
				return new Response<>(201, "No data found");
			}
		} catch (Exception e) {
			return new Response<>(201, "Somthing went wrong");
		}
	}

	@Override
	public Response<Object> blockOrUnBlocksubCategory(Long userId, Long subTeamId, SubCategoryStatus careerStatusNew) {
		try {
			Optional<SubCategory> careerDetailNew = subCategoryDao.findById(subTeamId);
			if (careerDetailNew.isPresent()) {
				SubCategory careerNew = careerDetailNew.get();
				careerNew.setSubCategoryStatus(careerStatusNew);
				subCategoryDao.save(careerNew);
				return new Response<>(200, "Job sub-category blocked buccessfully");
			} else {
				return new Response<>(201, "No data found");
			}
		} catch (Exception e) {
			return new Response<>(201, "Somthing went wrong");
		}
	}

	@Override
	public Response<Object> getFormList(Long subCategoryFormId) {
		List<SubCategoryFormNew> isListExists = categoryDao.findBySubCategoryFormId(subCategoryFormId);

		if (!isListExists.isEmpty()) {
			return new Response<>(200, "List fetched successfully", isListExists);
		}
		return new Response<>(205, "No data found");
	}

	@Override
	public Response<Object> getAllListSubCategoryForm() {
		List<SubCategoryFormNew> isSubCategoryFormExists = categoryDao.findAll();
		if (!isSubCategoryFormExists.isEmpty()) {
			return new Response<Object>(200, "List fetched successfully", isSubCategoryFormExists);
		} else {
			return new Response<>(205, "No data found");

		}
	}

}
