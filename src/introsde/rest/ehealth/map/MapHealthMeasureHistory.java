package introsde.rest.ehealth.map;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "measure")	
@XmlType(propOrder = { "mid", "value", "created"})
@XmlAccessorType(XmlAccessType.FIELD)
public class MapHealthMeasureHistory {
	private int mid;		
	private String value;
	private String created;
	
	public MapHealthMeasureHistory() {
		
	}

	public MapHealthMeasureHistory(int mid, String value, String created) {
		super();
		this.mid = mid;
		this.value = value;
		this.created = created;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
}
