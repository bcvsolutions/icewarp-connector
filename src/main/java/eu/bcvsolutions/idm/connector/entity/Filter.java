package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 <filter>
 	<namemask>stringval</namemask>
 	<typemask>stringval</typemask>
 </filter>
 */
@XmlRootElement
public class Filter {
	@XmlElement
	private String namemask;
	@XmlElement
	private String typemask;

	public void setNamemask(String namemask) {
		this.namemask = namemask;
	}

	public void setTypemask(String typemask) {
		this.typemask = typemask;
	}

	public String getNamemaskAttribute() {
		return namemask;
	}

	public String getTypemaskAttribute() {
		return typemask;
	}
}
