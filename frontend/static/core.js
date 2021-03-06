const session_token_name = "sessiontoken";

function get_api_page(endpoint) {
	return window.location.protocol + '//' + window.location.host + endpoint
}

function apply_session(session) {
	// We used to have more stuff in here.
	console.log(session);
	window.location.assign("/");
}

function send_notification(dest_user, type, msg, cb) {
	var req = new XMLHttpRequest();
	req.open('POST', get_api_page("/api/notifications/send"), true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = function() { if (cb != null) cb(req); };
	req.send("dest=" + encodeURIComponent(dest_user) + "&type=" + encodeURIComponent(type) + "&body=" + encodeURIComponent(msg));
}

function dismiss_notification(id) {
	var req = new XMLHttpRequest();
	req.open('POST', get_api_page("/api/notifications/dismiss"), true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = function() { window.location.reload(true); };
	req.send("id=" + id);
}

function logout() {
	var req = new XMLHttpRequest();
	req.open('POST', get_api_page("/api/session/logout"), true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = function() {
		if (req.readyState == XMLHttpRequest.DONE) {
			if (req.status == 200) {
				console.log("logged out");
				window.location.reload(true);
			} else {
				alert("error: " + req.status);
			}
		}
	};
	req.send(null);
}

/*
 * The next 3 functions were stolen from:
 * https://stackoverflow.com/questions/14573223/set-cookie-and-get-cookie-with-javascript
 */

function set_cookie(name, value, days) {
	var expires = "";
	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		expires = "; expires=" + date.toUTCString();
	}
	var cookie_assignment = name + "=" + (value || "") + expires + "; path=/";
	document.cookie = cookie_assignment;
	console.log("Set cookie: " + name + " = " + value);
}

function get_cookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
	}
	return null;
}

function delete_cookie(name) {
	document.cookie = name + '= ; expires=Thu, 01 Jan 1970 00:00:01 GMT; path=/;';
}
