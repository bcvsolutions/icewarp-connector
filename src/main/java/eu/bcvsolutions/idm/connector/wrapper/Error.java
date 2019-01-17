package eu.bcvsolutions.idm.connector.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 * 
 * class for parsing error responses
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Error {
	@XmlAttribute
	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	} 
}
