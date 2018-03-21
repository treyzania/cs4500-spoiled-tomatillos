package edu.northeastern.cs4500.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs4500.Magic;
import edu.northeastern.cs4500.data.FriendRequest;
import edu.northeastern.cs4500.data.FriendRequestRepository;
import edu.northeastern.cs4500.data.FriendState;
import edu.northeastern.cs4500.data.Session;
import edu.northeastern.cs4500.data.SessionRepository;
import edu.northeastern.cs4500.data.User;
import edu.northeastern.cs4500.data.UserRepository;

@RestController
public class FriendController {

	@Autowired
	private SessionRepository sessionRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private FriendRequestRepository frRepo;

	@RequestMapping(value = "/api/friends/request/send", method = RequestMethod.POST)
	public Object sendFriendRequest(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("recipient") String recipient) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return "error: not logged in"; 
		}

		User dest = this.userRepo.findUserByUsername(recipient);
		if (dest == null) {
			return "error: recipient does not exist";
		}

		// Check for outstanding requests.
		List<FriendRequest> frs = this.frRepo.findBySenderAndReciever(s.getUser(), dest);
		if (!frs.isEmpty()) {
			return "error: already a request sent, perhaps already friends";
		}

		// Finally, create it and return it.
		FriendRequest fr = new FriendRequest(s.getUser(), dest);
		this.frRepo.saveAndFlush(fr);
		return fr;

	}

	@RequestMapping(value = "/api/friends/list", method = RequestMethod.GET)
	public Object getFriends(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return "error: not logged in";
		}

		return this.frRepo.findUserFriends(s.getUser())
				.stream()
				.filter(f -> f.getState() == FriendState.ACCEPTED)
				.collect(Collectors.toList());

	}

	@RequestMapping(value = "/api/friends/request/sent", method = RequestMethod.GET)
	public Object getFriendRequestsSent(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return "error: not logged in";
		}

		return this.frRepo.findBySender(s.getUser())
				.stream()
				.filter(f -> f.getState() == FriendState.PROPOSED)
				.collect(Collectors.toList());

	}

	@RequestMapping(value = "/api/friends/request/recieved", method = RequestMethod.GET)
	public Object getFriendRequestsRecieved(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return "error: not logged in";
		}

		return this.frRepo.findByReciever(s.getUser())
				.stream()
				.filter(f -> f.getState() == FriendState.PROPOSED)
				.collect(Collectors.toList());

	}

	@RequestMapping(value = "/api/friends/respond", method = RequestMethod.PUT)
	public Object respondToFriendRequest(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("sender") String senderName,
			@RequestParam("state") String setting) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return "error: not logged in";
		}

		User sender = this.userRepo.findUserByUsername(senderName);
		if (sender == null) {
			return "error: no sender";
		}

		// Try to find other friend requests.
		List<FriendRequest> frs = this.frRepo.findBySenderAndReciever(s.getUser(), sender);
		Optional<FriendRequest> lastOpt = frs
				.stream()
				.filter(f -> f.getState() != FriendState.PROPOSED)
				.findFirst();
		if (!lastOpt.isPresent()) {
			return "error: no outstanding request, perhaps already friends?";
		}

		// Try to parse the state, rejecting if necessary.
		FriendState fs = null;
		try {
			fs = FriendState.valueOf(setting);
		} catch (IllegalArgumentException e) {
			return "error: invalid state";
		}

		// Make sure that we aren't allowing silly state transitions.
		if (fs != FriendState.ACCEPTED && fs != FriendState.DENIED) {
			return "error: invalid state transition";
		}

		FriendRequest fr = lastOpt.get();
		fr.setState(fs);
		this.frRepo.saveAndFlush(fr);
		return fr;

	}

	@RequestMapping(value = "/api/friends/delete", method = RequestMethod.PUT)
	public Object deleteFriend(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("friend") String friendName) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return "error: not logged in";
		}

		User friend = this.userRepo.findUserByUsername(friendName);
		if (friend == null) {
			return "error: other user does not exist";
		}

		// Try to see if there's an active friendship.
		Optional<FriendRequest> frOpt = this.frRepo.findBySenderAndReciever(s.getUser(), friend)
				.stream()
				.filter(f -> f.getState() == FriendState.ACCEPTED)
				.findFirst();
		if (!frOpt.isPresent()) {
			return "error: not friends";
		}

		FriendRequest fr = frOpt.get();
		fr.setState(FriendState.DELETED);
		this.frRepo.saveAndFlush(fr);
		return fr;

	}

}
