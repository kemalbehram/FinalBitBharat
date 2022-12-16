package com.mobiloitte.microservice.wallet.constants;

/**
 * The Interface WalletConstants.
 * 
 * @author Ankush Mohapatra
 */
public interface WalletConstants {

	// Coins Constants
	/** The btc. */
	String BTC = "BTC";

	/** The bch. */
	String BCH = "BCH";

	/** The eth. */
	String ETH = "ETH";

	/** The ltc. */
	String LTC = "LTC";

	/** The xlm. */
	String XLM = "XLM";

	/** The xrp. */
	String XRP = "XRP";

	String XINDIA = "XINDIA";

	/** The dash. */
	String DASH = "DASH";

	/** The miota. */
	String MIOTA = "IOTA";

	/** The usdt. */
	String USDT = "USDT";
	String INR = "INR";

	/** The neo. */
	String NEO = "NEO";

	/** The kto. */
	String KTO = "KTO";

	/** The usd. */
	String USD = "USD";

	String USDC = "USDC";

	String OMG = "OMG";

	String BNB = "BNB";

	String POLKADOT = "DOT";

	String MATIC = "MATIC";

	String AVAX = "AVAX";

	String SOLANA = "SOL";
	

	String REQUEST_CONVERT = "convert";
	String percent_change_24h = "percent_change_24h";
	String percent_change_1h = "percent_change_1h";
	String percent_change_7d = "percent_change_7d";

	/** The eos. */
	String EOS = "EOS";

	String TRX = "TRX";

	/** The btc coin. */
	// URL CONSTANTS
	String BTC_LOWER_CASE = "btc";

	/** The eth coin. */
	String ETH_LOWER_CASE = "eth";

	String AVAX_LOWER_CASE = "avax";

	String MATIC_LOWER_CASE = "matic";

	String POLKADOT_LOWER_CASE = "polkadot";

	/** The bch lower case. */
	String BCH_LOWER_CASE = "bch";

	String SOLANA_LOWER_CASE = "solana";

	/** The ltc coin. */
	String LTC_LOWER_CASE = "ltc";

	String EOS_LOWER_CASE = "eos";

	String USDT_LOWER_CASE = "usdt";

	String TRX_LOWER_CASE = "trx";

	String MIOTA_LOWER_CASE = "miota";

	String USDC_LOWER_CASE = "usdc";

	String OMG_LOWER_CASE = "omg";

	String DASH_LOWER_CASE = "dash";

	String BNB_LOWER_CASE = "bnb";
	// API response constants

	/** The success code. */
	int SUCCESS_CODE = 200;

	/** The withdraw failure code. */
	int WITHDRAW_FAILURE_CODE = 202;

	/** The failure code. */
	int FAILURE_CODE = 205;

	/** The not found code. */
	int NOT_FOUND_CODE = 404;

	/** The bad request code. */
	int BAD_REQUEST_CODE = 400;

	/** The server error code. */
	int SERVER_ERROR_CODE = 500;

	/** The deposit success. */
	String DEPOSIT_SUCCESS = "Deposit Successful";

	/** The deposit failed. */
	String DEPOSIT_FAILED = "Deposit Failed";

	/** The no new deposits found. */
	String NO_NEW_DEPOSITS_FOUND = "No new deposits found";

	/** The withdraw list fetched. */
	String WITHDRAW_LIST_FETCHED = "Withdraw list fetched successfully";

	/** The withdraw success. */
	String WITHDRAW_SUCCESS = "Withdraw Successful";

	/** The withdraw request posted successfully. */
	String WITHDRAW_REQUEST_POSTED_SUCCESSFULLY = "Withdraw request posted successfully. Kindly check your email";

	/** The withdraw failed. */
	String WITHDRAW_FAILED = "Withdraw Failed";

	/** The no withdraw found. */
	String NO_WITHDRAW_FOUND = "No withdraw found";

	/** The no storage details found. */
	String NO_STORAGE_DETAILS_FOUND = "No storage found, ask admin to update the storage details";

