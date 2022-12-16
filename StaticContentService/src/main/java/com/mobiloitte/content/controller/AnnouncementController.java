
package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.AnnouncementDto;
import com.mobiloitte.content.dto.AnnouncementUpdateDto;
//import com.mobiloitte.content.dto.NewsLettarDto;
import com.mobiloitte.content.enums.AnnouncementStatus;
//import com.mobiloitte.content.enums.NewsLettarStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.AnnouncementService;
//import com.mobiloitte.content.service.NewsLettarService;

@RestController
public class AnnouncementController {

	@Autowired
	private AnnouncementService announcementService;

	@PostMapping("/add-announcement")
	public Response<Object> addAnnouncement(@RequestBody AnnouncementDto announcementDto) {
		return announcementService.postNewsLetter(announcementDto);

	}

	@GetMapping("/get-all-announcement-for-website")
	public Response<Object> getAllAnnouncementForWebsite(@RequestParam Integer page, @RequestParam Integer pageSize,
			@RequestParam(required = false) String title) {
		return announcementService.getAllAnnouncementForWebsite(page, pageSize, title);
	}

	@GetMapping("/get-announcement-by-id")
	public Response<Object> getAnnouncement(@RequestParam Long announcementId) {
		return announcementService.getAnnouncement(announcementId);
	}

	@PostMapping("/active-block-announcement")
	public Response<Object> activeBlockAnnouncement(@RequestParam AnnouncementStatus status,
			@RequestParam Long announcementId, @RequestHeader Long userId) {
		return announcementService.activeBlockAnnouncement(status, announcementId);

	}

	@GetMapping("/get-announcement-list")
	public Response<Object> getAnnouncementList(@RequestHeader Long userId, @RequestParam Integer page,
			@RequestParam Integer pageSize, @RequestParam(required = false) Long fromDate, @RequestParam(required = false) Long toDate) {
		return announcementService.getAnnouncementList(page, pageSize,fromDate,toDate);
	}

	@PostMapping("/update-announcement")
	public Response<String> updateAnnouncement(@RequestBody AnnouncementUpdateDto announcementDto) {
		return announcementService.updateAnnouncement(announcementDto);
	}

	@DeleteMapping("/delete-announcement")
	public Response<Object> deleteAnnouncement(@RequestParam Long announcementId) {
		return announcementService.deleteAnnouncement(announcementId);
	}

}
