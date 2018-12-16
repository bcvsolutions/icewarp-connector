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
 * item object for setting properties
 *
<item>
	<apiproperty>
		<propname>stringval</propname>
	</apiproperty>
	<propertyval>
		<classname>propertyClassName</classname>
		<val>value</val>
	</propertyval>
	<propertyright>enumval</propertyright>
</item>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Item {
	@XmlElementWrapper(name = "apiproperty")
	@XmlElement(name = "propname")
	private List<String> propname;
	@XmlElement
	private PropertyVal propertyval;
	@XmlElement(name = "propertyval")
	private PropertyState propertystate;
	@XmlElement(name = "propertyval")
	private PropertyName propertyname;
	@XmlElement
	private String propertyright;

	public Item() {}

	public Item(String propname, PropertyVal propertyval) {
		this.propname = Arrays.asList(propname);
		this.propertyval = propertyval;
		this.propertyright = "2";
	}

	public Item(String propname, PropertyState propertyState) {
		this.propname = Arrays.asList(propname);
		this.propertystate = propertyState;
		this.propertyright = "2";
	}

	public Item(String propname, PropertyName propertyName) {
		this.propname = Arrays.asList(propname);
		this.propertyname = propertyName;
		this.propertyright = "2";
	}

	public List<String> getPropname() {
		return propname;
	}

	public void setPropname(List<String> propname) {
		this.propname = propname;
	}

	public PropertyVal getPropertyval() {
		return propertyval;
	}

	public void setPropertyval(PropertyVal propertyval) {
		this.propertyval = propertyval;
	}

	public String getPropertyright() {
		return propertyright;
	}

	public void setPropertyright(String propertyright) {
		this.propertyright = propertyright;
	}
}
