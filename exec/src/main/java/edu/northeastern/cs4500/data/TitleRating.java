package edu.northeastern.cs4500.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "ratings")
public class TitleRating implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Title title;
	
	@ManyToOne
	private User user;
	
	private int rating = 0;
	
	public TitleRating() {
		
	}
	
	public TitleRating(Title title, User user, int rating) {
		this.title = title;
		this.user = user;
		this.rating = rating;
	}
	
	public Title getTitle() {
		return this.title;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public int getRating() {
		return this.rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
