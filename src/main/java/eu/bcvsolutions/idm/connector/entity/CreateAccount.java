package eu.bcvsolutions.idm.connector.entity;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
<iq sid="sidval">
<query xmlns="admin:iq:rpc">
<commandname>createaccount</commandname>
	<commandparams>
		<domainstr>stringval</domainstr>
		<accountproperties>
			<items>
				<apiproperty>
					<propname>stringval</propname>
				</apiproperty>
				<propertyval>
					<classname>tpropertynovalue</classname>
					<val></val>
				</propertyval>
				<propertyright>enumval</propertyright>
			</items>
			<items>
				<apiproperty>
					<propname>stringval</propname>
				</apiproperty>
				<propertyval>
					<classname>tpropertystringlist</classname>
				<val>
					<items>item1</items>
					<items>item2</items>
				</val>
				</propertyval>
				<propertyright>enumval</propertyright>
			</items>
		</accountproperties>
	</commandparams>
</query>
</iq>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CreateAccount")
public class CreateAccount {
	@XmlElement
	private String domainStr;
	@XmlElementWrapper(name = "accountproperties")
	@XmlElement(name = "item")
	private List<Item> items;

	private String name;
	private String email;
	private String displayemail;
	private String accounttype;
	private String accountstate;
	private String admintype;

	public String getDomainStr() {
		return domainStr;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void setDomainStr(String domainStr) {
		this.domainStr = domainStr;
	}

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
