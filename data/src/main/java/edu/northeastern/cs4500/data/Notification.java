package edu.northeastern.cs4500.data;

import java.sql.Timestamp;
import java.time.Clock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "notification")
public class Notification {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private User sender;

	@ManyToOne
	private User target;

	private Timestamp published;

	private String type;
	private String contents;

	private boolean dismissed;

	public Notification() {

	}

	public Notification(User sender, User target, String t, String c) {
		this.published = Timestamp.from(Clock.systemUTC().instant());
		this.sender = sender;
		this.target = target;
		this.type = t;
		this.contents = c;
	}

	public Timestamp getPublishedTime() {
		return this.published;
	}

	public User getTarget() {
		return this.target;
	}

	public String getType() {
		return this.type;
	}

	public String getContents() {
		return this.contents;
	}

	public boolean isDismissed() {
		return this.dismissed;
	}

	public void dismiss() {
		this.dismissed = true;
	}

}
