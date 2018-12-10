package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 <item>
	 <name>stringval</name>
	 <email>stringval</email>
	 <displayemail>stringval</displayemail>
	 <accounttype>intval</accounttype>
	 <accountstate>
	 <classname>taccountstate</classname>
	 <state>enumval</state>
	 </accountstate>
	 <admintype>enumval</admintype>
	 <quota>
		 <classname>taccountquota</classname>
		 <mailboxsize>intval</mailboxsize>
		 <mailboxquota>intval</mailboxquota>
	 </quota>
	 <image>
		 <classname>taccountimage</classname>
		 <base64data>stringval</base64data>
		 <contenttype>stringval</contenttype>
	 </image>
 </item>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item", propOrder = {
		"name",
		"email",
		"displayemail",
		"accounttype",
		"accountstate",
		"admintype"
})
public class AccountResponse {
	@XmlElement
	protected String name;
	@XmlElement
	protected String email;
	@XmlElement
	protected String displayemail;
	@XmlElement
	protected String accounttype;
	@XmlElement
	protected String accountstate;
	@XmlElement
	protected String admintype;

	// quota and image???
}
