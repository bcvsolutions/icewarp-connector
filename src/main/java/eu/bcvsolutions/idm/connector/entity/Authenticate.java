package eu.bcvsolutions.idm.connector.entity;

import eu.bcvsolutions.idm.connector.wrapper.Query;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
<iq sid="sidval">
<query xmlns="admin:iq:rpc">
	<commandname>authenticate</commandname>
	<commandparams>
		<authtype>enumval</authtype>
		<email>stringval</email>
		<password>stringval</password>
		<digest>stringval</digest>
		<persistentlogin>enumval</persistentlogin>
	</commandparams>
</query>
</iq>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Authenticate {
	@XmlElement
	private String authtype;
	@XmlElement
	private String email;
	@XmlElement
	private String password;
	@XmlElement
	private String persistentlogin;

	public String getAuthtype() {
		return authtype;
	}

	public void setAuthtype(String authtype) {
		this.authtype = authtype;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPersistentlogin() {
		return persistentlogin;
	}

	public void setPersistentlogin(String persistentlogin) {
		this.persistentlogin = persistentlogin;
	}
}
