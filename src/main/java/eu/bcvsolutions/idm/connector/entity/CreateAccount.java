package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CreateAccount")
public class CreateAccount {
	@XmlElement
	protected String name;
	@XmlElement
	protected String email;
	@XmlElement
	protected String displayemail;
	@XmlElement
	protected String accounttype;
	@XmlElement
	protected String accountstate;
	@XmlElement
	protected String admintype;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		// if display email is same as email
		setDisplayemail(email);
	}

	public String getDisplayemail() {
		return displayemail;
	}

	public void setDisplayemail(String displayemail) {
		this.displayemail = displayemail;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getAccountstate() {
		return accountstate;
	}

	public void setAccountstate(String accountstate) {
		this.accountstate = accountstate;
	}

	public String getAdmintype() {
		return admintype;
	}

	public void setAdmintype(String admintype) {
		this.admintype = admintype;
	}
}
