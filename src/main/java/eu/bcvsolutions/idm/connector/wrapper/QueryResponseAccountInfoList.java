package eu.bcvsolutions.idm.connector.wrapper;

import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoListResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Petr Hanák
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "query")
public class QueryResponseAccountInfoList {

	@XmlElement(name = "result")
	private GetAccountsInfoListResponse accountsInfoListResponse;

	public GetAccountsInfoListResponse getAccountsInfoListResponse() {
		return accountsInfoListResponse;
	}

	public void setAccountsInfoListResponse(GetAccountsInfoListResponse accountsInfoListResponse) {
		this.accountsInfoListResponse = accountsInfoListResponse;
	}
}
