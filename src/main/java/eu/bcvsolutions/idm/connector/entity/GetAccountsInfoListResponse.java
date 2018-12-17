package eu.bcvsolutions.idm.connector.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 <iq sid="sidval" type="result">
 <query xmlns="admin:iq:rpc">
	<result>
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
	</result>
 </query>
 </iq>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "result")
public class GetAccountsInfoListResponse {
	@XmlElement(name = "item")
	private List<AccountResponse> accounts;

	public List<AccountResponse> getAccounts() {
		if (accounts == null) {
			accounts = new ArrayList<AccountResponse>();
		}
		return accounts;
	}

	public void setAccounts(List<AccountResponse> accounts) {
		this.accounts = accounts;
	}
}
