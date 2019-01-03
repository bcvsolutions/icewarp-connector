package eu.bcvsolutions.idm.connector.wrapper;

import eu.bcvsolutions.idm.connector.entity.GetAccountMemberInfoListResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Petr Hanák
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "query")
public class QueryResponseMemberInfoList {

	@XmlElement(name = "result")
	private GetAccountMemberInfoListResponse getAccountMemberInfoListResponse;

	public GetAccountMemberInfoListResponse getGetAccountMemberInfoListResponse() {
		return getAccountMemberInfoListResponse;
	}

	public void setGetAccountMemberInfoListResponse(GetAccountMemberInfoListResponse getAccountMemberInfoListResponse) {
		this.getAccountMemberInfoListResponse = getAccountMemberInfoListResponse;
	}
}
