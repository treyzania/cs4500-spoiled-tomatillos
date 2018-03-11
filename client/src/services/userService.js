
function getUserById(id){
    httpGetAsync("./api/user/" + id, returnResults)
}

function getCurrentUser(){
    httpGetAsync("./api/user/current", returnResults)
}

function createUser(username, password) {
    var params = "username=" + username + "&password=" + password
    httpPostAsync("./api/user/create", returnResults, params)
}

function userLogin(username, password) {
    var params = "username=" + username + "&password=" + password
    httpPostAsync("./api/user/login", returnResults, params)
}

function userLogout(username, password) {
    var params = "username=" + username + "&password=" + password
    httpPostAsync("./api/user/logout", returnResults, params)
}

/*
 * Asynchronus http.get call function.  theURL is API Target, callback should be returnResults
 */
function httpGetAsync(theUrl, callback){
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous
    xmlHttp.send(null);
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
