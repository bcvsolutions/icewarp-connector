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
	private List<Item> items;

	public String getClassname() {
		return classname;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
