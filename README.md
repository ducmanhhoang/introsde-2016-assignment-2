**Assignment 02: RESTful Services**

**Introduction to Service Design and Engineering | University of Trento |** 

**Name:** DUC MANH HOANG (worked alone)
**ID No.:** MAT.180387

**Description:**

In this assignment is implemented a server and a client calling this server. 
The server was deployed on Heroku **https://assignment02.herokuapp.com**.
Instead the client was implemented to call Heroku server.



**Code:**

* **src/introsde.rest.ehealth.client.json:** Client Json
* **src/introsde.rest.ehealth.client.xml:** Client Xml
* **src/introsde.rest.ehealth:** Standalone HTTP Server
* **src/introsde.rest.ehealth.map:** Classes to manage the mapping operation.
* **src/introsde.rest.ehealth.dao:** Classe DAO "data access object"
* **src/introsde.rest.ehealth.model:**   Classes to represent your domain model.
* **src/introsde.rest.ehealth.resources:** will include the *resources* that are exposed throught the RESTful API, which can be seen as the controllers that receive requests and respond with a representation of the resources that are requested
* **src/introsde.rest.ehealth.utils:** Containing the utility functions to create random data.



**Tasks Server:**

* With that model, expose the following services through a RESTful API as follows: these CRUD operations for person plus some services to access and update health profile measurements
 * Request #1: GET /person should list all the people (see above Person model to know what data to return here) in your database (wrapped under the root element "people")
 * Request #2: GET /person/{id} should give all the personal information plus current measures of person identified by {id} (e.g., current measures means current health profile)
 * Request #3: PUT /person/{id} should update the personal information of the person identified by {id} (e.g., only the person's information, not the measures of the health profile)
 * Request #4: POST /person should create a new person and return the newly created person with its assigned id (if a health profile is included, create also those measurements for the new person).
 * Request #5: DELETE /person/{id} should delete the person identified by {id} from the system
 * Request #6: GET /person/{id}/{measureType} should return the list of values (the history) of {measureType} (e.g. weight) for person identified by {id}
 * Request #7: GET /person/{id}/{measureType}/{mid} should return the value of {measureType} (e.g. weight) identified by {mid} for person identified by {id}
 * Request #8: POST /person/{id}/{measureType} should save a new value for the {measureType} (e.g. weight) of person identified by {id} and archive the old value in the history
 * Request #9: GET /measureTypes should return the list of measures your model supports in the following formats:

		    <measureTypes>
		        <measureType>weight</measureType>
		        <measureType>height</measureType>
		        <measureType>steps</measureType>
		        <measureType>bloodpressure</measureType>
		    </measureTypes>

		{
		   "measureType": [
		        "weight",
		        "height",
		        "steps",
		        "bloodpressure"
		    ]
		}

 * Extra #1 (Request #10): PUT /person/{id}/{measureType}/{mid} should update the value for the {measureType} (e.g., weight) identified by {mid}, related to the person identified by {id}
 * Extra #2 (Request #11): GET /person/{id}/{measureType}?before={beforeDate}&after={afterDate} should return the history of {measureType} (e.g., weight) for person {id} in the specified range of date
 * Extra #3 (Request #12): GET /person?measureType={measureType}&max={max}&min={min} retrieves people whose {measureType} (e.g., weight) value is in the [{min},{max}] range (if only one for the query parameters is provided, use only that)
* Requirements:
 * SQLite database
 * Server deployed on Heroku



**Tasks Client:**

* Implement a client that can send all of these requests and print the responses. Implement ANT target (ant execute.client), which does all the calls sequentially and save the requests/responses information into a file (e.g. client-server-json.log and client-server-xml.log, push this file to a Github repository).

