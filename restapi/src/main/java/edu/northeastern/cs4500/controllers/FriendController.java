package edu.northeastern.cs4500.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<FriendRequest> sendFriendRequest(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("recipient") String recipient) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return ResponseEntity.badRequest().header(Magic.REASON_STR, "bad session").build(); 
		}

		User dest = this.userRepo.findUserByUsername(recipient);
		if (dest == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "user not found").build();
		}

		// Check for outstanding requests.
		List<FriendRequest> frs = this.frRepo.findBySenderAndReciever(s.getUser(), dest);
		if (!frs.isEmpty()) {
			return ResponseEntity.badRequest().header(Magic.REASON_STR, "friend request already exists").build();
		}

		// Finally, create it and return it.
		FriendRequest fr = new FriendRequest(s.getUser(), dest);
		this.frRepo.saveAndFlush(fr);
		return ResponseEntity.ok(fr);

	}

	@RequestMapping(value = "/api/friends/list", method = RequestMethod.GET)
	public ResponseEntity<List<FriendRequest>> getFriends(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return ResponseEntity.badRequest().header(Magic.REASON_STR, "bad session").build();
		}

		return ResponseEntity.ok(
				this.frRepo.findUserFriends(s.getUser())
					.stream()
					.filter(f -> f.getState() == FriendState.ACCEPTED)
					.collect(Collectors.toList()));

	}

	@RequestMapping(value = "/api/friends/request/sent", method = RequestMethod.GET)
	public ResponseEntity<List<FriendRequest>> getFriendRequestsSent(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return ResponseEntity.badRequest().header(Magic.REASON_STR, "bad session").build();
		}

		return ResponseEntity.ok(
				this.frRepo.findBySender(s.getUser())
					.stream()
					.filter(f -> f.getState() == FriendState.PROPOSED)
					.collect(Collectors.toList()));

	}

	@RequestMapping(value = "/api/friends/request/recieved", method = RequestMethod.GET)
	public ResponseEntity<List<FriendRequest>> getFriendRequestsRecieved(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return ResponseEntity.badRequest().header(Magic.REASON_STR, "bad session").build();
		}

		return ResponseEntity.ok(
				this.frRepo.findByReciever(s.getUser())
					.stream()
					.filter(f -> f.getState() == FriendState.PROPOSED)
					.collect(Collectors.toList()));

	}

	@RequestMapping(value = "/api/friends/respond", method = RequestMethod.PUT)
	public ResponseEntity<FriendRequest> respondToFriendRequest(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("sender") String senderName,
			@RequestParam("state") String setting) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return ResponseEntity.badRequest().header(Magic.REASON_STR, "bad session").build();
		}

		User sender = this.userRepo.findUserByUsername(senderName);
		if (sender == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "dest not found").build();
		}

		// Try to find other friend requests.
		List<FriendRequest> frs = this.frRepo.findBySenderAndReciever(s.getUser(), sender);
		Optional<FriendRequest> lastOpt = frs
				.stream()
				.filter(f -> f.getState() != FriendState.PROPOSED)
				.findFirst();
		if (!lastOpt.isPresent()) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "no outstanding friend requests").build();
		}

		// Try to parse the state, rejecting if necessary.
		FriendState fs = null;
		try {
			fs = FriendState.valueOf(setting);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().header(Magic.REASON_STR, "invalid friend state " + setting).build();
		}

		// Make sure that we aren't allowing silly state transitions.
		if (fs != FriendState.ACCEPTED && fs != FriendState.DENIED) {
			return ResponseEntity.badRequest().header(Magic.REASON_STR, "unpermitted transition").build();
		}

		FriendRequest fr = lastOpt.get();
		fr.setState(fs);
		this.frRepo.saveAndFlush(fr);
		return ResponseEntity.ok(fr);

	}

	@RequestMapping(value = "/api/friends/delete", method = RequestMethod.PUT)
	public ResponseEntity<FriendRequest> deleteFriend(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("friend") String friendName) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return ResponseEntity.badRequest().header(Magic.REASON_STR, "bad session").build();
		}

		User friend = this.userRepo.findUserByUsername(friendName);
		if (friend == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "user not found").build();
		}

		// Try to see if there's an active friendship.
		Optional<FriendRequest> frOpt = this.frRepo.findBySenderAndReciever(s.getUser(), friend)
				.stream()
				.filter(f -> f.getState() == FriendState.ACCEPTED)
				.findFirst();
		if (!frOpt.isPresent()) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "friend request not found").build();
		}

		FriendRequest fr = frOpt.get();
		fr.setState(FriendState.DELETED);
		this.frRepo.saveAndFlush(fr);
		return ResponseEntity.ok(fr);

	}

}
