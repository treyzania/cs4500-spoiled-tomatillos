package edu.northeastern.cs4500;

import org.junit.Assert;
import org.junit.Test;

import edu.northeastern.cs4500.data.AuthKey;
import edu.northeastern.cs4500.util.Salting;

public class SaltingTest {

	@Test
	public void testSalterPurity() {

		String salt = Salting.createNewSalt();
		String hash = Salting.computeHashWithSalt("hello", salt);
		Assert.assertEquals(hash, Salting.computeHashWithSalt("hello", salt));

	}

	@Test
	public void testAuthKeyCheck() {

		String password = "password123456";

		AuthKey k = new AuthKey(null, password);
		Assert.assertTrue(k.isMatched(password));

	}

	@Test
	public void testAuthKeyCheckFail() {

		String password = "correct horse battery staple";
		String wrong = "false llama lightbulb paperclip";

		AuthKey k = new AuthKey(null, password);
		Assert.assertFalse(k.isMatched(wrong));

	}

}
