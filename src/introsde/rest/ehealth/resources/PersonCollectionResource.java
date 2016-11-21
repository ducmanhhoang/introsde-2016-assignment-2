package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.map.MapHealthMeasureHistory;
import introsde.rest.ehealth.map.MapHealthProfile;
import introsde.rest.ehealth.map.MapMessage;
import introsde.rest.ehealth.map.MapPerson;
import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.MeasureDefinition;
import introsde.rest.ehealth.model.Person;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Stateless
@LocalBean
@Path("/person")
public class PersonCollectionResource {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	// THIS IS NOT WORKING
	@PersistenceUnit(unitName = "introsde-jpa")
	EntityManager entityManager;

	// THIS IS NOT WORKING
	@PersistenceContext(unitName = "introsde-jpa", type = PersistenceContextType.TRANSACTION)
	private EntityManagerFactory entityManagerFactory;

	/*
	 * **Request #1:** *GET /person* should list all the names in your database
	 * (wrapped under the root element "people") URL:
	 * http://localhost:5900/sdelab/person
	 * 
	 * AND
	 * 
	 * Extra #4 (Request #12): GET
	 * /person?measureType={measureType}&max={max}&min={min} retrieves people
	 * whose {measureType} (e.g., weight) value is in the [{min},{max}] range
	 * (if only one for the query parameters is provided, use only that)
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<MapPerson> getPersonsBrowser(@Context UriInfo ui) {

		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		if (queryParams.size() == 0) {
			System.out.println("Getting list of Person...");
			List<Person> tmp_people = Person.getAll();
			List<MapPerson> people = new ArrayList<MapPerson>();
			for (Person tmp_person : tmp_people) {
				MapPerson mPerson = new MapPerson();
				mPerson.setPersonId(tmp_person.getIdPerson());
				mPerson.setFirstname(tmp_person.getName());
				mPerson.setLastname(tmp_person.getLastname());
				mPerson.setBirthdate(tmp_person.getBirthdate());

				MapHealthProfile mHP = new MapHealthProfile();
				if (tmp_person.getLifeStatus().size() != 0) {
					for (LifeStatus lf : tmp_person.getLifeStatus()) {
						if (lf.getMeasureDefinition().getMeasureName().equalsIgnoreCase("weight")) {
							mHP.setWeight(Double.parseDouble(lf.getValue()));
						}
						if (lf.getMeasureDefinition().getMeasureName().equalsIgnoreCase("height")) {
							mHP.setHeight(Double.parseDouble(lf.getValue()));
						}
					}
				} else {
					mHP.setWeight(0);
					mHP.setHeight(0);
				}
				mPerson.sethProfile(mHP);
				people.add(mPerson);
			}
			return people;
		} else {
			System.out.println(
					"Getting list of person having " + queryParams.getFirst("measureType") + " value in range between "
							+ queryParams.getFirst("min") + " and " + queryParams.getFirst("max") + " ...");

			double dmin = Double.parseDouble(queryParams.getFirst("min"));
			double dmax = Double.parseDouble(queryParams.getFirst("max"));

			List<HealthMeasureHistory> hmhs = HealthMeasureHistory.getAll();
			List<MapPerson> people = new ArrayList<MapPerson>();

			for (HealthMeasureHistory hmh : hmhs) {
				double value = Double.parseDouble(hmh.getValue());
				if (value >= dmin && value <= dmax) {
					Person tmp_person = hmh.getPerson();

					MapPerson mPerson = new MapPerson();
					mPerson.setPersonId(tmp_person.getIdPerson());
					mPerson.setFirstname(tmp_person.getName());
					mPerson.setLastname(tmp_person.getLastname());
					mPerson.setBirthdate(tmp_person.getBirthdate());

					MapHealthProfile mHP = new MapHealthProfile();
					if (tmp_person.getLifeStatus().size() != 0) {
						for (LifeStatus lf : tmp_person.getLifeStatus()) {
							if (lf.getMeasureDefinition().getMeasureName().equalsIgnoreCase("weight")) {
								mHP.setWeight(Double.parseDouble(lf.getValue()));
							}
							if (lf.getMeasureDefinition().getMeasureName().equalsIgnoreCase("height")) {
								mHP.setHeight(Double.parseDouble(lf.getValue()));
							}
						}
					} else {
						mHP.setWeight(0);
						mHP.setHeight(0);
					}
					mPerson.sethProfile(mHP);
					people.add(mPerson);
				}
			}
			return people;
		}

	}

	/*
	 * **Request #2:** *GET /person/{id}* should give all the personal
	 * information plus current measures of person identified by {id} (e.g.,
	 * current measures means current health profile) URL:
	 * http://localhost:5900/sdelab/person/3
	 */
	@GET
	@Path("{personId}")
	public MapPerson getPerson(@PathParam("personId") int id) {
		Person person = Person.getPersonById(id);
		MapPerson mPerson = new MapPerson();
		mPerson.setPersonId(person.getIdPerson());
		mPerson.setFirstname(person.getName());
		mPerson.setLastname(person.getLastname());
		mPerson.setBirthdate(person.getBirthdate());

		MapHealthProfile mHP = new MapHealthProfile();
		if (person.getLifeStatus().size() != 0) {
			for (LifeStatus lf : person.getLifeStatus()) {
				if (lf.getMeasureDefinition().getMeasureName().equalsIgnoreCase("weight")) {
					mHP.setWeight(Double.parseDouble(lf.getValue()));
				}
				if (lf.getMeasureDefinition().getMeasureName().equalsIgnoreCase("height")) {
					mHP.setHeight(Double.parseDouble(lf.getValue()));
				}
			}
		} else {
			mHP.setWeight(0);
			mHP.setHeight(0);
		}
		mPerson.sethProfile(mHP);
		return mPerson;
	}

