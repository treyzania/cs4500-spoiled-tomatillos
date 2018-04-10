function submit_search() {

	// First, build the actual query we want.
	var given = document.getElementById("search_query").value;
	console.log("got search query: " + given);
	var query = "";
	for (var i = 0; i < given.length; i++) {
		var c = given.charAt(i);
		console.log("character " + i + " is " + c);
		if (c == " ") {
			query += "+";
		} else {
			query += c;
		}
	}

	// Then just do the redirect.
	window.location.assign("/search?query=" + query);

}


