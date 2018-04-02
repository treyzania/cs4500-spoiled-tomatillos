package edu.northeastern.cs4500.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs4500.Magic;
import edu.northeastern.cs4500.data.Notification;
import edu.northeastern.cs4500.data.NotificationRepository;
import edu.northeastern.cs4500.data.Session;
import edu.northeastern.cs4500.data.SessionRepository;
import edu.northeastern.cs4500.data.User;
import edu.northeastern.cs4500.data.UserRepository;
import edu.northeastern.cs4500.services.AdminSecretService;

@RestController
public class NotificationController {

	@Autowired
	private SessionRepository sessionRepo;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private NotificationRepository notifRepo;
	
	@Autowired
	private AdminSecretService adminService;
	
	@RequestMapping(value = "/api/notifications/send", params = {"dest", "type", "body"}, method = RequestMethod.POST)
	public ResponseEntity<Notification> sendNotification(
			@RequestHeader(value = Magic.ADMIN_SECRET_STR, required = false) String adminSecret,
			@CookieValue(value = Magic.SESSION_COOKIE_NAME, required = false) String userToken,
			@RequestParam("dest") String destUsername,
			@RequestParam("type") String type,
			@RequestParam("body") String body) {
		
		if ((adminSecret == null && userToken == null) || (adminSecret != null && userToken != null)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "not logged in").build();
		}
		
		// Figure out what kind of user we're talking about.
		User u;
		if (userToken != null) {
			
			// Look up the sender token, make sure they're good.
			Session s = this.sessionRepo.findByToken(userToken);
			if (s == null) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
			}
			
			u = s.getUser();
			
		} else {
			
			// Check to see if the admin secret provided is good.
			if (!this.adminService.getSuperuserSecret().equals(adminSecret)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "invalid admin secret").build();
			}
			
			// In this case we do nothing.
			u = null;
			
		}
		
		// Look up the destination user.
		User d = this.userRepo.findUserByUsername(destUsername);
		if (d == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "destination user not found").build();
		}
		
		// Now do the work.
		Notification n = new Notification(u, d, type, body);
		this.notifRepo.saveAndFlush(n);
		return ResponseEntity.ok(n);
		
	}
	
	@RequestMapping(value = "/api/notifications/unread", method = RequestMethod.GET)
	public ResponseEntity<List<Notification>> getUnread(@CookieValue(Magic.SESSION_COOKIE_NAME) String token) {
		
		// Figure out the session.
		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}
		
		// f u n c t i o n a l p r o g r a m m i n g
		List<Notification> ns = this.notifRepo.findByTarget(s.getUser());
		return ResponseEntity.ok(
				ns.stream()
					.filter(n -> !n.isDismissed())
					.collect(Collectors.toList()));
		
	}
	
	@RequestMapping(value = "/api/notifications/dismiss", method = RequestMethod.POST)
	public ResponseEntity<Notification> dismissNotification(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@RequestParam("id") Integer id) {
		
		// Check to see if the session is ok.
		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}
		
		// Now find the notification.
		Notification n = this.notifRepo.findOne(id);
		if (n == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "bad notification id").build();
		}
		
		// Make sure the caller is the target for the notification.
		if (n.getTarget().getId().intValue() != s.getUser().getId().intValue()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "not your notification").build();
		}
		
		// Now do the real work.
		n.dismiss();
		this.notifRepo.saveAndFlush(n);
		return ResponseEntity.ok(n);
		
	}
	
}
