package com.mobiloitte.content.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.dao.BurnedDao;
import com.mobiloitte.content.dao.HoldXindiaDao;
import com.mobiloitte.content.dao.HoldXindiaUpdateDto;
import com.mobiloitte.content.dao.HoldingListDao;
import com.mobiloitte.content.dao.XindiaDao;
import com.mobiloitte.content.dto.BurnedUpdateDto;
import com.mobiloitte.content.dto.Burneddto;
import com.mobiloitte.content.dto.HoldXindiaDto;
import com.mobiloitte.content.dto.HoldingXindiaUpdateDto;
import com.mobiloitte.content.dto.XindiaDto;
import com.mobiloitte.content.dto.XindiaHoldingDto;
import com.mobiloitte.content.dto.XindiaUpdateDto;
import com.mobiloitte.content.entities.Burned;
import com.mobiloitte.content.entities.HoldXindia;
import com.mobiloitte.content.entities.Partnership;
import com.mobiloitte.content.entities.Xindia;
import com.mobiloitte.content.entities.XinidiaHoldingList;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.XindiaService;

@Service
public class XindiaServiceImpl implements XindiaService {

	@Autowired
	private XindiaDao xindiaDao;

	@Autowired
	private BurnedDao burnedDao;
	@Autowired
	private HoldXindiaDao xindiaDao2;
	@Autowired
	private HoldingListDao holdingListDao;

	@Override
	public Response<Object> addXindia(XindiaDto xindiaDto) {
		Xindia xindia = new Xindia();
		xindia.setCoinImage(xindiaDto.getCoinImage());
		xindia.setCoinName(xindiaDto.getCoinName());
		xindia.setTitle(xindiaDto.getTitle());
		xindia.setTitle2(xindiaDto.getTitle2());
		xindia.setAbout(xindiaDto.getAbout());
		xindia.setBuy(xindiaDto.getBuy());
		xindia.setFooter(xindiaDto.getFooter());
		xindiaDao.save(xindia);
		return new Response<>(200, "Data added successfully");
	}

	@Override
	public Response<Object> getXindia() {
		List<Xindia> listNew = xindiaDao.findAll();
		if (!listNew.isEmpty()) {
			return new Response<>(200, "Data found successfully", listNew);
		}
		return new Response<>(200, "No data found");
	}

	@Override
	public Response<Object> updateXindia(XindiaUpdateDto updateDto, Long xindiaId) {
		Optional<Xindia> dataNew = xindiaDao.findByXindiaId(xindiaId);
		if (dataNew.isPresent()) {
			dataNew.get().setCoinImage(updateDto.getCoinImage());
			dataNew.get().setCoinName(updateDto.getCoinName());
			dataNew.get().setTitle(updateDto.getTitle());
			dataNew.get().setTitle2(updateDto.getTitle2());
			dataNew.get().setAbout(updateDto.getAbout());
			dataNew.get().setBuy(updateDto.getBuy());
			dataNew.get().setFooter(updateDto.getFooter());
			xindiaDao.save(dataNew.get());
			return new Response<>(200, "Data updated successfully");
		}
		return new Response<>(200, "No data to update");
	}

	@Override
	public Response<Object> getXindiaById(Long xindiaId) {
		Optional<Xindia> dataNew1 = xindiaDao.findByXindiaId(xindiaId);
		if (dataNew1.isPresent()) {
			return new Response<>(200, "Data found successfully", dataNew1);
		}
		return new Response<>(200, "No data");

	}

	@Override
	public Response<Object> addHoldXindia(HoldXindiaDto holdxindia) {
		HoldXindia holdXindia2 = new HoldXindia();
		holdXindia2.setOtcDiscount(holdxindia.getOtcDiscount());
		holdXindia2.setToBecome(holdxindia.getToBecome());
		holdXindia2.setTradingFeeDiscount(holdxindia.getTradingFeeDiscount());
		holdXindia2.setWeekly(holdxindia.getWeekly());
		xindiaDao2.save(holdXindia2);
		return new Response<>(200, "Data added successfully");
	}

	@Override
	public Response<Object> getHoldXindia() {
		List<HoldXindia> listNew = xindiaDao2.findAll();
		if (!listNew.isEmpty()) {
			return new Response<>(200, "Data found successfully", listNew);
		}
		return new Response<>(200, "No data found");
	}

	@Override
	public Response<Object> updateHoldXindia(HoldXindiaUpdateDto holdXindiaUpdateDto, Long holdxindiaId) {
		Optional<HoldXindia> dataNew = xindiaDao2.findByHoldXindiaId(holdxindiaId);
		if (dataNew.isPresent()) {
			dataNew.get().setOtcDiscount(holdXindiaUpdateDto.getOtcDiscount());
			dataNew.get().setToBecome(holdXindiaUpdateDto.getToBecome());
			dataNew.get().setTradingFeeDiscount(holdXindiaUpdateDto.getTradingFeeDiscount());
			dataNew.get().setWeekly(holdXindiaUpdateDto.getWeekly());
			xindiaDao2.save(dataNew.get());
			return new Response<>(200, "Data updated successfully");
		}
		return new Response<>(200, "No data to update");
	}

