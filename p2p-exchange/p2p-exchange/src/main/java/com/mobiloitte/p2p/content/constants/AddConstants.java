package com.mobiloitte.p2p.content.constants;

import java.math.BigDecimal;

import com.mobiloitte.p2p.content.model.Response;

public class AddConstants {
	protected AddConstants() {

	}

	public static final String RESULT_LIST = "RESULT_LIST";
	public static final String TOTAL_COUNT = "TOTAL_COUNT";
	public static final String BUY = "BUY";
	public static final String BTC = "BTC";
	public static final String ETH = "ETH";

	public static final String USDT = "LTC";

	public static final String TRX = "TRX";

	public static final String BNB = "BNB";

	public static final String USD = "USD";

	/** The utf 8. */
	String UTF_8 = "UTF-8";

	/** The blank. */
	String BLANK = "";

	/** The true. */
	String TRUE = "true";

	/** The false. */
	String FALSE = "false";

	/** The default balance. */
	BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(-1L);

	/** The storage hot. */
	protected String BLOCKED_BALLANCE_SHOULD_BE_GRATTER_THAN_TRADE_FEES = "Blocked Ballance Should be Gratter Than TradeFees";
	protected String SEND_TRADE_REQUEST_SUCCESSFULLY = "Send Trade Request Successfully";

	/** The confirm. */
	String CONFIRM = "CONFIRM";

	/** The pending. */
	String PENDING = "PENDING";

	/** The rejected. */
	String REJECTED = "REJECTED";

	/** The fiat currency. */
	String FIAT_CURRENCY = "fiat";

	/** The crypto currency. */
	String CRYPTO_CURRENCY = "crypto";

	/** The deposit. */
	String NOT_APPLICABLE_FOR_RELEASE_COINS = "You Are Not Applicable To Release Coins";

	/** The withdraw. */
	protected String FIRST_PAID_ANYWAY = "First Of All Buyer Has to Paid";

	/** The address. */
	protected String COINNAME_SHOULD_NOT_NULL = "CoinName  Should Not be Null";

	/** The balance. */
	protected String ADDVERTISEMENT_NOT_FOUND = "Advertisement data not found";

	/** The block. */
	protected String ESCROW_NOT_FOUND = "Escrow data not found";

	/** The cancel. */
	protected String CANCEL = "CANCEL";

	/** The trade deduct. */
	protected String TRADE_DEDUCT = "TRADE_DEDUCT";

	/** The trade credit. */
	protected String MARKET_PRICE_NOT_FOUND = "Market Price  Not Found For This Coin";

	/** The taker fee. */
	protected String KYC_NOT_FOUND = "KYC Not Found";

	/** The maker fee. */

	// CONSTANTS FOR API RESPONSE

	/** The result. */
	protected String TRADE_FEE_LESS_THAN_BLOCKED_BALANCE = "Trade Fees Should Be Less Than Block Balance";

	/** The status. */
	protected String AMOUNT_SHOULD_LESS_THAN_WALLET_BALANCE = "Amount Should Be Less Than Wallet Balance";

	/** The status code. */
	protected String STATUS_CODE = "statusCode";

	/** The message. */
	protected String MESSAGE = "message";

	/** The success. */
	protected String SUCCESS = "success";

	/** The failure. */
	String FAILURE = "failure";
	protected String TRADING_DATA_GET_SUCCESSFULLY = "TRADING_DATA_GET_SUCCESSFULLY";
	public static final int TRADE_DATA_NOT_FOUND_CODE = 780;
	protected String TRADE_DATA_NOT_FOUND = "trade data not found";

	/** The total fee. */
	protected String WALLET_BALANCE_SHOULD_PRESENT = "Wallet Ballance Should Be Present";

	// SUCCESS RESPONSES
	/** The insertion successful. */
	protected String ADDVERTISEMENT_PLACED_SUCCESSFULLY = "Advertisement Placed Successfully";