* Instructions:
 * Step 1. Print URL of the server you are calling (the server should be deployed on Heroku. It should be a URL of your partner student or of your own server if you work alone)
 * Step 2. For each of of requests in the plan of Step 3, including extras, you have to send the request first with the Accept and Content-type (for POST/PUT requests) headers both set to Applicaiton/XML and then both set to Application/JSON. After getting the response, print the following in the console:

        Request #[NUMBER]: [HTTP METHOD] [URL] Accept: [TYPE] Content-type: [TYPE] 
        => Result: [RESPONSE STATUS = OK, ERROR]
        => HTTP Status: [HTTP STATUS CODE = 200, 404, 500 ...]
        [BODY]  
        Example: 
            Request #7: GET /person/5/weight/899 Accept: APPLICATION/XML Content-Type: APPLICATION/XML  
            => Result: OK
            => HTTP Status: 200
            <measure>
                <mid>899</mid>
                <value>72</value>
                <created>2011-12-09</created>
            </measure>

 * Step 3. Use the following plan of requests to guide your implementation of the client.
  * Step 3.1. Send R#1 (GET BASE_URL/person). Calculate how many people are in the response. If more than 2, result is OK, else is ERROR (less than 3 persons). Save into a variable id of the first person (first_person_id) and of the last person (last_person_id)
  * Step 3.2. Send R#2 for first_person_id. If the responses for this is 200 or 202, the result is OK.
  * Step 3.3. Send R#3 for first_person_id changing the firstname. If the responses has the name changed, the result is OK.
  * Step 3.4. Send R#4 to create the following person. Store the id of the new person. If the answer is 201 (200 or 202 are also applicable) with a person in the body who has an ID, the result is OK.

            {
                  "firstname"     : "Chuck",
                  "lastname"      : "Norris",
                  "birthdate"     : "1945-01-01"
                  "healthProfile" : {
                            "weight"  : 78.9,
                            "height"  : 172
                  }
            }

   * Step 3.5. Send R#5 for the person you have just created. Then send R#1 with the id of that person. If the answer is 404, your result must be OK.
   * Step 3.6. Follow now with the R#9 (GET BASE_URL/measureTypes). If response contains more than 2 measureTypes - result is OK, else is ERROR (less than 3 measureTypes). Save all measureTypes into array (measure_types)
   * Step 3.7. Send R#6 (GET BASE_URL/person/{id}/{measureType}) for the first person you obtained at the beginning and the last person, and for each measure types from measure_types. If no response has at least one measure - result is ERROR (no data at all) else result is OK. Store one measure_id and one measureType.
   * Step 3.8. Send R#7 (GET BASE_URL/person/{id}/{measureType}/{mid}) for the stored measure_id and measureType. If the response is 200, result is OK, else is ERROR.
   * Step 3.9. Choose a measureType from measure_types and send the request R#6 (GET BASE_URL/person/{first_person_id}/{measureType}) and save count value (e.g. 5 measurements). Then send R#8 (POST BASE_URL/person/{first_person_id}/{measureType}) with the measurement specified below. Follow up with another R#6 as the first to check the new count value. If it is 1 measure more - print OK, else print ERROR. Remember, first with JSON and then with XML as content-types

                <measure>
                    <value>72</value>
                    <created>2011-12-09</created>
                </measure>

   * Step 3.10. Send R#10 using the {mid} or the measure created in the previous step and updating the value at will. Follow up with at R#6 to check that the value was updated. If it was, result is OK, else is ERROR.

              <measure>
                  <value>90</value>
                  <created>2011-12-09</created>
              </measure>

   * Step 3.11. Send R#11 for a measureType, before and after dates given by your fellow student (who implemented the server). If status is 200 and there is at least one measure in the body, result is OK, else is ERROR
   * Step 3.12. Send R#12 using the same parameters as the previous steps. If status is 200 and there is at least one person in the body, result is OK, else is ERROR



**How to run:**

Running the project requires java and ant.

Ant source file build.xml is annotated. 
* Main targets are:
* **execute.client:** Evaluation Client.The server is: **https://assignment02.herokuapp.com**. 
* **start:** To start the local server.



**References:**

https://sites.google.com/a/unitn.it/introsde_2016-17/

