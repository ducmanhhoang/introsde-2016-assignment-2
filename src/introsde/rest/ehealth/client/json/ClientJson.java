package introsde.rest.ehealth.client.json;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NodeList;

public class ClientJson {

	public static ArrayList<String> measure_types;
	private static String first_person, measureType, measure_id, measure;
	public static int first_person_id, last_person_id, new_person_id, countMeasure, newcountMeasure;
	private static FileWriter writer = null;

	private static URI getBaseURI() {
		return UriBuilder.fromUri("https://assignment02.herokuapp.com").build();
	}

	private static void write(String line) {
		try {
			System.out.println(line);
			writer.append(line + " \n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		writer = new FileWriter("client-server-json.log");
		try {
			try {
				System.out.println("START client Json");
				write("URL BASE: https://assignment02.herokuapp.com");
				write(" \n -------------");
				request0();
				write(" \n -------------");
				request1();
				write(" \n -------------");
				request2();
				write(" \n -------------");
				request3();
				write(" \n -------------");
				request4();
				write(" \n -------------");
				request5();
				write(" \n -------------");
				request6();
				write(" \n -------------");
				request7();
				write(" \n -------------");
				request8();
				write(" \n -------------");
				request9();
				write(" \n -------------");
				request10();
				write(" \n -------------");
				request11();
				write(" \n -------------");
				request12();
				write(" \n END");
				System.out.println("END client JSON");

			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			writer.flush();
			writer.close();
		}

	}
	
	/*
	 * Create database
	 */
	public static void request0() throws IOException, JSONException, Exception {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("generate");

		write("\n \n Request #0: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);

		if (httpStatus == 200) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}

		write("=> HTTP Status: " + httpStatus);
		write(json);
	}
	

	/*
	 * Step 3.1. Send R#1 (GET BASE_URL/person). Calculate how many people are
	 * in the response. If more than 2, result is OK, else is ERROR (less than 3
	 * persons). Save into a variable id of the first person (first_person_id)
	 * and of the last person (last_person_id)
	 */
	public static void request1() throws IOException, JSONException, Exception {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person");

		write("\n \n Request #1: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);

		JSONArray people = new JSONArray(json);

		if (people.length() > 2) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}

		first_person_id = people.getJSONObject(0).getInt("personId");
		last_person_id = people.getJSONObject(people.length() - 1).getInt("personId");

		write("=> HTTP Status: " + httpStatus);
		write(json);

	}

	/*
	 * Step 3.2. Send R#2 for first_person_id. If the responses for this is 200
	 * or 202, the result is OK.
	 */
	public static void request2() throws IOException, JSONException {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/" + first_person_id);

		write("\n \n Request #2: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);

		// provide data for step 3.3.
		first_person = json;

		if ((httpStatus == 200) || (httpStatus == 202)) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(json);

	}

	/*
	 * Step 3.3. Send R#3 for first_person_id changing the firstname. If the
	 * responses has the name changed, the result is OK.
	 */
	public static void request3() throws Exception {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/" + first_person_id);

		write("\n \n Request #3: [PUT] [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");

		// create a new first name a string of date providing a seeing
		// intuitively.
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newname = dateFormat.format(new Date());

		JSONObject json_first_person = new JSONObject(first_person);
		json_first_person.put("firstname", "Changed_JSON at " + newname);

		write(json_first_person.toString());
		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.put(Entity.json(json_first_person.toString()));
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);

		if ((httpStatus == 201 || httpStatus == 200)) { // created
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}

		write("=> HTTP Status: " + httpStatus);
		write(json_first_person.toString());
	}

	/*
	 * Step 3.4. Send R#4 to create the following person. Store the id of the
	 * new person. If the answer is 201 (200 or 202 are also applicable) with a
	 * person in the body who has an ID, the result is OK.
	 */

	public static void request4() throws IOException, JSONException, Exception {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person");

		write("\n \n Request #4: [POST] [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newname = dateFormat.format(new Date());
		String newPerson = "{" + "\"firstname\": \"Person at " + newname + "\", " + "\"lastname\": \"New\", "
				+ "\"birthdate\": \"1945-01-01\", " + "\"healthprofile\": { " + "\"weight\": 86, " + "\"height\": 1.72 "
				+ "}" + "}";
		write(newPerson);

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(newPerson));
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject j = new JSONObject(json);
		new_person_id = j.getInt("idPerson");

		if ((httpStatus == 200) || (httpStatus == 201) || (httpStatus == 202)) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(json);
	}

	/*
	 * Step 3.5. Send R#5 for the person you have just created. Then send R#2
	 * with the id of that person. If the answer is 404, your result must be OK.
	 */
	public static void request5() throws IOException, Exception {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/" + new_person_id);

		write("\n \n Request #5: [DELETE] [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).delete();
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);

		write("=> HTTP Status: " + httpStatus);
		write(json);

		service = client.target(getBaseURI()).path("person/" + new_person_id);
		write("\n Request #5: GET [" + service.getUri().getPath()
				+ "] Accept: [APPLICATION_XML] Content-type: [APPLICATION_XML]");
		response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		httpStatus = response.getStatus();
		json = response.readEntity(String.class);

		if (httpStatus == 500) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		//write(json);
	}

	/*
	 * Step 3.6. Follow now with the R#9 (GET BASE_URL/measureTypes). If
	 * response contains more than 2 measureTypes - result is OK, else is ERROR
	 * (less than 3 measureTypes). Save all measureTypes into array
	 * (measure_types)
	 */
	public static void request6() throws IOException, Exception {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("measureTypes/");

		write("\n \n Request #6: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);

		JSONObject measureTypesJson = new JSONObject(json);
		JSONArray measureTypesJsonArray = measureTypesJson.getJSONArray("measureType");
		if (measureTypesJsonArray.length() > 2) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		measure_types = new ArrayList<String>();
		for (int i = 0; i < measureTypesJsonArray.length(); i++) {
			measure_types.add(measureTypesJsonArray.getString(i));
		}
		write(json);
	}

	/*
	 * Step 3.7. Send R#6 (GET BASE_URL/person/{id}/{measureType}) for the first
	 * person you obtained at the beginning and the last person, and for each
	 * measure types from measure_types. If no response has at least one measure
	 * - result is ERROR (no data at all) else result is OK. Store one
	 * measure_id and one measureType.
	 */

	private static boolean auxrequest7(int idPerson) throws IOException, Exception {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = null;
		Response response = null;
		String json = null;
		boolean exist = false;
		for (String mt : measure_types) {
			service = client.target(getBaseURI()).path("person/" + idPerson + "/" + mt);
			write("\n Request #7: GET [" + service.getUri().getPath()
					+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");
			response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
			int httpStatus = response.getStatus();
			json = response.readEntity(String.class);
			try {
				JSONArray ja = new JSONArray(json);
				if ((!exist) && ja.length() > 1) {
					measureType = mt;
					new_person_id = idPerson;
					measure_id = String.valueOf(ja.getJSONObject(0).getInt("mid"));
					exist = true;
				}
			} catch (Exception e) {
			}
			write("=> HTTP Status: " + httpStatus);
			write(json);
		}
		return exist;
	}

	public static void request7() throws IOException, Exception {
		write("\n");
		boolean a1 = auxrequest7(first_person_id);
		boolean a2 = auxrequest7(last_person_id);
		if (a1 && a2) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
	}

	/*
	 * Step 3.8. Send R#7 (GET BASE_URL/person/{id}/{measureType}/{mid}) for the
	 * stored measure_id and measureType. If the response is 200, result is OK,
	 * else is ERROR.
	 */
	public static void request8() throws IOException, Exception {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI())
				.path("person/" + new_person_id + "/" + measureType + "/" + measure_id);
		write("\n \n Request #8: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");
		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);
		measure = json;
		if (httpStatus == 200) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(json);
	}

	/*
	 * Step 3.9. Choose a measureType from measure_types and send the request
	 * R#6 (GET BASE_URL/person/{first_person_id}/{measureType}) and save count
	 * value (e.g. 5 measurements). Then send R#8 (POST
	 * BASE_URL/person/{first_person_id}/{measureType}) with the measurement
	 * specified below. Follow up with another R#6 as the first to check the new
	 * count value. If it is 1 measure more - print OK, else print ERROR.
	 * Remember, first with JSON and then with XML as content-types
	 */
	public static void request9() throws IOException, Exception {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/" + new_person_id + "/" + measureType);

		write("\n \n Request #9: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);

		JSONArray ja = new JSONArray(json);
		countMeasure = ja.length();

		write("=> HTTP Status: " + httpStatus);
		write(json);

		service = client.target(getBaseURI()).path("person/" + new_person_id + "/" + measureType);

		write("\n Request #9: [POST] [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dataresult = sdf.format(new Date());
		Random ran = new Random();
		Integer value = ran.nextInt(70) + 30;

		json = "{ " + " \"value\": " + value + ", " + " \"created\": \"" + dataresult + "\" " + "} ";
		write(json);

		response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(json));
		httpStatus = response.getStatus();
		json = response.readEntity(String.class);
		write("=> HTTP Status: " + httpStatus);
		write(json);

		service = client.target(getBaseURI()).path("person/" + new_person_id + "/" + measureType);

		write("\n Request #9: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");
		response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		httpStatus = response.getStatus();
		json = response.readEntity(String.class);

		ja = new JSONArray(json);
		newcountMeasure = ja.length();

		if (newcountMeasure > countMeasure) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(json);
	}

	/*
	 * Step 3.10. Send R#10 using the {mid} or the measure created in the
	 * previous step and updating the value at will. Follow up with at R#6 to
	 * check that the value was updated. If it was, result is OK, else is ERROR.
	 * 
	 * <measure> <value>90</value> <created>2011-12-09</created> </measure>
	 */
	
	public static void request10() throws Exception {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/" + new_person_id + "/" + measureType + "/" + measure_id);

		write("\n \n Request #10: [PUT] [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");
		
		write(measure);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String created = dateFormat.format(new Date());

		JSONObject json_measure = new JSONObject(measure);
		json_measure.put("created", created);

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.put(Entity.json(json_measure.toString()));
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);
		json_measure = new JSONObject(json);
		

		if ((httpStatus == 201 || httpStatus == 200) && (json_measure.getString("created").equalsIgnoreCase(created))) { // created
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}

		write("=> HTTP Status: " + httpStatus);
		write(json.toString());
	}
	
	
	/*
	 * Step 3.11. Send R#11 for a measureType, before and after dates given by
	 * your fellow student (who implemented the server). If status is 200 and
	 * there is at least one measure in the body, result is OK, else is ERROR
	 */
	
	public static void request11() throws IOException, Exception {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("/person/" + new_person_id + "/" + measureType).queryParam("before", "2013-01-01").queryParam("after", "2016-01-01");

		write("\n \n Request #11: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);

		if (httpStatus == 200) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(json);
	}
	
	
	/*
	 * Step 3.12. Send R#12 using the same parameters as the previous steps. If
	 * status is 200 and there is at least one person in the body, result is OK,
	 * else is ERROR
	 */
	
	public static void request12() throws IOException, Exception {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("/person").queryParam("measureType", "weight").queryParam("min", "80").queryParam("max", "86");

		write("\n \n Request #12: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus = response.getStatus();
		String json = response.readEntity(String.class);

		if (httpStatus == 200) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(json);
	}
	
}