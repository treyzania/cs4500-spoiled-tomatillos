package edu.northeastern.cs4500.data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "auth")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FriendRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private FriendState state = FriendState.PROPOSED;
	
	@ManyToOne
	private User sender;
	
	@ManyToOne
	private User reciever;
	
	public FriendRequest() {
		
	}
	
	public FriendRequest(User sender, User recv) {
		this.sender = sender;
		this.reciever = recv;
	}
	
	public FriendState getState() {
		return this.state;
	}
	
	public void setState(FriendState state) {
		this.state = state;
	}
	
	public User getSender() {
		return this.sender;
	}
	
	public User getReciever() {
		return this.reciever;
	}
	
}
