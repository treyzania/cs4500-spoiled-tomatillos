package edu.northeastern.cs4500.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "titles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Title implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2554050879868131104L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;
	private Integer releaseYear;

	private String summary;

	public Title() {

	}

	public Title(String name, int year) {
		this.name = name;
		this.releaseYear = Integer.valueOf(year);
	}

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getReleaseYear() {
		return this.releaseYear;
	}

	public void setReleaseYear(Integer year) {
		this.releaseYear = year;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
