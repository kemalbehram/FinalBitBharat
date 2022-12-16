package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.ListingDto;
import com.mobiloitte.content.dto.ListingProjectTeamDto;
import com.mobiloitte.content.model.Response;

public interface ListingService {

	Response<Object> addListing( ListingDto listingDto);


	Response<Object> projectListing(ListingProjectTeamDto listingProjectTeamDto);

	Response<Object> getProjectList();


	Response<Object> getListByListingId(Long listingId);

	Response<Object> getAllList();

}
