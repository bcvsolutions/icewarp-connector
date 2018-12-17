package eu.bcvsolutions.idm.connector.wrapper;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "iq")
public class IqResponse {
	@XmlAttribute
	private String sid;

	@XmlElementWrapper(name = "query")
	@XmlElement(name = "result")
	private List<String> result;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}
}
