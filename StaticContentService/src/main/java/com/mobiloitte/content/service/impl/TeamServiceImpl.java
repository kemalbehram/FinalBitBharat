package com.mobiloitte.content.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.dao.TeamDao;
import com.mobiloitte.content.dto.TeamDto;
import com.mobiloitte.content.dto.TeamUpdateDto;
import com.mobiloitte.content.entities.Team;
import com.mobiloitte.content.enums.TeamStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.TeamService;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamDao teamDao;

	@Override
	public Response<Object> addTeam(TeamDto teamDto) {
		Team team = new Team();

		team.setTeamName(teamDto.getTeamName());
		team.setEducationalDetails(teamDto.getEducationalDetails());
		team.setImageUrl(teamDto.getImageUrl());
		team.setInstagram(teamDto.getInstagram());
		team.setProffessionType(teamDto.getProffessionType());
		team.setTeam(teamDto.getTeam());
		team.setTelegram(teamDto.getTelegram());
		team.setTwitter(teamDto.getTwitter());
		team.setTeamStatus(TeamStatus.ACTIVE);
		teamDao.save(team);
		return new Response<>(200, "Team added successfully");
	}

	@Override
	public Response<Object> getTeam() {
		List<Team> list = teamDao.findAll();
		if (!list.isEmpty()) {
			return new Response<>(200, "List of team get successfully", list);
		}
		return new Response<>(203, "No data found");
	}

	@Override
	public Response<Object> updateTeam(Long projectTeamId, TeamUpdateDto teamUpdateDto) {
		Optional<Team> isDataPresent = teamDao.findByProjectTeamId(projectTeamId);
		if (isDataPresent.isPresent()) {
			isDataPresent.get().setImageUrl(teamUpdateDto.getImageUrl());
			isDataPresent.get().setEducationalDetails(teamUpdateDto.getEducationalDetails());
			isDataPresent.get().setInstagram(teamUpdateDto.getInstagram());
			isDataPresent.get().setProffessionType(teamUpdateDto.getProffessionType());
			isDataPresent.get().setTeam(teamUpdateDto.getTeam());
			isDataPresent.get().setTeamName(teamUpdateDto.getTeamName());
			isDataPresent.get().setTelegram(teamUpdateDto.getTelegram());
			isDataPresent.get().setTwitter(teamUpdateDto.getTwitter());
			isDataPresent.get().setTeamStatus(TeamStatus.ACTIVE);

			teamDao.save(isDataPresent.get());
			return new Response<>(200, "Team update successfully");
		}
		return new Response<>(205, "No data found to update");
	}

	@Override
	public Response<Object> updateStatus(TeamStatus teamStatus, Long projectTeamId) {
		Optional<Team> isDataPresent1 = teamDao.findByProjectTeamId(projectTeamId);
		if (isDataPresent1.isPresent()) {
			isDataPresent1.get().setTeamStatus(teamStatus);
			teamDao.save(isDataPresent1.get());
			return new Response<>(200, "Team update successfully");
		}
		return new Response<>(205, "No data found to update");

	}

	@Override
	public Response<Object> getById(Long projectTeamId) {
		Optional<Team> isDataPresent2 = teamDao.findByProjectTeamId(projectTeamId);
		if (isDataPresent2.isPresent()) {

			return new Response<>(200, "Team data", isDataPresent2.get());
		}
		return new Response<>(205, "No data found");
	}

	@Override
	public Response<Object> getTeamweb() {
		List<Team> listFound = teamDao.findByTeamStatus(TeamStatus.ACTIVE);
		if (!listFound.isEmpty()) {

			return new Response<>(200, "Team data", listFound);
		}
		return new Response<>(205, "No data found");
	}
}
