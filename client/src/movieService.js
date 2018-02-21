var key = '53f856f34ff5b6efc67de7e14ac5617d';

/*
 * Searches for movies from the tmdb database based on the id input.  Returns a single movie.
 */
function searchMovieById(id){
	var urlBase = "https://api.themoviedb.org/3/movie/MOVIE_ID?api_key=API_KEY&language=en-US";
	var url = urlBase.replace("MOVIE_ID", id).replace("API_KEY", key);
	httpGetAsync(url, returnResults);
}
	
/*
 * Searches for movies from the tmdb database based on the text input.  Returns a list of the 20 most relevant movies. 
 */
function searchMovieByTitle(title) {
	var urlBase = "https://api.themoviedb.org/3/search/movie?api_key=API_KEY&language=en-US&query=TEXT&include_adult=false";
	var url = urlBase.replace("API_KEY", key).replace("TEXT", title)
	httpGetAsync(url, returnResults);
}


/*
 * Asynchronus http.get call function.  theURL is API Target, callback should be returnResults
 */
function httpGetAsync(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous 
    xmlHttp.send(null);
}

/*
 * Callback function for asynchronus http.get call function.  Returns results from the .get.
 */
function returnResults(response) {
	return response;
}