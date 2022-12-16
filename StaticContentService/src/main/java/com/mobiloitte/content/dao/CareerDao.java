package com.mobiloitte.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Career;

public interface CareerDao extends JpaRepository<Career, Long> {



	Optional<Career> findByTeamName(String teamName);

	List<Career> findByTeamId(Long teamId);

//	List<Career> findBySubTeamId(Long subTeamId);

//	List<Career> findByTeamId(Long teamId);
	
	

}
