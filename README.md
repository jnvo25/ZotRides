# ZotRides
## CS 122B - Project 4
#### Team 16 - Ethan Wong & Jonathan Vo

ZotRides is a web application for a car rental service.  Users can currently interact with all functionalities of the website, which now has implemented full-text searching with autocomplete, as well as fuzzy searching.  Users can also use the mobile application to do basic functionalities within ZotRides, including logging in, searching for cars with full-text / fuzzy search, viewing the resulting cars, and viewing single car pages.

### Important Notes
1) Full-text / autocomplete / fuzzy searching is done on a car's model (equivalent to a movie's title), but results are formatted to show additional information "make" + "model" + "year".  This might give confusion since full-text/fuzzy/autocomplete don't consider a car's make or year. 
2) Our GitHub repository is organized with our web app front-end in the my-app folder, the mobile application in the moble-app folder, and the backend in the src/main/java/com/example/zotrides folder.
3) For convenience sake, we have updated our login forms to contain a valid username / login combo by default (so that it is faster for us while demoing), but any valid user/pass combo can be used and it is still fully functional as before.

### Demo Video URL
View Our Demo Video : https://youtu.be/t5GtmBNAnRs

### How To Deploy
##### Web App Deployment
Our project 4 is based off of provided examples, so similarly to deploy on our AWS instance, one would just have to install and configure Tomcat, use Maven to create the package, copy it to the Tomcat apps folder, and then go to the Tomcat manager portal and click to the URL of the website.  

Our app uses the React front-end framework, but we have already included the build folder and set up everything to use it as the source directory for deployment as well as work with the specific IP address of our EC2 instance.  *However*, if one were to want to deploy our project on another device, they would need to first change the IP address in the URLs located in my-app/package.json and myapp/src/Host.js to match that of their own devices.  Then they would need to have npm installed with create-react-app and run "npm install" followed by "npm run build", all inside the my-app folder in order to generate the build file.  Afterwards, they can deploy the web application using mvn package.

##### Mobile App Deployment
Import the mobile-app/app folder into Android Studio as a Gradle project, set up configurations, and run on Pixel 4A/3A with API 30.  To change which server the mobile-app connects to, the IP address needs to be changed in the mobile-app/app/java/edu/uci/ics/zotrides/BackendServerConn.java file

### Fuzzy Search 
We conducted fuzzy search on a car's model, and allowed for a threshold of an edit distance of 33% the provided token's length (rounded down to the nearest integer).  This would reduce the allowed edit distance for shorter models and increase the allowed edit distance for larger models.  We implemented the fuzzy-search by unioning the edth(model, query, threshold) result with the fuzzy-text-search result MATCH ... AGAINST ... in the WHERE clause while filtering out Cars to return.  We implemented the threshold by doing integer division of the trimmed query's length by 3.  

### Member Contributions

Jonathan worked on implementing the front-end interface of full-text search, autocomplete, and beautified the android application. For the full-text search, he added a search bar to the home page of the website and adjusted the landing page of the search.  He implemented it using a autocomplete compatible library and modified it to the instruction’s specifications. He also assisted Ethan in writing the android application via pair programming and did some adjustments himself.

Ethan implemented the backend servlets for the search functionalities.  He updated existing search queries to use full-text indices to enable full-text searching and autocomplete, as well as integrated the union with edth to allow for fuzzy-text searching on a certain threshold.  Ethan also took the lead on pair programming with Jonathan for the development of the mobile application's backend.
