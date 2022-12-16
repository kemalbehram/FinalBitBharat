package com.mobiloitte.usermanagement.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mobiloitte.usermanagement.constants.MessageConstant;
import com.mobiloitte.usermanagement.dao.KycDao;
import com.mobiloitte.usermanagement.dao.UserDao;
import com.mobiloitte.usermanagement.dto.DocumentStatusDto;
import com.mobiloitte.usermanagement.dto.EmailDto;
import com.mobiloitte.usermanagement.dto.GetKycUsersListDto;
import com.mobiloitte.usermanagement.dto.KycDto;
import com.mobiloitte.usermanagement.dto.KycEmailAlertDto;
import com.mobiloitte.usermanagement.dto.UpdateUserKycDto;
import com.mobiloitte.usermanagement.enums.DocumentStatus;
import com.mobiloitte.usermanagement.enums.KycStatus;
import com.mobiloitte.usermanagement.feign.NotificationClient;
import com.mobiloitte.usermanagement.model.Document;
import com.mobiloitte.usermanagement.model.KYC;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.service.AdminService;
import com.mobiloitte.usermanagement.service.UserKycService;
import com.mobiloitte.usermanagement.util.CloudinaryUtil;

@Service
public class UserKycServiceImpl extends MessageConstant implements UserKycService {

	@Autowired
	private KycDao kycDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AdminService adminService;

	ModelMapper modelMapper = new ModelMapper();

	@Value("${tokenSecretKey}")
	private String tokenSecretKey;

	@Autowired
	private CloudinaryUtil cloudinaryUtil;

	@Value("${jwtconfig.expirationTime}")
	private int expirationTime;

	@Value("${tokenExpirationTime}")
	private int tokenExpirationTime;

	@Autowired
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private NotificationClient notificationClient;

	@Value("${spring.project.name}")
	private String projectName;

	@Value("${user.kyc.limit.enabled}")
	private boolean userKycLimitEnabled;

	private static final Logger LOGGER = LogManager.getLogger(AdminServiceImpl.class);

	@Override
	@Transactional
	public Response<Object> saveKycDetails(KycDto kycDto, Long userId) {
		Optional<KYC> checkUser = kycDao.findByUserUserId(userId);
		Optional<User> userDetails = userDao.findByUserId(userId);

		if (!checkUser.isPresent()) {
			KYC kyc = modelMapper.map(kycDto, KYC.class);
			User user = new User();
			user.setUserId(userId);
			kyc.setUser(user);
			if (userDetails.get().getUserDetail().getCountry().equalsIgnoreCase("INDIA")) {
				List<Object> documentsNames = new ArrayList<>();
				List<Object> documentsNamess = new ArrayList<>();

				for (Document documentsName : kycDto.getDocument()) {
					documentsNames.add(documentsName.getDocName());

				}
				documentsNamess.addAll(documentsNames);
			}
			List<Document> documents = new ArrayList<>();
			for (Document document : kycDto.getDocument()) {

				Document doc = new Document();
				doc.setDocName(document.getDocName());
				doc.setDocIdNumber(document.getDocIdNumber());
				doc.setFrontIdUrl(document.getFrontIdUrl());
				doc.setBackIdUrl(document.getBackIdUrl());
				doc.setSelfieUrl(document.getSelfieUrl());
				doc.setDocumentNumber(document.getDocumentNumber());
				doc.setDocIdNumber2(document.getDocIdNumber2());
				doc.setDocName2(document.getDocName2());
				doc.setFrontIdUrl2(document.getFrontIdUrl2());
				doc.setBackIdUrl2(document.getBackIdUrl2());
				doc.setDocumentStatus(DocumentStatus.PENDING);
				doc.setKyc(kyc);
				doc.setLatest(true);
				documents.add(doc);
			}

			kyc.setDocument(documents);
			kyc.setKycStatus(KycStatus.PENDING);
			kycDao.save(kyc);

			Map<String, String> setData = new HashMap<>();
			setData.put(EMAIL_TOKEN, userDetails.get().getEmail());
			EmailDto emailDto = new EmailDto(userId, KYC_SUBMISSIONS, userDetails.get().getEmail(), setData);

			return new Response<>(200, "KYC Request Submitted Successfully");
		} else if (!checkUser.get().getKycStatus().equals(KycStatus.ACCEPTED)) {
			List<Document> actualDocs = checkUser.get().getDocument();
			List<Document> savedDocument = new ArrayList<>(actualDocs);
			List<Document> docToSave = new ArrayList<>();
			KYC kyc = checkUser.get();
			List<Document> toSaveDocument = kycDto.getDocument();
			for (Document doc : toSaveDocument) {
				doc.setLatest(true);
				Optional<Document> existingDocOptional = savedDocument.parallelStream()
						.filter(d -> d.getDocumentNumber() == doc.getDocumentNumber() && d.isLatest()).findAny();
				if (existingDocOptional.isPresent()) {
					Document existingDoc = existingDocOptional.get();
					if (existingDoc.getDocumentStatus() == DocumentStatus.REJECTED) {
						existingDoc.setLatest(false);
						docToSave.add(existingDoc);
						doc.setKyc(kyc);
						doc.setDocumentStatus(DocumentStatus.PENDING);
						docToSave.add(doc);
					} else {
						existingDoc.setDocumentStatus(DocumentStatus.PENDING);
						existingDoc.setDocName(doc.getDocName());
						existingDoc.setDocIdNumber(doc.getDocIdNumber());
						existingDoc.setFrontIdUrl(doc.getFrontIdUrl());
						existingDoc.setBackIdUrl(doc.getBackIdUrl());
						existingDoc.setSelfieUrl(doc.getSelfieUrl());
						docToSave.add(existingDoc);
					}
				} else {
					doc.setDocumentStatus(DocumentStatus.PENDING);
					doc.setKyc(kyc);
					docToSave.add(doc);
				}

			}
			kyc.setKycStatus(KycStatus.PENDING);
			kyc.setDocument(docToSave);
			kycDao.save(kyc);
			return new Response<>(200, "KYC Request Submitted Successfully");
		} else {
			return new Response<>(201,
					messageSource.getMessage(USERMANAGEMENT_USER_KYC_ALREADY_ACCEPTED, new Object[0], Locale.US));
		}
	}

