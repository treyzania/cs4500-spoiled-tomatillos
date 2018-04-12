package edu.northeastern.cs4500.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findUserByUsername(String username);
	List<User> findUserByUsernameLike(String like);

}
