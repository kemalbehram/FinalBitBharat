package com.mobiloitte.usermanagement.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.dto.NomimeeUpdateDto;
import com.mobiloitte.usermanagement.dto.NomineeDto;
import com.mobiloitte.usermanagement.dto.NomineeStatusDto;
import com.mobiloitte.usermanagement.enums.NomineeStatus;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.NomineeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class NomineeController.
 * 
 * @author Priyank Mishra
 * 
 */

@RestController
public class NomineeController {

	@Autowired
	private NomineeService nomineeService;

	@ApiOperation(value = "API to add Nominee")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Nominee Already Exists"), })
	@PostMapping("/add-nominee")
	public Response<Object> addNominee(@RequestBody NomineeDto nomineeDto, @RequestHeader Long userId) {
		return nomineeService.addNominee(nomineeDto, userId);
	}

	@PostMapping("/add-nominee-fee")
	public Response<Object> addNomineeFee(@RequestParam BigDecimal nomineeFee, @RequestHeader Long userId) {
		return nomineeService.addNomineeFee(nomineeFee, userId);
	}

	@GetMapping("/get-nominee-fee")
	public Response<Object> getNomineeFee(@RequestHeader Long userId) {
		return nomineeService.getNomineeFee(userId);
	}

	@ApiOperation(value = "API to get all Nominee")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Nominee Already Exists"), })
	@GetMapping("/get-all-list")
	public Response<Object> getAllNomineeList() {
		return nomineeService.getAllNomineeList();
	}

	@ApiOperation(value = "API to get all nominne list")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "nominee history fetched successfully"),
			@ApiResponse(code = 205, message = "Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/admin/get-all-nominne-list")
	public Response<Map<String, Object>> getNomineeList(@RequestParam(required = false) Long fkUserId,
			@RequestParam(required = false) String email, @RequestParam Integer page, @RequestParam Integer pageSize,
			@RequestParam(required = false) String phoneNo, @RequestParam(required = false) Float sharePercentage,
			@RequestParam(required = false) NomineeStatus nomineeStatus, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate) {
		return nomineeService.getNomineeList(fkUserId, email, page, pageSize, phoneNo, sharePercentage, nomineeStatus);
	}

	@ApiOperation(value = "API to get Nominee by NomineeId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Nominee Already Exists"), })
	@GetMapping("/get-details-by-nomineeId")
	public Response<Object> getListById(@RequestParam Long nomineeId) {
		return nomineeService.getListById(nomineeId);
	}

	@ApiOperation(value = "API to get Nominee by userId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Nominee Already Exists"), })
	@GetMapping("/get-details-by-userId")
	public Response<Object> getListByuserId(@RequestHeader Long userId, @RequestParam(required = false) String email,
			@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) String phoneNo,
			@RequestParam(required = false) Float sharePercentage,
			@RequestParam(required = false) NomineeStatus nomineeStatus, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate) {
		return nomineeService.getListByuserId(userId, email, page, pageSize, phoneNo, sharePercentage, nomineeStatus,
				fromDate, toDate);
	}

	@ApiOperation(value = "API to Delete Nominee by NomineeId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = " Nominee Already Exists"), })
	@DeleteMapping("/delete-nominee-by-nomineeId")
	public Response<Object> deleteNomineeByNomineeId(@RequestParam Long nomineeId) {
		return nomineeService.deleteNomineeByNomineeId(nomineeId);
	}

	@ApiOperation(value = "API to update the details of the Nominee")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/nominee-details-update")
	public Response<String> NomineeDetailsUpdate(@RequestBody NomimeeUpdateDto profileUpdateDto,
			@RequestParam Long nomineeId) {
		return nomineeService.NomineeDetailsUpdate(profileUpdateDto, nomineeId);
	}

	@ApiOperation(value = "API to get Nominee by userId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "Nominee Already Exists"), })
	@GetMapping("/admin/get-details-by-userId-NEW")
	public Response<Object> getListByNewuserId(@RequestParam Long userId,
			@RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname,
			@RequestParam(required = false) Float sharePercentage,
			@RequestParam(required = false) NomineeStatus nomineeStatus) {
		return nomineeService.getListByNewuserId(userId, firstname, lastname, sharePercentage, nomineeStatus);
	}

	@PostMapping("/admin/nominee-approve-reject")
	public Response<Object> nomineeApprove(@RequestBody NomineeStatusDto nomineeStatusDto) {
		return nomineeService.nomineeApprove(nomineeStatusDto);
	}
}
