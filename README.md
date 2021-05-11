# ZotRides
## CS 122B - Project 3
#### Team 16 - Ethan Wong & Jonathan Vo

ZotRides is a web application for a car rental service.  Users can currently interact with all functionalities of the website, which now has implemented security features to utilize reCAPTCHA and HTTPS.  The data set has also been expanded via XML Parsing.  Employees can log in via the _dashboard entry point and have access to an admin board.

### Important Notes
For convenience sake, we have updated our login forms to contain a valid username / login combo by default (so that it is faster for us while demoing), but any valid user/pass combo can be used and it is still fully functional as before.

### Demo Video URL
View Our Demo Video : https://www.youtube.com/watch?v=KHkQxvqmgBM

### How To Deploy
Our project 3 is based off of provided examples, so similarly to deploy on our AWS instance, one would just have to install and configure Tomcat, use Maven to create the package, copy it to the Tomcat apps folder, and then go to the Tomcat manager portal and click to the URL of the website.  

Our app uses the React front-end framework, but we have already included the build folder and set up everything to use it as the source directory for deployment as well as work with the specific IP address of our EC2 instance.  *However*, if one were to want to deploy our project on another device, they would need to first change the IP address in the URLs located in my-app/package.json and myapp/src/Host.js to match that of their own devices.  Then they would need to have npm installed with create-react-app and run "npm install" followed by "npm run build", all inside the my-app folder in order to generate the build file.  Afterwards, they can deploy the web application using mvn package.

### Queries with Prepared Statements
Below are a list of all files and a link to them that used some form of Prepared Statements.  These may link to a previous commit but the files haven't changed.

All of these files can be found under src/main/java/com/example/zotrides directory, but for convenience sake they are linked here.

[AddCarServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/AddCarServlet.java)
[AddLocationServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/AddLocationServlet.java)
[BrowseServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/BrowseServlet.java)
[CarImageServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/CarImageServlet.java)
[CategoryServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/CategoryServlet.java)
[EmployeeLoginServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/EmployeeLoginServlet.java)
[LoginServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/LoginServlet.java)
[MetadataServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/MetadataServlet.java)
[PaginateServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/PaginateServlet.java)
[PaymentServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/PaymentServlet.java)
[SAXParserCar.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/SAXParserCar.java)
[SAXParserMapping.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/SAXParserMapping.java)
[SAXParserPickup.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/SAXParserPickup.java)
[SearchServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/SearchServlet.java)
[SingleCarServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/SingleCarServlet.java)
[SingleLocationServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/SingleLocationServlet.java)
[SortServlet.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/SortServlet.java)
[UpdateSecurePassword.java](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/src/main/java/com/example/zotrides/UpdateSecurePassword.java)

### Parsing Optimization Strategies
The two parsing optimization strategies we used were batch processing and the load data sql inserts.  

For adding the cars and pickup locations to the databse, we used the batch processing technique to optimize our queries by processing them in two giant batches rather than each individual query at a time .  The cars XML file had around 8500 valid cars to be inserted (after the invalid ones were recorded) and took around 6 minutes to fully parse, record, and insert into our database.  The pickup locations XML had about 6000 valid locations (after the invalid ones were recorded) and took around 2.5 minutes to fully parse, record, and insert into our database.  This was much faster than when we used the naive implementation, which was for every encountered XML file we would send it to the database instead of doing so in batches.  Our naive implementation was very slow and took close to around 15 minutes to fully execute, although we did notice slight variations in our other tests since our Amazon EC2 instance would sometimes be much faster and other times would be very laggy.

For adding the mappings between cars and pickup locations, we created a LOAD DATA file to use to directly insert entries into the mySQL database.  Since we had significantly more mappings to parse (around 120K were inserted into our database vs 6-9K cars and locations), we felt batch processing alone would not be fast enough and wanted to try an even faster method of data insertions.  Compared to our naive implementation which took around 7 minutes, it only took about 1 minute to fully parse and create the LOAD DATA file and around another 45 seconds to insert it on SQL using the LOAD DATA INFILE command!

### Inconsistent Data Reports
Our data inconsistency reports can be viewed under the inconsistency-reports directory, or by clicking on the links added below for convenience.  

The reports show each error, showing the name of the tag and the value that caused the error along with a short description of why.  It also reports all missing required tags or attributes.  Our data inconsistency reports also track the number of missing and required (NOT NULL or KEY entries) XML tags (either missing tags or missing attributes), inconsistent data types (string instead of integer, length of string too long, etc.), and number of duplicate entries (rejected by database).

Statistics about the number of duplicated entries, invalid entries, etc. are located at the very bottom of each inconsistency report.

[Inconsistencies From Parsing Cars](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/inconsistency-reports/carstats.txt)

[Inconsistencnies From Parsing Pickup Locations](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/inconsistency-reports/mapstats.txt)

[Inconsistencies From Parsing Car-Location Mappings](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-16/blob/d6390a868d1e4103a324be671a1bdb8d18ac3bbf/inconsistency-reports/mapstats.txt)

### Member Contributions

Jonathan generated all the XML files for the custom domains including the required errors. These files include tens of thousands of entries to match the one given by the class. He also implemented the employee login and recaptcha on the both logins. Jonathan also created the front end for the dashboard and forms that communicate with the backend to give information to the user. Jonathan also implemented the XML parsing programs for pickup and mapping. He also introduced password encryption and decryption to the back end and created the employee login servlet.

Ethan implemented HTTPS redirection, converted all queries to use prepared statements, wrote the stored procedures for the dashboard inserts which included retreiving metadata / adding cars / adding locations, helped write the naive XML parsing programs, converted naive XML parsing into optimized XML parsing programs, and conducted timing trials for the naive vs optimized XML parsings.  
