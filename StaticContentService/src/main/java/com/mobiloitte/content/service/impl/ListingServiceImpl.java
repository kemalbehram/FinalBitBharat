package com.mobiloitte.content.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.constants.EmailConstants;
import com.mobiloitte.content.dao.ListingDao;
import com.mobiloitte.content.dao.ListingProjectDao;
import com.mobiloitte.content.dto.ListingDto;
import com.mobiloitte.content.dto.ListingProjectTeamDto;
import com.mobiloitte.content.entities.Listing;
import com.mobiloitte.content.entities.ListingProjectTeam;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.ListingService;
import com.mobiloitte.content.utils.MailSender;

@Service
public class ListingServiceImpl implements ListingService {

	@Autowired
	private ListingDao listingDao;

	@Autowired
	private ListingProjectDao listingProjectDao;
	@Autowired
	private MailSender mailSender;

	@Override
	public Response<Object> addListing(ListingDto listingDto) {
		Listing listing = new Listing();
		listing.setCreateTime(new Date());
		listing.setEmail(listingDto.getEmail());
		listing.setTokenName(listingDto.getTokenName());
		listing.setCoinImage(listingDto.getCoinImage());
		listing.setWebsite(listingDto.getWebsite());
		listing.setWhitepaperLink(listingDto.getWhitepaperLink());
		listing.setDescription(listingDto.getDescription());
		listing.setTicker(listingDto.getTicker());
		listingDao.save(listing);
		Map<String, Object> sendMailData = setEmailDataForListingSubmitSuccess(listingDto.getEmail());
		mailSender.sendMailToListingSubmissionSuccess(sendMailData, "en");
		return new Response<>(200, "Listing added successfully");
	}

	private Map<String, Object> setEmailDataForListingSubmitSuccess(String email) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, email);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.FORM_SUBMITTED_SUCCESSFULLY);
		return sendMailData;
	}

	@Override
	public Response<Object> getListByListingId(Long listingId) {
		List<Listing> isListingIdExists = listingDao.findByListingId(listingId);
		if (!isListingIdExists.isEmpty()) {
			return new Response<>(200, "List Fetched Successfully", isListingIdExists);
		}
		return new Response<>(205, "No data found");
	}

	@Override
	public Response<Object> projectListing(ListingProjectTeamDto listingProjectTeamDto) {
		ListingProjectTeam listingProjectTeam = new ListingProjectTeam();
		listingProjectTeam.setRepresentativeName(listingProjectTeamDto.getRepresentativeName());
		listingProjectTeam.setRepresentativeEmail(listingProjectTeamDto.getRepresentativeEmail());
		listingProjectTeam.setRepresentativeContactNumber(listingProjectTeamDto.getRepresentativeContactNumber());
		listingProjectTeam.setRepresentativeTelegramLink(listingProjectTeamDto.getRepresentativeTelegramLink());
		listingProjectTeam.setCompanyName(listingProjectTeamDto.getCompanyName());
		listingProjectTeam.setCompanyRegisteredAddress(listingProjectTeamDto.getCompanyRegisteredAddress());
		listingProjectTeam.setProjectTeamEmail(listingProjectTeamDto.getProjectTeamEmail());
		listingProjectTeam.setListingEnum(listingProjectTeamDto.getListingEnum());
		listingProjectTeam.setCoinTokenName(listingProjectTeamDto.getCoinTokenName());
		listingProjectTeam.setCoinImageUrl(listingProjectTeamDto.getCoinImageUrl());
		listingProjectTeam.setWebsiteUrl(listingProjectTeamDto.getWebsiteUrl());
		listingProjectTeam.setContactAddress(listingProjectTeamDto.getContactAddress());
		listingProjectTeam.setDate(listingProjectTeamDto.getDate());
		listingProjectTeam.setProjetDescription(listingProjectTeamDto.getProjetDescription());
		listingProjectTeam.setTokenSupply(listingProjectTeamDto.getTokenSupply());
		listingProjectTeam.setGitHubRepoName(listingProjectTeamDto.getGitHubRepoName());
		listingProjectTeam.setTwitter(listingProjectTeamDto.getTwitter());
		listingProjectTeam.setTelegram(listingProjectTeamDto.getTelegram());
		listingProjectTeam.setMedium(listingProjectTeamDto.getMedium());
		listingProjectTeam.setDiscord(listingProjectTeamDto.getDiscord());
		listingProjectTeam.setTicker(listingProjectTeamDto.getTicker());
		listingProjectDao.save(listingProjectTeam);
		return new Response<>(200, "Listing added successfully");
	}

	@Override
	public Response<Object> getProjectList() {
		List<ListingProjectTeam> list = listingProjectDao.findAll();
		if (!list.isEmpty()) {
			return new Response<>(200, "List fetched successfully", list);
		}
		return new Response<>(205, "No data found");
	}

	@Override
	public Response<Object> getAllList() {
		List<Listing> list = listingDao.findAll();
		if (!list.isEmpty()) {
			return new Response<Object>(200, "List fetched successfully", list);
		} else {
			return new Response<>(205, "No data found");
		}
	}

}
