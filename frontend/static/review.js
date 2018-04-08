function submit_review() {

	var content = document.getElementById("review_body").value;
	var statusBox = document.getElementById("review_submit_status");

	if (content.length > 1024) {
		statusBox.innerHTML = "Review too long!";
	}

	// Actually submit the review.
	var url = get_api_page("/api/title/" + movie_id + "/review/create");
	console.log('request url: ' + url);
	var req = new XMLHttpRequest();
	req.open('POST', url, true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	req.onreadystatechange = function() {

		if (req.status == 200) { // Successful
			window.location.reload(true); // Lazy way of showing updates.
		} else if (req.status == 403) { // FORBIDDEN
			statusBox.innerHTML = "Not logged in!";
		} else {
			statusBox.innerHTML = "Error!  HTTP " + req.status;
		}

	};

	req.send("desc=" + encodeURIComponent(content));

}