	/*
	 * **Request #3:** *PUT /person/{id}* should update the personal information
	 * of the person identified by {id} (e.g., only the person's information,
	 * not the measures of the health profile)
	 * 
	 * Data: application/xml
	 * 
	 * <person> <firstname>Hoi</firstname> <lastname>Nghiem</lastname>
	 * <birthdate>1990-01-01T00:00:00+01:00</birthdate> </person>
	 * 
	 * URL: http://localhost:5900/sdelab/person
	 */

	@PUT
	@Path("{personId}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person updatePerson(@PathParam("personId") int personId, MapPerson mPerson) {
		System.out.println("Updating a person...");

		Person person = Person.getPersonById(personId);

		person.setName(mPerson.getFirstname());
		person.setLastname(mPerson.getLastname());
		person.setBirthdate(mPerson.getBirthdate());

		return Person.updatePerson(person);
	}

	/*
	 * **Request #4:** *POST /person* should create a new person and return the
	 * newly created person with its assigned id (if a health profile is
	 * included, create also those measurements for the new person). The body of
	 * the request should contain a **Person resource** that follows the
	 * examples presented before, without an id (which should be generated after
	 * being created)
	 * 
	 * Data: application/xml
	 * 
	 * <person> <firstname>Hoi</firstname> <lastname>Nghiem</lastname>
	 * <birthdate>1990-01-01T00:00:00+01:00</birthdate> <healthprofile>
	 * <weight>78.0</weight> <height>1.67</height> </healthprofile> </person>
	 * 
	 * URL: http://localhost:5900/sdelab/person
	 */

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Person newPerson(MapPerson mPerson) throws IOException {
		System.out.println("Creating new person...");
		Person person = new Person();
		person.setName(mPerson.getFirstname());
		person.setLastname(mPerson.getLastname());
		person.setBirthdate(mPerson.getBirthdate());
		person.setEmail("");
		person.setUsername("");
		person = Person.savePerson(person);

		List<LifeStatus> lLf = new ArrayList<LifeStatus>();
		if (mPerson.gethProfile() != null) {

			List<MeasureDefinition> mds = MeasureDefinition.getAll();
			for (MeasureDefinition md : mds) {
				if (md.getIdMeasureDef() == 1) {
					LifeStatus ls = new LifeStatus();
					ls.setPerson(person);
					ls.setMeasureDefinition(md);
					ls.setValue(String.valueOf(mPerson.gethProfile().getWeight()));
					lLf.add(ls);
				}
				if (md.getIdMeasureDef() == 2) {
					LifeStatus ls = new LifeStatus();
					ls.setPerson(person);
					ls.setMeasureDefinition(md);
					ls.setValue(String.valueOf(mPerson.gethProfile().getHeight()));
					lLf.add(ls);
				}
			}

		}
		person.setLifeStatus(lLf);
		person = Person.updatePerson(person);
		return person;
	}

	/*
	 * **Request #5:** *DELETE /person/{id}* should delete the person identified
	 * by {id} from the system
	 * 
	 * URL: http://localhost:5900/sdelab/person/3
	 */

