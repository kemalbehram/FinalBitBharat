package com.mobiloitte.microservice.wallet.tronutils;

public class TronHexConverter {
	
	private TronHexConverter() {}

	public static String convertBase58ToTronHex(String base58) {
	    byte[] decoded = decode58(base58);
	    return decoded.length <=0 ? "" : org.spongycastle.util.encoders.Hex.toHexString(decoded);
	}


	private static byte[] decode58(String input) {
	    byte[] decodeCheck = Base58.decode(input);
	    if (decodeCheck.length <= 4) {
	        return new byte[0];
	    }
	    byte[] decodeData = new byte[decodeCheck.length - 4];
	    System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
	    byte[] hash0 = Sha256Hash.hash(decodeData);
	    byte[] hash1 = Sha256Hash.hash(hash0);
	    if (hash1[0] == decodeCheck[decodeData.length] &&
	            hash1[1] == decodeCheck[decodeData.length + 1] &&
	            hash1[2] == decodeCheck[decodeData.length + 2] &&
	            hash1[3] == decodeCheck[decodeData.length + 3]) {
	        return decodeData;
	    }
	    return new byte[0];
	}

}
