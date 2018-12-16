package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 * property for name setting
 *
<propertyval>
	<classname>propertyClassName</classname>
	<state>value</state>
</propertyval>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PropertyName {
	@XmlElement
	private final String classname = "TAccountName";
	@XmlElement
	private String name;
	@XmlElement
	private String surname;

	public PropertyName() {
	}

	public PropertyName(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public String getClassname() {
		return classname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}
