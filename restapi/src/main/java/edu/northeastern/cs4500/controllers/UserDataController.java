package edu.northeastern.cs4500.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs4500.Magic;
import edu.northeastern.cs4500.data.AuthKey;
import edu.northeastern.cs4500.data.AuthKeyRepository;
import edu.northeastern.cs4500.data.Notification;
import edu.northeastern.cs4500.data.NotificationRepository;
import edu.northeastern.cs4500.data.Session;
import edu.northeastern.cs4500.data.SessionRepository;
import edu.northeastern.cs4500.data.User;
import edu.northeastern.cs4500.data.UserRepository;

@RestController
public class UserDataController {

	private static final int USERNAME_MIN_LENGTH = 3;
	private static final int PASSWORD_MIN_LENGTH = 8;
	private static final String NEW_USER_MAIL = "Welcome to the website!  You can visit your user page here: {{user:%s}}";

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AuthKeyRepository authRepo;

	@Autowired
	private SessionRepository sessionRepo;
	
	@Autowired
	private NotificationRepository notificationRepo;

	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable int id) {

		User u = this.userRepo.findOne(Integer.valueOf(id));
		if (u != null) {
			return ResponseEntity.ok(u);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@RequestMapping(value = "/api/user/by-name", method = RequestMethod.GET, params = {"name"})
	public ResponseEntity<User> getUserByName(@RequestParam("name") String name) {

		User u = this.userRepo.findUserByUsername(name);
		if (u != null) {
			return ResponseEntity.ok(u);
		} else {
			return ResponseEntity.notFound().build();
		}

	}
	
	@RequestMapping(value = "/api/user/create", method = RequestMethod.POST, params = {"username", "password"})
	public ResponseEntity<User> createUser(
			@RequestParam("username") String username,
			@RequestParam("password") String password) {

		if (username.length() < USERNAME_MIN_LENGTH) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "username too short (< 3 chars)").build();
		}

		if (password.length() < PASSWORD_MIN_LENGTH) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "password too short (< 8 chars)").build();
		}

		// Make sure there isn't alreay a user with this name.
		User ou = this.userRepo.findUserByUsername(username);
		if (ou != null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "user already exists").build();
		}

		// Create and commit the user data.
		User u = new User(username);
		this.userRepo.saveAndFlush(u);

		// Then create and commit the password, doing it after the user was created in the DB so that the numbers work out.
		AuthKey k = new AuthKey(u, password);
		this.authRepo.saveAndFlush(k);

		// Now create the "welcome notification(s)".
		Notification n = new Notification(null, u, "Welcome", String.format(NEW_USER_MAIL, u.getId()));	
		this.notificationRepo.saveAndFlush(n);
		
		return ResponseEntity.ok(u);

	}

	@RequestMapping(value = "/api/user/search", method = RequestMethod.GET, params = {"query"})
	public ResponseEntity<List<User>> findUserByName(@RequestParam("query") String q) {

		// This is probably one of the worst ways of doing this.
		Set<User> us = new HashSet<>();
		for (String qp : q.split("\\+")) {
			for (User qu : this.userRepo.findUserByUsernameLike("*" + qp + "*")) {
				us.add(qu);
			}
		}

		return ResponseEntity.ok(Arrays.asList(us.toArray(new User[us.size()])));

	}

	@RequestMapping(value = "/api/session/login", method = RequestMethod.POST, params = {"username", "password"})
	public ResponseEntity<Session> login(
			@RequestParam("username") String username,
			@RequestParam("password") String password) {

		// First we look up the user and their last auth key stuff.
		User u = this.userRepo.findUserByUsername(username);
		AuthKey k = this.authRepo.findFirstByUserUsername(username);

		// Make sure they didn't give us bogus information.
		if (u == null || k == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad username or password").build();
		}

		// Then a simple check to see if the password matches.
		if (k.isMatched(password)) {
			Session s = new Session(u);
			this.sessionRepo.saveAndFlush(s);
			String setCookie = String.format("%s=%s; path=/; max-age=%s", Magic.SESSION_COOKIE_NAME, s.getToken(), 60 * 60 * 24);
			return ResponseEntity.ok()
					.header("Set-Cookie", setCookie)
					.body(s);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad username or password").build();
		}

	}

	@RequestMapping(value = "/api/session/logout", method = RequestMethod.POST)
	public ResponseEntity<Session> logout(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {

		// Just query the data and logout.
		Session s = this.sessionRepo.findByToken(token);
		if (s != null && s.isActive()) {
			s.logout();
			this.sessionRepo.flush();
			String deleteCookie = Magic.SESSION_COOKIE_NAME + "=deleted; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT"; 
			return ResponseEntity.ok()
					.header("Set-Cookie", deleteCookie)
					.body(s);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}

	}

	@RequestMapping(value = "/api/user/current", method = RequestMethod.GET)
	public ResponseEntity<User> getSessionUser(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {

		// Find the session by their token and return the user data referenced by the session.
		Session s = this.sessionRepo.findByToken(token);
		if (s != null && s.isActive()) {
			return ResponseEntity.ok(s.getUser());
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}

	}

}
