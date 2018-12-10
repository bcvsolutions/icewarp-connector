package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 *
 * gets the sidval for authentication when using methods
 *
<iq sid="sidval" type="result">
<query xmlns="admin:iq:rpc">
	<result>intval</result>
</query>
</iq>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "authenticate")
public class AuthenticateResponse {

	@XmlElement
	private int result;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
}
