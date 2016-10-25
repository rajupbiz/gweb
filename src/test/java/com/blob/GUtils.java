package com.blob;

import java.util.Random;

import org.junit.Test;

public class GUtils {

	@Test
	public void testGid() {
		// fail("Not yet implemented");

		System.out.println("  >> "+generateRandomString());
		System.out.println("  >> "+generateRandomString());
		System.out.println("  >> "+generateRandomString());
		System.out.println("  >> "+generateRandomString());
		
		
	}

	private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int RANDOM_STRING_LENGTH = 2;

	/**
	 * This method generates random string
	 */
	public String generateRandomString() {

		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	/**
	 * This method generates random numbers
	 */
	private int getRandomNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(CHAR_LIST.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

}
