package edu.northeastern.cs4500.util;

import edu.northeastern.cs4500.data.Title;
import edu.northeastern.cs4500.data.User;

public class NotificationBuilder {

	private StringBuilder data = new StringBuilder();
	
	public NotificationBuilder() {
		
	}
	
	public NotificationBuilder append(String str) {
		this.data.append(str);
		return this;
	}
	
	private void appendNamespacedId(String ns, int id) {
		this.data.append(String.format("{{%s:%s}}", ns, id));
	}
	
	public NotificationBuilder appendTitle(Title t) {
		this.appendNamespacedId("titleid", t.getId());
		return this;
	}
	
	public NotificationBuilder appendUser(User u) {
		this.appendNamespacedId("userid", u.getId());
		return this;
	}
	
	public NotificationBuilder appendUrl(String url) {
		this.append(String.format("{{url:%s}}", url));
		return this;
	}
	
	public String build() {
		return this.data.toString();
	}
	
}
