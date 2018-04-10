function attempt_checked_register() {

	var username = document.getElementById("reg_username").value;
	var password = document.getElementById("reg_password").value;
	var passconf = document.getElementById("reg_passconf").value;

	var statusBox = document.getElementById("reg_status");

	if (username.length < 3) {
		statusBox.innerHTML = "<div class=\"alert alert-danger\">Username must be 3+ characters!</div>";
		return;
	}

	if (password.length < 8) {
		statusBox.innerHTML = "<div class=\"alert alert-danger\">Password must be 8+ characters!</div>";
		return
	}

	if (password != passconf) {
		statusBox.innerHTML = "<div class=\"alert alert-danger\">Passwords don't match!</div>";
		return;
	}

	var url = get_api_page("/api/user/create");
	console.log('register request url: ' + url);
	var req = new XMLHttpRequest();
	req.open('POST', url, true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	req.onreadystatechange = function() {

		if (req.readyState !== XMLHttpRequest.DONE) {
			return;
		}

		console.log("register response: " + req.status)
		if (req.status == 200) { // Registration successful.

			statusBox.innerHTML = "Success!  Logging in...";

			// Now try to actually log in.
			var url2 = get_api_page("/api/session/login");
			console.log('login request url: ' + url2);
			var req2 = new XMLHttpRequest();
			req2.open('POST', url2, true);
			req2.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

			req2.onreadystatechange = function() {

				if (req2.readyState !== XMLHttpRequest.DONE) {
					return;
				}

				console.log("login response: " + req2.status + " (" + req2.readyState + ")")
				if (req2.status == 200) { // Login successful!

					var session = JSON.parse(req2.responseText);
					statusBox.innerHTML = "OK!  Redirecting...";

					// Now set the session cookie.
					apply_session(session);

				} else if (req2.status == 403) {
					statusBox.innerHTML = "Invalid username or password.";
				} else {
					statusBox.innerHTML = "Error!  HTTP " + req2.status;
				}

			};

			req2.send("username=" + username + "&password=" + password)

		} else {
			statusBox.innerHTML = "Error!  HTTP " + req.status;
		}

	};

	req.send("username=" + username + "&password=" + password)
	statusBox.innerHTML = "Registering...";

}
