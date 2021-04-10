# ZotRides
## CS 122B - Project 1
#### Team 16 - Ethan Wong & Jonathan Vo

ZotRides is a web application for a car rental service.  Users can currently view the top 20 rated cars and all associated information (model, make, year, category, pickup locations, etc.) for that car. Users can also view information about pickup locations and the cars they offer for rental.

### Notes
Since this is a custom schema, we provided a very small sample data insertion sql script (as per instructed to on Piazza) called "sampledata.sql" inside our repository.  Our actual data insertion script is larger in size than the movies data insertion script and has around the same number of data entries.

### Demo Video URL
View Our Demo Video : https://youtu.be/17lYLFmbk7Y

### How To Deploy
Our project 1 is based off of provided examples, so similarly to deploy one would just have to install and configure Tomcat, use Maven to create the package, copy it to the Tomcat apps folder, and then go to the Tomcat manager portal and click to the URL of the website.

### Member Contributions
We worked together for most aspects of the project, but Jonathan took the lead on frontend aspects and Ethan took the lead on backend aspects.

Jonathan used Bootstrap, CSS, and JavaScript to polish up the appearance of the website.  He also created the data insertion SQL file,  implemented the hyper links between pages.  He also worked with Ethan to implement the Javascript files to interact with the servlet API, extract, and display data.  

Ethan wrote the schema for the database, the queries to extract car / pickup location data, updated the servlet files to correspond to the correct AJAX requests, and developed the logic for displaying multiple pickup locations / addresses under one row on the website.  He also worked with Jonathan to implement the Javascript files to interact with the servlet API, extract, and display data.  
