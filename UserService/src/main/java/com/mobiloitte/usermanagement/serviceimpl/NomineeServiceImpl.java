package com.mobiloitte.usermanagement.serviceimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.usermanagement.constants.EmailConstants;
import com.mobiloitte.usermanagement.constants.MessageConstant;
import com.mobiloitte.usermanagement.dao.NomineeDao;
import com.mobiloitte.usermanagement.dao.NomineeFeeDao;
import com.mobiloitte.usermanagement.dao.UserDao;
import com.mobiloitte.usermanagement.dto.EmailDto;
import com.mobiloitte.usermanagement.dto.FundUserDto;
import com.mobiloitte.usermanagement.dto.NomimeeUpdateDto;
import com.mobiloitte.usermanagement.dto.NomineeDto;
import com.mobiloitte.usermanagement.dto.NomineeListDto;
import com.mobiloitte.usermanagement.dto.NomineeStatusDto;
import com.mobiloitte.usermanagement.enums.NomineeStatus;
import com.mobiloitte.usermanagement.feign.NotificationClient;
import com.mobiloitte.usermanagement.feign.WalletClient;
import com.mobiloitte.usermanagement.model.Nominee;
import com.mobiloitte.usermanagement.model.NomineeFee;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.service.NomineeService;
import com.mobiloitte.usermanagement.util.MailSender;

/**
 * The Class NomineeServiceImpl.
 * 
 * @author Priyank Mishra
 */

@Service
public class NomineeServiceImpl extends MessageConstant implements NomineeService {

	@Autowired
	private NomineeDao nomineeDao;

	@Autowired
	private UserDao dao;
	@Autowired
	private WalletClient walletClient;
	@Autowired
	private NomineeFeeDao nomineeFeeDao;
	@Autowired
	private MailSender mailSender;
	@Autowired
	private EntityManager em;
	@Autowired
	private NotificationClient notificationClient;

	@Override
	public Response<Object> addNominee(NomineeDto nomineeDto, Long userId) {

		Optional<Nominee> nomineeExists = nomineeDao.findByNomineeId(nomineeDto.getNomineeId());
		Response<FundUserDto> responseData = walletClient.fundUserParam(userId);

		if (!nomineeExists.isPresent()) {
			Nominee nominee = new Nominee();
			nominee.setFirstName(nomineeDto.getFirstName());
			nominee.setLastName(nomineeDto.getLastName());
			nominee.setEmail(nomineeDto.getEmail());
			nominee.setUserId(userId);
			nominee.setRelationShip(nomineeDto.getRelationShip());
			nominee.setImageUrl(nomineeDto.getImageUrl());
			nominee.setImageUrl1(nomineeDto.getImageUrl1());
			nominee.setNomineeStatus(NomineeStatus.PENDING);
			nominee.setAddress(nomineeDto.getAddress());
			nominee.setDob(nomineeDto.getDob());
			nominee.setPhoneNo(nomineeDto.getPhoneNo());
			nominee.setSharePercentage(nomineeDto.getSharePercentage());

			List<Nominee> nomineeAdd = nomineeDao.findByUserId(nominee.getUserId());

			if (!nomineeAdd.isEmpty()) {
				Float count = 0F;
				Float left = 0F;
				for (Nominee data : nomineeAdd) {
					count = data.getSharePercentage() + count;
					left = 100 - count;
				}
				if ((count > 100)) {
					return new Response<>(205, "Share Percentage Limit Exceeds");
				} else if ((count <= 100) && !(count > 100) && (nomineeDto.getSharePercentage() <= left)) {
					if (responseData.getStatus() == 200) {
						List<NomineeFee> listFetched = nomineeFeeDao.findAll();
						if (!listFetched.isEmpty()) {
							if (responseData.getData().getAvailableFund()
									.compareTo(listFetched.get(0).getNomineeFee()) == 1) {
								walletClient.updateFund(userId, responseData.getData().getAvailableFund()
										.subtract(listFetched.get(0).getNomineeFee()));
								walletClient.updateLocked(userId, responseData.getData().getLockedAmount()
										.add(listFetched.get(0).getNomineeFee()));
								nominee.setNomineeFee(listFetched.get(0).getNomineeFee());
								nomineeDao.save(nominee);
								return new Response<>(200, "Nominee Add Successfully");
							}
							return new Response<>(205, "Please update balance to add nominee");
						}
					}

				}
				return new Response<>(205, "Share Percentage Limit Exceeds");
			}
			if (nomineeDto.getSharePercentage() <= 100) {
				if (responseData.getStatus() == 200) {
					List<NomineeFee> listFetched = nomineeFeeDao.findAll();
					if (!listFetched.isEmpty()) {
						if (responseData.getData().getAvailableFund()
								.compareTo(listFetched.get(0).getNomineeFee()) == 1) {
							walletClient.updateFund(userId, responseData.getData().getAvailableFund()
									.subtract(listFetched.get(0).getNomineeFee()));
							walletClient.updateLocked(userId,
									responseData.getData().getLockedAmount().add(listFetched.get(0).getNomineeFee()));
							nominee.setNomineeFee(listFetched.get(0).getNomineeFee());
							nomineeDao.save(nominee);
							return new Response<>(200, "Nominee Add Successfully");
						}
						return new Response<>(205, "Please update balance to add nominee");
					}
				}

			}
		}
		return new Response<>(205, "Share Percentage Limit Exceeds");
	}

