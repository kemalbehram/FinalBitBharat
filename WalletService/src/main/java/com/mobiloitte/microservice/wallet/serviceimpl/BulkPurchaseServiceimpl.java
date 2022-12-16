package com.mobiloitte.microservice.wallet.serviceimpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mobiloitte.microservice.wallet.dao.BulkPurchaseDao;
import com.mobiloitte.microservice.wallet.dto.BulkPurchaseDto;
import com.mobiloitte.microservice.wallet.model.BulkPurchaseRequest;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.BulkPurchaseService;
import com.mobiloitte.microservice.wallet.utils.MailSender;

@Service
public class BulkPurchaseServiceimpl implements BulkPurchaseService {

	@Autowired
	private BulkPurchaseDao bulkPurchaseDao;

	@Autowired
	private MailSender mailSender;

	private String toMail = "support@crypto.exchange";

	public static final String SUBJECT = "Mail for bulk purchase";

	@Override
	public Response<String> setPurchaseRequest(BulkPurchaseDto bulkPurchaseDto) {
		BulkPurchaseRequest bulkPurchaseRequest = new BulkPurchaseRequest();
		bulkPurchaseRequest.setEmail(bulkPurchaseDto.getEmail());
		bulkPurchaseRequest.setCountry(bulkPurchaseDto.getCountry());
		bulkPurchaseRequest.setCoin(bulkPurchaseDto.getCoin());
		bulkPurchaseRequest.setName(bulkPurchaseDto.getName());
		bulkPurchaseRequest.setMessage(bulkPurchaseDto.getMessage());

		bulkPurchaseRequest.setPaymentMode(bulkPurchaseDto.getPaymentMode());
		bulkPurchaseRequest.setPhoneNo(bulkPurchaseDto.getPhoneNo());
		bulkPurchaseRequest.setQuantity(bulkPurchaseDto.getQuantity());
		bulkPurchaseRequest.setState(bulkPurchaseDto.getState());

		Map<String, Object> sendMailMap = new HashMap<>();
		sendMailMap.put("EMAIL", bulkPurchaseDto.getEmail());
		sendMailMap.put("TOMail", toMail);
		sendMailMap.put("COUNTRYNAME", bulkPurchaseDto.getCountry());
		sendMailMap.put("COIN", bulkPurchaseDto.getCoin());
		sendMailMap.put("SUBJECT", SUBJECT);
		sendMailMap.put("NAME", bulkPurchaseDto.getName());
		sendMailMap.put("MESSAGE", bulkPurchaseDto.getMessage());
		sendMailMap.put("PAYMENTMODE", bulkPurchaseDto.getPaymentMode());
		sendMailMap.put("PHONE", bulkPurchaseDto.getPhoneNo());
		sendMailMap.put("QUANTITY", bulkPurchaseDto.getQuantity());
		sendMailMap.put("STATE", bulkPurchaseDto.getState());

		boolean contactUsIsSend = mailSender.sendMailSenderforBulkPurchase(sendMailMap);
		if (contactUsIsSend) {
			bulkPurchaseDao.save(bulkPurchaseRequest);
			return new Response<>(200, "Purchase Request and mail  Send SuccesFully");
		} else {

			return new Response<>(205, "mail not send");
		}

	}

	@Override
	public Response<Object> getPurchaseRequestDetails(Long requestId) {
		Map<String, Object> responseMap = new HashMap<>();

		Optional<BulkPurchaseRequest> data = bulkPurchaseDao.findById(requestId);
		if (data.isPresent()) {
			responseMap.put("statusCode", 200);
			responseMap.put("message", data);
			responseMap.put("totaldata", null);
			return new Response<>(200, "Purchase Request Details Get Successfully", responseMap);
		} else {
			responseMap.put("statusCode", 205);
			responseMap.put("message", "data Not Found");
			responseMap.put("totaldata", null);
			return new Response<>(200, "Data Not Found", responseMap);
		}
	}

	@Override
	public Response<Map<String, Object>> getPurchaseRequestHistory(Integer page, Integer pageSize) {
		try {
			Page<BulkPurchaseRequest> getRequestList;
			Long getTotalCount;

			getRequestList = bulkPurchaseDao.findAll(PageRequest.of(page, pageSize, Direction.DESC, "requestId"));
			getTotalCount = bulkPurchaseDao.count();

			if (getRequestList.hasContent()) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("RESULT_LIST", getRequestList.getContent());
				responseMap.put("TOTAL_COUNT", getTotalCount);
				return new Response<>(200, "Purchase Request Details Get Successfully", responseMap);
			} else
				return new Response<>(205, "DATA_NOT_FOUND");
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	@Override
	public Response<String> setPurchaseRequestResolved(Long requestId, Boolean isResolved) {

		Optional<BulkPurchaseRequest> data = bulkPurchaseDao.findById(requestId);
		if (data.isPresent()) {
			data.get().setIsResolved(isResolved);
			bulkPurchaseDao.save(data.get());
			return new Response<>(200, "Admin Request Seen SuccesFully");

		} else
			return new Response<>(200, "Request Not Found");

	}
}
