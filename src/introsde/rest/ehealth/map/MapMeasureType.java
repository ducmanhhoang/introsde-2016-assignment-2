package introsde.rest.ehealth.map;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="measureTypes")
@XmlType(propOrder = { "measureType" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MapMeasureType {
	private List<String> measureType;
	
	public MapMeasureType() {
		
	}

	public List<String> getMeasureType() {
		return measureType;
	}

	public void setMeasureType(List<String> measureType) {
		this.measureType = measureType;
	}
	
	
}
