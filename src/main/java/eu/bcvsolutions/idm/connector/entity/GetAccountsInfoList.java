package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 *
<iq sid="sidval">
<query xmlns="admin:iq:rpc">
	<commandname>getaccountsinfolist</commandname>
	<commandparams>
		<domainstr>stringval</domainstr>
		<filter>
			<namemask>stringval</namemask>
			<typemask>stringval</typemask>
		</filter>
		<offset>intval</offset>
		<count>intval</count>
	</commandparams>
</query>
</iq>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GetAccountsInfoList {
	@XmlElement
	protected String domainstr;
	@XmlElement
	protected Filter filter;
	@XmlElement
	protected String offset;
	@XmlElement
	protected String count;

	public String getDomainstr() {
		return domainstr;
	}

	public void setDomainstr(String domainstr) {
		this.domainstr = domainstr;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
}
