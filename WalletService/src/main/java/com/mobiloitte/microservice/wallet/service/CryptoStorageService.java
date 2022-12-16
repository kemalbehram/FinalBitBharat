package com.mobiloitte.microservice.wallet.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mobiloitte.microservice.wallet.dto.ColdStorageRequestDto;
import com.mobiloitte.microservice.wallet.dto.CreateStorageWalletRequestDto;
import com.mobiloitte.microservice.wallet.dto.StorageAddressRequestDto;
import com.mobiloitte.microservice.wallet.dto.StorageLimitDto;
import com.mobiloitte.microservice.wallet.entities.StorageDetail;
import com.mobiloitte.microservice.wallet.model.Response;

/**
 * The Interface CryptoStorageService.
 * @author Ankush Mohapatra
 */
public interface CryptoStorageService {
	
	/**
	 * Gets the storage data by storage type.
	 *
	 * @param storageType the storage type
	 * @return the storage data by storage type
	 */
	Response<List<StorageDetail>> getStorageDataByStorageType(String storageType);
	
	/**
	 * Generate address for storage.
	 *
	 * @param storageAddressRequestDto the storage address request dto
	 * @return the response
	 */
	Response<String> generateAddressForStorage(StorageAddressRequestDto storageAddressRequestDto);
	
	/**
	 * Creates the storage wallet.
	 *
	 * @param createStorageWalletRequestDto the create storage wallet request dto
	 * @return the response
	 */
	Response<String> createStorageWallet(CreateStorageWalletRequestDto createStorageWalletRequestDto);
	
	/**
	 * Update storage balance.
	 *
	 * @param createStorageWalletRequestDto the create storage wallet request dto
	 * @return the response
	 */
	Response<String> updateStorageBalance(CreateStorageWalletRequestDto createStorageWalletRequestDto);
	
	/**
	 * Update cold storage address.
	 *
	 * @param coldStorageRequestDto the cold storage request dto
	 * @return the response
	 */
	Response<String> updateColdStorageAddress(ColdStorageRequestDto coldStorageRequestDto);
	
	Response<Boolean> updateStorageLimit(StorageLimitDto storageLimitDto);

	Response<Object> getStorageDetailsCoinHot(String coin, String storageType);

	Response<Map<String, Object>> getStorageDetailsWithLatestTIme(String coinName);

	Response<List<StorageDetail>> findColdWalletByCoin(String coinName);

	Response<Object> getStorageList();

	Response<Object> updateBalance(BigDecimal storageBalance, String coinName);

}