	/** The insufficient storage balance. */
	String INSUFFICIENT_STORAGE_BALANCE = "Insufficient storage balance, ask admin to update the storage balance";

	/** The insufficient wallet balance. */
	String INSUFFICIENT_WALLET_BALANCE = "Insufficient wallet balance, please update your balance";

	/** The no such coin found. */
	String NO_SUCH_COIN_FOUND = "No such coin found";

	/** The no data found. */
	String NO_DATA_FOUND = "No data found";

	/** The wallet not found. */
	String WALLET_NOT_FOUND = "Wallet not found";

	/** The user not found. */
	String USER_NOT_FOUND = "User not found";

	/** The balance not found. */
	String BALANCE_NOT_FOUND = "Balance not found";

	/** The coin not found. */
	String COIN_NOT_FOUND = "Coin not found";

	/** The transaction list not found. */
	String TRANSACTION_LIST_NOT_FOUND = "Transaction list not found";

	/** The insertion failed. */
	String INSERTION_FAILED = "insertion failed";

	/** The failed to fetch address. */
	String FAILED_TO_FETCH_ADDRESS = "Failed to fetch address from Live Blockchain API";

	/** The bad request. */
	String BAD_REQUEST = "Bad Request";

	/** The server error. */
	String SERVER_ERROR = "server error";

	/** The wallet details found successfully. */
	String WALLET_DETAILS_FOUND_SUCCESSFULLY = "wallet details found successfully";

	/** The balance updated successfully. */
	String BALANCE_UPDATED_SUCCESSFULLY = "balance updated successfully";

	/** The balance updation failed. */
	String BALANCE_UPDATION_FAILED = "balance updation failed";

	/** The updation failed. */
	String UPDATION_FAILED = "updation failed";

	/** The insufficient block balance. */
	String INSUFFICIENT_BLOCK_BALANCE = "Insufficient block balance";

	/** The wallet history updated successfully. */
	String WALLET_HISTORY_UPDATED_SUCCESSFULLY = "wallet history updated successfully";

	/** The wallet history updation failed. */
	String WALLET_HISTORY_UPDATION_FAILED = "wallet history updation failed";

	/** The balance transferred successfully. */
	String BALANCE_TRANSFERRED_SUCCESSFULLY = "balance transferred successfully from one user to other";

	/** The balance transfer failed. */
	String BALANCE_TRANSFER_FAILED = "balance transfer failed from one user to other";

	/** The storage details fetched successfully. */
	String STORAGE_DETAILS_FETCHED_SUCCESSFULLY = "storage details fetched successfully";

	/** The storage address generated for coin. */
	String STORAGE_ADDRESS_GENERATED_FOR_COIN = "storage address generated for coin: ";

	/** The no such storage found please create storage first. */
	String NO_SUCH_STORAGE_FOUND_PLEASE_CREATE_STORAGE_FIRST = "no such storage found, please create its storage first";

	/** The storage address is already present. */
	String STORAGE_ADDRESS_IS_ALREADY_PRESENT = "storage address is already present";

	/** The storage wallet created successfully. */
	String STORAGE_WALLET_CREATED_SUCCESSFULLY = "storage wallet created successfully for coin: ";

	/** The storage wallet is already present. */
	String STORAGE_WALLET_IS_ALREADY_PRESENT = "storage wallet is already present";

	/** The failed to fetch balance. */
	String FAILED_TO_FETCH_BALANCE = "failed to fetch balance";

	/** The no such storage wallet found or storage address found. */
	String NO_SUCH_STORAGE_WALLET_FOUND_OR_STORAGE_ADDRESS_FOUND = "no such storage found, or no storage address found for coin: ";

	/** The no storage address found. */
	String NO_STORAGE_ADDRESS_FOUND = "No storage address or wallet found for coin: %s, Please generate it first !!!";

