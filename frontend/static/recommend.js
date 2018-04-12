function show_recommendations(divId) {
    let div = document.getElementById(divId);

    if(div.className === 'friends hidden') {
        div.className = "friends";
    }
    else if (div.className === 'friends'){
        div.className = "friends hidden";
    }
}

function submit_recommendation(destName, movieName, senderName, movieId) {

    var url = get_api_page("/api/notifications/send");
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

    req.send("dest=" +destName + "&type=Recommendation&body=Your friend, " + senderName + ", has recommended the movie " + "<a href=\'/title/" + movieId + "\'>" + movieName + " </a> to you!");
}
