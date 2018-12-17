package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GetAccountMemberInfoList {
	// alias or email of group
	@XmlElement(name = "groupUid")
	private String groupUid;

	public String getGroupUid() {
		return groupUid;
	}

	public void setGroupUid(String groupUid) {
		this.groupUid = groupUid;
	}
}
