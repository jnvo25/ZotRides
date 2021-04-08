/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */

/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {

    console.log("handleResult: populating star info from resultData");

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    let locInfoElement = jQuery("#loc_info");
    console.log(resultData[0])

    // append two html <p> created to the h3 body, which will refresh the page
    locInfoElement.append("<p>Location address: " + resultData[0]["location_address"] + "</p>" +
        "<p>Location phone: " + resultData[0]["location_phone"] + "</p>");

    console.log("handleResult: populating pickup locations table from resultData");

    let locTableBodyElement = jQuery("#loc_table_body");
    // Concatenate the html tags with resultData jsonObject to create table rows
    let rowHTML = "";
    rowHTML += "<tr>";
    rowHTML += "<th>" + resultData[0]["location_address"] + "</th>";
    rowHTML += "<th>" + resultData[0]["location_phone"] + "</th>";

    rowHTML += "<th>";

    let cars = resultData[0]["cars_offered"].split(";");
    let carIDs = resultData[0]["cars_ids"].split(";");
    if (cars.length > 0) {
        rowHTML += '<a href="single-star.html?id=' + carIDs[0] + '">' + cars[0] + '</a>';
    }
    for (let j = 1; j < cars.length; j++) {
        rowHTML += '<br><a href="single-star.html?id=' + carIDs[j] + '">' + cars[j] + '</a>';
    }
    rowHTML += "</th>";

    rowHTML += "</tr>";

    // Append the row created to the table body, which will refresh the page
    locTableBodyElement.append(rowHTML);
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let carId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-loc?id=" + carId, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});