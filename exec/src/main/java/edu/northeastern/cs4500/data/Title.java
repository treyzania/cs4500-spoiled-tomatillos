package edu.northeastern.cs4500.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "titles")
public class Title implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	private int releaseYear;
	
	private String summary;
	
	public Title() {
		
	}
	
	public Title(String name, int year) {
		this.name = name;
		this.releaseYear = year;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getReleaseYear() {
		return this.releaseYear;
	}
	
	public void setReleaseYear(int year) {
		this.releaseYear = year;
	}
	
	public String getSummary() {
		return this.summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
}
