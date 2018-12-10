package eu.bcvsolutions.idm.connector.communication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.bcvsolutions.idm.connector.IceWarpConfiguration;
import eu.bcvsolutions.idm.connector.IceWarpConnector;
import eu.bcvsolutions.idm.connector.entity.CreateAccount;
import eu.bcvsolutions.idm.connector.wrapper.Iq;
import eu.bcvsolutions.idm.connector.wrapper.IqResponse;
import eu.bcvsolutions.idm.connector.wrapper.Query;
import eu.bcvsolutions.idm.connector.entity.Authenticate;
import eu.bcvsolutions.idm.connector.entity.AuthenticateResponse;
import eu.bcvsolutions.idm.connector.entity.Filter;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoList;
import eu.bcvsolutions.idm.connector.wrapper.QueryResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectionFailedException;

/**
 * @author Petr Hanák
 */
public class Connection {

	private static final Log log = Log.getLog(IceWarpConnector.class);

	private IceWarpConfiguration configuration;

	private String sid = "";

	public Connection(IceWarpConfiguration configuration) {
		this.configuration = configuration;
	}

	private final String AUTHENTICATE="<iq>\n" +
			"<query xmlns=\"admin:iq:rpc\">\n" +
			"  <commandname>authenticate</commandname>\n" +
			"  <commandparams>\n" +
			"    <authtype>0</authtype>\n" +
			"    <email>idmadmin</email>\n" +
			"    <password>vdcctr0y</password>\n" +
			"    <persistentlogin>1</persistentlogin>\n" +
			"  </commandparams>\n" +
			"</query>\n" +
			"</iq>";

	public void getAuthentication() {
		// send request for authentication and set sid to variable for later
		Authenticate authenticate = new Authenticate();
		authenticate.setEmail(configuration.getUsername());
		authenticate.setPassword(configuration.getStringPassword());
		authenticate.setPersistentlogin("1");
		authenticate.setAuthtype("0");

		Query query = new Query();
		query.setCommand(authenticate);
		Iq iq = new Iq();
		iq.setQuery(query);

		try {
			log.info(getXMLBody(iq));
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getXMLBody(iq));
			if (response.getStatus() != 200) {
				throw new ConnectionFailedException("Can't connect to system, return code " + response.getStatus());
			}
			log.info(response.getBody());

			AuthenticateResponse authenticateResponse = new AuthenticateResponse();
			QueryResponse queryResponse = new QueryResponse();
			queryResponse.setResult(authenticateResponse);
			IqResponse iqResponse = new IqResponse();
			iqResponse.setQueryResponse(queryResponse);

			iqResponse = (IqResponse) getObject(response.getBody(), iqResponse);
			log.info("sid: " + iqResponse.getSid());
			this.sid = iqResponse.getSid();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AuthenticateResponse xmlTest() {
		Authenticate authenticate = new Authenticate();
		authenticate.setAuthtype("0");
		authenticate.setEmail(configuration.getUsername());
		authenticate.setPassword(configuration.getStringPassword());
		authenticate.setPersistentlogin("1");

		Filter filter = new Filter();
		filter.setNamemask("ahoj");
		filter.setTypemask("7");
		GetAccountsInfoList getAccountsInfoList = new GetAccountsInfoList();
		getAccountsInfoList.setDomainstr(configuration.getDomain());
		getAccountsInfoList.setCount("300");
		getAccountsInfoList.setOffset("0");
		getAccountsInfoList.setFilter(filter);

		CreateAccount createAccount = new CreateAccount();
		createAccount.setName("Hanák Petr");
		createAccount.setAccounttype("0");
		createAccount.setAdmintype("0");
		createAccount.setAccountstate("1");
		createAccount.setEmail("petr.hanak@bcvsolutions.eu");

		Query query = new Query();

		Iq iq = new Iq();
		iq.setSid("insert authentication sidval");
		iq.setQuery(query);

		try {
			query.setCommand(getAccountsInfoList);
			log.info(getXMLBody(iq));
			query.setCommand(authenticate);
			log.info(getXMLBody(iq));
			query.setCommand(createAccount);
			log.info(getXMLBody(iq));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
//		try {
//			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getXMLBody(authenticateTest));
//			if (response.getStatus() != 200) {
//				throw new ConnectionFailedException("Can't connect to system, return code " + response.getStatus());
//			}
//			return (AuthenticateResponse) getObject(response.getBody(), new AuthenticateResponse());
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	private Object getObject(String xml, Object response) throws JAXBException {
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

		JAXBContext jaxbContext = JAXBContext.newInstance(response.getClass());
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return unmarshaller.unmarshal(inputStream);
	}

	// TODO try to make all entities to inherit some class and use it here instead of Object?
	private String getXMLBody(Object request) throws JAXBException {
		OutputStream stream = new ByteArrayOutputStream();

		JAXBContext jaxbContext = JAXBContext.newInstance(request.getClass());
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(request, stream);
		return stream.toString();
	}

	private HttpResponse<String> post(String url, String body) {
		try {
			HttpResponse<String> response = Unirest.post(url)
					.header("content-type", "text/xml")
					.body(body)
					.asString();
			return response;
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getPassword(GuardedString password) {
		GuardedStringAccessor accessor = new GuardedStringAccessor();
		password.access(accessor);
		char[] result = accessor.getArray();
		return new String(result);
	}
}
