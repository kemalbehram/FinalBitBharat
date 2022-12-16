package com.mobiloitte.content.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mobiloitte.content.dao.DisclaimerDao;
import com.mobiloitte.content.dto.DisclaimerDto;
import com.mobiloitte.content.dto.DisclaimerUpdateDto;
import com.mobiloitte.content.entities.Disclaimer;
import com.mobiloitte.content.enums.AnnouncementStatus;
import com.mobiloitte.content.enums.DisclaimerStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.DisclaimerService;

@Service
public class DisclaimerServiceImpl implements DisclaimerService {

	@Autowired
	private DisclaimerDao disclaimerDao;

	@Override
	public Response<Object> getDisclaimerList() {
		List<Disclaimer> list = disclaimerDao.findAll();

		if (!list.isEmpty()) {
			return new Response<>(200, "DISCLAIMER_LIST_FOUND_SUCCESSFULLY", list);
		} else {
			return new Response<>(205, "data is not present");
		}
	}

	@Override
	public Response<Object> getDisclaimer(Long disclaimerId) {
		Optional<Disclaimer> letterExist = disclaimerDao.findById(disclaimerId);

		if (letterExist.isPresent()) {
			return new Response<>(200, "Disclaimer details found successfully", letterExist);

		} else {
			return new Response<>(205, "details are not present");
		}
	}

	@Override
	public Response<Object> postDisclaimer(DisclaimerDto disclaimerDto) {
		Disclaimer announcement = new Disclaimer();
		announcement.setCreationDate(new Date());
		announcement.setDisclaimer(disclaimerDto.getDisclaimer());
		announcement.setDisclaimerStatus(DisclaimerStatus.ACTIVE);
		disclaimerDao.save(announcement);
		return new Response<>(200, "Disclaimer Details Saved Successfully");
	}

	@Override
	public Response<Object> deleteDisclaimer(AnnouncementStatus status, Long disclaimerId) {
		Optional<Disclaimer> announcementExist = disclaimerDao.findById(disclaimerId);

		if (announcementExist.isPresent()) {
			Disclaimer announcement = announcementExist.get();
			announcement.setDisclaimerStatus(DisclaimerStatus.DELETE);
			disclaimerDao.save(announcement);
			return new Response<>(200, "Disclaimer Deleted successfully");
		} else {
			return new Response<>(205, "Something went wrong");

		}
	}

	@Override
	public Response<String> updateDisclaimer(DisclaimerUpdateDto disclaimerUpdateDto) {
		Optional<Disclaimer> getAnnouncementDetails = disclaimerDao.findById(disclaimerUpdateDto.getDisclaimerID());
		if (getAnnouncementDetails.isPresent()) {
			getAnnouncementDetails.get().setDisclaimer(disclaimerUpdateDto.getDisclaimer());
			disclaimerDao.save(getAnnouncementDetails.get());
			return new Response<>(200, "Disclaimer Updated successfully");
		} else {
			return new Response<>(205, "Something Went Wrong");
		}
	}
}
