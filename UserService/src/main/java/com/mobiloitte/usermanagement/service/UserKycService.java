package com.mobiloitte.usermanagement.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mobiloitte.usermanagement.dto.DocumentStatusDto;
import com.mobiloitte.usermanagement.dto.KycDto;
import com.mobiloitte.usermanagement.enums.KycStatus;
import com.mobiloitte.usermanagement.model.KYC;
import com.mobiloitte.usermanagement.model.Response;

public interface UserKycService {

	Response<List<KYC>> getPendingKycList();

	Response<Object> getKycUsersList(String search, KycStatus kycStatus, Integer page, Integer pageSize, Long fromDate,
			Long toDate, String country);

	Response<Object> uploadFile(MultipartFile file);

	Response<KYC> getKycDetails(Long userId);

	Response<Object> saveKycDetails(KycDto kycDto, Long userId);

	Response<KYC> getAllKycDetails(Long userId);

	Response<Object> changedocStatus(DocumentStatusDto documentStatusDto);	

}
