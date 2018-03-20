package edu.northeastern.cs4500.data;

import java.util.List;

public interface FriendRequestRepository {

	List<FriendRequest> findBySender(User sender);
	List<FriendRequest> findByReciever(User reciever);
	
	List<FriendRequest> findBySenderOrReciever(User sender, User reciever);
	
	public default List<FriendRequest> findUserFriends(User u) {
		return this.findBySenderOrReciever(u, u);
	}
	
}
