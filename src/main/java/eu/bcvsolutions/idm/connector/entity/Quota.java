package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Roman Kučera
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Quota {

	@XmlElement
	private final String classname = "TAccountQuota";
	@XmlElement
	private String mailboxsize;
	@XmlElement
	private String mailboxquota;

	public String getMailboxsize() {
		return mailboxsize;
	}

	public void setMailboxsize(String mailboxsize) {
		this.mailboxsize = mailboxsize;
	}

	public String getMailboxquota() {
		return mailboxquota;
	}

	public void setMailboxquota(String mailboxquota) {
		this.mailboxquota = mailboxquota;
	}

	public String getClassname() {
		return classname;
	}
}
