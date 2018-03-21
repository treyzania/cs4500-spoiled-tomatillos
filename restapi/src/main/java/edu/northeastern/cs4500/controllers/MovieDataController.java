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

import edu.northeastern.cs4500.data.Title;
import edu.northeastern.cs4500.data.TitleRatingRepository;
import edu.northeastern.cs4500.data.TitleRepository;

@SuppressWarnings("unused")
@RestController
public class MovieDataController {

	@Autowired
	private TitleRepository titleRepo;

	@Autowired
	private TitleRatingRepository ratingRepo;

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

	@RequestMapping(value = "/api/title/by-name", method = RequestMethod.GET, params = {"name"})
	public Title findTitleByName(@RequestParam("name") String name) {
		// Just a straight pull from the database.
		return this.titleRepo.findByName(name);
	}

	@RequestMapping(value = "/api/search", method = RequestMethod.POST, params = {"query"})
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
