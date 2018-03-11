package edu.northeastern.cs4500;

import org.junit.Assert;
import org.junit.Test;

import edu.northeastern.cs4500.data.Session;

public class SessionTests {

	@Test
	public void testSessionTokenUniqueness() {

		Session a = new Session();
		Session b = new Session();

		Assert.assertNotEquals(a.getToken(), b.getToken());

	}

}
