package com.mobiloitte.microservice.wallet.constants;

import java.math.BigDecimal;

/**
 * The Interface OtherConstants.
 * @author Ankush Mohapatra
 */
public interface OtherConstants {

	/** The utf 8. */
	String UTF_8="UTF-8";

	/** The blank. */
	String BLANK="";

	/** The true. */
	String TRUE="true";

	/** The false. */
	String FALSE="false";

	/** The default balance. */
	BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(-1L);

	/** The storage hot. */
	String STORAGE_HOT="HOT";

	/** The storage cold. */
	String STORAGE_COLD="COLD";

	/** The confirm. */
	String CONFIRM="CONFIRM";

	/** The pending. */
	String PENDING="PENDING";
	String COMPLETE="COMPLETE";

	/** The rejected. */
	String REJECTED="REJECTED";

	/** The fiat currency. */
	String FIAT_CURRENCY="fiat";

	/** The crypto currency. */
	String CRYPTO_CURRENCY="crypto";

	/** The deposit. */
	String DEPOSIT="DEPOSIT";

	/** The withdraw. */
	String WITHDRAW="WITHDRAW";
	
	String DIRECT_WALLET_WITHDRAW="DIRECT_WALLET_WITHDRAW";

	/** The address. */
	String ADDRESS  = "address";

	/** The balance. */
	String BALANCE  = "balance";

	/** The block. */
	String BLOCK = "BLOCK";

	/** The cancel. */
	String CANCEL = "CANCEL";

	/** The trade deduct. */
	String TRADE_DEDUCT = "TRADE_DEDUCT";

	/** The trade credit. */
	String TRADE_CREDIT = "TRADE_CREDIT";

	/** The taker fee. */
	String TAKER_FEE = "TAKER_FEE";

	/** The maker fee. */
	String MAKER_FEE = "MAKER_FEE";

	//CONSTANTS FOR API RESPONSE

	/** The result. */
	String RESULT="result";

	/** The status. */
	String STATUS="status";

	/** The status code. */
	String STATUS_CODE="statusCode";

	/** The message. */
	String MESSAGE="message";

	/** The success. */
	String SUCCESS="success";

	/** The failure. */
	String FAILURE="failure";

	/** The result list. */
	String RESULT_LIST="resultlist";

	/** The total count. */
	String TOTAL_COUNT="totalCount";

	/** The total fee. */
	String TOTAL_FEE="totalFee";

	//SUCCESS RESPONSES
	/** The insertion successful. */
	String WALLET_CREATED_SUCCESSFULLY="wallet created successfully";

	/** The withdraw successful. */
	String WITHDRAW_SUCCESSFUL="Withdraw Successful";

	//CRYPTO COIN API POST METHOD REQUESTS
	/** The sendto. */
	String SENDTO  = "SendTo";

	/** The sendfrom. */
	String SENDFROM  = "SendFrom";

	/** The changeaddress. */
	String CHANGEADDRESS  = "ChangeAddress";

	/** The amounttotransfer. */
	String AMOUNTTOTRANSFER  = "AmountToTransfer";

	/** The sent amount. */
	String SENT_AMOUNT  = "sent-amount";

	/** The fee. */
	String FEE  = "fee";

	/** The tx hash. */
	String TX_HASH  = "tx-hash";

	/** The code. */
	String CODE  = "code";

	/** The coin list fetched successfully. */
	String COIN_LIST_FETCHED_SUCCESSFULLY  = "coin list fetched successfully";

	/** The coin details fetched successfully. */
	String COIN_DETAILS_FETCHED_SUCCESSFULLY = "coin details for a coin fetched successfully";

	/** The withdraw fee update successfully. */
	String WITHDRAW_FEE_UPDATE_SUCCESSFULLY = "withdraw fee updated successfully";

	/** The withdraw fee updation failed. */
	String WITHDRAW_FEE_UPDATION_FAILED = "withdraw fee updation failed";

	//EXCEPTION CONSTANTS
	/** The no wallet found for coin. */
	String NO_WALLET_FOUND_FOR_COIN  = "No wallet found for coin: ";

	/** The and userid. */
	String AND_USERID  = " and userId: ";

	/** The bad request exception. */
	String BAD_REQUEST_EXCEPTION = "Bad Request Exception";

	//ETHEREUM LIVE API REQUESTS & RESPONSE
	/** The eth result. */
	String ETH_RESULT="Result";

	/** The from eth address. */
	String FROM_ETH_ADDRESS="fromAddr";

	/** The to eth address. */
	String TO_ETH_ADDRESS="toAddr";

	/** The private key. */
	String PRIVATE_KEY="privateKey";

	/** The eth txn hash. */
	String ETH_TXN_HASH="txid";

	/** The eth value. */
	String ETH_VALUE  = "value";

	//XRP LIVE API REQUESTS & RESPONSE
	/** The secret. */
	String SECRET="secret";

	/** The xrp balance. */
	String XRP_BALANCE="xrpBalance";

	/** The xrp source address. */
	String XRP_SOURCE_ADDRESS="srcAddr";

	/** The xrp destination address. */
	String XRP_DESTINATION_ADDRESS="destAddr";

	/** The xrp to tag id. */
	String XRP_TO_TAG_ID="toTag";

	/** The amount. */
	String AMOUNT="amount";

	/** The xrp deposit address. */
	String XRP_DEPOSIT_ADDRESS="addr";

	/** The xrp deposit tag id. */
	String XRP_DEPOSIT_TAG_ID="destTags";

	/** The xrp response code. */
	String XRP_RESPONSE_CODE="responseCode";

	String XRP_TXN_HASH="transaction_hash";

	/** The xrp data. */
	String XRP_DATA="data";

	//ERC-20 TOKENS REQUESTS AND RESPONSES
	/** The hash. */
	String HASH="hash";

	/** The from. */
	String FROM="from";

	/** The value. */
	String VALUE="value";

	/** The erc 20 from address. */
	String ERC_20_FROM_ADDRESS="fromAddress";

	/** The erc 20 deposits. */
	String ERC_20_DEPOSITS="deposits";
	
	String ERC_20_TXN_HASH="transactionHash";

	String ERC_20_RESULTS="result";

	String BUY="BUY";

	String SELL="SELL";

	String APPROVE="APPROVE";

	//NEO API CONSTANTS
	String NEO_RESPONSE_CODE="response_code";

	String FROM_ADDRESS="from_address";

	//EOS API CONSTANTS
	String EOS_ACCOUNT_NAME="account_name";

	String EOS_PRIVATE_KEY="private_key";

	String EOS_PUBLIC_KEY="public_key";

	String EOS_SENDER="sender";

	String EOS_RECEIVER="receiver";

	String EOS_MEMO="memo";

	String EOS_SECRET_KEY="key";

	String EOS_TXN_ID="txid";

	//XLM LIVE API REQUESTS & RESPONSE
	String XLM_ADDRESS="publicKey";

	String XLM_PRIVATE_KEY="secretKey";

	String XLM_FROM_ADDRESS="from_address";

	String XLM_FROM_PRIVATE_KEY="secret_address";

	String XLM_TO_ADDRESS="to_address";

	String XLM_TO_MEMO="to_memo";

	String XLM_TXN_HASH="Hash";

	String XLM_DATA="data";

	String TAG = "tag";


	//TRX
	String HEX_ADDRESS="hexAddress";
	
	String TO_ADDRESS="toAddress";
	
	//MIOTA
	String SEED="seed";
}
