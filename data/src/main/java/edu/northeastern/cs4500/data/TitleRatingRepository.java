package edu.northeastern.cs4500.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRatingRepository extends JpaRepository<TitleRating, Integer> {

	TitleRating findTitleRatingByTitleAndUser(Title t, User u);
	List<TitleRating> findTitleRatingsByTitle(Title t);
	
}
