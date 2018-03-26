package edu.northeastern.cs4500.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity(name = "ratings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TitleRating implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -399898883459221063L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private Title title;

	@ManyToOne
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private User user;

	private Integer rating = 0;

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

	public Integer getRating() {
		return this.rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

}
