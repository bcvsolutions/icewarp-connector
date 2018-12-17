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
public class IqResponseMemberInfoList {
	@XmlAttribute
	private String sid;

	@XmlElement(name = "query")
	private QueryResponseMemberInfoList queryResponse;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public QueryResponseMemberInfoList getQueryResponse() {
		return queryResponse;
	}

	public void setQueryResponse(QueryResponseMemberInfoList queryResponse) {
		this.queryResponse = queryResponse;
	}
}
