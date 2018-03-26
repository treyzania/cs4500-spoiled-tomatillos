package edu.northeastern.cs4500.util;

import java.security.SecureRandom;

public class Tokens {

	private Tokens() {

	}

	public static final String TOKEN_DICT = "abcdefghijklmnopqrstuvwxyz0123456789";
	public static final int TOKEN_LENGTH = 50;

	public static final String createNewToken() {

		StringBuilder sb = new StringBuilder();
		SecureRandom sr = new SecureRandom();
		for (int i = 0; i < TOKEN_LENGTH; i++) {
			sb.append(Character.toString(TOKEN_DICT.charAt(sr.nextInt(TOKEN_DICT.length()))));
		}

		return sb.toString();

	}

}
