package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.SupportDto;
import com.mobiloitte.content.model.Response;

public interface SupportService {

	Response<Object> submitSupportRequest(SupportDto supportDto);

}
