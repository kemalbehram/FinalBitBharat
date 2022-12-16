package com.mobiloitte.microservice.wallet.service;

import com.mobiloitte.microservice.wallet.dto.WithdrawRequestDto;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.model.Response;

public interface WithdrawType2 {

	Response<String> withdrawBalance(WithdrawRequestDto withdrawRequest, Long fkUserId);

	Response<String> transferFunds(WithdrawRequestDto withdrawRequest, Long fkUserId,
			CoinDepositWithdrawal coinDepositWithdrawal);
	
	Response<String> transferFundsForTaggedCoins(WithdrawRequestDto withdrawRequest, Long fkUserId,
			CoinDepositWithdrawal coinDepositWithdrawal, Coin coinDetails);
}