	@Override
	public Response<Object> getAllNomineeList() {
		List<Nominee> isNomineeExists = nomineeDao.findByNomineeStatus(NomineeStatus.ACTIVE);
		if (!isNomineeExists.isEmpty()) {
			return new Response<Object>(200, "Nominee List fetched", isNomineeExists);
		} else {
			return new Response<>(205, "Nominee List Not Found");
		}
	}

	@Override
	public Response<Object> getListById(Long nomineeId) {
		Optional<Nominee> isNomineeIDExists = nomineeDao.findById(nomineeId);
		if (isNomineeIDExists.isPresent()) {
			NomineeDto nomineeDto = new NomineeDto();
			nomineeDto.setEmail(isNomineeIDExists.get().getEmail());
			nomineeDto.setFirstName(isNomineeIDExists.get().getFirstName());
			nomineeDto.setImageUrl(isNomineeIDExists.get().getImageUrl());
			nomineeDto.setLastName(isNomineeIDExists.get().getLastName());
			nomineeDto.setRelationShip(isNomineeIDExists.get().getRelationShip());
			nomineeDto.setImageUrl1(isNomineeIDExists.get().getImageUrl1());
			nomineeDto.setNomineeStatus(isNomineeIDExists.get().getNomineeStatus());
			nomineeDto.setAddress(isNomineeIDExists.get().getAddress());
			nomineeDto.setDob(isNomineeIDExists.get().getDob());
			nomineeDto.setPhoneNo(isNomineeIDExists.get().getPhoneNo());
			nomineeDto.setReason(isNomineeIDExists.get().getReason());
			nomineeDto.setSharePercentage(isNomineeIDExists.get().getSharePercentage());
			return new Response<>(200, "Nominee Fetched Successfully", nomineeDto);
		} else {
			return new Response<>(205, "Nominee Detail Doesnot Exists");
		}
	}

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	@Override
	public Response<Object> getListByuserId(Long userId, String email, Integer page, Integer pageSize, String phoneNo,
			Float sharePercentage, NomineeStatus nomineeStatus, Long fromDate, Long toDate) {
		StringBuilder query = new StringBuilder(
				"select c.nomineeId, c.email, c.phoneNo, c.sharePercentage, c.nomineeStatus, c.firstName, c.lastName, c.relationShip, c.date from Nominee c");

		List<String> conditions = new ArrayList<>();

		if (userId != null && !userId.equals("BLANK")) {
			conditions.add(" c.userId=:userId ");
		}
		if (email != null && !email.equals("BLANK")) {
			conditions.add(" c.email=:email ");
		}
		if (phoneNo != null && !phoneNo.equals("BLANK")) {
			conditions.add(" c.phoneNo=:phoneNo ");
		}
		if (sharePercentage != null && !sharePercentage.equals("BLANK")) {
			conditions.add("c.sharePercentage=:sharePercentage");
		}
		if (nomineeStatus != null && !nomineeStatus.equals("BLANK")) {
			conditions.add("c.nomineeStatus=:nomineeStatus");
		}
		if (fromDate != null && !String.valueOf(fromDate).equals("BLANK")) {
			conditions.add(" c.date >= :fromDate ");
		}
		if (toDate != null && !String.valueOf(toDate).equals("BLANK")) {
			conditions.add(" c.date <= :toDate ");
		}
		if (!conditions.isEmpty()) {
			query.append(" where ");
			query.append(String.join(" and ", conditions.toArray(new String[0])));
		}
		query.append(" order by c.nomineeId desc");
		Query createQuery = em.createQuery(query.toString());
		if (userId != null && !userId.equals("BLANK"))
			createQuery.setParameter("userId", userId);

		if (email != null && !email.equals("BLANK"))
			createQuery.setParameter("email", email);
		if (phoneNo != null && !phoneNo.equals("BLANK"))
			createQuery.setParameter("phoneNo", phoneNo);
		if (sharePercentage != null && !sharePercentage.equals("BLANK")) {
			createQuery.setParameter("sharePercentage", sharePercentage);
		}
		if (nomineeStatus != null && !nomineeStatus.equals("BLANK")) {
			createQuery.setParameter("nomineeStatus", nomineeStatus);
		}
		if (fromDate != null && !String.valueOf(fromDate).equals("BLANK"))
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null && !String.valueOf(toDate).equals("BLANK"))
			createQuery.setParameter("toDate", new Date(toDate));
		int filteredResultCount = createQuery.getResultList().size();
		createQuery.setFirstResult(page * pageSize);
		createQuery.setMaxResults(pageSize);
		List<Object[]> list = createQuery.getResultList();
		List<NomineeListDto> response = list.parallelStream().map(o -> {
			NomineeListDto dto = new NomineeListDto();
			dto.setNomineeId((Long) o[0]);
			dto.setEmail((String) o[1]);
			dto.setPhoneNo((String) o[2]);
			dto.setSharePercentage((Float) o[3]);

			dto.setNomineeStatus((NomineeStatus) o[4]);
			dto.setFirstName((String) o[5]);
			dto.setLastName((String) o[6]);
			dto.setRelationShip((String) o[7]);
			dto.setDate((Date) o[8]);
			return dto;
		}).collect(Collectors.toList());
		Map<String, Object> data = new HashMap<>();
		data.put("RESULT_LIST", response);
		data.put(TOTAL_COUNT, filteredResultCount);

