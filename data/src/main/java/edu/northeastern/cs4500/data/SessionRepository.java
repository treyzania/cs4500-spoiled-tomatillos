package edu.northeastern.cs4500.data;

import javax.persistence.OrderBy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {

	@OrderBy("loginTime DESC")
	Session findFirstByUser(User u);
	
	@OrderBy("loginTime DESC")
	Session findFirstByUserUsername(User u);

	Session findByToken(String token);
	
}
