package com.mobiloitte.usermanagement.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mobiloitte.usermanagement.dao.BlockedP2pUserDao;
import com.mobiloitte.usermanagement.dao.UserDao;
import com.mobiloitte.usermanagement.dto.BlockedByListDto;
import com.mobiloitte.usermanagement.model.BlockedP2pUser;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.service.AdminP2PService;

@Service
public class AdminP2PServiceImpl implements AdminP2PService {

	@Autowired
	private BlockedP2pUserDao blockedP2PUserDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private MessageSource messageSource;

	@Override
	public Response<Object> getuserBlockedByList(Long userId, Long blockedUserId, Integer page, Integer pageSize) {
		try {
			List<BlockedP2pUser> blockedByList = blockedP2PUserDao.findByBlockedUserId(blockedUserId,
					PageRequest.of(page, pageSize, Direction.DESC, "blockedId"));
			if (!blockedByList.isEmpty()) {
				List<BlockedByListDto> blockedByListDto = new ArrayList<>();
				for (BlockedP2pUser blockedByDetail : blockedByList) {
					BlockedByListDto listDto = new BlockedByListDto();
					listDto.setBlockedId(blockedByDetail.getBlockedId());
					listDto.setDateTime(blockedByDetail.getCreationTime());
					Optional<User> userDetail = userDao.findById(blockedByDetail.getBlockedBy());
					if (userDetail.isPresent()) {
						listDto.setUserName(userDetail.get().getUserDetail().getFirstName());
					}
					blockedByListDto.add(listDto);
				}
				return new Response<>(200,
						messageSource.getMessage("user.blockedby.list.fetch.successfully", new Object[0], Locale.US),
						blockedByListDto);
			} else {
				return new Response<>(201,
						messageSource.getMessage("user.blockedby.list.not.fetched", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(201,
					messageSource.getMessage("user.blockedby.list.not.fetched", new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> removeUserFromBlockList(Long userId, Long blockedId) {
		try {
			Optional<BlockedP2pUser> isBlocked = blockedP2PUserDao.findById(blockedId);
			if (isBlocked.isPresent()) {
				blockedP2PUserDao.deleteById(blockedId);
				return new Response<>(200,
						messageSource.getMessage("user.removed.from.block.list", new Object[0], Locale.US));
			} else {
				return new Response<>(201,
						messageSource.getMessage("user.not.removed.from.block.list", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(201,
					messageSource.getMessage("user.not.removed.from.block.list", new Object[0], Locale.US));
		}
	}

}