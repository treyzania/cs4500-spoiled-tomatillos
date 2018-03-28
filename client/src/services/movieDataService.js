function getMovieId(id){
    httpGetAsync("./api/title/" + id, returnResults, null);
}

function createMovie(name, year, desc){
    var params = "name=" + name + "&year=" + year + "&desc=" + desc;
    httpPostAsync("./api/title/create", returnResults, params);
}

function getMovieName(name){
    var params = "name=" + name;
    httpGetAsync("./api/title/by-name", returnResults, params);
}

function getMovieName(name){
    var params = "name=" + name;
    httpGetAsync("./api/title/by-name", returnResults, params);
}

function searchMovieLocal(query){
    var params = "query=" + query;
    httpGetAsync("./api/search", returnResults, params);
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