	@Override
	public Response<List<KYC>> getPendingKycList() {
		List<KYC> kycDetail = kycDao.findByKycStatus(KycStatus.PENDING);
		if (kycDetail != null && !kycDetail.isEmpty()) {
			kycDetail.parallelStream().forEach(k -> k.setUser(null));
			return new Response<>(200, "Pending List For Kyc", kycDetail);
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US), kycDetail);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> getKycUsersList(String search, KycStatus kycStatus, Integer page, Integer pageSize,
			Long fromDate, Long toDate, String country) {

		StringBuilder query = new StringBuilder(
				"select k.kycId,k.kycStatus,k.createTime,k.updateTime,k.user.email as email,k.user.userDetail.firstName as firstName,k.user.userDetail.lastName as lastName,k.user.userId as userId,k.user.userDetail.country from KYC k where k.user.role.role='USER'");

		if (kycStatus != null) {
			LOGGER.debug("filtering with user status {}", kycStatus);
			query.append("and k.kycStatus=:kycStatus");
		}
		if (country != null) {
			LOGGER.debug("filtering with user status {}", country);
			query.append("and k.user.userDetail.country=:country");
		}
		if (fromDate != null) {
			LOGGER.debug("filtering with from date {}", new Date(fromDate));
			query.append(" and k.createTime >= :fromDate");
		}
		if (toDate != null) {
			LOGGER.debug("filtering with to date {}", new Date(toDate));
			query.append(" and k.createTime <= :toDate");
		}

		if (search != null) {
			LOGGER.debug("filtering with name {}", search);
			query.append(
					" and ((k.user.userDetail.firstName like :search) or (k.user.userDetail.lastName like :search) or (k.user.email like :search) or(k.kycId like :search))");
		}

		// add
		query.append(" order by k.kycId desc");
		Query createQuery = em.createQuery(query.toString());

		if (kycStatus != null)
			createQuery.setParameter("kycStatus", kycStatus);
		if (country != null)
			createQuery.setParameter("country", country);
		if (fromDate != null)
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null)
			createQuery.setParameter("toDate", new Date(toDate));
		if (search != null)
			createQuery.setParameter("search", '%' + search + '%');
		int filteredResultCount = createQuery.getResultList().size();
		LOGGER.debug("Total Results {}", filteredResultCount);
		if (page != null && pageSize != null) {
			createQuery.setFirstResult(page * pageSize);
			createQuery.setMaxResults(pageSize);
		}

		List<Object[]> list = createQuery.getResultList();

