package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 * property for state setting
 *
<propertyval>
<classname>propertyClassName</classname>
<state>value</state>
</propertyval>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "propertyval")
public class PropertyState {
	@XmlElement
	private final String classname = "TAccountState";
	@XmlElement
	private String state;

	public PropertyState() {}

	public PropertyState(String state) {
		this.state = state;
	}

	public String getClassname() {
		return classname;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
