package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.AnnouncementUpdateDto;
import com.mobiloitte.content.dto.LinkDto;
import com.mobiloitte.content.dto.LinkUpdateDto;
import com.mobiloitte.content.enums.Status;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.LinkService;

@RestController
public class LinkController {

	@Autowired
	private LinkService linkService;

	@PostMapping("/add-link")
	public Response<Object> addLink(@RequestHeader Long userId, @RequestBody LinkDto linkDto) {
		return linkService.addLink(userId, linkDto);
	}

	@GetMapping("/get-link-list")
	public Response<Object> getLinkList() {
		return linkService.getLinkList();
	}

	@GetMapping("/get-Link-list-By-Id")
	public Response<Object> getLinkListById(@RequestParam Long linkId) {
		return linkService.getLinkListById(linkId);
	}

	@DeleteMapping("/delete-link")
	public Response<Object> deleteLink(@RequestHeader Long userId, @RequestParam Long linkId) {
		return linkService.deleteLink(userId, linkId);
	}

	@PostMapping("Update-Link")
	public Response<Object> updateLink(@RequestHeader Long userId, @RequestParam Long linkId,
			@RequestBody LinkUpdateDto linkUpdateDto) {
		return linkService.updateLink(userId, linkId, linkUpdateDto);
	}

	@PostMapping("/block-or-unBlock-link")
	public Response<Object> blockOrUnBlockLink(@RequestHeader Long userId, @RequestParam Long linkId,
			@RequestParam Status Status) {
		return linkService.blockOrUnBlockLink(userId, linkId, Status);
	}
}
