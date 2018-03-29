package edu.northeastern.cs4500.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
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

	@ElementCollection
	@CollectionTable(name = "user_caps")
	private Set<UserCapability> capabilities;
	
	public User() {

	}

	public User(int id) {
		this.id = id;
	}
	
	public User(String username) {
		this.username = username;
		this.capabilities = new HashSet<>();
	}

	public Integer getId() {
		return this.id;
	}

	public String getUsername() {
		return this.username;
	}
	
	public void addCapability(String cap) {
		if (!this.hasCapability(cap)) {
			this.capabilities.add(new UserCapability(cap));
		}
	}

	public boolean removeCapability(String cap) {
		return this.capabilities.removeIf(c -> c.getName().equals(cap));
	}
	
	public boolean hasCapability(String cap) {
		return this.capabilities.stream().anyMatch(c -> c.getName().equals(cap));
	}
	
}