	@DELETE
	@Path("{personId}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public MapMessage deletePerson(@PathParam("personId") int id) {
		String content = "Can not completed deleting!";
		Person person = new Person();
		person.setIdPerson(id);
		Person.removePerson(person);
		content = "Deleting is completed!";
		MapMessage mm = new MapMessage();
		mm.setContent(content);
		return mm;
	}

	/*
	 * **Request #6:** *GET /person/{id}/{measureType}* should return the list
	 * of values (the history) of {measureType} (e.g. weight) for person
	 * identified by {id}
	 * 
	 * URL: http://localhost:5900/sdelab/person/1/weight
	 * 
	 * AND
	 * 
	 * Extra #3 (Request #11): GET
	 * /person/{id}/{measureType}?before={beforeDate}&after={afterDate} should
	 * return the history of {measureType} (e.g., weight) for person {id} in the
	 * specified range of date
	 * 
	 * URL:
	 * localhost:5900/sdelab/person/1/weight?before=2013-01-01&after=2016-11-05
	 *
	 */

	@GET
	@Path("{personId}/{measureType}")
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<MapHealthMeasureHistory> getHealthMeasureHistoryType(@PathParam("personId") int id,
			@PathParam("measureType") String measureType, @Context UriInfo ui) throws Exception {

		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		if (queryParams.size() == 0) {

			System.out.println("Getting list of Health Measure History Type...");
			List<HealthMeasureHistory> hmhs = HealthMeasureHistory.getAll();
			List<MapHealthMeasureHistory> mhmhs = new ArrayList<MapHealthMeasureHistory>();

			for (HealthMeasureHistory hmh : hmhs) {
				MapHealthMeasureHistory mhmh = new MapHealthMeasureHistory();
				if (hmh.getPerson().getIdPerson() == id) {
					if (hmh.getMeasureDefinition().getMeasureName().equalsIgnoreCase(measureType)) {
						mhmh.setValue(hmh.getValue());
						mhmh.setCreated(hmh.getTimestamp());
						mhmh.setMid(hmh.getIdMeasureHistory());
						mhmhs.add(mhmh);
					}
				}
			}

			return mhmhs;
		} else {
			System.out.println("Getting list of Health Measure History Type in the period of time...");

			DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
			Date before = null;
			Date after = null;

			before = df.parse(queryParams.getFirst("before"));
			after = df.parse(queryParams.getFirst("after"));
			System.out.println(before.toString());
			System.out.println(after.toString());

			List<HealthMeasureHistory> hmhs = HealthMeasureHistory.getAll();
			List<MapHealthMeasureHistory> mhmhs = new ArrayList<MapHealthMeasureHistory>();

			for (HealthMeasureHistory hmh : hmhs) {
				MapHealthMeasureHistory mhmh = new MapHealthMeasureHistory();
				if (hmh.getPerson().getIdPerson() == id) {
					if (hmh.getMeasureDefinition().getMeasureName().equalsIgnoreCase(measureType)) {
						Date timestamp = df.parse(hmh.getTimestamp());
						if (timestamp.after(before) && timestamp.before(after)) {
							mhmh.setValue(hmh.getValue());
							mhmh.setCreated(hmh.getTimestamp());
							mhmh.setMid(hmh.getIdMeasureHistory());
							mhmhs.add(mhmh);
						}
					}
				}
			}
			return mhmhs;
		}
	}

	/*
	 * **Request #7:** *GET /person/{id}/{measureType}/{mid}* should return the
	 * value of {measureType} (e.g. weight) identified by {mid} for person
	 * identified by {id}
	 * 
	 * URL: http://localhost:5900/sdelab/person/1/weight/1
	 */

	@GET
	@Path("{personId}/{measureType}/{mid}")
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public MapHealthMeasureHistory getHealthMeasureHistoryTypeAndMID(@PathParam("personId") int id,
			@PathParam("measureType") String measureType, @PathParam("mid") int mid) {
		System.out.println("Getting list of Health Measure History Type...");
		List<HealthMeasureHistory> hmhs = HealthMeasureHistory.getAll();
		MapHealthMeasureHistory mhmh = new MapHealthMeasureHistory();

		for (HealthMeasureHistory hmh : hmhs) {
			if (hmh.getPerson().getIdPerson() == id) {
				if (hmh.getMeasureDefinition().getMeasureName().equalsIgnoreCase(measureType)) {
					if (hmh.getIdMeasureHistory() == mid) {
						mhmh.setValue(hmh.getValue());
						mhmh.setCreated(hmh.getTimestamp());
						mhmh.setMid(hmh.getIdMeasureHistory());
					}
				}
			}
		}
		return mhmh;
	}

