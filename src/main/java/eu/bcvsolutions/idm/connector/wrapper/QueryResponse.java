package eu.bcvsolutions.idm.connector.wrapper;

import eu.bcvsolutions.idm.connector.entity.AccountResponse;
import eu.bcvsolutions.idm.connector.entity.AuthenticateResponse;
import eu.bcvsolutions.idm.connector.entity.GetAccountPropertiesResponse;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoListResponse;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Petr Hanák
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "query")
public class QueryResponse {
	private AuthenticateResponse authenticateResponse;
//	private GetAccountsInfoListResponse getAccountsInfoListResponse;
	private GetAccountPropertiesResponse getAccountPropertiesResponse;

//	@XmlElement(name = "result")
//	private Object result;

	@XmlElement(name = "result")
	private GetAccountsInfoListResponse getAccountsInfoListResponse;

	public GetAccountsInfoListResponse getGetAccountsInfoListResponse() {
		return getAccountsInfoListResponse;
	}

	public void setGetAccountsInfoListResponse(GetAccountsInfoListResponse getAccountsInfoListResponse) {
		this.getAccountsInfoListResponse = getAccountsInfoListResponse;
	}

//	public Object getResult() {
//		return result;
//	}
//
//	public void setResult(Object result) {
//		this.result = result;
//	}

}
