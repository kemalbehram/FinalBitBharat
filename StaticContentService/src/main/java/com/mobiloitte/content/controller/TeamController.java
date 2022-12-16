package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.TeamDto;
import com.mobiloitte.content.dto.TeamUpdateDto;
import com.mobiloitte.content.enums.TeamStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.TeamService;

@RestController
public class TeamController {

	@Autowired 
	private TeamService teamService;
	
	
	@PostMapping("/add-team")
	public Response<Object> addTeam(@RequestBody TeamDto teamDto){
		return teamService.addTeam(teamDto);
	}
	
	@GetMapping("/get-team")
	public Response<Object> getTeam(){
		return teamService.getTeam();
	}
	@GetMapping("/get-team-web")
	public Response<Object> getTeamweb(){
		return teamService.getTeamweb();
	}
	
	@PostMapping("/update")
	public Response<Object> updateTeam(@RequestParam Long projectTeamId,@RequestBody TeamUpdateDto teamUpdateDto){
		return teamService.updateTeam(projectTeamId,teamUpdateDto);
	}
	
	@PostMapping("/update-status")
	public Response<Object> updateStatus(@RequestParam TeamStatus teamStatus, @RequestParam Long projectTeamId){
		return teamService.updateStatus(teamStatus,projectTeamId);
	}
	
	@GetMapping("/getById")
	public Response<Object> getById(@RequestParam Long projectTeamId){
		return teamService.getById(projectTeamId);
	}
	
	
}