	/*
	 * **Request #8:** *POST /person/{id}/{measureType}* should save a new value
	 * for the {measureType} (e.g. weight) of person identified by {id} and
	 * archive the old value in the history. The body of the request should
	 * contain a **Measure resource** that follows the examples presented
	 * before, without an id (which should be generated after being created)
	 * 
	 * URL: http://localhost:5900/sdelab/person/1/weight
	 * 
	 * Data: application/xml
	 * 
	 * <measure> <value>68</value> <created>2015-06-07</created> </measure>
	 */

	@POST
	@Path("{personId}/{measureType}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Person updateMeasurement(MapHealthMeasureHistory mhmhs, @PathParam("personId") int id,
			@PathParam("measureType") String measureType) throws IOException {
		System.out.println("Creating new life status and store the old value into history...");

		HealthMeasureHistory hmh = new HealthMeasureHistory();
		// Get id of measure type in measure definition table
		List<MeasureDefinition> mds = MeasureDefinition.getAll();
		int idMD = 0;
		for (MeasureDefinition md : mds) {
			if (md.getMeasureName().equalsIgnoreCase(measureType)) {
				idMD = md.getIdMeasureDef();
				break;
			}
		}

		MeasureDefinition md = MeasureDefinition.getMeasureDefinitionById(idMD);
		hmh.setMeasureDefinition(md);

		Person person = Person.getPersonById(id);
		hmh.setPerson(person);

		for (LifeStatus ls : person.getLifeStatus()) {
			if (ls.getMeasureDefinition().getIdMeasureDef() == idMD) {
				hmh.setValue(ls.getValue());
			}
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String created = dateFormat.format(new Date());
		hmh.setTimestamp(created);

		if (hmh.getValue() != null)
			HealthMeasureHistory.saveHealthMeasureHistory(hmh);

		for (LifeStatus ls : person.getLifeStatus()) {
			if (ls.getMeasureDefinition().getIdMeasureDef() == idMD) {
				ls.setValue(String.valueOf(mhmhs.getValue()));
			}
		}

		person = Person.updatePerson(person);
		return person;
	}

	/*
	 * Extra #2 (Request #10): PUT /person/{id}/{measureType}/{mid} should
	 * update the value for the {measureType} (e.g., weight) identified by
	 * {mid}, related to the person identified by {id}
	 * 
	 * Data: application/xml
	 * 
	 * <measure><mid>1</mid><value>83</value><created>2012-12-27
	 * 23:00:00</created></measure>
	 */

	@PUT
	@Path("{personId}/{measureType}/{mid}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public MapHealthMeasureHistory updateHealthMeasureHistoryTypeAndMID(MapHealthMeasureHistory mhmhParam,
			@PathParam("personId") int id, @PathParam("measureType") String measureType, @PathParam("mid") int mid) {
		System.out.println("Getting list of Health Measure History Type...");

		HealthMeasureHistory hmhParam = new HealthMeasureHistory();
		hmhParam.setIdMeasureHistory(mid);

		List<MeasureDefinition> mds = MeasureDefinition.getAll();
		int idMD = 0;
		for (MeasureDefinition md : mds) {
			if (md.getMeasureName().equalsIgnoreCase(measureType)) {
				idMD = md.getIdMeasureDef();
				break;
			}
		}
		MeasureDefinition md = MeasureDefinition.getMeasureDefinitionById(idMD);
		hmhParam.setMeasureDefinition(md);

		hmhParam.setPerson(Person.getPersonById(id));
		hmhParam.setTimestamp(mhmhParam.getCreated());
		hmhParam.setValue(mhmhParam.getValue());

		HealthMeasureHistory.updateHealthMeasureHistory(hmhParam);

		List<HealthMeasureHistory> hmhs = HealthMeasureHistory.getAll();
		MapHealthMeasureHistory mhmh = new MapHealthMeasureHistory();

		for (HealthMeasureHistory hmh : hmhs) {
			if (hmh.getPerson().getIdPerson() == id) {
				if (hmh.getMeasureDefinition().getMeasureName().equalsIgnoreCase(measureType)) {
					if (hmh.getIdMeasureHistory() == mid) {
						mhmh.setValue(hmh.getValue());
						mhmh.setCreated(hmh.getTimestamp());
						mhmh.setMid(hmh.getIdMeasureHistory());
					}
				}
			}
		}

		return mhmh;
	}

}
