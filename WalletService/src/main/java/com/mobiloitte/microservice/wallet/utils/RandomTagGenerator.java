package com.mobiloitte.microservice.wallet.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Random Tag Generator
 * @author Ankush Mohapatra
 */
public class RandomTagGenerator {
	
	/**
	 * Instantiates a new random tag generator.
	 */
	protected RandomTagGenerator() {
	}
	/**
	 * Generate random number.
	 *
	 * @return the string
	 */
	public static String generateRandomNumber() {
		Integer random = new Random().nextInt(9999);
		if (random < 1000) {
			random = 1000 + random;
		}
		return random.toString();
	}
	
	/**
	 * Generate tag.
	 *
	 * @param id the id
	 * @return the string
	 */
	public static String generateTag()
	{
		int getRandInteger = ThreadLocalRandom.current().nextInt();
		if(String.valueOf(getRandInteger).contains("-"))
		{
			return String.valueOf(getRandInteger + (getRandInteger * -2));
		}
		else
			return String.valueOf(getRandInteger);
	}
	
	public static String generateRandEosAcc()
	{
		int length = 12;
		String chars = "abcdefghijklmnopqrstuvwxyz"
		             + "12345";
		return new Random().ints(length, 0, chars.length())
		                         .mapToObj(i -> "" + chars.charAt(i))
		                         .collect(Collectors.joining());
	}
	
	/**
	 * Generate random token.
	 *
	 * @return the string
	 */
	public static String generateRandomToken()
	{
		return RandomStringUtils.randomAlphanumeric(5);
	}

}
