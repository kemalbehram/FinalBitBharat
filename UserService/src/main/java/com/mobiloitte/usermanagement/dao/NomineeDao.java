package com.mobiloitte.usermanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.enums.NomineeStatus;
import com.mobiloitte.usermanagement.model.Nominee;

/**
 * The Interface NomineeDao.
 * 
 * @author Priyank Mishra
 */
public interface NomineeDao extends JpaRepository<Nominee, Long> {

	List<Nominee> findByUserId(Long userId);

	Optional<Nominee> findByNomineeId(Long nomineeId);

	List<Nominee> findByNomineeStatus(NomineeStatus active);

	List<Nominee> findByUserIdAndNomineeStatus(Long userId, NomineeStatus active);

	List<Nominee> findAllByUserId(Long userId);

	List<Nominee> findByFirstNameOrLastNameOrSharePercentageOrNomineeStatus(String firstname, String lastname,
			Float sharePercentage, NomineeStatus nomineeStatus);

	List<Nominee> findByUserIdAndFirstNameOrLastNameOrSharePercentageOrNomineeStatus(Long userId, String firstname,
			String lastname, Float sharePercentage, NomineeStatus nomineeStatus);

	List<Nominee> findByFirstNameOrLastNameOrSharePercentageOrNomineeStatusAndUserId(String firstname, String lastname,
			Float sharePercentage, NomineeStatus nomineeStatus, Long userId);

}
