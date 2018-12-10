package eu.bcvsolutions.idm.connector.wrapper;

import eu.bcvsolutions.idm.connector.entity.AuthenticateResponse;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoListResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "query")
public class QueryResponse {
	private AuthenticateResponse authenticateResponse;
	private GetAccountsInfoListResponse getAccountsInfoListResponse;

	@XmlElement
	private Object result;

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
