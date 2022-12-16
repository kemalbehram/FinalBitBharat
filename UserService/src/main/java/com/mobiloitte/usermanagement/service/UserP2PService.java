package com.mobiloitte.usermanagement.service;

import com.mobiloitte.usermanagement.model.Response;

public interface UserP2PService {

	Response<Object> blockP2PUser(Long userId, Long blockUserId);

	Response<Object> getP2PUserProfile(Long userId, Long p2pUserId);

}