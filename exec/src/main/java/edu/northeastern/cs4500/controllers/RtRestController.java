package edu.northeastern.cs4500.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs4500.data.Title;
import edu.northeastern.cs4500.data.User;

@RestController
public class RtRestController {

	@RequestMapping(value = "/api/user/select/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable int id) {
		return null;
	}

	@RequestMapping(value = "/api/user/create", method = RequestMethod.POST)
	public User createUser(@PathVariable String username) {
		return null;
	}

	@RequestMapping(value = "/api/title/select/{id}", method = RequestMethod.GET)
	public Title getTitle(@PathVariable int id) {
		return null;
	}

	@RequestMapping(value = "/api/title/create", method = RequestMethod.POST, params = {"name", "year", "desc"})
	public Title createTitle(
			@RequestParam("name") String name,
			@RequestParam("year") int year,
			@RequestParam("desc") String description) {
		return null;
	}

}
