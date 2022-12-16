package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.PartnerDto;
import com.mobiloitte.content.dto.PartnerUpdateDto;
import com.mobiloitte.content.model.Response;

public interface PartnershipService {

	Response<Object> addPartner(PartnerDto partnerDto);

	Response<Object> updatePartner(PartnerUpdateDto partnerupdateDto, Long partnerId);

	Response<Object> getDataById(Long partnerId);

	Response<Object> getDatalist();

	Response<Object> deletepartner(Long partnerid);

}
