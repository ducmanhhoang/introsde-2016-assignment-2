package introsde.rest.ehealth.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import introsde.rest.ehealth.map.MapHealthProfile;
import introsde.rest.ehealth.map.MapMeasureType;
import introsde.rest.ehealth.map.MapPerson;
import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.MeasureDefinition;
import introsde.rest.ehealth.model.Person;

@Stateless
@LocalBean
@Path("/measureTypes")
public class MeasureResource {
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
	
	
	// **Request #9:** *GET /measureTypes* should return the list of measures your model supports in the following formats:
	// URL: http://localhost:5900/sdelab/measureType
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public MapMeasureType getPersonsBrowser() {
		System.out.println("Getting list of Person...");
		List<MeasureDefinition> mds = MeasureDefinition.getAll();
		List<String> mmts = new ArrayList<String>();
		for (MeasureDefinition md : mds) {
			mmts.add(md.getMeasureName());
		}
		MapMeasureType mt = new MapMeasureType();
		mt.setMeasureType(mmts);
		return mt;
	}
}
