package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.CareerDto;
import com.mobiloitte.content.dto.CareerUpdateDto;
import com.mobiloitte.content.dto.SubCategoryDto;
import com.mobiloitte.content.dto.SubCategoryFormDto;
import com.mobiloitte.content.dto.SubCategoryUpdateDto;
import com.mobiloitte.content.enums.CareerStatus;
import com.mobiloitte.content.enums.SubCategoryStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.CareerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class CareerController {

	@Autowired
	private CareerService careerService;

	@ApiOperation(value = "API to add Careers")
	@PostMapping("/add-career")
	public Response<Object> addCareer(@RequestHeader Long userId, @RequestBody CareerDto careerDto) {
		return careerService.addCareer(userId, careerDto);
	}

	@ApiOperation(value = "API to get all Careers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Career Already Exists"), })
	@GetMapping("/get-all-career")
	public Response<Object> getAllCareerList() {
		return careerService.getAllCareerList();
	}

	@ApiOperation(value = "API to update the details of the Careers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/career-details-update")
	public Response<Object> careerDetailsUpdate(@RequestHeader(required = true) Long userId,
			@RequestBody CareerUpdateDto careerUpdateDto) {
		return careerService.careerDetailsUpdate(userId, careerUpdateDto);
	}

	@ApiOperation(value = "API to Delete Career by TeamId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = " Career Already Exists"), })
	@DeleteMapping("/delete-careers-by-teamId")
	public Response<Object> deleteCareerByCareerId(@RequestHeader(required = true) Long userId,
			@RequestParam Long teamId) {
		return careerService.deleteCareerByTeamId(userId, teamId);
	}
	@ApiOperation(value = "API to Delete Career by TeamId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = " Career Already Exists"), })
	@DeleteMapping("/delete-careers-by-subTeamId")
	public Response<Object> deleteSubCategoryByID(@RequestHeader(required = true) Long userId,
			@RequestParam Long subTeamId) {
		return careerService.deleteSubCategoryByID(userId, subTeamId);
	}

	@ApiOperation(value = "API to get Career by TeamId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Career Already Exists"), })
	@GetMapping("/get-details-by-teamId")
	public Response<Object> getListByTeamId(@RequestParam Long teamId) {
		return careerService.getListByTeamId(teamId);
	}

	@ApiOperation(value = "API to get Career by TeamId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Career Already Exists"), })
	@GetMapping("/get-details-by-subCategoryId")
	public Response<Object> getsubCategoryId(@RequestParam Long subTeamId) {
		return careerService.getsubCategoryId(subTeamId);
	}

	@ApiOperation(value = "API to add Sub-Category")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Career Already Exists"), })
	@PostMapping("/add-sub-category")
	public Response<Object> addSubCategory(@RequestHeader(required = true) Long userId,
			@RequestBody(required = false)  SubCategoryDto subCategoryDto) {
		return careerService.addSubCategory(userId, subCategoryDto);
	}

	@ApiOperation(value = "API to get all Sub Category")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Career Already Exists"), })
	@GetMapping("/get-all-sub-category")
	public Response<Object> getAllSubCategoryList() {
		return careerService.getAllSubCategoryList();
	}

	@ApiOperation(value = "API to update the details of the Sub Category")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/sub-category-details-update")
	public Response<Object> subCategoryDetailsUpdate(@RequestHeader Long userId,
			@RequestBody SubCategoryUpdateDto subCategoryUpdateDto) {
		return careerService.subCategoryDetailsUpdate(userId, subCategoryUpdateDto);
	}

	@PostMapping("/add-sub-category-form")
	public Response<Object> addSubCategoryForm(@RequestBody SubCategoryFormDto subCategoryFormDto) {
		return careerService.addSubCategoryForm(subCategoryFormDto);
	}

	@PostMapping("/block-or-unBlock-career")
	public Response<Object> blockOrUnBlockCareer(@RequestHeader Long userId, @RequestParam Long teamId,
			@RequestParam CareerStatus careerStatus) {
		return careerService.blockOrUnBlockCareer(userId, teamId, careerStatus);
	}

	@PostMapping("/block-or-unBlock-subCategory")
	public Response<Object> blockOrUnBlocksubCategory(@RequestHeader Long userId, @RequestParam Long subTeamId,
			@RequestParam SubCategoryStatus careerStatus) {
		return careerService.blockOrUnBlocksubCategory(userId, subTeamId, careerStatus);
	}
	
	@GetMapping("/get-Form-List")
	public Response<Object> getFormList(@RequestParam Long SubCategoryFormId){
		return careerService.getFormList(SubCategoryFormId);
	}
	
	@GetMapping("/get-all-list-sub-category-form")
	public Response<Object> getAllListSubCategoryForm() {
		return careerService.getAllListSubCategoryForm();
	}

}
