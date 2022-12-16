package com.mobiloitte.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Team;
import com.mobiloitte.content.enums.TeamStatus;

public interface TeamDao extends JpaRepository<Team, Long>{

	Optional<Team> findByProjectTeamId(Long projectTeamId);

	List<Team> findByTeamStatus(TeamStatus active);

}
