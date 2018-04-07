package edu.northeastern.cs4500.controllers;

import java.util.List;
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
import edu.northeastern.cs4500.data.Review;
import edu.northeastern.cs4500.data.ReviewRepository;
import edu.northeastern.cs4500.data.Session;
import edu.northeastern.cs4500.data.SessionRepository;
import edu.northeastern.cs4500.data.Title;
import edu.northeastern.cs4500.data.TitleRating;
import edu.northeastern.cs4500.data.TitleRatingRepository;
import edu.northeastern.cs4500.data.TitleRepository;

@RestController
public class UserActionsController {

	// TODO Make these return proper HTTP error codes.

	@Autowired
	private ReviewRepository reviewRepo;

	@Autowired
	private TitleRatingRepository ratingRepo;

	@Autowired
	private TitleRepository titleRepo;

	@Autowired
	private SessionRepository sessionRepo;

	@RequestMapping(value = "/api/title/{id}/review/create", method = RequestMethod.POST)
	public ResponseEntity<Review> createReview(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@PathVariable int id,
			@RequestParam("desc") String body) {

		// Find the session, hopefully.
		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}

		// Find the title, hopefully.
		Title t = this.titleRepo.findOne(Integer.valueOf(id));
		if (t == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "title not found").build();
		}

		// Then just save and flush.
		Review r = new Review(t, s.getUser(), body);
		this.reviewRepo.saveAndFlush(r);
		return ResponseEntity.ok(r);

	}

	@RequestMapping(value = "/api/review/{id}", method = RequestMethod.GET)
	public ResponseEntity<Review> getReviewById(@PathVariable int id) {

		Review r = this.reviewRepo.findOne(Integer.valueOf(id));
		if (r != null) {
			return ResponseEntity.ok(r);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@RequestMapping(value = "/api/title/{id}/review/all", method = RequestMethod.GET)
	public ResponseEntity<List<Review>> getReviewByTitleId(@PathVariable int id) {

		// We have to find the title, first.
		Title t = this.titleRepo.findOne(Integer.valueOf(id));
		if (t != null) {
			return ResponseEntity.ok(this.reviewRepo.findReviewsByTitle(t));
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@RequestMapping(value = "/api/title/{id}/rating/user", method = RequestMethod.PUT)
	public ResponseEntity<String> setRating(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@PathVariable int id,
			@RequestParam("value") int val) {

		// Find the session information.
		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header(Magic.REASON_STR, "bad session").build();
		}

		// Find the title
		Title t = this.titleRepo.findOne(Integer.valueOf(id));
		if (t == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "title not found").build();
		}

		// Find or create the rating.
		TitleRating tr = this.ratingRepo.findTitleRatingByTitleAndUser(t, s.getUser());
		if (tr == null) {
			tr = new TitleRating(t, s.getUser(), 0);
		}

		// Update and then write it back to the database.
		tr.setRating(val);
		this.ratingRepo.saveAndFlush(tr);

		return ResponseEntity.ok("OK"); // Not sure what to return here.

	}

	@RequestMapping(value = "/api/title/{id}/ratings/all", method = RequestMethod.GET)
	public ResponseEntity<List<TitleRating>> getRatings(@PathVariable int id) {

		// Find the title.
		Title t = this.titleRepo.findOne(Integer.valueOf(id));
		if (t == null) {
			return ResponseEntity.notFound().header(Magic.REASON_STR, "title not found").build();
		}

		// Find the ratings on the title.
		List<TitleRating> trs = this.ratingRepo.findTitleRatingsByTitle(t);
		if (trs == null) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}

		// Just return the numbers.
		return ResponseEntity.ok(trs);

	}

}
