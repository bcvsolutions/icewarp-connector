package eu.bcvsolutions.idm.connector.entity;

import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 *
<accountlist>
	<classname>TPropertyStringList</classname>
	<val>
		<item>email1@domain</item>
		<item>email2@domain</item>
	</val>
</accountlist>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class AccountList {
	@XmlElement
	private final String classname = "TPropertyStringList";
	@XmlElementWrapper(name = "val")
	@XmlElement(name = "item")
	private List<String> items;

	public void addAccounts(String... emails) {
		items.addAll(Arrays.asList(emails));
	}

	public String getClassname() {
		return classname;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}
}
