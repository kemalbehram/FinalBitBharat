package com.mobiloitte.content.service;

import com.mobiloitte.content.dao.HoldXindiaUpdateDto;
import com.mobiloitte.content.dto.BurnedUpdateDto;
import com.mobiloitte.content.dto.Burneddto;
import com.mobiloitte.content.dto.HoldXindiaDto;
import com.mobiloitte.content.dto.HoldingXindiaUpdateDto;
import com.mobiloitte.content.dto.XindiaDto;
import com.mobiloitte.content.dto.XindiaHoldingDto;
import com.mobiloitte.content.dto.XindiaUpdateDto;
import com.mobiloitte.content.model.Response;

public interface XindiaService {

	Response<Object> addXindia(XindiaDto xindiaDto);

	Response<Object> getXindia();

	Response<Object> updateXindia(XindiaUpdateDto updateDto, Long xindiaId);

	Response<Object> getXindiaById(Long xindiaId);

	Response<Object> addHoldXindia(HoldXindiaDto holdxindia);

	Response<Object> getHoldXindia();

	Response<Object> updateHoldXindia(HoldXindiaUpdateDto holdXindiaUpdateDto, Long holdxindiaId);

	Response<Object> getholdXindiaById(Long holdxindiaId);

	Response<Object> addHolding(XindiaHoldingDto holdingDto);

	Response<Object> getHoldingXindia();

	Response<Object> getholdingXindiaById(Long holdingxindiaId);

	Response<Object> updateHoldingXindia(HoldingXindiaUpdateDto holdXindiaUpdateDto, Long holdListxindiaId);

	Response<Object> deleteHoldingXindia(Long holdListxindiaId);

	Response<Object> addXindiaburned(Burneddto burneddto);

	Response<Object> getXindialist();

	Response<Object> updateXindiaburned(BurnedUpdateDto burnedUpdateDto, Long burnedid);

	Response<Object> getburnedXindiaById(Long burnedid);

	Response<Object> deletexindia(Long burnedid);

}
