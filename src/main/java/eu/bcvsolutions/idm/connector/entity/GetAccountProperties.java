package eu.bcvsolutions.idm.connector.entity;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 *
<iq sid="sidval">
<query xmlns="admin:iq:rpc">
	<commandname>getaccountproperties</commandname>
	<commandparams>
		<accountemail>stringval</accountemail>
		<accountpropertylist>
			<item>
			<propname>stringval</propname>
			</item>
			<item>
			<propname>stringval</propname>
			</item>
		</accountpropertylist>
		<accountpropertyset>enumval</accountpropertyset>
	</commandparams>
</query>
</iq>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GetAccountProperties {
	@XmlElement
	private String accountemail;
	@XmlElement(name = "accountpropertylist")
	private List<Item> items;
	@XmlElement
	private String accountpropertyset;

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

	public String getAccountpropertyset() {
		return accountpropertyset;
	}

	public void setAccountpropertyset(String accountpropertyset) {
		this.accountpropertyset = accountpropertyset;
	}
}
