package edu.northeastern.cs4500.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs4500.data.AuthKey;
import edu.northeastern.cs4500.data.AuthKeyRepository;
import edu.northeastern.cs4500.data.Session;
import edu.northeastern.cs4500.data.SessionRepository;
import edu.northeastern.cs4500.data.Title;
import edu.northeastern.cs4500.data.TitleRatingRepository;
import edu.northeastern.cs4500.data.TitleRepository;
import edu.northeastern.cs4500.data.User;
import edu.northeastern.cs4500.data.UserRepository;

@SuppressWarnings("unused")
@RestController
public class RtRestController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AuthKeyRepository authRepo;

	@Autowired
	private SessionRepository sessionRepo;

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

	@RequestMapping(value = "/api/session/login", method = RequestMethod.POST, params = {"username", "password"})
	public Session login(@RequestParam("username") String username, @RequestParam("password") String password) {

		// First we look up the user and their last auth key stuff.
		User u = this.userRepo.findUserByUsername(username);
		AuthKey k = this.authRepo.findFirstByUserUsername(username);

		// Then a simple check to see if the password matches.
		if (k.isMatched(password)) {
			Session s = new Session(u);
			this.sessionRepo.saveAndFlush(s);
			return s;
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/api/search", method = RequestMethod.GET, params = {"query"})
	public List<Object> search(@RequestParam("query") String query) {
		
		// Use a set here to avoid duplicates.
		Set<Title> titles = new HashSet<>();
		for (String comp : query.split("\\+")) {
			titles.addAll(this.titleRepo.findByNameLikeIgnoreCase("%" + comp + "%"));
		}
		
		// Now convert it to a list, but in a flexible way.
		List<Object> out = new ArrayList<>();
		out.addAll(titles);
		return out;
		
	}

}
