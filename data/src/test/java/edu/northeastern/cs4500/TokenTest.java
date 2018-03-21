package edu.northeastern.cs4500;

import org.junit.Assert;
import org.junit.Test;

import edu.northeastern.cs4500.util.Tokens;

public class TokenTest {

	@Test
	public void testTokenForm() {

		String token = Tokens.createNewToken();

		System.out.println("test token: " + token);

		Assert.assertEquals(Tokens.TOKEN_LENGTH, token.length());
		for (char c : token.toCharArray()) {
			Assert.assertTrue(Tokens.TOKEN_DICT.contains(Character.toString(c)));
		}

	}

	@Test
	public void testTokenUniqueness() {

		String t1 = Tokens.createNewToken();
		String t2 = Tokens.createNewToken();

		Assert.assertNotEquals(t1, t2);

	}

}
