package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.CareerDto;
import com.mobiloitte.content.dto.CareerUpdateDto;
import com.mobiloitte.content.dto.SubCategoryDto;
import com.mobiloitte.content.dto.SubCategoryFormDto;
import com.mobiloitte.content.dto.SubCategoryUpdateDto;
import com.mobiloitte.content.enums.CareerStatus;
import com.mobiloitte.content.enums.SubCategoryStatus;
import com.mobiloitte.content.model.Response;

public interface CareerService {

	Response<Object> addCareer(Long userId, CareerDto careerDto);

	Response<Object> getAllCareerList();

	Response<Object> careerDetailsUpdate(Long userId, CareerUpdateDto careerUpdateDto);

	Response<Object> deleteCareerByTeamId(Long teamId, Long userId);

	Response<Object> getListByTeamId(Long teamId);

	Response<Object> getAllSubCategoryList();

	Response<Object> addSubCategory(Long userId, SubCategoryDto subCategoryDto);

	Response<Object> addSubCategoryForm(SubCategoryFormDto subCategoryFormDto);

	Response<Object> subCategoryDetailsUpdate(Long userId, SubCategoryUpdateDto subCategoryUpdateDto);

	Response<Object> blockOrUnBlockCareer(Long userId, Long teamId, CareerStatus careerStatus);

	Response<Object> getsubCategoryId(Long subTeamId);

//	Response<Object> blockOrUnBlocksubCategory(Long userId, Long subTeamId, CareerStatus careerStatus);

	Response<Object> blockOrUnBlocksubCategory(Long userId, Long subTeamId, SubCategoryStatus careerStatus);

	Response<Object> deleteSubCategoryByID(Long userId, Long subTeamId);

	Response<Object> getFormList(Long subCategoryFormId);

	Response<Object> getAllListSubCategoryForm();

}
