package com.mobiloitte.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.dto.CareerDto;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.CareerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class CareerController {
	@Autowired
	private CareerService careerService;

	@ApiOperation(value = "API to add Careers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Career Already Exists"), })
	@PostMapping("/add-career")
	public Response<Object> addCareer(@RequestBody CareerDto careerDto, @RequestHeader Long userId) {
		return careerService.addCareer(careerDto, userId);
	}

	@ApiOperation(value = "API to get all Careers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Career Already Exists"), })
	@GetMapping("/get-all-career")
	public Response<Object> getAllCareerList() {
		return careerService.getAllCareerList();
	}

	@ApiOperation(value = "API to get Career by CareerId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Career Already Exists"), })
	@GetMapping("/get-details-by-careerId")
	public Response<Object> getListById(@RequestParam Long careerId) {
		return careerService.getListById(careerId);
	}

	@ApiOperation(value = "API to get Career by userId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = " Career Already Exists"), })
	@GetMapping("/get-careers-by-userId")
	public Response<Object> getListByuserId(@RequestHeader Long userId) {
		return careerService.getListByuserId(userId);
	}
	@ApiOperation(value = "API to Delete Career by CareerId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = " Career Already Exists"), })
	@DeleteMapping("/delete-careers-by-careerId")
	public Response<Object> deleteCareerByCareerId(@RequestParam Long careerId){
		return careerService.deleteCareerByCareerId(careerId);
	}
} 
