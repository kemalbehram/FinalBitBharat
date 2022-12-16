package com.mobiloitte.content.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.dao.PartnerDao;
import com.mobiloitte.content.dto.PartnerDto;
import com.mobiloitte.content.dto.PartnerUpdateDto;
import com.mobiloitte.content.entities.Partnership;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.PartnershipService;

@Service
public class PartnerShipServiceImpl implements PartnershipService {

	@Autowired
	private PartnerDao partnerDao;

	@Override
	public Response<Object> addPartner(PartnerDto partnerDto) {
		Partnership partnership = new Partnership();
		partnership.setText(partnerDto.getText());
		partnership.setUrl(partnerDto.getUrl());
		partnership.setLogo(partnerDto.getLogo());
		partnership.setDescription(partnerDto.getDescription());
		partnership.setVisible(false);
		partnerDao.save(partnership);
		return new Response<>(200, "Partnership added successfully");
	}

	@Override
	public Response<Object> updatePartner(PartnerUpdateDto partnerupdateDto, Long partnerId) {
		Optional<Partnership> dataFetch = partnerDao.findByPartnershipId(partnerId);
		if (dataFetch.isPresent()) {
			dataFetch.get().setText(partnerupdateDto.getText());
			dataFetch.get().setUrl(partnerupdateDto.getUrl());
			dataFetch.get().setLogo(partnerupdateDto.getLogo());
			dataFetch.get().setDescription(partnerupdateDto.getDescription());
			dataFetch.get().setVisible(false);
			partnerDao.save(dataFetch.get());
			return new Response<>(200, "Partnership updated successfully");
		}
		return new Response<>(205, "no data to update");
	}

	@Override
	public Response<Object> getDataById(Long partnerId) {
		Optional<Partnership> dataFetch = partnerDao.findByPartnershipId(partnerId);
		if (dataFetch.isPresent()) {
			return new Response<>(200, "Data fetched successfully", dataFetch.get());
		}
		return new Response<>(205, "no data present");
	}

	@Override
	public Response<Object> getDatalist() {
		List<Partnership> listNew = partnerDao.findAll();
		if (!listNew.isEmpty()) {
			return new Response<>(200, "List fetched successfully", listNew);
		}
		return new Response<>(205, "no data present");
	}

	@Override
	public Response<Object> deletepartner(Long partnerid) {
		Optional<Partnership> dataFetch = partnerDao.findByPartnershipId(partnerid);
		if (dataFetch.isPresent()) {
			partnerDao.deleteById(partnerid);
			return new Response<>(200, "Partnership deleted successfully");
		}
		return new Response<>(200, "No Data");
	}

}
