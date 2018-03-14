package edu.northeastern.cs4500.data;

import java.util.List;

import javax.persistence.OrderBy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	@OrderBy("submitted ASC")
	List<Review> findReviewsByUser(User u);
	
	@OrderBy("submitted ASC")
	List<Review> findReviewsByTitle(Title t);
	
}
