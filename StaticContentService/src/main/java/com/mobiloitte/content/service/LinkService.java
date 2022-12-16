package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.LinkDto;
import com.mobiloitte.content.dto.LinkUpdateDto;
import com.mobiloitte.content.dto.ListingDto;
import com.mobiloitte.content.enums.Status;
import com.mobiloitte.content.model.Response;

public interface LinkService {

	Response<Object> addLink(Long userId, LinkDto linkDto);

	Response<Object> getLinkList();

	Response<Object> getLinkListById(Long linkId);

	Response<Object> deleteLink(Long userId, Long linkId);

	Response<Object> updateLink(Long userId, Long linkId, LinkUpdateDto linkUpdateDto);

	Response<Object> blockOrUnBlockLink(Long userId, Long linkId, Status status);

}
