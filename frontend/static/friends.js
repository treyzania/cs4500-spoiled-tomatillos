function respond_to_friend_request(sender, mode) {

	var req = new XMLHttpRequest();
	req.open('POST', get_api_page("/api/friends/request/respond"), true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = function() {

		console.log(req.status);

		if (req.readyState != 4) {
			return; // not sure if this works this way
		}

		if (req.status == 200) { // Successful
			window.location.reload(true)
		} else if (req.status == 403) { // FORBIDDEN
			alert("not logged in!")
		} else {
			alert("Error!  HTTP " + req.status);
		}

	};

	req.send("sender=" + encodeURIComponent(sender) + "&state=" + encodeURIComponent(mode));

}

function send_friend_request(user) {

	var req = new XMLHttpRequest();
	req.open('POST', get_api_page("/api/friends/request/send"), true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = function() {

		console.log(req.status);

		if (req.readyState != 4) {
			return; // not sure if this works this way
		}

		if (req.status == 200) { // Successful
			window.location.reload(true);
		} else if (req.status == 403) { // FORBIDDEN
			alert("not logged in!")
		} else {
			alert("Error!  HTTP " + req.status);
		}

	};

	req.send("recipient=" + encodeURIComponent(page_user_name));

}
