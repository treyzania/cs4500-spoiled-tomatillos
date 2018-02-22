package edu.northeastern.cs4500.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthKeyRepository extends JpaRepository<AuthKey, Integer> {
	
	AuthKey findByUserUsername(String username);
	AuthKey findByUserId(Integer userId);
	
}
