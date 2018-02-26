package edu.northeastern.cs4500.data;

import javax.persistence.OrderBy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthKeyRepository extends JpaRepository<AuthKey, Integer> {
	
	@OrderBy("timeUpdated DESC")
	AuthKey findFirstByUserUsername(String username);
	
}
