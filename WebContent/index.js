/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    // Populate the star table
    // Find the empty table body by id "car_table_body"
    let carTableBodyElement = jQuery("#car_table_body");
    let cardrow = jQuery("#card-row");


    // Iterate through resultData, no more than 20 entries
    console.log(resultData[0]["location_phone"]);
    for (let i = 0; i < Math.min(20, resultData.length); i++) {
        cardrow.append(
            createCard(
                resultData[i]["car_name"],
                resultData[i]["car_id"],
                resultData[i]["car_category"],
                resultData[i]["car_rating"],
                resultData[i]["car_votes"],
                resultData[i]["location_address"].split(";"),
                resultData[i]["location_ids"].split(";"),
                resultData[i]["location_phone"].split(";")
        ));
    }
}

function createCard(name, id, category, rating, votes, locations, location_ids, phonenumber) {
    var rowHTML = "";
    rowHTML += '<div class="col-4">';
    rowHTML += '<div class="card">';
    rowHTML += '<img src="https://d1zgdcrdir5wgt.cloudfront.net/media/vehicle/images/ZPBASTzWQm29XWKlBjoT0g.730x390.jpg" class="card-img-top" alt="...">';
    rowHTML += '<div class="card-body">';
    rowHTML += '<a href="single-star.html?id=' + id + '">';
    rowHTML += '<h5 class="card-title">' + name + '</h5>';
    rowHTML += '</a>';
    rowHTML += '<p><i>' + category + '</i></p>';
    rowHTML += '<p>' + rating + '&#9733; (' + votes + ' votes)</p>';
    rowHTML += '<ul class="list-group list-group-flush">';
    for(var i=0; i<locations.length; i++) {
        rowHTML += '<a href="single-location.html?id=' + location_ids[i] + '">';
        rowHTML +='<li class="list-group-item">' + locations[i] + "</li>";
        rowHTML += "</a>";
        rowHTML += "<p>" + phonenumber[i] + "</p>";
    }
    rowHTML += '</ul>';
    rowHTML += '</div>';
    rowHTML += '</div>';
    rowHTML += '</div>';
    return rowHTML;
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/cars", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});