package com.mobiloitte.microservice.wallet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.StorageDetail;

/**
 * The Interface StorageDetailsDao.
 * 
 * @author Ankush Mohapatra
 */
public interface StorageDetailsDao extends JpaRepository<StorageDetail, Long> {

	/**
	 * Find by coin type and storage type.
	 *
	 * @param coinType    the coin type
	 * @param storageType the storage type
	 * @return the optional
	 */
	Optional<StorageDetail> findByCoinTypeAndStorageType(String coinType, String storageType);

	/**
	 * Find by storage type.
	 *
	 * @param storageType the storage type
	 * @return the list
	 */
	List<StorageDetail> findByStorageType(String storageType);

	Optional<StorageDetail> findByaddress(String string);

	List<StorageDetail> findByStorageTypeAndCoinType(String storageCold, String coinName);

	Optional<StorageDetail> findByCoinType(String coinType);
}
