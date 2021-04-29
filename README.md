# ZotRides
## CS 122B - Project 2
#### Team 16 - Ethan Wong & Jonathan Vo

ZotRides is a web application for a car rental service.  Users can currently interact with all functionalities of the website as described in Project 2 write up.

### Important Notes
On our "cars list" page, the hyperlink functionality is implemented but the links are not highlighted blue beacause we wanted to keep the color-design consistent.  To visit single car page, click on the car's image, to visit single-location page click on the address text, to browse by that category, click on the car's category.

Since this is a custom schema, we provided a very small sample data insertion sql script (as per instructed to on Piazza) called "sampledata.sql" inside our repository.  Our actual data insertion script is larger in size than the movies data insertion script and has around the same number of data entries as moviedata.sql.

### Demo Video URL
View Our Demo Video : https://youtu.be/BJh7hayHStA

### How To Deploy
Our project 2 is based off of provided examples, so similarly to deploy on our AWS instance, one would just have to install and configure Tomcat, use Maven to create the package, copy it to the Tomcat apps folder, and then go to the Tomcat manager portal and click to the URL of the website.  

Our app uses the React front-end framework, but we have already included the build folder and set up everything to use it as the source directory for deployment as well as work with the specific IP address of our EC2 instance.  *However*, if one were to want to deploy our project on another device, they would need to first change the IP address in the URLs located in my-app/package.json and myapp/src/Host.js to match that of their own devices.  Then they would need to have npm installed with create-react-app and run "npm install" followed by "npm run build", all inside the my-app folder in order to generate the build file.  Afterwards, they can deploy the web application using mvn package.

### Substring Matching Design
We utilized the "LIKE" operator on mySQL for case-insensitive comparisons.  mySQL did not seem to support the "ILIKE" operator so we did not explore potential usage of it.  

For searching, we implemented searching for the car's make, model, and pickup location with substring matching (not exact matching) by using "%VALUE%" to implement substring search.  We inserted such additional restrictions given by the user from frontend to backend to implement searching across the entire database entiries.  

To implement browsing, we changed the pattern to only match the first character e.g. "A%".  For wildcard matching, I used the RLIKE operator to indicate non-alphanumeric characters should be matched with the pattern : model RLIKE "^[^A-Za-z0-9].*"  This indicates that the start of the string should be a non-alphanumeric character, and there can be anything else after it

### Member Contributions
Jonathan took the lead on implementing most of the front-end and Ethan took the lead on implementing most of the backend.

Jonathan implemented the various front end features of the website using HTML, CSS, and Javascript with the ReactJS frameworks. Jonathan worked in the backend to connect some requests and modify input handling. He implemented hyperlinking and navigation between pages in the front end. Jonathan has also built the pages of the website, complete with forms and jQuery requests connecting them to the database. These pages include the login page, payment page, browse by page. Jonathan also helped set up the project for production build and assisted in coming up with communication procedures between the front and back end. 

Ethan focused on implementing most of the back-end logic.  He incorporated course examples to add login filtering, login credential verification, substring pattern matching for searching and browsing, verify valid pagination (next/prev) requests, and perform caching.  Most of this involved working with servlet files to connect AJAX requests to database queries or to update stored information in the session, as well as formatting JSON responses to send back to the frontend API.