		return new Response<>(200, SUCCESS, data);
	}

	@Override
	public Response<Object> deleteNomineeByNomineeId(Long nomineeId) {
		Optional<Nominee> isNomineeIdExists = nomineeDao.findById(nomineeId);
		if (isNomineeIdExists.isPresent()) {
			nomineeDao.deleteById(isNomineeIdExists.get().getNomineeId());
			return new Response<>(200, "Nominee Deleted Successfully");
		} else {
			return new Response<>(205, "Nominee Detail Doesnot Exists");
		}
	}

	@Override
	@Transactional
	public Response<String> NomineeDetailsUpdate(NomimeeUpdateDto nomimeeUpdateDto, Long nomineeId) {
		Optional<Nominee> verifyNominee = nomineeDao.findByNomineeId(nomimeeUpdateDto.getNomineeId());

		if (verifyNominee.isPresent()) {

			verifyNominee.get().setFirstName(nomimeeUpdateDto.getFirstName());
			verifyNominee.get().setLastName(nomimeeUpdateDto.getLastName());
			verifyNominee.get().setEmail(nomimeeUpdateDto.getEmail());
			verifyNominee.get().setRelationShip(nomimeeUpdateDto.getRelationShip());
			verifyNominee.get().setAddress(nomimeeUpdateDto.getAddress());
			verifyNominee.get().setDob(nomimeeUpdateDto.getDob());
			verifyNominee.get().setPhoneNo(nomimeeUpdateDto.getPhoneNo());

			nomineeDao.save(verifyNominee.get());
			return new Response<>(200, "Nominee Details Updated Successfully");
		} else {
			return new Response<>(205, "Nominee Detail Doesnot Exists");
		}
	}

	@Override
	public Response<Object> getListByNewuserId(Long userId, String firstname, String lastname, Float sharePercentage,
			NomineeStatus nomineeStatus) {
		List<Nominee> isuserIDExists = nomineeDao.findByUserId(userId);
		List<Nominee> isNomineeExists = nomineeDao.findByFirstNameOrLastNameOrSharePercentageOrNomineeStatusAndUserId(
				firstname, lastname, sharePercentage, nomineeStatus, userId);
		if ((!isuserIDExists.isEmpty()) && (firstname == null) && (lastname == null) && (sharePercentage == null)
				&& (nomineeStatus == null)) {
			return new Response<Object>(200, "Nominee Fetched Successfully", isuserIDExists);
		} else if (!isNomineeExists.isEmpty()) {
			return new Response<>(200, "Nominee Fetched Successfully", isNomineeExists);
		}

		else {
			return new Response<>(205, "Nominee Detail Doesnot Exists");
		}
	}

	@Override
	public Response<Object> nomineeApprove(NomineeStatusDto nomineeStatusDto) {

		Optional<Nominee> isNomineeExists = nomineeDao.findByNomineeId(nomineeStatusDto.getNomineeId());
		Optional<User> isListPresent = dao.findByUserId(isNomineeExists.get().getUserId());
		String email = isListPresent.get().getEmail().toString();
		Response<FundUserDto> responseData = walletClient.fundUserParam(isNomineeExists.get().getUserId());
		if (isNomineeExists.isPresent()) {
			if (nomineeStatusDto.getStatus().equals(NomineeStatus.ACTIVE)) {

				isNomineeExists.get().setNomineeStatus(nomineeStatusDto.getStatus());
				isNomineeExists.get().setReason(nomineeStatusDto.getReason());
				walletClient.updateLocked(nomineeStatusDto.getUserId(),
						responseData.getData().getLockedAmount().subtract(isNomineeExists.get().getNomineeFee()));
				walletClient.nomineeComision(isNomineeExists.get().getNomineeFee());
				nomineeDao.save(isNomineeExists.get());
				Map<String, Object> sendMailData = setEmailDataForNomineeSubmitSuccess(email);
				mailSender.sendMailToApproveSubmissionSuccess(sendMailData, "en");
				Map<String, String> setData = new HashMap<>();
				setData.put(EMAIL_TOKEN, email);
				EmailDto emailDto = new EmailDto(nomineeStatusDto.getUserId(), NOMINEE_APPROVEL, email, setData);
				notificationClient.sendNotification(emailDto);
				return new Response<>(200, "Nominee Approved");
			} else if (nomineeStatusDto.getStatus().equals(NomineeStatus.REJECTED)) {
				isNomineeExists.get().setNomineeStatus(nomineeStatusDto.getStatus());
				isNomineeExists.get().setReason(nomineeStatusDto.getReason());
				walletClient.updateLocked(nomineeStatusDto.getUserId(),
						responseData.getData().getLockedAmount().subtract(isNomineeExists.get().getNomineeFee()));
				walletClient.updateFund(nomineeStatusDto.getUserId(),
						responseData.getData().getAvailableFund().add(isNomineeExists.get().getNomineeFee()));
				nomineeDao.save(isNomineeExists.get());
				Map<String, Object> sendMailData = setEmailDataForNomineeRejectSubmitSuccess(email);
				mailSender.sendMailTorEJECTSubmissionSuccess(sendMailData, "en");
				Map<String, String> setData = new HashMap<>();
				setData.put(EMAIL_TOKEN, email);
				EmailDto emailDto = new EmailDto(nomineeStatusDto.getUserId(), NOMINEE_REJECTION, email, setData);
				notificationClient.sendNotification(emailDto);
				return new Response<>(200, "Nominee rejected");
			}
			return new Response<>(205, "Nominee Not Present");
		}
		if ((isNomineeExists.isPresent()) && (isNomineeExists.get().getNomineeStatus().equals(NomineeStatus.ACTIVE))) {
			isNomineeExists.get().setNomineeStatus(nomineeStatusDto.getStatus());
			isNomineeExists.get().setReason(nomineeStatusDto.getReason());
			nomineeDao.save(isNomineeExists.get());
			Map<String, Object> sendMailData = setEmailDataForNomineeRejectSubmitSuccess(email);
			mailSender.sendMailTorEJECTSubmissionSuccess(sendMailData, "en");
		}
		return new Response<>(205, "Nominee Not Present");
	}

	private Map<String, Object> setEmailDataForNomineeSubmitSuccess(String email) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, email);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.APPROVED_SUBMITTED_SUCCESSFULLY);
		return sendMailData;
	}

	private Map<String, Object> setEmailDataForNomineeRejectSubmitSuccess(String email) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, email);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.NOMINEE_REJECTED);
		return sendMailData;
	}

	@Override
	public Response<Object> addNomineeFee(BigDecimal nomineeFee, Long userId) {
		Optional<NomineeFee> dataFetch = nomineeFeeDao.findByUserId(userId);
		if (!dataFetch.isPresent()) {
			NomineeFee fee = new NomineeFee();
			fee.setNomineeFee(nomineeFee);
			fee.setUserId(userId);
			nomineeFeeDao.save(fee);
			return new Response<>(200, "Data Added Successfully");
		} else if (dataFetch.isPresent()) {
			dataFetch.get().setNomineeFee(nomineeFee);
			nomineeFeeDao.save(dataFetch.get());
			return new Response<>(200, "Data updated Successfully");
		}
		return new Response<>(500, "something went wrong");
	}

	@Override
	public Response<Object> getNomineeFee(Long userId) {
		List<NomineeFee> dataFetch = nomineeFeeDao.findAll();
		if (!dataFetch.isEmpty()) {
			Map<String, Object> map = new HashMap<>();
			map.put("fee", dataFetch.get(0).getNomineeFee());
			return new Response<>(200, "Data fetched Successfully", map);
		}
		return new Response<>(205, "Data not found");
	}

