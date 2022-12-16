package com.mobiloitte.usermanagement.util;

import org.apache.commons.lang.RandomStringUtils;

public class randomId {

	public String key() {
		String generatedAlphaNumeric = RandomStringUtils.randomAlphanumeric(8);
		System.out.print(generatedAlphaNumeric);
		return generatedAlphaNumeric;
	}
}
