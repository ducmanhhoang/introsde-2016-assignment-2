package introsde.rest.ehealth.client.xml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONException;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ClientXml {

	public static ArrayList<String> measure_types = new ArrayList<String>();
	private static String first_person, measure_id, measureType, measure;
	public static int first_person_id, last_person_id, new_person_id, countMeasure, newcountMeasure;
	private static FileWriter writer = null;

	private static URI getBaseURI() {
		return UriBuilder.fromUri("https://assignment02.herokuapp.com").build();
	}

	public static NodeList getNodes(String source, String query) throws Exception {
		NodeList nl = null;
		try {
			if (!source.isEmpty()) {
				InputSource input_source = new InputSource(new StringReader(source));

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document document = db.parse(input_source);

				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPath xpath = xpathFactory.newXPath();

				nl = (NodeList) xpath.evaluate(query, document, XPathConstants.NODESET);
			}
		} catch (Exception e) {
			nl = null;
		}

		return nl;
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

		writer = new FileWriter("client-server-xml.log");
		try {
			try {
				System.out.println("START client XML");
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
				System.out.println("END client XML");

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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);

		if (httpStatus == 200) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}

		write("=> HTTP Status: " + httpStatus);
		write(xml);
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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);

		NodeList n = getNodes(xml, "//person");
		if (n.getLength() > 2) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}

		NodeList n1 = getNodes(xml, "/mapPeople/person[1]/personId/text()");
		first_person_id = Integer.parseInt(n1.item(0).getNodeValue());
		NodeList n2 = getNodes(xml, "/mapPeople/person[last()]/personId/text()");
		last_person_id = Integer.parseInt(n2.item(0).getNodeValue());

		write("=> HTTP Status: " + httpStatus);
		write(xml);
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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus = response.getStatus();
		first_person = response.readEntity(String.class);

		if ((httpStatus == 200) || (httpStatus == 202)) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(first_person);

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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		NodeList nl = getNodes(first_person, "/person/firstname/text()");
		String name = nl.item(0).getNodeValue();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newname = dateFormat.format(new Date());
		first_person = first_person.replace(name, "Changed_XML at " + newname);

		write(first_person);
		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
				.put(Entity.xml(first_person));
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);

		if ((httpStatus == 200)) { // created
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}

		write("=> HTTP Status: " + httpStatus);
		write(xml);
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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newname = dateFormat.format(new Date());
		String newPerson = "<person><firstname>Person at " + newname
				+ "</firstname><lastname>XML</lastname><birthdate>2000-03-22</birthdate><healthprofile><weight>78.0</weight><height>1.67</height></healthprofile></person>";
		// write(newPerson);
		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
				.post(Entity.xml(newPerson));
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);
		write(xml);
		NodeList n1 = getNodes(xml, "/person/idPerson/text()");
		new_person_id = Integer.parseInt(n1.item(0).getNodeValue());

		if ((httpStatus == 200) || (httpStatus == 201) || (httpStatus == 202)) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(xml);

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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).delete();
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);
		write("=> HTTP Status: " + httpStatus);
		write(xml);

		service = client.target(getBaseURI()).path("person/" + new_person_id);
		write("\n Request #5: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");
		response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		httpStatus = response.getStatus();
		xml = response.readEntity(String.class);

		if (httpStatus == 500) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		//write(xml);
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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);

		NodeList n1 = getNodes(xml, "//measureType");
		if (n1.getLength() > 2) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);

		n1 = getNodes(xml, "/measureTypes/measureType/text()");
		for (int i = 0; i < n1.getLength(); i++) {
			String aux = n1.item(i).getNodeValue();
			measure_types.add(aux);
		}
		write(xml);

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
		String xml = null;
		boolean exist = false;
		for (String mt : measure_types) {
			service = client.target(getBaseURI()).path("person/" + idPerson + "/" + mt);
			write("\n Request #7: GET [" + service.getUri().getPath()
					+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");
			response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
			int httpStatus = response.getStatus();
			xml = response.readEntity(String.class);

			try {
				NodeList n1 = getNodes(xml, "//measure");
				if ((!exist) && n1.getLength() > 1) {
					n1 = getNodes(xml, "//measure/mid/text()");
					measureType = mt;
					new_person_id = idPerson;
					measure_id = n1.item(0).getNodeValue();
					exist = true;
				}
			} catch (Exception e) {
			}
			write("=> HTTP Status: " + httpStatus);
			write(xml);
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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);
		measure = xml;

		if (httpStatus == 200) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(xml);
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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);

		NodeList n1 = getNodes(xml, "//measure");
		countMeasure = n1.getLength();

		write("=> HTTP Status: " + httpStatus);
		write(xml);

		service = client.target(getBaseURI()).path("person/" + new_person_id + "/" + measureType);

		write("\n Request #9: [POST] [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dataresult = sdf.format(new Date());
		Random ran = new Random();
		Integer value = ran.nextInt(70) + 30;

		xml = "<measure> <value>" + value + "</value> <created>" + dataresult + "</created> </measure>";
		write(xml);

		response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).post(Entity.xml(xml));
		httpStatus = response.getStatus();
		xml = response.readEntity(String.class);
		write("=> HTTP Status: " + httpStatus);
		write(xml);

		service = client.target(getBaseURI()).path("person/" + new_person_id + "/" + measureType);

		write("\n Request #9: GET [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");
		response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		httpStatus = response.getStatus();
		xml = response.readEntity(String.class);

		n1 = getNodes(xml, "//measure");
		newcountMeasure = n1.getLength();
		if (newcountMeasure > countMeasure) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(xml);
	}

	
	
	/*
	 * Step 3.10. Send R#10 using the {mid} or the measure created in the
	 * previous step and updating the value at will. Follow up with at R#6 to
	 * check that the value was updated. If it was, result is OK, else is ERROR.
	 */
	public static void request10() throws Exception {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/" + new_person_id + "/" + measureType + "/" + measure_id);

		write("\n \n Request #10: [PUT] [" + service.getUri().getPath()
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");
		
		write(measure);
		NodeList nl = getNodes(measure, "/measure/created/text()");
		String created_node = nl.item(0).getNodeValue();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String created = dateFormat.format(new Date());
		measure = measure.replace(created_node, created);

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
				.put(Entity.xml(measure));
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);
		
		nl = getNodes(xml, "/measure/created/text()");
		created_node = nl.item(0).getNodeValue();

		if ((httpStatus == 200) && (created.equalsIgnoreCase(created_node))) { // created
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}

		write("=> HTTP Status: " + httpStatus);
		write(xml);
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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);

		if (httpStatus == 200) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(xml);
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
				+ "] Accept: APPLICATION_XML Content-type: APPLICATION_XML");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus = response.getStatus();
		String xml = response.readEntity(String.class);

		if (httpStatus == 200) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " + httpStatus);
		write(xml);
	}
}