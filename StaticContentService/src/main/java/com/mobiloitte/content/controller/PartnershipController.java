package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.PartnerDto;
import com.mobiloitte.content.dto.PartnerUpdateDto;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.PartnershipService;

/**
 * @author Priyank Mishra
 * 
 * */

@RestController
public class PartnershipController {

	
	@Autowired 
	private PartnershipService partnershipService;
	
	@PostMapping("/add-partner")
	public Response<Object> addPartner(@RequestBody PartnerDto partnerDto){
		return partnershipService.addPartner(partnerDto);
	}
	
	@PostMapping("/update-partner")
	public Response<Object> updatePartner(@RequestBody PartnerUpdateDto partnerupdateDto,@RequestParam Long partnerId){
		return partnershipService.updatePartner(partnerupdateDto,partnerId);
	}
	
	@GetMapping("/get-partner-by-id")
	public Response<Object> getDataById(@RequestParam Long partnerId){
		return partnershipService.getDataById(partnerId);
	}
	@GetMapping("/get-partner-list")
	public Response<Object> getDatalist(){
		return partnershipService.getDatalist();
	}
	@DeleteMapping("delete-partner")
	public Response<Object> deletepartner(@RequestParam Long partnerid) {
		return partnershipService.deletepartner(partnerid);
	}
}
