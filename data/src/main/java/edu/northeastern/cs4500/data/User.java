package edu.northeastern.cs4500.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4257918875857087299L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String username;
	
	public User() {
		
	}
	
	public User(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	
}
