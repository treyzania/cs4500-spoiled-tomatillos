package edu.northeastern.cs4500.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs4500.data.MessageObject;

@RestController
public class HelloController {

	@RequestMapping("/api/hello/string")
	public String sayHello() {
		return "Hello, World!";
	}
	
	@RequestMapping("/api/hello/object")
	public MessageObject sayHello_obj() {
		return new MessageObject("Hello, World!");
	}
	
}