		List<GetKycUsersListDto> response = list.parallelStream().map(p -> {
			GetKycUsersListDto getKycUsersListDto = new GetKycUsersListDto();
			getKycUsersListDto.setKycId((Long) p[0]);
			getKycUsersListDto.setKycStatus((KycStatus) p[1]);
			getKycUsersListDto.setCreateTime((Date) p[2]);
			getKycUsersListDto.setUpdateTime((Date) p[3]);
			getKycUsersListDto.setEmail((String) p[4]);
			getKycUsersListDto.setFirstName((String) p[5]);
			getKycUsersListDto.setLastName((String) p[6]);
			getKycUsersListDto.setUserId((Long) p[7]);
			getKycUsersListDto.setCountry((String) p[8]);
			return getKycUsersListDto;

		}).collect(Collectors.toList());
		Collections.sort(response, (a, b) -> b.getKycId().compareTo(a.getKycId()));
		Map<String, Object> data = new HashMap<>();
		data.put("list", response);
		data.put("totalCount", filteredResultCount);
		return new Response<>(200, "KYC List for Particular User", data);
	}

	@Override
	public Response<KYC> getKycDetails(Long userId) {

		Optional<KYC> kycDetail = kycDao.findByUserUserId(userId);
		if (kycDetail.isPresent()) {
			List<Document> document = new ArrayList<>();
			KYC kyc = kycDetail.get();
			for (Document doc : kyc.getDocument()) {
				if (doc.isLatest()) {
					document.add(doc);
				}
			}
			kyc.setDocument(document);
			return new Response<>(kyc);
		} else {
			return new Response<>(200,
					messageSource.getMessage("KYC Details Fetched Successfully", new Object[0], Locale.US), new KYC());
		}
	}

	@Override
	public Response<Object> uploadFile(MultipartFile uploadFile) {
		String url = cloudinaryUtil.uploadFile(uploadFile);
		return new Response<>(url);
	}

	@Override
	public Response<KYC> getAllKycDetails(Long userId) {
		Optional<KYC> findByUserUserId = kycDao.findByUserUserId(userId);
		if (findByUserUserId.isPresent()) {
			KYC kyc = findByUserUserId.get();
			return new Response<>(kyc);
		} else {
			return new Response<>(200, "KYC Details Fetched Successfully", new KYC());
		}
	}

	@Override
	public Response<Object> changedocStatus(DocumentStatusDto documentStatusDto) {
		Optional<KYC> findByKycId = kycDao.findByKycId(documentStatusDto.getKycId());
		if (findByKycId.get().getKycStatus().equals(KycStatus.ACCEPTED)) {

			return new Response<>(201, messageSource.getMessage("user.kyc.status.approved", new Object[0], Locale.US));
		}
		if (findByKycId.get().getKycStatus().equals(KycStatus.REJECTED)) {
			return new Response<>(201, messageSource.getMessage("user.kyc.status.rejected", new Object[0], Locale.US));
		}
		if (findByKycId.isPresent()) {
			KYC kyc = findByKycId.get();
			for (Document doc : kyc.getDocument()) {
				if (doc.getDocumentId() == documentStatusDto.getDocumentId() && doc.isLatest()) {
					doc.setDocumentStatus(documentStatusDto.getStatus());
					if (doc.getDocumentStatus().equals(DocumentStatus.REJECTED)
							&& documentStatusDto.getReason() != null) {
						doc.setReason(documentStatusDto.getReason());
						kyc.setKycStatus(KycStatus.REJECTED);
						KycEmailAlertDto kycEmailAlert = new KycEmailAlertDto();
						kycEmailAlert.setUserName(kyc.getUser().getUserDetail().getFirstName());
						kycEmailAlert.setUserEmail(kyc.getUser().getEmail());
						kycEmailAlert.setDocName(doc.getDocName());
						kycEmailAlert.setStatus(DocumentStatus.REJECTED.name());
						Map<String, String> setData = new HashMap<>();
						setData.put(EMAIL_TOKEN, kyc.getUser().getEmail());
						setData.put(KYC_REASON_TOKEN, documentStatusDto.getReason());
						EmailDto emailDto = new EmailDto(kyc.getUser().getUserId(), KYC_STATUS_UPDATE_REJECTION,
								kyc.getUser().getEmail(), setData);
						notificationClient.sendNotification(emailDto);
					}
					Boolean result = changeKycStatus(kyc.getDocument());
					if (result) {
						kyc.setKycStatus(KycStatus.ACCEPTED);

						if (userKycLimitEnabled) {
							UpdateUserKycDto updateUserKycDto = new UpdateUserKycDto();
							updateUserKycDto.setUserId(kyc.getUser().getUserId());
							updateUserKycDto.setUsdAmount(20000.0);
							adminService.updateUserKycLimit(updateUserKycDto);
							kycDao.save(kyc);
							KycEmailAlertDto kycEmailAlert = new KycEmailAlertDto();
							kycEmailAlert.setUserName(kyc.getUser().getUserDetail().getFirstName());
							kycEmailAlert.setUserEmail(kyc.getUser().getEmail());
							kycEmailAlert.setDocName(doc.getDocName());
							kycEmailAlert.setStatus(DocumentStatus.ACCEPTED.name());
							Map<String, String> setData = new HashMap<>();
							setData.put(EMAIL_TOKEN, kyc.getUser().getEmail());
							EmailDto emailDto = new EmailDto(kyc.getUser().getUserId(), KYC_STATUS_UPDATE_APPROVAL,
									kyc.getUser().getEmail(), setData);
							notificationClient.sendNotification(emailDto);
							return new Response<>(200, "KYC Approved Successfully");

						} else {
							kycDao.save(kyc);
							return new Response<>(200, "KYC Approved Successfully");
						}
					} else {
						kycDao.save(kyc);
						return new Response<>(200, "KYC Approved Successfully");
					}
				}
			}
			return new Response<>(201,
					messageSource.getMessage(USERMANAGEMENT_DOCUMENT_NOT_FOUND_FOR_USER, new Object[0], Locale.US));

		}
		return new Response<>(400, "User Not Found With KycId" + documentStatusDto.getKycId());
	}

	private Boolean changeKycStatus(List<Document> document) {
		int i = 0;
		for (Document doc : document) {
			if (doc.getDocumentStatus().equals(DocumentStatus.ACCEPTED) && doc.isLatest()) {
				i++;
			}
		}
		return i == 1;
	}

}
