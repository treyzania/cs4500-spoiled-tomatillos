package edu.northeastern.cs4500.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs4500.Magic;
import edu.northeastern.cs4500.data.Title;
import edu.northeastern.cs4500.data.TitleRatingRepository;
import edu.northeastern.cs4500.data.TitleRepository;
import edu.northeastern.cs4500.services.AdminSecretService;
import edu.northeastern.cs4500.services.AdminSecretServiceBean;

@SuppressWarnings("unused")
@RestController
public class MovieDataController {

	@Autowired
	private TitleRepository titleRepo;

	@Autowired
	private TitleRatingRepository ratingRepo;

	@Autowired
	private AdminSecretService secretService;

	@RequestMapping(value = "/api/title/{id}", method = RequestMethod.GET)
	public ResponseEntity<Title> getTitle(@PathVariable int id) {

		Title t = this.titleRepo.getOne(Integer.valueOf(id));
		if (t != null) {
			return ResponseEntity.ok(t);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@RequestMapping(value = "/api/title/create", method = RequestMethod.POST, params = {"name", "year", "src", "desc"})
	public ResponseEntity<Title> createTitle(
			@RequestHeader(Magic.ADMIN_SECRET_STR) String secret, 
			@RequestParam("name") String name,
			@RequestParam("year") int year,
			@RequestParam(value = "desc", required = false) String description,
			@RequestParam(value = "src", required = false) String source) {

		if (!this.secretService.getSuperuserSecret().equals(secret)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		Title t = new Title(name, year, source != null ? source : "manual");
		if (description != null) {
			t.setSummary(description);
		}

		this.titleRepo.saveAndFlush(t);
		return ResponseEntity.ok(t);

	}

	@RequestMapping(value = "/api/title/by-name", method = RequestMethod.GET, params = {"name"})
	public ResponseEntity<Title> findTitleByName(@RequestParam("name") String name) {

		// Just a straight pull from the database.
		Title t = this.titleRepo.findByName(name);
		if (t != null) {
			return ResponseEntity.ok(t);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@RequestMapping(value = "/api/search", method = RequestMethod.GET, params = {"query"})
	public ResponseEntity<List<Object>> search(@RequestParam("query") String query) {

		// Use a set here to avoid duplicates.
		Set<Title> titles = new HashSet<>();
		for (String comp : query.split("\\+")) {
			titles.addAll(this.titleRepo.findByNameLikeIgnoreCase("%" + comp + "%"));
		}

		// Now convert it to a list, but in a flexible way.
		List<Object> out = new ArrayList<>();
		out.addAll(titles);
		return ResponseEntity.ok(out);

	}

	@RequestMapping(value = "/api/title/by-full-source", method = RequestMethod.GET, params = {"source"})
	public ResponseEntity<List<Title>> findTitleByFullSource(@RequestParam("source") String source) {
		return ResponseEntity.ok(this.titleRepo.findBySourceIgnoreCase(source));
	}

	@RequestMapping(value = "/api/title/by-source-category", method = RequestMethod.GET, params = "sourcecat")
	public ResponseEntity<List<Title>> findTitleBySourceCategory(@RequestParam("sourcecat") String sourceCat) {
		return ResponseEntity.ok(this.titleRepo.findBySourceLikeIgnoreCase(sourceCat + ",%"));
	}

}
