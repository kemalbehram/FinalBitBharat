package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.AnnouncementDto;
import com.mobiloitte.content.dto.AnnouncementUpdateDto;
import com.mobiloitte.content.enums.AnnouncementStatus;
import com.mobiloitte.content.model.Response;

public interface AnnouncementService {

	Response<Object> postNewsLetter(AnnouncementDto announcementDto);

	
	Response<Object> getAllAnnouncementForWebsite(Integer page, Integer pageSize, String title);

	Response<Object> getAnnouncement(Long announcementId);

	Response<Object> activeBlockAnnouncement(AnnouncementStatus status, Long announcementId);

	Response<Object> getAnnouncementList(Integer page, Integer pageSize, Long fromDate, Long toDate);

	Response<String> updateAnnouncement(AnnouncementUpdateDto announcementDto);

	Response<Object> deleteAnnouncement(Long announcementId);


}