//	@Override
//	public Response<Map<String, Object>> getNomineeList(Long fkUserId, String email, Integer page, Integer pageSize,
//			String phoneNo, Float sharePercentage, NomineeStatus nomineeStatus, Long fromDate, Long toDate) {

	@Override
	public Response<Map<String, Object>> getNomineeList(Long fkUserId, String email, Integer page, Integer pageSize,
			String phoneNo, Float sharePercentage, NomineeStatus nomineeStatus) {
		StringBuilder query = new StringBuilder(
				"select c.nomineeId, c.email, c.phoneNo, c.sharePercentage, c.nomineeStatus from Nominee c");

		List<String> conditions = new ArrayList<>();

		if (fkUserId != null) {
			conditions.add(" c.userId=:userId ");
		}
		if (email != null && !email.equals("BLANK")) {
			conditions.add(" c.email=:email ");
		}
		if (phoneNo != null && !phoneNo.equals("BLANK")) {
			conditions.add(" c.phoneNo=:phoneNo ");
		}
		if (sharePercentage != null) {
			conditions.add("c.sharePercentage=:sharePercentage");
		}
		if (nomineeStatus != null) {
			conditions.add("c.nomineeStatus=:nomineeStatus");
		}
		if (!conditions.isEmpty()) {
			query.append(" where ");
			query.append(String.join(" and ", conditions.toArray(new String[0])));
		}
		query.append(" order by c.nomineeId desc");
		Query createQuery = em.createQuery(query.toString());
		if (fkUserId != null)
			createQuery.setParameter("userId", fkUserId);

		if (email != null && !email.equals("BLANK"))
			createQuery.setParameter("email", email);
		if (phoneNo != null && !phoneNo.equals("BLANK"))
			createQuery.setParameter("phoneNo", phoneNo);
		if (sharePercentage != null) {
			createQuery.setParameter("sharePercentage", sharePercentage);
		}
		if (nomineeStatus != null) {
			createQuery.setParameter("nomineeStatus", nomineeStatus);
		}
		int filteredResultCount = createQuery.getResultList().size();
		createQuery.setFirstResult(page * pageSize);
		createQuery.setMaxResults(pageSize);
		List<Object[]> list = createQuery.getResultList();
		List<NomineeListDto> response = list.parallelStream().map(o -> {
			NomineeListDto dto = new NomineeListDto();
			dto.setNomineeId((Long) o[0]);
			dto.setEmail((String) o[1]);
			dto.setPhoneNo((String) o[2]);
			dto.setSharePercentage((Float) o[3]);

			dto.setNomineeStatus((NomineeStatus) o[4]);
			return dto;
		}).collect(Collectors.toList());
		Map<String, Object> data = new HashMap<>();
		data.put("RESULT_LIST", response);
		data.put(TOTAL_COUNT, filteredResultCount);

		return new Response<>(200, SUCCESS, data);
	}

}
