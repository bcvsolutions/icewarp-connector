package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
<iq sid="f5bdb9bcd7b52397f2bd004c2abefb3f">
<query xmlns="admin:iq:rpc">
	<commandname>deleteaccounts</commandname>
	<commandparams>
		<domainstr>testdomain</domainstr>
		<accountlist>
			<classname>TPropertyStringList</classname>
			<val>
				<item>email@domain</item>
			</val>
		</accountlist>
		<leavedata>0</leavedata>
	</commandparams>
</query>
</iq>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class DeleteAccounts {
	@XmlElement
	String domainstr;
	@XmlElement
	String leavedata;
}
