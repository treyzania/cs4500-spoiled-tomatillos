function attempt_checked_login() {

	var username = document.getElementById("login_username").value;
	var password = document.getElementById("login_password").value;

	var statusBox = document.getElementById("login_status");

	if (username.length < 3) {
		statusBox.innerHTML = "Username must be at least 3 characters!";
		return;
	}

	if (password.length < 8) {
		statusBox.innerHTML = "Password must be 8+ characters!";
		return;
	}

	var req = new XMLHttpRequest();
	req.open('POST', get_api_page("/api/session/login"), true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	req.onreadystatechange = function() {

		if (req.readyState !== XMLHttpRequest.DONE) {
			return; // Skip it, we only care about the result.
		}

		console.log("login response: " + req.status)
		if (req.status == 200) { // Successful

			var session = JSON.parse(req.responseText);
			statusBox.innerHTML = "Success!";

			// Now set the session cookie.
			apply_session(session);

		} else if (req.status == 403) { // FORBIDDEN
			statusBox.innerHTML = "Invalid username or password.";
		} else {
			statusBox.innerHTML = "Error!  HTTP " + req.status;
		}

	};

	req.send("username=" + username + "&password=" + password);

}
