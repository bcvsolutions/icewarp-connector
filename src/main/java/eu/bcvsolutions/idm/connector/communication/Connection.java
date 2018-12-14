package eu.bcvsolutions.idm.connector.communication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.bcvsolutions.idm.connector.IceWarpConfiguration;
import eu.bcvsolutions.idm.connector.IceWarpConnector;
import eu.bcvsolutions.idm.connector.entity.Account;
import eu.bcvsolutions.idm.connector.entity.CreateAccount;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoListResponse;
import eu.bcvsolutions.idm.connector.entity.Item;
import eu.bcvsolutions.idm.connector.entity.PropertyName;
import eu.bcvsolutions.idm.connector.entity.PropertyState;
import eu.bcvsolutions.idm.connector.entity.PropertyVal;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectionFailedException;
import org.identityconnectors.framework.common.objects.Attribute;

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
			"  <commandparams xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"authenticate\">\n" +
			"    <authtype>0</authtype>\n" +
			"    <email>idmadmin</email>\n" +
			"    <password>vdcctr0y</password>\n" +
			"    <persistentlogin>1</persistentlogin>\n" +
			"  </commandparams>\n" +
			"</query>\n" +
			"</iq>";

	public void authenticate() {
		// send request for authentication and set sid to variable for later
		Authenticate authenticate = new Authenticate();
		authenticate.setEmail(configuration.getUsername());
		authenticate.setPassword(configuration.getStringPassword());
		authenticate.setPersistentlogin("1");
		authenticate.setAuthtype("0");

		try {
			log.info("vygenerovane telo\n" + getWrappedXml(authenticate));
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(authenticate));
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
			log.info("this.sid: " + this.sid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getAccountsInfoList() {
		Filter filter = new Filter();
		filter.setTypemask("7");
		GetAccountsInfoList getAccountsInfoList = new GetAccountsInfoList();
		getAccountsInfoList.setDomainstr(configuration.getDomain());
		getAccountsInfoList.setCount("20");
		getAccountsInfoList.setFilter(filter);

		try {
			log.info("vygenerovane telo\n" + getWrappedXml(getAccountsInfoList));
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(getAccountsInfoList));
			if (response.getStatus() != 200) {
				throw new ConnectionFailedException("Can't connect to system, return code " + response.getStatus());
			}
			log.info(response.getBody());

			GetAccountsInfoListResponse getAccountsInfoListResponse = new GetAccountsInfoListResponse();
			QueryResponse queryResponse = new QueryResponse();
			queryResponse.setResult(getAccountsInfoListResponse);
			IqResponse iqResponse = new IqResponse();
			iqResponse.setQueryResponse(queryResponse);

			iqResponse = (IqResponse) getObject(response.getBody(), iqResponse);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void createAccount(Set<Attribute> createAttributes) {
		Account account = new Account();
		createAttributes.forEach(attribute -> {
			if (attribute.getName().equals(IceWarpConnector.NAME)) {
				account.setObjectId(String.valueOf(attribute.getValue().get(0)));
				account.setUsername(String.valueOf(attribute.getValue().get(0)));
				return;
			}
			if (attribute.getName().equals(IceWarpConnector.EMAIL)) {
				account.setEmail(String.valueOf(attribute.getValue().get(0)));
				return;
			}
			if (attribute.getName().equals(IceWarpConnector.FIRST_NAME)) {
				account.setFirstName(String.valueOf(attribute.getValue().get(0)));
				return;
			}
			if (attribute.getName().equals(IceWarpConnector.LAST_NAME)) {
				account.setLastName(String.valueOf(attribute.getValue().get(0)));
				return;
			}
			if (attribute.getName().equals(IceWarpConnector.ACCOUNT_STATE)) {
				if (String.valueOf(attribute.getValue().get(0)).equals("true")) {
					account.setAccountstate("0");
				} else {
					account.setAccountstate("1");
				}
				return;
			}
			if (attribute.getName().equals(IceWarpConnector.ADMIN_TYPE)) {
				account.setAdmintype(String.valueOf(attribute.getValue().get(0)));
				return;
			} else {
				account.setAdmintype("0");
			}

			// roles
			if (attribute.getName().equals(IceWarpConnector.GROUP_ALIAS)) {
				List<Object> list = attribute.getValue();
				account.setGroupAlias(String.valueOf(attribute.getValue().get(0)));
			}
			if (attribute.getName().equals(IceWarpConnector.GROUP_NAME)) {
				account.setGroupName(String.valueOf(attribute.getValue().get(0)));
			}
		});

		if (configuration.getAccountType().toLowerCase().equals(IceWarpConnector.USER)) {
			account.setAccounttype("0");
		} else if (configuration.getAccountType().toLowerCase().equals(IceWarpConnector.GROUP)) {
			account.setAccounttype("7");
		}

		CreateAccount createAccount = new CreateAccount();
		createAccount.setDomainStr(configuration.getDomain());
		Item name = new Item("A_Name", new PropertyName(account.getFirstName(), account.getLastName()));
		Item type = new Item("U_Type", new PropertyVal("NativeInt", account.getAccounttype()));
		Item email = new Item("U_Mailbox", new PropertyVal("TPropertyString", account.getUsername()));
		Item accountState = new Item("A_State", new PropertyState(account.getAccountstate()));
		Item adminType = new Item("A_AdminType", new PropertyVal("TPropertyString", account.getAdmintype()));
		List<Item> items = Arrays.asList(name, type, email, adminType, accountState);
		createAccount.setItems(items);

		try {
			log.info(getWrappedXml(createAccount));
//			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(createAccount));
//			if (response.getStatus() != 200) {
//				throw new ConnectionFailedException("Can't connect to system, return code " + response.getStatus());
//			}
//			log.info(response.getBody());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public String getWrappedXml(Object request) throws JAXBException {
		Query query = new Query();
		query.setCommand(request);
		Iq iq = new Iq();
		iq.setSid(this.sid);
		iq.setQuery(query);
		return getXMLBody(iq);
	}

	private Object getObject(String xml, Object response) throws JAXBException {
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

		JAXBContext jaxbContext = JAXBContext.newInstance(response.getClass());
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return unmarshaller.unmarshal(inputStream);
	}

	// TODO try to make all entities to inherit some class and use it here instead of Object?
	public String getXMLBody(Object request) throws JAXBException {
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
