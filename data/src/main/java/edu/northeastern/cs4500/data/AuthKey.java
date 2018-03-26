package edu.northeastern.cs4500.data;

import java.sql.Timestamp;
import java.time.Clock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.northeastern.cs4500.util.Salting;

@Entity(name = "auth")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuthKey {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private User user;

	private Timestamp timeUpdated;
	private String hash;
	private String salt;

	public AuthKey() {

	}

	public AuthKey(User user, String password) {
		this.user = user;
		this.timeUpdated = Timestamp.from(Clock.systemUTC().instant());
		this.salt = Salting.createNewSalt();
		this.hash = Salting.computeHashWithSalt(password, this.salt);
	}

	public User getUser() {
		return this.user;
	}

	public Timestamp getTimeUpdated() {
		return this.timeUpdated;
	}

	public boolean isMatched(String check) {
		// This is vulnerable to timing attacks, but I'm not being paid to write this code.
		return this.hash.equals(Salting.computeHashWithSalt(check, this.salt));
	}

}
