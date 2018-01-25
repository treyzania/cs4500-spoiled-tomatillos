package edu.northeastern.cs4500.data;

public class MessageObject {

	private String message;
	
	public MessageObject(String msg) {
		this.message = msg;
	}
	
	public MessageObject() {
		this(null);
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
