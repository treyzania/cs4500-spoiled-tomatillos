package edu.northeastern.cs4500.data;

import javax.persistence.Embeddable;

@Embeddable
public class UserCapability {

	private String name;

	public UserCapability() {
		
	}
	
	public UserCapability(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}
