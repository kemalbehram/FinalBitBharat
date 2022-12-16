package com.mobiloitte.microservice.wallet.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.microservice.wallet.dao.BankDao;
import com.mobiloitte.microservice.wallet.dto.SearchAndFilterBankDto;
import com.mobiloitte.microservice.wallet.dto.SearchBankDetailDto;
import com.mobiloitte.microservice.wallet.entities.BankDetails;
import com.mobiloitte.microservice.wallet.enums.BankStatus;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.AdminBankService;



@Service
public class AdminBankServiceImpl implements AdminBankService {

	@Autowired
	private BankDao bankDao;

	@Autowired
	private EntityManager em;

	@Override
	public Response<Object> getBankAccountDetails(Long userId, Long bankDetailsId) {
		try {
			Optional<BankDetails> bankDetails = bankDao.findById(bankDetailsId);
			if (bankDetails.isPresent()) {
				return new Response<>(200, "Bank Account Detail Fetched Successfully", bankDetails);
			} else {
				return new Response<>(201, "Bank Account Detail Not Found");
			}
		} catch (Exception e) {
			return new Response<>(201, "Bank Account Detail Not Found");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> searchAndFilterBankAccountList(Long userId, SearchAndFilterBankDto searchAndFilterBankDto) {
		Map<String, Object> map = new HashMap<>();
		try {
			StringBuilder query = new StringBuilder(
					"select bankDetailId, accountHolderName, accountNo, bankName, creationTime, updationTime, bankStatus from BankDetails where isDeleted=false");
			List<String> conditions = new ArrayList<>();
			if (searchAndFilterBankDto.getSearch() != null) {
				conditions.add("((bankName like :search) or (accountHolderName like :search))");
			}
			if (searchAndFilterBankDto.getBankStatus() != null) {
				conditions.add("(bankStatus =:bankStatus)");
			}
			if (searchAndFilterBankDto.getFromDate() != null) {
				conditions.add("(creationTime >=:fromDate)");
			}
			if (searchAndFilterBankDto.getToDate() != null) {
				conditions.add("(creationTime <=:toDate)");
			}
			if (!conditions.isEmpty()) {
				query.append(" and ");
				query.append(String.join(" and ", conditions.toArray(new String[0])));
			}
			query.append(" order by bankDetailId desc ");
			Query createQuery = em.createQuery(query.toString());

			if (searchAndFilterBankDto.getSearch() != null)
				createQuery.setParameter("search", '%' + searchAndFilterBankDto.getSearch() + '%');

			if (searchAndFilterBankDto.getBankStatus() != null)
				createQuery.setParameter("bankStatus", searchAndFilterBankDto.getBankStatus());

			if (searchAndFilterBankDto.getFromDate() != null)
				createQuery.setParameter("fromDate", new Date(searchAndFilterBankDto.getFromDate()));

			if (searchAndFilterBankDto.getToDate() != null)
				createQuery.setParameter("toDate", new Date(searchAndFilterBankDto.getToDate()));
			int filteredResultCount = createQuery.getResultList().size();
			createQuery.setFirstResult(searchAndFilterBankDto.getPage() * searchAndFilterBankDto.getPageSize());
			createQuery.setMaxResults(searchAndFilterBankDto.getPageSize());
			List<Object[]> list = createQuery.getResultList();
			List<SearchBankDetailDto> response = list.parallelStream().map(o -> {
				SearchBankDetailDto dto = new SearchBankDetailDto();
				dto.setBankDetailId((Long) o[0]);
				dto.setAccountHolderName((String) o[1]);
				dto.setAccountNo((String) o[2]);
				dto.setBankName((String) o[3]);
				dto.setCreationTime((Date) o[4]);
				dto.setUpdationTime((Date) o[5]);
				dto.setBankStatus((BankStatus) o[6]);
				return dto;
			}).collect(Collectors.toList());
			if (!response.isEmpty()) {
				map.put("size", filteredResultCount);
				map.put("list", response);
				return new Response<>(200, "Bank Account List Fetched Successfully", map);
			} else {
				map.put("size", 0);
				map.put("list", Collections.emptyList());
				return new Response<>(201, "Bank Account Search List Not Fetched", map);
			}
		} catch (Exception e) {
			map.put("size", 0);
			map.put("list", Collections.emptyList());
			return new Response<>(201, "Bank Account Search List Not Fetched", map);
		}
	}

	@Override
	public Response<Object> approveOrRejectBank(Long userId, Long bankDetailId, BankStatus bankStatus) {
		try {
			Optional<BankDetails> bankDedail = bankDao.findByBankDetailId(bankDetailId);
			if (bankDedail.isPresent()) {
				BankDetails bank = bankDedail.get();
				bank.setBankStatus(bankStatus);
				bankDao.save(bank);
				return new Response<>(200, "Bank Account Status Changed Successfully");
			} else {
				return new Response<>(201, "Bank Account Status Not Changed");
			}
		} catch (Exception e) {
			return new Response<>(201, "Somthing Went Wrong");
		}
	}

}