package com.mobiloitte.ohlc.constants;

public interface OhlcConstants {

	//API response constants
	
		/** The success code. */
		int SUCCESS_CODE=200;
		
		/** The failure code. */
		int FAILURE_CODE=205;
		
		/** The not found code. */
		int NOT_FOUND_CODE=404;
		
		/** The bad request code. */
		int BAD_REQUEST_CODE=400;
		
		/** The server error code. */
		int SERVER_ERROR_CODE=500;
		
		/** The server error code. */
		int NO_DATA_FOUND_CODE=201;
		
	
	//API string response constants
		
		/** The success. */
		String SUCCESS="success";
		
		/** The failure. */
		String FAILURE="failure";

		/** The no data found. */
		String NO_DATA_FOUND="no data found";
		
		/** The no data found. */
		String COINPAIR_LIST_NOT_FOUND="coinpair list not found.";
}
