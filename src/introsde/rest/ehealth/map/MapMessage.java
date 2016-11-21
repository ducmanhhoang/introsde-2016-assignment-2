package introsde.rest.ehealth.map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="message")
@XmlType(propOrder = { "content" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MapMessage {
	private String content;
	
	public MapMessage() {
		
	}

	public MapMessage(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
