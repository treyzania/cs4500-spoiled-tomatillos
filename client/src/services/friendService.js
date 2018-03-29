function sendFriendRequest(recipient) {
    var params = "recipient=" + recipient;
    httpPostAsync("./api/friends/request/send", returnResults, params);
}

function getFriends() {
    httpGetAsync("./api/friends/list", returnResults, null);
}

function getSentRequests(){
    httpGetAsync("./api/friends/request/sent", returnResults, null);
}

function getRecievedRequests(){
    httpGetAsync("./api/friends/request/recieved", returnResults, null);
}

function friendRequestRespond(sender, state){
    var params = "sender=" + sender + "&state=" + state;
    httpPutAsync("./api/friends/respond", returnResults, params);
}

function friendDelete(friend){
    var params = "friend=" + friend;
    httpPutAsync("./api/friends/delete", returnResults, params)
}

/*
 * Asynchronus http.get call function.  theURL is API Target, callback should be returnResults
 */
function httpGetAsync(theUrl, callback, params){
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous
    xmlHttp.send(params);
}

/*
 * Asynchronus http.put call function.  theURL is API Target, callback should be returnResults
 */
function httpGetAsync(theUrl, callback, params){
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("PUT", theUrl, true); // true for asynchronous
    xmlHttp.send(params);
}

/*
 * Asynchronus http.post call function.  theURL is the API Target, callback should be returnResults
 */

function httpPostAsync(theUl, callback, params) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
        }
    }
    xmlHttp.open("post", theUrl, true)
    xmlHttp.send(params)


/*
 * Callback function for asynchronus http.get call function.  Returns results from the .get.
 */
function returnResults(response) {
	return response;
}