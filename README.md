- # General
    - #### Team#: 16
    
    - #### Names: Ethan Wong, Jonthan Vo
    
    - #### Project 5 Video Demo Link: https://youtu.be/iEx2SnGjb7I

    - #### Instruction of deployment: 
      Our project 5 is based off of provided examples, so similarly to deploy on our AWS instance, one would just have to install and configure Tomcat, use Maven to create the package, copy it to the Tomcat apps folder, and then go to the Tomcat manager portal and click to the URL of the website.  Our app uses the React front-end framework, but we have already included the build folder and set up everything to use it as the source directory for deployment.

    - #### Collaborations and Work Distribution:
    
    - #### Notes:
      The current commit reflects that of what is deployed on the master instance and has connection pooling, manual mySQL routing in servlets, and HTTP (not HTTPS since it was optional) enabled.
      The images are located in the jmetertest_images/ folder under the root directory.  
      The logs are under the jmetertest_logs/ folder under the root directory.  
      The processing script is called log_processing.py and is under the root directory.
    
- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
      /--
    
    - #### Explain how Connection Pooling is utilized in the ZotRides code.
      Our ZotRides web application uses connection pooling to reduce the overhead of having to create and release resources for a new Connection every time we want to run a query to our mySQL database.  With connection pooling, we create and maintain a pool of up to 100 connections throughout the lifecycle of our web application’s deployment.  This way, whenever a servlet wants to run a query to the database, it can use a pre-setup connection from the pool of the data source it wants to connect to and put it back when it is finished using it.  We configured our data source(s) to use connection pooling in context.xml by adding lines to specify the data source factory and settings for maxTotal, maxIdle, and maxWaitMillis.  We also set up our connection pooling to allow at most 30 connections to be idle to prevent excess pools of connections and waste resources.  We also enabled the connection pools to cache prepared statements to ensure PreparedStatements are using the correct connection they were associated with.
    
    - #### Explain how Connection Pooling works with two backend SQL.
      With two backend SQL databases, they are each hosted on separate servers (the master and slave servers) and thus require different data sources.  We integrated two connection pools, one with the data source for the master server and another with the data source for the slave server.  We updated context.xml to use two data sources, one named zotrides-master and another one named zotrides-slave.  We updated the data source URLs to include the private IP addresses for the master and slave AWS instance servers, as the master and slave databases were located on their respective servers.  We also registered these two data sources in web.xml.  Connection pooling also saves a pool of connections to use, but this time there are two pools for each data source. 
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
      /--      

    - #### How read/write requests were routed to Master/Slave SQL?
      We required all write requests to utilize connections from the zotrides-master data source, which is the data source that connects to the mySQL database on the master server.  This would result in all update / insertion queries updating the master mySQL database, which would replicate over to the slave instance via binary logging.  For read requests, we split up requests between master and slave servers by updating our Java servlets to create connections through different data sources in different endpoints.  For those servlets that only had one endpoint, we evenly split read queries between master and slave data sources.  
    

- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
    The log_processing script iterates through the generated log file. First, the user must insert the log file into the same directory as the python script. The user must point the file reader to the location of the log text file. Then the user must run the python script. The python script will print the calculated averages into the console after iterating through every single line of the log file. The log processing script is in the base directory under the name log_processing.py

- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 747                        | 614.694274254615                    | 613.6150395488461         | Very similar to case 1 of the scaled version; Doesn’t have to go through load balancer, which may be contributing to less overhead and its faster time|
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | 4318                       | 4167.352439253461                   | 4165.253905935            | Almost twice as slow as the scaled version, which makes sense since the same amount of users / requests are handled by only one server.  Its response time graph is vertically shifted upward relative to the scaled case #2, but it retains a similar shape. |
| Case 3: HTTPS/10 threads                       | ![](path to image in img/)   | 6390                       | 6260.94768054577                    | 6258.820132648847         | Due to the extra overhead from security procedures, the introduction of TLS means there is more processing, encryption, and decryption being done, therefore increasing the time it takes |
| Case 4: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 5733                       | 5603.813402152307                   | 5601.32314367             | Again we see a similar case for the scaled version being 2x faster than the single instance because of the reasons mentioned above in the case with connection pooling;Connection pooling is used in single instance case 2 and it could be seen that connection pooling’s omission of reallocating / deallocating resources in a new connection for every query proves to be more efficient than without connection pooling|

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 750                        | 623.7718428007693                   | 623.0220014153847         | Graphical analysis shows very similar shape and trend to case 1 of the single instance, but perhaps the scaled version’s higher measurements are due to slight variations or that the load balancer redirection causes overhead that would have been more beneficial in situations with multiple threads |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | 2625                       | 2501.3928786542306                  | 2500.176422861923         | Due to load balancing, distribution is approximately half that of the single instance because the total incoming requests from the 10 threads/users are split among 2 servers |
| Case 3: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 2990                       | 2864.163649957308                   | 2862.3193730723074        | Lack of connection pooling causes it to have to create a new connection for every query, and resource allocation / deallocation adds some overhead making it slower than the scaled version case #2;  It is also significantly faster than case 4 of the single server because load balancing reduces the amount of stress on each master / slave server. |