	@Override
	public Response<Object> getholdXindiaById(Long holdxindiaId) {
		Optional<HoldXindia> dataNew1 = xindiaDao2.findByHoldXindiaId(holdxindiaId);
		if (dataNew1.isPresent()) {
			return new Response<>(200, "Data found successfully", dataNew1);
		}
		return new Response<>(200, "No data found");
	}

	@Override
	public Response<Object> addHolding(XindiaHoldingDto holdingDto) {
		XinidiaHoldingList holdingList = new XinidiaHoldingList();
		holdingList.setHolding(holdingDto.getHolding());
		holdingList.setOtherPrivilege(holdingDto.getOtherPrivilege());
		holdingList.setTradingFee(holdingDto.getTradingFee());
		holdingList.setXindiaOtc(holdingDto.getXindiaOtc());
		holdingListDao.save(holdingList);
		return new Response<>(200, "Data added successfully");
	}

	@Override
	public Response<Object> getHoldingXindia() {
		List<XinidiaHoldingList> listNew = holdingListDao.findAll();
		if (!listNew.isEmpty()) {
			return new Response<>(200, "Data found successfully", listNew);
		}
		return new Response<>(200, "No data found");
	}

	@Override
	public Response<Object> getholdingXindiaById(Long holdingxindiaId) {
		Optional<XinidiaHoldingList> dataNew2 = holdingListDao.findByHoldingId(holdingxindiaId);
		if (dataNew2.isPresent()) {
			return new Response<>(200, "Data found successfully", dataNew2);
		}
		return new Response<>(200, "No data found");
	}

	@Override
	public Response<Object> updateHoldingXindia(HoldingXindiaUpdateDto holdXindiaUpdateDto, Long holdListxindiaId) {
		Optional<XinidiaHoldingList> dataNew2 = holdingListDao.findByHoldingId(holdListxindiaId);
		if (dataNew2.isPresent()) {
			dataNew2.get().setHolding(holdXindiaUpdateDto.getHolding());
			dataNew2.get().setOtherPrivilege(holdXindiaUpdateDto.getOtherPrivilege());
			dataNew2.get().setTradingFee(holdXindiaUpdateDto.getTradingFee());
			dataNew2.get().setXindiaOtc(holdXindiaUpdateDto.getXindiaOtc());
			holdingListDao.save(dataNew2.get());
			return new Response<>(200, "Data updated successfully");
		}
		return new Response<>(200, "No data to update");
	}

	@Override
	public Response<Object> deleteHoldingXindia(Long holdListxindiaId) {
		Optional<XinidiaHoldingList> dataNew2 = holdingListDao.findByHoldingId(holdListxindiaId);
		if (dataNew2.isPresent()) {
			holdingListDao.deleteById(holdListxindiaId);
			return new Response<>(200, "Data deleted successfully");
		}
		return new Response<>(200, "No data found to delete");
	}

	@Override
	public Response<Object> addXindiaburned(Burneddto burneddto) {
		Burned burned = new Burned();
		burned.setBurned(burneddto.getBurned());
		burned.setCirculatingSupply(burneddto.getCirculatingSupply());
		burned.setPendindBurned(burneddto.getPendindBurned());
		burnedDao.save(burned);
		return new Response<>(200, "Data added successfully");
	}

	@Override
	public Response<Object> getXindialist() {
		List<Burned> listburned = burnedDao.findAll();
		if (!listburned.isEmpty()) {
			return new Response<>(200, "Data found successfully", listburned);
		}
		return new Response<>(200, "No data found");
	}

	@Override
	public Response<Object> updateXindiaburned(BurnedUpdateDto burnedUpdateDto, Long burnedid) {
		Optional<Burned> updateburned = burnedDao.findByBurnedId(burnedid);
		if (updateburned.isPresent()) {
			updateburned.get().setBurned(burnedUpdateDto.getBurned());
			updateburned.get().setCirculatingSupply(burnedUpdateDto.getCirculatingSupply());
			updateburned.get().setPendindBurned(burnedUpdateDto.getPendindBurned());
			burnedDao.save(updateburned.get());
			return new Response<>(200, "Data updated successfully");
		}
		return new Response<>(200, "No data found to update");
	}

	@Override
	public Response<Object> getburnedXindiaById(Long burnedid) {
		Optional<Burned> updateburned = burnedDao.findByBurnedId(burnedid);
		if (updateburned.isPresent()) {
			return new Response<>(200, "Data found successfully", updateburned);
		}
		return new Response<>(200, "No data found");
	}

	@Override
	public Response<Object> deletexindia(Long burnedid) {
		Optional<Burned> updateburned = burnedDao.findByBurnedId(burnedid);
		if (updateburned.isPresent()) {
			burnedDao.deleteById(burnedid);
			return new Response<>(200, "Data deleted successfully");
		}
		return new Response<>(200, "No data to delete");
	}
}