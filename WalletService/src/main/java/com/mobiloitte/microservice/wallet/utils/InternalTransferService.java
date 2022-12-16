package com.mobiloitte.microservice.wallet.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.cryptocoins.ETHUtil;
import com.mobiloitte.microservice.wallet.dao.CoinDepositWithdrawalDao;
import com.mobiloitte.microservice.wallet.dao.StorageDetailsDao;
import com.mobiloitte.microservice.wallet.dao.ThresholdTransferHistoryDao;
import com.mobiloitte.microservice.wallet.dao.WalletDao;
import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.entities.StorageDetail;
import com.mobiloitte.microservice.wallet.entities.ThresholdTransferHistory;
import com.mobiloitte.microservice.wallet.entities.Wallet;

@Component
public class InternalTransferService {
	
	private static final Logger LOGGER = LogManager.getLogger(InternalTransferService.class);

	@Autowired
	private WalletDao walletDao;

	
	
	@Autowired
	private ETHUtil ethUtil;
	
	@Autowired
	private StorageDetailsDao storageDetailDao;
	
	@Autowired
	private CoinDepositWithdrawalDao coinDepositWithdrawalDao;
	
	@Autowired
	private ThresholdTransferHistoryDao thresholdTransferHistoryDao;
	
	@Autowired
	private StorageDetailsDao storageDetailsDao;

	//private InternalTransferService() {}

	@Transactional
	@Scheduled(initialDelayString = "${initial.delay.transfer.fixedrate}", fixedRateString = "${internal.transfer.scheduler.fixedrate}")
	public void transferErc20ToHotWallet()
	{
		List<Wallet> getErc20Wallets = walletDao.findByCoinNameIn(Arrays.asList("OMG"/*,"USDT"*/));
		if(getErc20Wallets.isEmpty())
			return;
		for (Wallet wallet : getErc20Wallets) {
			if(wallet.getWalletAddress() != null)
			{
				BigDecimal networkBalance = getActualNetworkBalance(wallet.getCoinName(), wallet.getWalletAddress());
				if(networkBalance.compareTo(BigDecimal.ZERO) > 0)
				{
					if(networkBalance.compareTo(wallet.getCoin().getThresholdPrice()) >=0)
					{
						Optional<StorageDetail> getSpecificStorageDetail = storageDetailDao.findByCoinTypeAndStorageType(wallet.getCoinName(), OtherConstants.STORAGE_HOT);
						if(getSpecificStorageDetail.isPresent() && getSpecificStorageDetail.get().getAddress() != null)
						{
							String getTxnHash = transferToHotWallet(wallet.getCoinName(), getSpecificStorageDetail.get().getAddress(), wallet.getWalletFileName(),
									wallet.getWalletAddress(), networkBalance);
							if(getTxnHash!=null)
							{
								saveTransactionDetails(wallet.getCoinName(), getSpecificStorageDetail.get().getAddress(), getTxnHash, networkBalance);
								LOGGER.info(networkBalance.toPlainString()+" "+wallet.getCoinName()+" transferred to HOT storage successfully");
								break;
							}
							else
								LOGGER.info("No storage found / HOT storage address is not been generated..");
						}
						else
							LOGGER.info("Failed to transfer!!!");
					}
					else
						LOGGER.info("Threshold price not reached...");
				}
				else
					LOGGER.info(networkBalance.toPlainString()+" "+wallet.getCoinName()+" cannot be used to transfer to HOT storage...");
			}
			else
				LOGGER.info("No wallet found...");
		}
	}
	
