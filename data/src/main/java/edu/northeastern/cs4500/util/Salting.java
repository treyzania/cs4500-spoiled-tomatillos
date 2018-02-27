package edu.northeastern.cs4500.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Salting {
	
	public static final String HASH_ALGO = "SHA-256"; 
	private static final int SALT_LEN = 64; // in bytes
	private static final int HASH_ROUNDS = 128; // Never change this or we'll break all the passwords.
	
	private Salting() {
		
	}
	
	public static String computeHashWithSalt(String pw, String salt) {
		
		byte[] pwBytes = pw.getBytes();
		byte[] saltBytes = Base64.getDecoder().decode(salt);
		
		MessageDigest digest = getSalterDigest();
		if (digest == null) {
			throw new IllegalStateException("This should never happen.");
		}
		
		// Preseed the last buffer.
		digest.update(pwBytes);
		digest.update(saltBytes);
		byte[] last = digest.digest();
		
		// Actually do the heavier rounds.
		for (int i = 0; i < HASH_ROUNDS; i++) {
			digest.reset();
			digest.update(last);
			digest.update(saltBytes);
			digest.digest(last); // Doesn't allocate new bytes.
		}
		
		return Base64.getEncoder().encodeToString(last);
		
	}
	
	private static MessageDigest getSalterDigest() {
		try {
			return MessageDigest.getInstance(HASH_ALGO);
		} catch (NoSuchAlgorithmException e) {
			return null; // We're gonna shit the bed anyways.
		}
	}
	
	public static String createNewSalt() {
		
		SecureRandom sr = new SecureRandom();
		byte[] buf = new byte[SALT_LEN];
		sr.nextBytes(buf);
		return Base64.getEncoder().encodeToString(buf);
		
	}
	
}