	protected String BITCOIN_RELEASE_SUCCESSFULLY = "Bitcoin Release SuccesFully";
	protected String BITCOINS_NOT_FOUND = "BitCoins Not Found";

	/** The withdraw successful. */
	protected String ENTER_USERNAME = "Please Enter Your Username";

	// CRYPTO COIN API POST METHOD REQUESTS
	/** The sendto. */
	protected String AMOUNT_SHOULD_IN_RANGE = "Amount Should Be In range";
	protected String SMS_AUTHENTICATION_WILL_BE_REQUIRED = "SMS Authentication will be required";
	/** The sendfrom. */
	String SENDFROM = "SendFrom";
	protected String PLEASE_VERIFY_SMS_AUTHENTICATION = "Please verify your SMS Authentication";

	/** The changeaddress. */
	protected String KYC_REQUIRED = "KYC should be required";

	/** The amounttotransfer. */
	protected String KYC_SHOULD_ACCEPTED = "Your KYC Should be Accepted";

	/** The sent amount. */
	String KYC_NOT_BE_NULL = "Your KYC Status should not be null";

	/** The fee. */
	protected String INSUFFICIENT_WALLET_BALLANCE = "InSufficient Wallet Balance";

	protected String NOT_APPLICABLE_FOR_TRADE_REQUEST = "You Are Not Applicable To Send Trade Request on This Advertisement";

	String CODE = "code";

	/** The coin list fetched successfully. */
	protected String FOR_MARKET_VALUE_URL_NOT_FOUND = "For Market Value Url Is Not Working";

	/** The coin details fetched successfully. */
	protected String ADDVERTISEMENT_DATA_GET_SUCCESSFULLY = "Advertisement Data Get SuccessFully";

	/** The withdraw fee update successfully. */
	protected String DATA_NOT_FOUND = "Data Not Found";

	/** The withdraw fee updation failed. */
	protected String ADDVERTISEMENT_UPDATE_SUCCESSFULLY = " AddVertisement update SUCCESSFully";

	// EXCEPTION CONSTANTS
	/** The no wallet found for coin. */
	protected String CONVERTED_DATA_GET_SUCCESSFULLY = "CONVERTED DATA GET SUCCESSFULLY";

	protected String WALLET_NOT_FOUND = "Wallet Not Found";

	/** The and userid. */
	String AND_USERID = " and userId: ";

	/** The bad request exception. */
	String BAD_REQUEST_EXCEPTION = "Bad Request Exception";

	// ETHEREUM LIVE API REQUESTS & RESPONSE
	/** The eth result. */
	String ADVERTISEMENT_NOT_FOUND = "Advertisement data not found";

	/** The from eth address. */
	protected String SEND_TRADE_REQUEST_ALREADY = "Send Trade Request Already Done";

	/** The to eth address. */

	/** The private key. */
	protected String TRADE_NOT_FOUND = "Trade data not found";

	/** The eth txn hash. */
	protected String ADVERTISEMENT_STATUS_GET_SUCCESS = "Advertisement Status get SuccessFully";

	/** The eth value. */
	protected String ADVERTISEMENT_LIST_NOT_FOUND = "Advertisement List Not Found";

	// XRP LIVE API REQUESTS & RESPONSE
	/** The secret. */
	protected String ALREADY_DISPUTED = "You Have Allready Press Dispute Button";

	/** The xrp balance. */
	protected String TRADE_DISPUTED_SUCCESSFULLY = "Trade Is Disputed SuccessFully";

	/** The xrp source address. */
	protected String TRADE_SHOULD_PAID_STATUS = "Trade Should Be Paid Status";

	/** The xrp destination address. */
	protected String XRP_DESTINATION_ADDRESS = "destAddr";

	/** The xrp to tag id. */
	String XRP_TO_TAG_ID = "toTag";

	/** The amount. */
	String AMOUNT = "amount";

	/** The xrp deposit address. */
	String XRP_DEPOSIT_ADDRESS = "addr";

	/** The xrp deposit tag id. */
	String XRP_DEPOSIT_TAG_ID = "destTags";

