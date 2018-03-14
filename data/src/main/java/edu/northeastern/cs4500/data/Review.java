package edu.northeastern.cs4500.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Clock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity(name = "reviews")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Review implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4625672388090748322L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private Title title;
	
	@ManyToOne
	@JsonIdentityReference(alwaysAsId = true)
	private User user;
	
	private Timestamp submitted;
	
	private String description;
	
	public Review() {
		
	}
	
	public Review(Title title, User user, String desc) {
		this.title = title;
		this.user = user;
		this.submitted = Timestamp.from(Clock.systemUTC().instant());
		this.description = desc;
	}
	
	public Title getTitle() {
		return this.title;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public Timestamp getTimestamp() {
		return this.submitted;
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public String getDescription() {
		return this.description;
	}
	
}
