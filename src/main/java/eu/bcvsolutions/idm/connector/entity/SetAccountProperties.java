package eu.bcvsolutions.idm.connector.entity;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 *
<iq sid="ab2692fa4ff05dadedafe53838cc5133">
<query xmlns="admin:iq:rpc">
	<commandname>setaccountproperties</commandname>
	<commandparams>
		<accountemail>email@testdomain</accountemail>
		<propertyvaluelist>
			<item>
				<apiproperty>
					<propname>A_State</propname>
				</apiproperty>
				<propertyval>
					<classname>TAccountState</classname>
					<state>0</state>
				</propertyval>
				<propertyright>2</propertyright>
			</item>
		</propertyvaluelist>
	</commandparams>
</query>
</iq>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SetAccountProperties")
public class SetAccountProperties {
	@XmlElement
	private String accountemail;
	@XmlElementWrapper(name = "propertyvaluelist")
	@XmlElement(name = "item")
	private List<Item> items;

	public String getAccountemail() {
		return accountemail;
	}

	public void setAccountemail(String accountemail) {
		this.accountemail = accountemail;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
