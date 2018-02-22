package edu.northeastern.cs4500.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Clock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.northeastern.cs4500.util.Tokens;

@Entity(name = "sessions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Session implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1656442524575397898L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private User user;

	private Timestamp loginTime;
	private String token;
	private boolean active;
	
	public Session() {
		this.loginTime = Timestamp.from(Clock.systemUTC().instant());
		this.token = Tokens.createNewToken();
		this.active = true;
	}
	
	public Session(User u) {
		this();
		this.user = u;
	}
	
	public Integer getId() {
		return this.id;
	}

	public User getUser() {
		return this.user;
	}
	
	public String getUsername() {
		return this.user.getUsername();
	}
	
	public Timestamp getLoginTime() {
		return this.loginTime;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
	public void logout() {
		this.active = false;
	}
	
}
