# Route Planner Demo for Ryanair

## Description 

        This project is an implementation of a RESTful API application which serves information about possible direct and 
    interconnected flights (maximum 1 stop) based on the data consumed from external APIs.
    
## Installation and execution

    Recommandations:
        * Java 8
        * Maven 3
        
    Requirements:
        * Access not restricted to Maven Repositories: https://repo.spring.io/snapshot
                                                    , https://repo.spring.io/milestone
        * Before Application's start, port 8080 should not be used by any other application.
    
    In the terminal:
        * To build the project, execute "mvn clean install" in the ${project.home}.
        
        * To run the project as a SpringBoot Application, execute "mvn spring-boot:run" in the ${project.home}. 
        Then, point to: "http://127.0.0.1:8080/routeplanner/api/v1/", where Swagger UI will be loaded and will offer the
        possibility to call the REST Api.
    
## Issues:
    I've had issues with deploying and starting the WAR's application in Tomcat, so probably, at least this part is 
    missing from my implementation.

## Words about implementation

    - The solution is based on the following algorithm:
       
       0.a Given: departure0, arrival0, departureTime0, arrivalTime0, which are required ... 
       0.b   And: X-MIN-DIFF-IN-HOURS-ARRIVAL-NEXT-DEPARTURE, (optional) ...
       0.c   And: X-LIMIT-RECORDS-PER-PAGE (optional) ...
       
       0.d   And: limitRecordsPerPage: 50,
       0.e   And: minimumDifferenceInHoursBetweenArrivalAndNextDeparture: 2
       
       0.f   And: iata.tzmap, a CSV file using TAB separator, which lists every IATA Airport Code and its TimeZone ...
       
       1.    At application startup:
       1.a      ... read values for limitRecordsPerPage and minimumDifferenceInHoursBetweenArrivalAndNextDeparture ...
       1.b      ... load iata.tzmap in memory ...
       
       2.  ... When client is calling http://127.0.0.1:8080/routeplanner/api/v1/interconnections ...
       2.b ... if X-MIN-DIFF-IN-HOURS-ARRIVAL-NEXT-DEPARTURE header's value exists as an int
                    , overwrite minimumDifferenceInHoursBetweenArrivalAndNextDeparture with the new value...
       2.c ... if X-LIMIT-RECORDS-PER-PAGE header's value exists as an int
                    , overwrite limitRecordsPerPage with the new value ...
       
       3. ... Use first URI (https://api.ryanair.com/core/3/routes) to obtain information about all direct routes...
       
       4. ... Filter the list of routes, and store in a map those routes that are having a departure the same like 
       departure0, or routes that are having an arrival the same like arrival0. Every key of this map is the connection
       node where flights from deparure0 arrive to, or where flights to arrival0 are possible. Every value of this map 
       is stored as an oject which contains information about the the connection point, as an Integer, in which 
       are encoded the following types of information, depending on its value:
            * 1 = pow(2, 0) - means that we have route from connection to arrival
            * 2 = pow(2, 1) - means that we have route from departure to connection 
            ( So, then, if the Integer's value is 3, which is 1 + 2, that means that connection point is having both 
            a route from connection to arrival and a route from departure to connection )
            * 4 = pow(2, 2) - is a way to encode the direct route from departure to arrival, if it exists, and storing 
       the value in map 
       ...
       
       5. ... For the next step of this algorithm, we will try to compute travels with direct flights between departure
       and arrival ( if any, i.e. the Integer value of the key in map is 4 ) and the 1-stop flights between departure 
       arrival ( if any, i.e. there are keys in map that have associated 3 as a value )...
       
       6. ... The two situations are using the same method, which it will use conversion in utc of arrival and departure
       date times, using information from iata.tzmap file, in order to apply 
       minimumDifferenceInHoursBetweenArrivalAndNextDeparture restriction. 
            Then, the second URI is called, for every key in map that has 3(1-stop travels), or 4(0-stop travels). I've 
       decided to use a limitRecordsPerPage for limiting the records, because the external REST service after X calls 
       will fail.
            At every step when information is available, it will be filtered with various filters ...
            
       7. ... The aim of our call is to gather limitRecordsPerPage records, so, when we can be sure that this limit was
       passed, the method will return with a number of records, possible greater than limitRecordsPerPage, in which case
       in the response body will be displayed only the first limitRecordsPerPage records.
       
    - The application is validating the input:
        * validations for IATA Airport Codes of departure, arrival, according with the information from iata.tzmap file
        * validations of departureDateTime, arrivalDateTime, against their format and against the possibility that an 
        arrivalDateTime in Utc could be before departureDateTime in Utc
       
    - The application is having a way of dealing with Custom and Generic errors.
      
    - The application is able to do retries for a number of 5 attempts when a call of the external REST services is 
      failing.
      
    - The application is generating useful logs.  
      
    - The application is using Swagger to document its REST Api. 
      
    - I've added some simple Spock Tests. Also, you can see a report of their execution at the following path:
      "${project.home}/build/spock-reports/index.html".
          