	/** The cold storage address updated successfully. */
	String COLD_STORAGE_ADDRESS_UPDATED_SUCCESSFULLY = "Cold storage address for coin: %s is updated successfully";

	/** The user txn history fetched succuessfully. */
	String USER_TXN_HISTORY_FETCHED_SUCCUESSFULLY = "user transaction history fetched succuessfully";

	/** The txn details fetched for a transaction. */
	String TXN_DETAILS_FETCHED_FOR_A_TRANSACTION = "transaction details for a transaction fetched successfully";

	/** The coinpair already exist. */
	String COINPAIR_ALREADY_EXIST = "Coin Pair already exist";

	/** The coinpair added successfully. */
	String COINPAIR_ADDED_SUCCESSFULLY = "Coin Pair added successfully.";

	/** The minumum withdraw amount update successfully. */
	String MINUMUM_WITHDRAW_AMOUNT_UPDATED_SUCCESSFULLY = "minimum withdraw amount updated successfully";

	/** The taker and maker fee update successfully. */
	String TAKER_AND_MAKER_FEE_UPDATED_SUCCESSFULLY = "taker and maker fee updated successfully";

	/** The deposit fee updated successfully. */
	String DEPOSIT_FEE_UPDATED_SUCCESSFULLY = "Deposit fee updated successfully";

	/** The all coinpair fetched successfully. */
	String ALL_COINPAIR_FETCHED_SUCCESSFULLY = "All coin Pair fetched successfully.";

	/** The fees profit calculated successfully. */
	String FEES_PROFIT_CALUCALTED_SUCCESSFULLY = "fees profit calculated successfully";

	/** The withdrawal amount is more than minumum withdrawal limit. */
	String WITHDRAWAL_AMOUNT_IS_MORE_THAN_MINUMUM_WITHDRAWAL_LIMIT = "withdrawal amount is more than minumum withdrawal limit";

	/** The user balance and coinlist fetched successfully. */
	String USER_BALANCE_AND_COINLIST_FETCHED_SUCCESSFULLY = "All user balance and coinlist fetched successfully";

	/** The live market data fetched successfully. */
	String LIVE_MARKET_DATA_FETCHED_SUCCESSFULLY = "live market data fetched successfully";

	/** The iswithdraw cannot be true for first time. */
	String ISWITHDRAW_CANNOT_BE_TRUE_FOR_FIRST_TIME = "isWithdraw cannot be true for the first time";

	/** The withdraw token expired. */
	String WITHDRAW_TOKEN_EXPIRED = "Cryptobiz: Withdrawal already done, link expired.";

	/** The withdraw request cancelled. */
	String WITHDRAW_REQUEST_CANCELLED = "withdraw request cancelled successfully";

	/** The all user address fetched successfully. */
	String ALL_USER_ADDRESS_FETCHED_SUCCESSFULLY = "all user address fetched successfully";

	String BUY_ORDER_PLACED_AND_EXECUTED_SUCCESSFULLY = "Buy order placed and executed successfully";

	String SELL_ORDER_PLACED_AND_EXECUTED_SUCCESSFULLY = "Sell order placed and executed successfully";

	String ORDER_PLACE_FAILED = "Order placed failed";

	String MARKET_PRICE_UPDATED_SUCCESSFULLY = "Market price updated successfully for coin :";

	/** The user order history fetched successfully. */
	String USER_ORDER_HISTORY_FETCHED_SUCCESSFULLY = "User order history fetched successfully";

	/** The no record found. */
	String NO_RECORDS_FOUND = "No records found";

	String FROM_USER_ID_WALLET_NOT_FOUND = "From user id wallet not found";

	String TO_USER_ID_WALLET_NOT_FOUND = "To user id wallet not found";

	String AMOUNT_TRANSFERED_SUCCESSFULLY = "Amount transfered Successfully";

	String AMOUNT_TRANSFERED_SUCCESSFULLY_TO_ADMIN = "request send to receiver end successfully";

	String REFERENCEID_DETAILS_FOUND = "Need to approvel of receiver";
}
