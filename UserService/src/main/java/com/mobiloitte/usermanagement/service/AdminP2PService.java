package com.mobiloitte.usermanagement.service;

import com.mobiloitte.usermanagement.model.Response;

public interface AdminP2PService {

	Response<Object> getuserBlockedByList(Long userId, Long blockedUserId, Integer page, Integer pageSize);

	Response<Object> removeUserFromBlockList(Long userId, Long blockedId);

}