package introsde.rest.ehealth.map;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "person")	
@XmlType(propOrder = {"personId", "firstname", "lastname", "birthdate", "hProfile" })
@XmlAccessorType(XmlAccessType.FIELD)
public class MapPerson {
	private int personId;
	private String firstname;		
	private String lastname;	
	private String birthdate;
	@XmlElement(name="healthprofile")
	private MapHealthProfile hProfile;
	
	public MapPerson() {
		
	}

	public MapPerson(int personId, String firstname, String lastname, String birthdate, MapHealthProfile hProfile) {
		super();
		this.personId = personId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthdate = birthdate;
		this.hProfile = hProfile;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public MapHealthProfile gethProfile() {
		return hProfile;
	}

	public void sethProfile(MapHealthProfile hProfile) {
		this.hProfile = hProfile;
	}
	
	
}
