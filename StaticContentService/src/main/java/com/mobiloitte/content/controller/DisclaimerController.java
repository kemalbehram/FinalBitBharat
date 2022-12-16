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
import com.mobiloitte.content.dto.DisclaimerDto;
import com.mobiloitte.content.dto.DisclaimerUpdateDto;
import com.mobiloitte.content.enums.AnnouncementStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.DisclaimerService;

@RestController
public class DisclaimerController {
	
	@Autowired
	private DisclaimerService disclaimerService;
	
	@PostMapping("/add-Disclaimer")
	public Response<Object> addDisclaimer(@RequestBody DisclaimerDto disclaimerDto) {
		return disclaimerService.postDisclaimer(disclaimerDto);

	}
	
	@PostMapping("/update-Disclaimer") 
	public Response<String> updateDisclaimer(@RequestBody DisclaimerUpdateDto DisclaimerUpdateDto) {
		return disclaimerService.updateDisclaimer(DisclaimerUpdateDto);
	}
	

	@GetMapping("/get-Disclaimer-by-id")
	public Response<Object> getDisclaimer(@RequestParam Long disclaimerId) {
		return disclaimerService.getDisclaimer(disclaimerId);
	}

	@DeleteMapping("/delete-disclaimer")
	public Response<Object> deleteDisclaimer(@RequestParam AnnouncementStatus status,
			@RequestParam Long disclaimerId) {
		return disclaimerService.deleteDisclaimer(status, disclaimerId);

	}

	@GetMapping("/get-Disclaimer-list")
	public Response<Object> getDisclaimerList() {
		return disclaimerService.getDisclaimerList();
	}

}
