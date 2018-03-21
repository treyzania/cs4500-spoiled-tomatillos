package edu.northeastern.cs4500.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository extends JpaRepository<Title, Integer> {

	Title findByName(String name);
	List<Title> findByNameLikeIgnoreCase(String inName);

}
