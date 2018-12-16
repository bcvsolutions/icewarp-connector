package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 *
<iq sid="sidval">
<query xmlns="admin:iq:rpc">
	<commandname>setaccountpassword</commandname>
	<commandparams>
		<accountemail>stringval</accountemail>
		<authtype>enumval</authtype>
		<password>stringval</password>
		<digest>stringval</digest>
		<ignorepolicy>enumval</ignorepolicy>
	</commandparams>
</query>
</iq>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SetAccountPassword {
	@XmlElement
	private String accountemail;
	@XmlElement
	private String authtype;
	@XmlElement
	private String password;
	@XmlElement
	private String digest;
	@XmlElement
	private String ignorepolicy;

	public String getAccountemail() {
		return accountemail;
	}

	public void setAccountemail(String accountemail) {
		this.accountemail = accountemail;
	}

	public String getAuthtype() {
		return authtype;
	}

	public void setAuthtype(String authtype) {
		this.authtype = authtype;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getIgnorepolicy() {
		return ignorepolicy;
	}

	public void setIgnorepolicy(String ignorepolicy) {
		this.ignorepolicy = ignorepolicy;
	}
}
