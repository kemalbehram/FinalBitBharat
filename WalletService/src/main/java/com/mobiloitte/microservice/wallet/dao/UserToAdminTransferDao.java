package com.mobiloitte.microservice.wallet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.UserToAdminTransfer;

public interface UserToAdminTransferDao extends JpaRepository<UserToAdminTransfer, Long> {

	Optional<UserToAdminTransfer> findByUserToAdminTransferId(Long referenceId);

	List<UserToAdminTransfer> findByUser1Id(Long userId);

	List<UserToAdminTransfer> findByUser2Id(Long userId);

	Optional<UserToAdminTransfer> findByUser2IdAndUserToAdminTransferId(Long userId, Long userToAdminTransferId);

	List<UserToAdminTransfer> findByUser1IdOrUser2Id(Long userId, Long userId2);

}
