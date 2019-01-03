package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 <item>
	 <name>stringval</name>
	 <email>stringval</email>
	 <displayemail>stringval</displayemail>
	 <accounttype>intval</accounttype>
	 <accountstate>
	 <classname>taccountstate</classname>
	 <state>enumval</state>
	 </accountstate>
	 <admintype>enumval</admintype>
	 <quota>
		 <classname>taccountquota</classname>
		 <mailboxsize>intval</mailboxsize>
		 <mailboxquota>intval</mailboxquota>
	 </quota>
	 <image>
		 <classname>taccountimage</classname>
		 <base64data>stringval</base64data>
		 <contenttype>stringval</contenttype>
	 </image>
 </item>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class AccountResponse {
	@XmlElement
	private String name;
	@XmlElement
	private String email;
	@XmlElement
	private String displayemail;
	@XmlElement
	private String accounttype;
	@XmlElement
	private PropertyState accountstate;
	@XmlElement
	private String admintype;
	@XmlElement
	private Quota quota;

	// quota and image???

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

	public String getAdmintype() {
		return admintype;
	}

	public void setAdmintype(String admintype) {
		this.admintype = admintype;
	}

	public PropertyState getAccountstate() {
		return accountstate;
	}

	public void setAccountstate(PropertyState accountstate) {
		this.accountstate = accountstate;
	}

	public Quota getQuota() {
		return quota;
	}

	public void setQuota(Quota quota) {
		this.quota = quota;
	}
}
