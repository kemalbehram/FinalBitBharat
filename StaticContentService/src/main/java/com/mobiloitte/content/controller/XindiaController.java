package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dao.HoldXindiaUpdateDto;
import com.mobiloitte.content.dto.BurnedUpdateDto;
import com.mobiloitte.content.dto.Burneddto;
import com.mobiloitte.content.dto.HoldXindiaDto;
import com.mobiloitte.content.dto.HoldingXindiaUpdateDto;
import com.mobiloitte.content.dto.XindiaDto;
import com.mobiloitte.content.dto.XindiaHoldingDto;
import com.mobiloitte.content.dto.XindiaUpdateDto;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.XindiaService;
/**
 * 
 * 
 * @author Priyank Mishra
 * */


@RestController
public class XindiaController {

	@Autowired
	private XindiaService xindiaService;

	@PostMapping("/admin/add-Xindia")
	public Response<Object> addXindia(@RequestBody XindiaDto xindiaDto) {
		return xindiaService.addXindia(xindiaDto);
	}

	@GetMapping("/get-Xindia")
	public Response<Object> getXindia() {
		return xindiaService.getXindia();
	}

	@PostMapping("/admin/update-Xindia")
	public Response<Object> updateXindia(@RequestBody XindiaUpdateDto updateDto,@RequestParam Long xindiaId) {
		return xindiaService.updateXindia(updateDto,xindiaId);
	}
	
	@GetMapping("/admin/get-Xindia-by-Id")
	public Response<Object> getXindiaById(@RequestParam Long xindiaId) {
		return xindiaService.getXindiaById(xindiaId);
	}
	
	@PostMapping("/admin/add-hold-Xindia")
	public Response<Object> addHoldXindia(@RequestBody HoldXindiaDto holdxindia) {
		return xindiaService.addHoldXindia(holdxindia);
	}
	@GetMapping("/get-HoldXindia")
	public Response<Object> getHoldXindia() {
		return xindiaService.getHoldXindia();
	}

	@PostMapping("/admin/update-HoldXindia")
	public Response<Object> updateHoldXindia(@RequestBody HoldXindiaUpdateDto holdXindiaUpdateDto,@RequestParam Long holdxindiaId) {
		return xindiaService.updateHoldXindia(holdXindiaUpdateDto,holdxindiaId);
	}
	
	@GetMapping("/admin/get-holdXindia-by-Id")
	public Response<Object> getholdXindiaById(@RequestParam Long holdxindiaId) {
		return xindiaService.getholdXindiaById(holdxindiaId);
	}
	
	@PostMapping("/admin/add-holdingList")
	public Response<Object> addHolding(@RequestBody XindiaHoldingDto holdingDto) {
		return xindiaService.addHolding(holdingDto);
	}
	
	@GetMapping("/get-HoldingXindia")
	public Response<Object> getHoldingXindia() {
		return xindiaService.getHoldingXindia();
	}
	@GetMapping("/admin/get-holdingXindia-by-Id")
	public Response<Object> getholdingXindiaById(@RequestParam Long holdingxindiaId) {
		return xindiaService.getholdingXindiaById(holdingxindiaId);
	}
	
	@PostMapping("/admin/update-HoldingXindia")
	public Response<Object> updateHoldingXindia(@RequestBody HoldingXindiaUpdateDto holdXindiaUpdateDto,@RequestParam Long holdListxindiaId) {
		return xindiaService.updateHoldingXindia(holdXindiaUpdateDto,holdListxindiaId);
	}
	
	@DeleteMapping("/admin/delete-HoldingXindia")
	public Response<Object> deleteHoldingXindia(@RequestParam Long holdListxindiaId) {
		return xindiaService.deleteHoldingXindia(holdListxindiaId);
	}
	
	@PostMapping("/admin/add-Xindia-Burned")
	public Response<Object> addXindiaburned(@RequestBody Burneddto burneddto ) {
		return xindiaService.addXindiaburned(burneddto);
	}
	
	@GetMapping("/get-Xindia-burned-list")
	public Response<Object> getXindialist() {
		return xindiaService.getXindialist();
	}
	
	@PostMapping("/admin/update-Xindia-burned")
	public Response<Object> updateXindiaburned(@RequestBody BurnedUpdateDto burnedUpdateDto,@RequestParam Long burnedid) {
		return xindiaService.updateXindiaburned(burnedUpdateDto,burnedid);
	}
	@GetMapping("/admin/get-BurnedXindia-by-Id")
	public Response<Object> getburnedXindiaById(@RequestParam Long burnedid) {
		return xindiaService.getburnedXindiaById(burnedid);
	}
	
	@DeleteMapping("delete-burnedxindia")
	public Response<Object> deletexindia(@RequestParam Long burnedid) {
		return xindiaService.deletexindia(burnedid);
	}
}
