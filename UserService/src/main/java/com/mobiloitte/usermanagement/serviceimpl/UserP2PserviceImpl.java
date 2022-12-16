package com.mobiloitte.usermanagement.serviceimpl;

import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.mobiloitte.usermanagement.dao.BlockedP2pUserDao;
import com.mobiloitte.usermanagement.dao.KycDao;
import com.mobiloitte.usermanagement.dao.UserDao;
import com.mobiloitte.usermanagement.dao.UserLoginDetailsDao;
import com.mobiloitte.usermanagement.dto.TradeCountDto;
import com.mobiloitte.usermanagement.dto.UserProfileDetailDto;
import com.mobiloitte.usermanagement.enums.KycStatus;
import com.mobiloitte.usermanagement.feign.P2PClient;
import com.mobiloitte.usermanagement.model.BlockedP2pUser;
import com.mobiloitte.usermanagement.model.KYC;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.model.UserLoginDetail;
import com.mobiloitte.usermanagement.service.UserP2PService;

@Service
public class UserP2PserviceImpl implements UserP2PService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private BlockedP2pUserDao blockedP2PUserDao;

	@Autowired
	private UserLoginDetailsDao userLoginDao;

	@Autowired
	private KycDao kycDao;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private P2PClient p2pClient;

	@Override
	public Response<Object> blockP2PUser(Long userId, Long blockUserId) {
		try {
			Optional<User> userExist = userDao.findByUserId(blockUserId);
			if (userExist.isPresent()) {
				Optional<BlockedP2pUser> isUserBlocked = blockedP2PUserDao.findByBlockedByAndBlockedUserId(userId,
						blockUserId);
				if (!isUserBlocked.isPresent()) {
					BlockedP2pUser blockUser = new BlockedP2pUser();
					blockUser.setBlockedBy(userId);
					blockUser.setBlockedUserId(blockUserId);
					blockUser.setIsBlocked(Boolean.TRUE);
					blockedP2PUserDao.save(blockUser);
					return new Response<>(200,
							messageSource.getMessage("user.blocked.suuccessfully", new Object[0], Locale.US));
				} else {
					return new Response<>(201,
							messageSource.getMessage("user.already.blocked", new Object[0], Locale.US));
				}
			} else {
				return new Response<>(201,
						messageSource.getMessage("user.not.blocked.suuccessfully", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(201,
					messageSource.getMessage("user.not.blocked.suuccessfully", new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> getP2PUserProfile(Long userId, Long p2pUserId) {
		try {
			Optional<User> userExist = userDao.findById(p2pUserId);
			if (userExist.isPresent()) {
				Optional<UserLoginDetail> loginDate = userLoginDao.findTopByUserUserIdOrderByUserLoginIdDesc(p2pUserId);
				Date lastSeen = loginDate.get().getCreateTime();
				Long blockCount = blockedP2PUserDao.countByBlockedUserId(p2pUserId);
				Date accountCreaDate = userExist.get().getCreateTime();
				String name = userExist.get().getUserDetail().getFirstName();

				Optional<KYC> userKycStatus = kycDao.findTopByUserUserIdAndKycStatusOrderByKycIdDesc(p2pUserId,
						KycStatus.ACCEPTED);
				Date documentStatus = userKycStatus.get().getUpdateTime();

				Date emaailVerificationTime = userExist.get().getEmailVerificationTime();
				Response<TradeCountDto> tradeCountDto = p2pClient.getTradeCount(p2pUserId);
				UserProfileDetailDto detailDto = new UserProfileDetailDto();
				detailDto.setUserId(p2pUserId);
				detailDto.setUserName(name);
				detailDto.setAccountCreated(accountCreaDate);
				detailDto.setBlocksCount(blockCount);
				detailDto.setLastSeen(lastSeen);
				detailDto.setEmailVerificationTime(emaailVerificationTime);
				detailDto.setIdPassportVerificationTime(documentStatus);
				if (tradeCountDto.getStatus() == 200) {
					detailDto.setFirstPurchase(tradeCountDto.getData().getFirstPurchase());
					detailDto.setTradeVolume(tradeCountDto.getData().getTradeValume());
					detailDto.setNoOfConfirmTrades(tradeCountDto.getData().getTradeCount());
				}
				return new Response<>(200,
						messageSource.getMessage("user.profile.detail.fetch.suuccessfully", new Object[0], Locale.US),
						detailDto);
			} else {
				return new Response<>(201,
						messageSource.getMessage("user.profile.detail.not.fetched", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(201,
					messageSource.getMessage("user.profile.detail.not.fetched", new Object[0], Locale.US));
		}
	}

}