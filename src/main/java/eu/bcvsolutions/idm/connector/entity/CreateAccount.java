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
	private String domainstr;
	@XmlElementWrapper(name = "accountproperties")
	@XmlElement(name = "item")
	private List<Item> items;


	public String getDomainStr() {
		return domainstr;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void setDomainStr(String domainStr) {
		this.domainstr = domainStr;
	}
}
