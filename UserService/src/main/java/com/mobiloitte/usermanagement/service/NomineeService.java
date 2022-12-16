package com.mobiloitte.usermanagement.service;

import java.math.BigDecimal;
import java.util.Map;

import com.mobiloitte.usermanagement.dto.NomimeeUpdateDto;
import com.mobiloitte.usermanagement.dto.NomineeDto;
import com.mobiloitte.usermanagement.dto.NomineeStatusDto;
import com.mobiloitte.usermanagement.enums.NomineeStatus;
import com.mobiloitte.usermanagement.model.Response;


/**
 * The Interface NomineeService.
 * 
 * @author Priyank Mishra
 */
public interface NomineeService {

	Response<Object> addNominee(NomineeDto nomineeDto, Long userId);

	Response<Object> getAllNomineeList();

	Response<Object> getListById(Long nomineeId);

	Response<Object> getListByuserId(Long userId, String email, Integer page, Integer pageSize, String phoneNo,
			Float sharePercentage, NomineeStatus nomineeStatus, Long fromDate, Long toDate);

	Response<Object> deleteNomineeByNomineeId(Long nomineeId);

	Response<String> NomineeDetailsUpdate(NomimeeUpdateDto profileUpdateDto, Long nomineeId);

	Response<Object> getListByNewuserId(Long userId, String firstname, String lastname, Float sharePercentage, NomineeStatus nomineeStatus);

	Response<Object> nomineeApprove(NomineeStatusDto nomineeStatusDto);

	Response<Object> addNomineeFee(BigDecimal nomineeFee, Long userId);

	Response<Object> getNomineeFee(Long userId);


//	Response<Map<String, Object>> getNomineeList(Long fkUserId, String email, Integer page, Integer pageSize,
//			String phoneNo, Float sharePercentage, NomineeStatus nomineeStatus);

//	Response<Map<String, Object>> getNomineeList(Long fkUserId, String email, Integer page, Integer pageSize,
//			String phoneNo, Float sharePercentage, NomineeStatus nomineeStatus, Long fromDate, Long toDate);

	Response<Map<String, Object>> getNomineeList(Long fkUserId, String email, Integer page, Integer pageSize,
			String phoneNo, Float sharePercentage, NomineeStatus nomineeStatus);
	
	

}
