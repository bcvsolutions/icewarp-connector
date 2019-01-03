package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 * Delete group members
 *
<iq sid="sidval">
<query xmlns="admin:iq:rpc">
	<commandname>deleteaccountmembers</commandname>
	<commandparams>
		<accountemail>stringval</accountemail>
		<members>
			<classname>tpropertymembers</classname>
				<val>
					<item>
						<classname>tpropertymember</classname>
						<val>stringval</val>
						<default>enumval</default>
						<recieve>enumval</recieve>
						<post>enumval</post>
						<digest>enumval</digest>
						<params>stringval</params>
					</item>
					<item>
						<classname>tpropertymember</classname>
						<val>stringval</val>
						<default>enumval</default>
						<recieve>enumval</recieve>
						<post>enumval</post>
						<digest>enumval</digest>
						<params>stringval</params>
					</item>
				</val>
		</members>
	</commandparams>
</query>
</iq>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DeleteAccountMembers")
public class DeleteAccountMembers {
	@XmlElement(name = "accountemail")
	private String groupEmail;
	@XmlElement
	private Members members;

	public String getGroupEmail() {
		return groupEmail;
	}

	public void setGroupEmail(String groupEmail) {
		this.groupEmail = groupEmail;
	}

	public Members getMembers() {
		return members;
	}

	public void setMembers(Members members) {
		this.members = members;
	}
}
