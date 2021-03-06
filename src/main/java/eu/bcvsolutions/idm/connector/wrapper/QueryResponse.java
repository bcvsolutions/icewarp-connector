package eu.bcvsolutions.idm.connector.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Petr Hanák
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "query")
public class QueryResponse {
	@XmlElement
	private String result;
	@XmlElement
	private Error error;
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
}
