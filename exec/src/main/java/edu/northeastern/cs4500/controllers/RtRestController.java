package edu.northeastern.cs4500.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs4500.data.Title;
import edu.northeastern.cs4500.data.TitleRating;
import edu.northeastern.cs4500.data.TitleRatingRepository;
import edu.northeastern.cs4500.data.TitleRepository;
import edu.northeastern.cs4500.data.User;
import edu.northeastern.cs4500.data.UserRepository;

@RestController
public class RtRestController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TitleRepository titleRepo;

	@Autowired
	private TitleRatingRepository ratingRepo;
	
	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable int id) {
		return this.userRepo.getOne(Integer.valueOf(id));
	}

	@RequestMapping(value = "/api/user/create", method = RequestMethod.POST, params = "username")
	public User createUser(@RequestParam("username") String username) {
		User u = new User(username);
		this.userRepo.saveAndFlush(u);
		return u;
	}

	@RequestMapping(value = "/api/title/{id}", method = RequestMethod.GET)
	public Title getTitle(@PathVariable int id) {
		return this.titleRepo.getOne(Integer.valueOf(id));
	}

	@RequestMapping(value = "/api/title/create", method = RequestMethod.POST, params = {"name", "year", "desc"})
	public Title createTitle(
			@RequestParam("name") String name,
			@RequestParam("year") int year,
			@RequestParam("desc") String description) {
		
		Title t = new Title(name, year);
		t.setSummary(description);
		
		this.titleRepo.saveAndFlush(t);
		return t;
		
	}

}
