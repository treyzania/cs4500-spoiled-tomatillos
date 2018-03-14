package edu.northeastern.cs4500.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	public Object createReview(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@PathVariable int id,
			@RequestBody String body) {

		// Find the session, hopefully.
		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return "error: invalid session token";
		}

		// Find the title, hopefully.
		Title t = this.titleRepo.findOne(Integer.valueOf(id));
		if (t == null) {
			return "error: title " + id + " does not exist";
		}

		// Then just save and flush.
		Review r = new Review(t, s.getUser(), body);
		this.reviewRepo.saveAndFlush(r);
		return r;

	}

	@RequestMapping(value = "/api/review/{id}", method = RequestMethod.GET)
	public Object getReviewById(@PathVariable int id) {
		return this.reviewRepo.findOne(Integer.valueOf(id));
	}

	@RequestMapping(value = "/api/title/{id}/review/all", method = RequestMethod.GET)
	public Object getReviewByTitleId(@PathVariable int id) {

		// We have to find the title, first.
		Title t = this.titleRepo.findOne(Integer.valueOf(id));

		if (t != null) {
			return this.reviewRepo.findReviewsByTitle(t);
		} else {
			return "error: title " + id + " does not exist";
		}

	}

	@RequestMapping(value = "/api/title/{id}/rating/user", method = RequestMethod.PUT)
	public String setRating(
			@CookieValue(Magic.SESSION_COOKIE_NAME) String token,
			@PathVariable int id,
			@RequestParam("value") int val) {

		// Find the session information.
		Session s = this.sessionRepo.findByToken(token);
		if (s == null) {
			return "error: invalid session token";
		}

		// Find the title
		Title t = this.titleRepo.findOne(Integer.valueOf(id));
		if (t == null) {
			return "error: title " + id + " does not exist";
		}

		// Find or create the rating.
		TitleRating tr = this.ratingRepo.findTitleRatingByTitleAndUser(t, s.getUser());
		if (tr != null) {
			tr = new TitleRating(t, s.getUser(), 0);
		}

		// Update and then write it back to the database.
		tr.setRating(val);
		this.ratingRepo.saveAndFlush(tr);

		return "saved"; // Not sure what to return here.

	}

	@RequestMapping(value = "/api/title/{id}/ratings/all", method = RequestMethod.GET)
	public Object getRatings(@PathVariable int id) {

		Title t = this.titleRepo.findOne(Integer.valueOf(id));

		if (t == null) {
			return "error: title " + id + " does not exist";
		}

		List<TitleRating> trs = this.ratingRepo.findTitleRatingsByTitle(t);

		if (trs == null) {
			return "error: title ratings is null?!?";
		}

		// Just return the numbers.
		return trs.stream().map(tr -> tr.getRating()).filter(n -> n != null).collect(Collectors.toList());

	}

}
