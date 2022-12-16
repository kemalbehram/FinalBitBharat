package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.TeamDto;
import com.mobiloitte.content.dto.TeamUpdateDto;
import com.mobiloitte.content.enums.TeamStatus;
import com.mobiloitte.content.model.Response;

public interface TeamService {

	Response<Object> addTeam(TeamDto teamDto);

	Response<Object> getTeam();

	Response<Object> updateTeam(Long projectTeamId, TeamUpdateDto teamUpdateDto);

	Response<Object> updateStatus(TeamStatus teamStatus, Long projectTeamId);

	Response<Object> getById(Long projectTeamId);

	Response<Object> getTeamweb();

}
