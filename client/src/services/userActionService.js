function createReview(id, desc) {
    var params = "desc=" + desc;
    httpPostAsync("/api/title/" + id + "/review/create", returnResults, params);
}

function getReviewById(id) {
    httpGetAsync("/api/review/" + id, returnResults, null);
}

function getReviewsForTitle(id) {
    httpGetAsync("/api/title/" + id + "/review/all", returnResults, null);
}

function setRating(id, value) {
    var params = "value=" + value;
    httpPutAsync("/api/title/" + id + "/rating/user", returnResults, params);
}

function getRatingsForTitle(id) {
    httpGetAsync("/api/title/" + id + "/ratings/all", returnResults, params);
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