	/** The xrp response code. */
	String XRP_RESPONSE_CODE = "responseCode";

	String XRP_TXN_HASH = "transaction_hash";

	/** The xrp data. */
	String XRP_DATA = "data";

	// ERC-20 TOKENS REQUESTS AND RESPONSES
	/** The hash. */
	String HASH = "hash";
	protected String AMOUNT_ADDED_SUCCESSFULLY = " Amount Added SuccesFully";
	protected String AMOUNT_UPDATED_SUCCESSFULLY = " Amount Updated SuccesFully";
	protected String CANCEL_TRADE = "Cancelled trade request successfully.";
	protected String CHECKYOUR_PEER_TO_PEER_EXCHANGE_ID = " Check Your peerToPeerExchangeId";

	/** The from. */
	protected String RELEASED_COINS_ALLREADY_COMPLETED = "Release Coins Already Completed";

	/** The value. */
	protected String TRADE_SHOULD_DISPUTE_STATUS = "Trade Should Be In Dispute Status";

	/** The erc 20 from address. */
	String ERC_20_FROM_ADDRESS = "fromAddress";

	/** The erc 20 deposits. */
	String ERC_20_DEPOSITS = "deposits";

	String APPROVE = "APPROVE";

	// NEO API CONSTANTS
	String NEO_RESPONSE_CODE = "response_code";

	String FROM_ADDRESS = "from_address";
	/** The Constant SUCCESS_CODE. */
	public static final int SUCCESS_CODE = 200;

	/** The Constant FAILURE_CODE. */
	public static final int FAILURE_CODE = 205;
	public static final String AMOUNT_PAID_SUCCESSFULLY = "Amount Paid By Buyer SuccessFully";

	/** The Constant BAD_REQUEST_CODE. */
	public static final int BAD_REQUEST_CODE = 400;

	public static final int NOTNULL = 201;
	public static final int ROLE_NOT_FOUND = 802;
	public static final int MARGIN_NOT_FOUND = 201;
	public static final int MIN_VALUE_NOT_FOUND = 201;

	public static final int TERMS_OF_TRADE_NOT_FOUND = 201;

	public static final int MAX_VALUE_NOT_FOUND = 801;
	public static final int URL_NOT_FOUND = 204;
	public static final int AMOUNT_DEDUCTION_CODE = 805;
	public static final int CHECK_KYC_STATUS_CODE = 806;
	public static final int COINS_NOT_FOUND_CODE = 208;
	public static final int SOMETHING_WRONG_CODE = 420;

	public static final int MISMATCHED_CODE = 201;
	public static final int WALLET_NOT_FOUND_CODE = 804;
	public static final int DATA_NOT_FOUND_CODE = 203;
	public static final int P2PID_NOT_FOUND_CODE = 301;

	public static final int INSUFFICIENT_BALLANCE_CODE = 206;
	protected String ENTER_THE_MAX_VALUE = "Enter the max value";
	protected String PLEASE_ENTER_ANY_ROLE = "Please Enter any role";
	protected String ENTER_THE_MARGIN = "Enter the margin";
	protected String ENTER_THE_MIN_VALUE = "Enter the min value";
	protected String ENTER_TERMS_OF_TRADE = "Enter the term of trade";
	protected String MAX_SHOULD_BE_GRATTER_THAN_MIN_VALUE = "Minimum transaction value should be less than maximum transaction value";
	protected String SOMETHING_WENT_WRONG = "SomeThing Went Wrong";

	protected String AMOUNT_DEDUCTED_SUCCESSFULLY = "Amount Deducted Successfully";
	protected String CHECK_YOUR_KYC_STATUS = "Check Your KYC Status";
	protected String RELEASED_COIN_ALLREADY_COMPLETED="RELEASED_COIN_ALLREADY_COMPLETED";

	public Response<Object> getBitCoinInUSD(BigDecimal amount, Long peerToPeerExchangeId, BigDecimal no) {
		// TODO Auto-generated method stub
		return null;
	}

}
