package eu.bcvsolutions.idm.connector.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "iq")
public class IqResponse {
	// possible types of responses are result, error
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String sid;

	@XmlElement(name = "query")
	private QueryResponse queryResponse;

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public QueryResponse getQueryResponse() {
		return queryResponse;
	}

	public void setQueryResponse(QueryResponse queryResponse) {
		this.queryResponse = queryResponse;
	}
}
