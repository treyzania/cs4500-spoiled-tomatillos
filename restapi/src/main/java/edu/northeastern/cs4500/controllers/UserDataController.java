package edu.northeastern.cs4500.controllers;

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
import edu.northeastern.cs4500.data.User;
import edu.northeastern.cs4500.data.UserRepository;

@RestController
public class UserDataController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AuthKeyRepository authRepo;

	@Autowired
	private SessionRepository sessionRepo;

	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable int id) {
		return this.userRepo.findOne(Integer.valueOf(id));
	}

	@RequestMapping(value = "/api/user/create", method = RequestMethod.POST, params = "username")
	public User createUser(@RequestParam("username") String username) {
		User u = new User(username);
		this.userRepo.saveAndFlush(u);
		return u;
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

}