	@Transactional
	@Scheduled(initialDelayString = "${initial.delay.transfer.fixedrate}", fixedRateString = "${ether.transfer.scheduler.fixedrate}")
	public void sendETHFromHotToUserERC20()
	{
		List<Wallet> getErc20Wallets = walletDao.findByCoinNameIn(Arrays.asList("OMG"/*,"USDT"*/));
		if(getErc20Wallets.isEmpty())
			return;
		for (Wallet wallet : getErc20Wallets) {
			if(wallet.getWalletAddress() != null)
			{
				BigDecimal networkBalance = getActualNetworkBalance(wallet.getCoinName(), wallet.getWalletAddress());
				if(networkBalance.compareTo(wallet.getCoin().getThresholdPrice()) >=0)
				{
					BigDecimal checkEthValue = ethUtil.getBalanceAPI(wallet.getWalletAddress());
					if(checkEthValue.compareTo(BigDecimal.valueOf(0.01)) <= 0)
					{
						Optional<StorageDetail> getStorageDetail = storageDetailsDao.findByCoinTypeAndStorageType(WalletConstants.ETH, OtherConstants.STORAGE_HOT);
						if(getStorageDetail.isPresent())
						{
							if(getStorageDetail.get().getAddress() != null && getStorageDetail.get().getWalletFile() != null)
							{
								String txnHash = ethUtil.transferFundsToToken(wallet.getWalletAddress(), getStorageDetail.get().getAddress(), getStorageDetail.get().getWalletFile());
								if(txnHash!=null)
								{
									saveTransactionDetails(WalletConstants.ETH, wallet.getCoinName(), getStorageDetail.get().getAddress(), wallet.getWalletAddress(), txnHash, BigDecimal.valueOf(0.01));
									LOGGER.info("0.01 ETH been transferred to "+wallet.getWalletAddress()+" successfully...");
									break;
								}
								else
									LOGGER.info("Failed to transfer!!!");
							}
							else
								LOGGER.info("No ETH storage address found, please generate one...");

						}
						else
							LOGGER.info("No storage found, please create one...");
					}
					else
						LOGGER.info(checkEthValue+" is more than 0.01");
				}
				else
					LOGGER.info("Threshold price not reached...");
			}
			else
				LOGGER.info("No wallet found...");
		}

	}
	
	private ThresholdTransferHistory saveTransactionDetails(String fromCoin, String toCoin,String fromAddress, String toAddress, String hash, BigDecimal amount) {
		ThresholdTransferHistory coinDepositWithdrawal = new ThresholdTransferHistory();
		try {
			coinDepositWithdrawal.setToCoinAddress(toAddress);
			coinDepositWithdrawal.setFromCoinAddress(fromAddress);
			coinDepositWithdrawal.setTxnHash(hash);
			coinDepositWithdrawal.setFromCoin(fromCoin);
			coinDepositWithdrawal.setToCoin(toCoin);
			coinDepositWithdrawal.setUnits(BigDecimal.valueOf(0.01));
			coinDepositWithdrawal = thresholdTransferHistoryDao.save(coinDepositWithdrawal);
			return coinDepositWithdrawal;
	} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	private BigDecimal getActualNetworkBalance(String coinName, String address)
	{
		BigDecimal networkBalance = null;
		switch (coinName) {
		case WalletConstants.OMG:
//			networkBalance = omgUtil.getBalanceAPI(address);
			networkBalance=null;
			break;
		default:
			networkBalance = OtherConstants.DEFAULT_BALANCE;
			break;
		}
		return networkBalance;
	}
	
	private String transferToHotWallet(String coinName, String toAddress, String privateKey, String fromAddress, BigDecimal amount)
	{
		String txnHash = null;
		switch (coinName) {
		case WalletConstants.OMG:
//			txnHash = omgUtil.transferFundsToToken(toAddress, fromAddress, privateKey, amount);
			txnHash=null;
			break;
		default:
			txnHash = null;
			break;
		}
		return txnHash;
	}
	
	private CoinDepositWithdrawal saveTransactionDetails(String coinType, String toAddress, String hash, BigDecimal amount) {
		CoinDepositWithdrawal coinDepositWithdrawal = new CoinDepositWithdrawal();
		try {
			coinDepositWithdrawal.setAddress(toAddress);
			coinDepositWithdrawal.setCoinType(coinType);
			coinDepositWithdrawal.setTxnHash(hash);
			coinDepositWithdrawal.setStatus(OtherConstants.CONFIRM);
			coinDepositWithdrawal.setTxnType("EXCHANGE_TRANSFER");
			coinDepositWithdrawal.setUnits(amount);
			coinDepositWithdrawal.setUserEmail("system");
			coinDepositWithdrawal.setUserName("system");
			return coinDepositWithdrawalDao.save(coinDepositWithdrawal);
	} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

}


