package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.DisclaimerDto;
import com.mobiloitte.content.dto.DisclaimerUpdateDto;
import com.mobiloitte.content.enums.AnnouncementStatus;
import com.mobiloitte.content.model.Response;

public interface DisclaimerService {

	Response<Object> getDisclaimer(Long disclaimerId);

	Response<Object> postDisclaimer(DisclaimerDto disclaimerDto);

	Response<Object> getDisclaimerList();

	Response<Object> deleteDisclaimer(AnnouncementStatus status, Long disclaimerId);

	Response<String> updateDisclaimer(DisclaimerUpdateDto disclaimerUpdateDto);

}
