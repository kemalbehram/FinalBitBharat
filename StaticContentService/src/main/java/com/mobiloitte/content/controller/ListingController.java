package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.ListingDto;
import com.mobiloitte.content.dto.ListingProjectTeamDto;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.ListingService;

@RestController
public class ListingController {
	
	@Autowired
	private ListingService listingService;
	
	@PostMapping("/add-listing-request-user")
	public Response<Object> addListing( @RequestBody ListingDto listingDto){
		return listingService.addListing(listingDto);
		
	}
	
	@GetMapping("Get-Listing-List-By-Listing-Id")
	public Response<Object> getListByListingId(@RequestParam Long listingId){
		return listingService.getListByListingId(listingId);
	}
	@PostMapping("/add-Project-team-listing")
	public Response<Object> projectListing(@RequestBody ListingProjectTeamDto listingProjectTeamDto){
		return listingService.projectListing(listingProjectTeamDto);
	}
	
	
	@GetMapping("Get-Project-Listing-List")
	public Response<Object> getProjectList(){
		return listingService.getProjectList();
	}
	@GetMapping("Get-Listing-List")
	public Response<Object> getAllList(){
		return listingService.getAllList();

}}
