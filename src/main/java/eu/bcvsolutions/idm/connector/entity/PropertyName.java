package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 * property for firstname setting
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
	@XmlElement(name = "name")
	private String firstname;
	@XmlElement
	private String surname;

	public PropertyName() {
	}

	public PropertyName(String name, String surname) {
		this.firstname = name;
		this.surname = surname;
	}

	public String getClassname() {
		return classname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}
