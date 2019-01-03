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
<members>
	<classname>tpropertymembers</classname>
	<val>
		<item>
			<classname>tpropertymember</classname>
			<val>email@ofmembertoadd</val>
			<default>enumval</default>
			<recieve>enumval</recieve>
			<post>enumval</post>
			<digest>enumval</digest>
			<params>stringval</params>
		</item>
	</val>
</members>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Members {
	@XmlElement
	private final String classname = "TPropertyMembers";
	@XmlElementWrapper(name = "val")
	@XmlElement(name = "item")
	private List<MemberItem> items;

	public String getClassname() {
		return classname;
	}

	public List<MemberItem> getItems() {
		return items;
	}

	public void setItems(List<MemberItem> items) {
		this.items = items;
	}
}
