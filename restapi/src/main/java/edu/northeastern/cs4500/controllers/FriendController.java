package edu.northeastern.cs4500.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import edu.northeastern.cs4500.data.Notification;
import edu.northeastern.cs4500.data.NotificationRepository;
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
	
	@Autowired
	private NotificationRepository notificationRepo;

	@RequestMapping(value = "/api/friends/request/send", method = RequestMethod.POST)
	public ResponseEntity<FriendRequest> sendFriendRequest(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("recipient") String recipient) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null || !s.isActive()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build(); 
		}

		User dest = this.userRepo.findUserByUsername(recipient);
		if (dest == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "user not found").build();
		}

		if (s.getUser().getId().intValue() == dest.getId().intValue()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).header(Magic.REASON_STR, "cannot send friend request to self").build();
		}
		
		// Check for outstanding requests.
		List<FriendRequest> frs = this.frRepo.findBySenderAndReciever(s.getUser(), dest);
		if (!frs.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).header(Magic.REASON_STR, "friend request already exists").build();
		}

		// Create and save the friend request.
		FriendRequest fr = new FriendRequest(s.getUser(), dest);
		this.frRepo.saveAndFlush(fr);
		
		// Send a notification to the recipient.
		String msg = String.format("Hey, I'd like to be your friend! -{{user:%s}}", s.getId().intValue());
		Notification n = new Notification(s.getUser(), dest, "Friend Request", msg);
		this.notificationRepo.saveAndFlush(n);
		
		// And return the created object.
		return ResponseEntity.ok(fr);

	}

	@RequestMapping(value = "/api/friends/list", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getFriends(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null || !s.isActive()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}

		return ResponseEntity.ok(
				this.frRepo.findUserFriends(s.getUser())
					.stream()
					.filter(f -> f.getState() == FriendState.ACCEPTED)
					.map(f -> {
						if (f.getSender().getId().intValue() != s.getUser().getId().intValue()) {
							return f.getSender();
						} else {
							return f.getReciever();
						}
					})
					.collect(Collectors.toList()));

	}
	
	@RequestMapping(value = "/api/friends/status", method = RequestMethod.GET, params = {"other"})
	public ResponseEntity<String> getFriendStatus(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("other") String other) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null || !s.isActive()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}

		User ou = this.userRepo.findUserByUsername(other);
		if (ou == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "other user not found").build();
		}
		
		List<FriendRequest> meSent = this.frRepo.findBySender(s.getUser());
		List<FriendRequest> theySent = this.frRepo.findBySender(ou);
		for (FriendRequest fr : meSent) {
			if (fr.getReciever().getId().intValue() == ou.getId().intValue() && fr.getState() != FriendState.DELETED) {
				return ResponseEntity.ok(fr.getState().name());
			}
		}
		
		for (FriendRequest fr : theySent) {
			if (fr.getReciever().getId().intValue() == s.getUser().getId().intValue() && fr.getState() != FriendState.DELETED) {
				return ResponseEntity.ok(fr.getState().name());
			}
		}
		
		return ResponseEntity.ok("none");

	}

	@RequestMapping(value = "/api/friends/request/sent", method = RequestMethod.GET)
	public ResponseEntity<List<FriendRequest>> getFriendRequestsSent(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null || !s.isActive()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
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
		if (s == null || !s.isActive()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}

		return ResponseEntity.ok(
				this.frRepo.findByReciever(s.getUser())
					.stream()
					.filter(f -> f.getState() == FriendState.PROPOSED)
					.collect(Collectors.toList()));

	}

	@RequestMapping(value = "/api/friends/request/respond", method = RequestMethod.POST)
	public ResponseEntity<FriendRequest> respondToFriendRequest(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("sender") String senderName,
			@RequestParam("state") String setting) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null || !s.isActive()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}

		User sender = this.userRepo.findUserByUsername(senderName);
		if (sender == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "dest not found").build();
		}

		// Try to find other friend requests.
		List<FriendRequest> frs = this.frRepo.findBySenderAndReciever(sender, s.getUser());
		Optional<FriendRequest> lastOpt = frs
				.stream()
				.filter(f -> f.getState() == FriendState.PROPOSED)
				.findFirst();
		if (!lastOpt.isPresent()) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "no outstanding friend requests").build();
		}

		// Try to parse the state, rejecting if necessary.
		FriendState fs = null;
		try {
			fs = FriendState.valueOf(setting);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "invalid friend state " + setting).build();
		}

		// Make sure that we aren't allowing silly state transitions.
		if (fs != FriendState.ACCEPTED && fs != FriendState.DENIED) {
			return ResponseEntity.status(HttpStatus.CONFLICT).header(Magic.REASON_STR, "unpermitted transition").build();
		}

		// Set up the friend request.
		FriendRequest fr = lastOpt.get();
		fr.setState(fs);
		this.frRepo.saveAndFlush(fr);
		
		// Now send a notification to the sender.
		String msg = String.format("User {{user:%s}} responded to your friend request with %s!", s.getUser().getId().intValue(), fs.name());
		Notification n = new Notification(null, fr.getSender(), "Friend Request", msg);
		this.notificationRepo.saveAndFlush(n);
		
		// Now actually respond.
		return ResponseEntity.ok(fr);

	}

	@RequestMapping(value = "/api/friends/delete", method = RequestMethod.PUT)
	public ResponseEntity<FriendRequest> deleteFriend(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("friend") String friendName) {

		Session s = this.sessionRepo.findByToken(token);
		if (s == null || !s.isActive()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}

		User friend = this.userRepo.findUserByUsername(friendName);
		if (friend == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "user not found").build();
		}

		// Try to see if there's an active friendship.
		Optional<FriendRequest> frOpt = this.frRepo.findBySenderAndReciever(friend, s.getUser())
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
