package edu.northeastern.cs4500.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	
	List<Notification> findByTargetAndPublishedAfter(User u);
	
}
