package edu.northeastern.cs4500;

import org.junit.Assert;
import org.junit.Test;

import edu.northeastern.cs4500.data.Title;
import edu.northeastern.cs4500.data.User;
import edu.northeastern.cs4500.util.NotificationBuilder;

public class NotificationBuilderTest {

	@Test
	public void testSimpleStr() {
		NotificationBuilder nb = new NotificationBuilder();
		nb.append("hello");
		nb.append("world");
		Assert.assertEquals("helloworld", nb.build());
	}
	
	@Test
	public void testUrl() {
		NotificationBuilder nb = new NotificationBuilder();
		nb
			.append("foo")
			.appendUrl("http://google.com/")
			.append("bar");
		Assert.assertEquals("foo{{url:http://google.com/}}bar", nb.build());
	}
	
	@Test
	public void testTitle() {
		NotificationBuilder nb = new NotificationBuilder();
		nb.appendTitle(new Title(42));
		Assert.assertEquals("{{titleid:42}}", nb.build());
	}
	
	@Test
	public void testUser() {
		NotificationBuilder nb = new NotificationBuilder();
		nb.appendUser(new User(1337));
		Assert.assertEquals("{{userid:1337}}", nb.build());
	}
	
	@Test
	public void testMulti() {
		NotificationBuilder nb = new NotificationBuilder();
		nb
			.append("a")
			.appendTitle(new Title(1))
			.append("b")
			.appendUser(new User(2))
			.append("c")
			.appendUrl("http://pie.gd/")
			.append("d");
		Assert.assertEquals("a{{titleid:1}}b{{userid:2}}c{{url:http://pie.gd/}}d", nb.build());
	}
	
}
