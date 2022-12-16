

package com.mobiloitte.usermanagement.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mobiloitte.usermanagement.dto.DocumentStatusDto;
import com.mobiloitte.usermanagement.dto.KycDto;
import com.mobiloitte.usermanagement.enums.KycStatus;
import com.mobiloitte.usermanagement.model.KYC;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.UserKycService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class UserKycController {

	@Autowired
	private UserKycService userKycService;

	@Value("${tokenSecretKey}")
	private String tokenSecretKey;

	@ApiOperation(value = "API to save the kyc details of user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping(value = "/save-kyc-details")
	public Response<Object> saveKycDetails(@Valid @RequestBody KycDto kycDto, @RequestHeader Long userId) {
		return userKycService.saveKycDetails(kycDto, userId);
	}

	@ApiOperation(value = "API to upload the file")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Response<Object> uploadFile(@RequestParam MultipartFile file) {
		return userKycService.uploadFile(file);
	}

	// To get the latest kyc of the user
	@ApiOperation(value = "API to get user kyc details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping(value = "/get-kyc-details")
	public Response<KYC> getKycDetails(@RequestHeader Long userId) {
		return userKycService.getKycDetails(userId);
	}

	// To get the latest detail of user kyc to the admin
	@ApiOperation(value = "API to get user kyc details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping(value = "/admin/kyc-management/get-kyc-details")
	public Response<KYC> getKycDetailsByAdmin(@RequestParam Long userId) {
		return userKycService.getKycDetails(userId);
	}

	// To get the all kyc detail of the user Rejected or pending or accepted
	@ApiOperation(value = "API to get user kyc details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping(value = "/get-all-kyc-details")
	public Response<KYC> getAllKycDetails(@RequestHeader Long userId) {
		return userKycService.getAllKycDetails(userId);
	}

	@ApiOperation(value = "API to get pending kyc details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping(value = "/admin/kyc-management/get-pending-kyc-list")
	public Response<List<KYC>> getPendingKycList() {
		return userKycService.getPendingKycList();
	}

	@ApiOperation(value = "API to get kyc details of all users")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping(value = "/admin/kyc-management/filter-kyc-users-list")
	public Response<Object> getKycUsersList(@RequestParam(required = false) String search,
			@RequestParam(required = false) KycStatus kycStatus, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate, @RequestParam(required = false) String country) {
		return userKycService.getKycUsersList(search, kycStatus, page, pageSize, fromDate, toDate, country);
	}

	@ApiOperation(value = "API for document status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "failure"), })
	@PostMapping(value = "/admin/kyc-management/doc-status")
	public Response<Object> documentStatus(@RequestBody DocumentStatusDto documentStatusDto) {
		return userKycService.changedocStatus(documentStatusDto);
	}

}
