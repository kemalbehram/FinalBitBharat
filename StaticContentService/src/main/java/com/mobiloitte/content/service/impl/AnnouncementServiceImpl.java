package com.mobiloitte.content.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.dao.AnnouncementDao;
//import com.mobiloitte.content.dao.NewsLetterStaticDao;
import com.mobiloitte.content.dto.AnnouncementDto;
import com.mobiloitte.content.dto.AnnouncementUpdateDto;
import com.mobiloitte.content.entities.Announcement;
//import com.mobiloitte.content.entities.NewsLetterStatic;
import com.mobiloitte.content.enums.AnnouncementStatus;
//import com.mobiloitte.content.enums.NewsLettarStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.AnnouncementService;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

	@Autowired
	private AnnouncementDao announcementDao;

	@Override
	public Response<Object> postNewsLetter(AnnouncementDto announcementDto) {
		Optional<Announcement> letterExist = announcementDao.findByTitle(announcementDto.getTitle());
		if (!letterExist.isPresent()) {
			Announcement announcement = new Announcement();

			announcement.setCreationDate(new Date());
			announcement.setDescription(announcementDto.getDescription());
			announcement.setAnnouncementStatus(AnnouncementStatus.ACTIVE);
			announcement.setTitle(announcementDto.getTitle());
			announcement.setImage(announcementDto.getImage());
			announcementDao.save(announcement);
			return new Response<>(200, "Announcement Details Saved Successfully");
		} else {
			return new Response<>(205, "title already exists");
		}
	}

	@Override
	public Response<Object> getAllAnnouncementForWebsite(Integer page, Integer pageSize, String title) {
		if (title == null) {
			List<Announcement> letterExist = announcementDao.findByAnnouncementStatusOrderByCreationDateDesc(
					AnnouncementStatus.ACTIVE, PageRequest.of(page, pageSize));

			Map<String, Object> map = new HashMap<>();
			Long count = announcementDao.countByAnnouncementStatus(AnnouncementStatus.ACTIVE);
			if (!letterExist.isEmpty()) {
				map.put("details", letterExist);
				map.put("count", count);
				return new Response<>(200, "Details Found Successfullly", map);
			}
			return new Response<>(205, "Details Not Found Successfullly");
		} else if (title != null) {
			List<Announcement> letterExist = announcementDao
					.findByTitleContainsAndAnnouncementStatusOrderByCreationDateDesc(title, AnnouncementStatus.ACTIVE,
							PageRequest.of(page, pageSize));
			Map<String, Object> map = new HashMap<>();
			Long count = announcementDao.countByAnnouncementStatus(AnnouncementStatus.ACTIVE);
			if (!letterExist.isEmpty()) {
				map.put("details", letterExist);
				map.put("count", count);
				return new Response<>(200, "Details Found Successfullly", map);
			}
			return new Response<>(205, "Details Not Found Successfullly");
		}
		return new Response<>(205, "Details Not Found Successfullly");

	}

	@Override
	public Response<Object> getAnnouncement(Long announcementId) {
		Optional<Announcement> letterExist = announcementDao.findById(announcementId);

		if (letterExist.isPresent()) {
			return new Response<>(200, "Announcement details found successfully", letterExist);

		} else {
			return new Response<>(205, "details are not present");
		}
	}

	@Override
	public Response<Object> activeBlockAnnouncement(AnnouncementStatus status, Long announcementId) {

		Optional<Announcement> letterExist = announcementDao.findById(announcementId);

		if (letterExist.isPresent()) {

			if (status == AnnouncementStatus.ACTIVE) {

				letterExist.get().setAnnouncementStatus(AnnouncementStatus.ACTIVE);
				announcementDao.save(letterExist.get());

				return new Response<>(200, "Announcement active successfully");
			}

			else if (status == AnnouncementStatus.BLOCK) {
				letterExist.get().setAnnouncementStatus(AnnouncementStatus.BLOCK);
				announcementDao.save(letterExist.get());
				return new Response<>(200, "Announcement blocked successfully");
			} else {
				return null;
			}
		}

		else {
			return new Response<>(205, "data is not present");
		}

	}

	@Override
	public Response<Object> getAnnouncementList(Integer page, Integer pageSize, Long fromDate, Long toDate) {
		if ((fromDate != null) && (toDate != null)) {
			List<Announcement> list = announcementDao
					.findByAnnouncementStatusAndCreationDateBetweenOrderByCreationDateDesc(AnnouncementStatus.ACTIVE,
							new Date(fromDate), new Date(toDate), PageRequest.of(page, pageSize));
			if (!list.isEmpty()) {
				return new Response<>(200, "Announcement list found successfully", list);
			} else {
				return new Response<>(205, "data is not present");
			}
		} else if ((fromDate != null)) {
			List<Announcement> list = announcementDao
					.findByAnnouncementStatusAndCreationDateOrderByCreationDateDesc(AnnouncementStatus.ACTIVE,
							new Date(fromDate), PageRequest.of(page, pageSize));
			if (!list.isEmpty()) {
				return new Response<>(200, "Announcement list found successfully", list);
			} else {
				return new Response<>(205, "data is not present");
			}
		} else if ((toDate != null)) {
			List<Announcement> list = announcementDao
					.findByAnnouncementStatusAndCreationDateOrderByCreationDateDesc(AnnouncementStatus.ACTIVE,
							new Date(toDate), PageRequest.of(page, pageSize));
			if (!list.isEmpty()) {
				return new Response<>(200, "Announcement list found successfully", list);
			} else {
				return new Response<>(205, "data is not present");
			}
		} else {
			List<Announcement> list = announcementDao.findByAnnouncementStatusOrderByCreationDateDesc(
					AnnouncementStatus.ACTIVE, PageRequest.of(page, pageSize));
			if (!list.isEmpty()) {
				return new Response<>(200, "Announcement list found successfully", list);
			} else {
				return new Response<>(205, "data is not present");
			}
		}
	}

	@Override
	public Response<String> updateAnnouncement(AnnouncementUpdateDto announcementDto) {
		Optional<Announcement> getAnnouncementDetails = announcementDao.findById(announcementDto.getAnnouncementId());
		if (getAnnouncementDetails.isPresent()) {
			getAnnouncementDetails.get().setTitle(announcementDto.getTitle());
			getAnnouncementDetails.get().setDescription(announcementDto.getDescription());
			getAnnouncementDetails.get().setImage(announcementDto.getImage());
		
			announcementDao.save(getAnnouncementDetails.get());
			return new Response<>(200, "Announcement Updated successfully");
		} else {
			return new Response<>(205, "Something Went Wrong");
		}
	}

	@Override
	public Response<Object> deleteAnnouncement(Long announcementId) {
		Optional<Announcement> announcementExist = announcementDao.findById(announcementId);

		if (announcementExist.isPresent()) {
			Announcement announcement = announcementExist.get();
			announcement.setAnnouncementStatus(AnnouncementStatus.DELETED);
			announcementDao.save(announcement);
			return new Response<>(200, "Announcement Deleted successfully");
		} else {
			return new Response<>(205, "Something went wrong");

		}
	}

}
