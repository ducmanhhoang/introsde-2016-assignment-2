package introsde.rest.ehealth.map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="healthprofile")
@XmlType(propOrder = {"weight", "height"})
@XmlAccessorType(XmlAccessType.FIELD)
public class MapHealthProfile {
	private double weight; // in kg
	private double height; // in m

	public MapHealthProfile(double weight, double height) {
		this.weight = weight;
		this.height = height;
	}

	public MapHealthProfile() {
		this.weight = 85.5;
		this.height = 1.72;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
}
