package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
<propertyval>
	<classname>propertyClassName</classname>
	<val>value</val>
</propertyval>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PropertyVal {
	@XmlElement
	private String classname;
	@XmlElement
	private String val;

	public PropertyVal() {}

	public PropertyVal(String className, String val) {
		this.classname = className;
		this.val = val;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}
}
