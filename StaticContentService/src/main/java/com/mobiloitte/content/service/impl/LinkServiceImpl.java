package com.mobiloitte.content.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.dao.LinkDao;
import com.mobiloitte.content.dto.LinkDto;
import com.mobiloitte.content.dto.LinkUpdateDto;
import com.mobiloitte.content.entities.Announcement;
import com.mobiloitte.content.entities.ContactUs;
import com.mobiloitte.content.entities.Link;
import com.mobiloitte.content.enums.AnnouncementStatus;
import com.mobiloitte.content.enums.Status;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.LinkService;

@Service
public class LinkServiceImpl implements LinkService {

	@Autowired

	private LinkDao linkDao;

	@Override
	public Response<Object> addLink(Long userId, LinkDto linkDto) {
		Optional<Link> isLinkNameExists = linkDao.findByLinkName(linkDto.getLinkName());
		if (!isLinkNameExists.isPresent()) {
			Link link = new Link();

			link.setLink(linkDto.getLink());
			link.setLinkName(linkDto.getLinkName());
			link.setStatus(Status.ACTIVE);
			linkDao.save(link);
			return new Response<>(200, "Link added successfully");
		}
		return new Response<>(205, "Link already present with linkName "+linkDto.getLinkName());
	}

	@Override
	public Response<Object> getLinkList() {
		List<Link> list = linkDao.findAll();
		if (!list.isEmpty()) {
			return new Response<>(200, "list found successfully", list);
		} else {
			return new Response<>(201, "data not found");
		}
	}

	@Override
	public Response<Object> getLinkListById(Long linkId) {
		List<Link> list = linkDao.findByLinkId(linkId);
		if (!list.isEmpty()) {
			return new Response<>(200, "Link List found successfully", list);
		} else {
			return new Response<>(205, "Data is not present");
		}
	}

	@Override
	public Response<Object> deleteLink(Long userId, Long linkId) {
		Optional<Link> linkExist = linkDao.findById(linkId);
		if (linkExist.isPresent()) {
			linkDao.deleteById(linkExist.get().getLinkId());
			return new Response<>(200, "Deleted Successfully");
		}
		return new Response<>(205, "Data Not Found");
	}

	@Override
	public Response<Object> updateLink(Long userId, Long linkId, LinkUpdateDto linkUpdateDto) {
		Optional<Link> isExists = linkDao.findById(linkId);
		if (isExists.isPresent()) {
			isExists.get().setLink(linkUpdateDto.getLink());
			isExists.get().setLinkName(linkUpdateDto.getLinkName());
			isExists.get().setStatus(Status.ACTIVE);
			linkDao.save(isExists.get());
			return new Response<>(200, "Link updated successfully");
		}
		return new Response<>(205, "Data Not Found");
	}

	@Override
	public Response<Object> blockOrUnBlockLink(Long userId, Long linkId, Status status) {
		try {
			Optional<Link> careerDetail = linkDao.findById(linkId);
			if (careerDetail.isPresent()) {
				Link career = careerDetail.get();
				career.setStatus(status);
				linkDao.save(career);

				boolean exist = linkDao.existsByStatus(status.BLOCK);

				if (!exist) {

					return new Response<>(200, "Link Unblocked Successfully");
				}
				return new Response<>(200, "Link Blocked Successfully");
			} else {
				return new Response<>(201, "Link Does Not Blocked");
			}
		} catch (Exception e) {
			return new Response<>(201, "Somthing Went Wrong");
		}
	}
}
