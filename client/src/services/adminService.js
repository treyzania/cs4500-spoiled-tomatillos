function createTitleADMIN(name, year, desc) {
    var url = "./api/movie/create";
    var params = "name=" + name + "&year=" + year + "&desc=" + desc;
    httpPostAsyncADMIN(theURL, returnResults, params);
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
    var reader = new FileReader();
    var adminSecret = reader.readAsText("./mnt/rttmp/adminsecret.txt")
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("PUT", theUrl, true); // true for asynchronous
    xmlHttp.setRequestHeader("Admin-Secret", adminSecret);
    xmlHttp.send(params);
}

/*
 * Asynchronus http.post call function.  theURL is the API Target, callback should be returnResults
 */

function httpPostAsyncADMIN(theUl, callback, params) {
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