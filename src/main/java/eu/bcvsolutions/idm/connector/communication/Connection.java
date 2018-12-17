package eu.bcvsolutions.idm.connector.communication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.bcvsolutions.idm.connector.IceWarpConfiguration;
import eu.bcvsolutions.idm.connector.IceWarpConnector;
import eu.bcvsolutions.idm.connector.entity.Account;
import eu.bcvsolutions.idm.connector.entity.AccountList;
import eu.bcvsolutions.idm.connector.entity.AddAccountMembers;
import eu.bcvsolutions.idm.connector.entity.CreateAccount;
import eu.bcvsolutions.idm.connector.entity.DeleteAccounts;
import eu.bcvsolutions.idm.connector.entity.GetAccountMemberInfoList;
import eu.bcvsolutions.idm.connector.entity.GetAccountMemberInfoListResponse;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoListResponse;
import eu.bcvsolutions.idm.connector.entity.Item;
import eu.bcvsolutions.idm.connector.entity.MemberItem;
import eu.bcvsolutions.idm.connector.entity.Members;
import eu.bcvsolutions.idm.connector.entity.PropertyName;
import eu.bcvsolutions.idm.connector.entity.PropertyState;
import eu.bcvsolutions.idm.connector.entity.PropertyVal;
import eu.bcvsolutions.idm.connector.entity.SetAccountProperties;
import eu.bcvsolutions.idm.connector.wrapper.Iq;
import eu.bcvsolutions.idm.connector.wrapper.IqResponse;
import eu.bcvsolutions.idm.connector.wrapper.IqResponseAccountInfoList;
import eu.bcvsolutions.idm.connector.wrapper.IqResponseMemberInfoList;
import eu.bcvsolutions.idm.connector.wrapper.Query;
import eu.bcvsolutions.idm.connector.entity.Authenticate;
import eu.bcvsolutions.idm.connector.entity.Filter;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoList;
import eu.bcvsolutions.idm.connector.wrapper.QueryResponse;
import eu.bcvsolutions.idm.connector.wrapper.QueryResponseAccountInfoList;

import eu.bcvsolutions.idm.connector.wrapper.QueryResponseMemberInfoList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
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
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;

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

	private final String GETACCOUNTINFOLIST = "<result>\n" +
			"      <item>\n" +
			"        <name>Jan Novák</name>\n" +
			"        <email>jnovak@testdomain</email>\n" +
			"        <displayemail>jnovak@testdomain</displayemail>\n" +
			"        <accounttype>0</accounttype>\n" +
			"        <accountstate>\n" +
			"          <classname>TAccountState</classname>\n" +
			"          <state>0</state>\n" +
			"        </accountstate>\n" +
			"        <admintype>0</admintype>\n" +
			"        <quota>\n" +
			"          <classname>TAccountQuota</classname>\n" +
			"          <mailboxsize>228</mailboxsize>\n" +
			"          <mailboxquota>0</mailboxquota>\n" +
			"        </quota>\n" +
			"      </item>\n" +
			"      <item>\n" +
			"        <name>Petr Hanák</name>\n" +
			"        <email>petr.ha@testdomain</email>\n" +
			"        <displayemail>petr.ha@testdomain</displayemail>\n" +
			"        <accounttype>0</accounttype>\n" +
			"        <accountstate>\n" +
			"          <classname>TAccountState</classname>\n" +
			"          <state>0</state>\n" +
			"        </accountstate>\n" +
			"        <admintype>0</admintype>\n" +
			"        <quota>\n" +
			"          <classname>TAccountQuota</classname>\n" +
			"          <mailboxsize>228</mailboxsize>\n" +
			"          <mailboxquota>0</mailboxquota>\n" +
			"        </quota>\n" +
			"      </item></result>";

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

			QueryResponse queryResponse = new QueryResponse();
			IqResponse iqResponse = new IqResponse();
			iqResponse.setQueryResponse(queryResponse);
			iqResponse = (IqResponse) getObject(response.getBody(), new IqResponse());

			if (iqResponse.getQueryResponse().getResult().equals("0")) {
				throw new ConnectionFailedException("Authenticate failed");
			} else {
				this.sid = iqResponse.getSid();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ConnectionFailedException("Authenticate failed");
		}
	}

	public GetAccountsInfoListResponse getAccountsInfoList(Filter filter) {
		GetAccountsInfoList getAccountsInfoList = new GetAccountsInfoList();
		getAccountsInfoList.setDomainstr(configuration.getDomain());
		getAccountsInfoList.setCount("5");
		getAccountsInfoList.setFilter(filter);

		GetAccountsInfoListResponse getAccountsInfoListResponse = new GetAccountsInfoListResponse();
		QueryResponseAccountInfoList queryResponse = new QueryResponseAccountInfoList();
		queryResponse.setAccountsInfoListResponse(getAccountsInfoListResponse);
		IqResponseAccountInfoList iqResponse = new IqResponseAccountInfoList();
		iqResponse.setQueryResponse(queryResponse);

		try {
//			GetAccountsInfoListResponse getAccountsInfoListResponse = (GetAccountsInfoListResponse) getObject(GETACCOUNTINFOLIST, new GetAccountsInfoListResponse());
//			log.info(getXMLBody(getAccountsInfoListResponse));
//			log.info(getAccountsInfoListResponse.getAccounts().get(0).getFirstname());

			log.info("vygenerovane telo\n" + getWrappedXml(getAccountsInfoList));
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(getAccountsInfoList));
			if (response.getStatus() != 200) {
				throw new ConnectionFailedException("Can't connect to system, return code " + response.getStatus());
			}
			log.info(response.getBody());
			iqResponse = (IqResponseAccountInfoList) getObject(response.getBody(), iqResponse);
			return (GetAccountsInfoListResponse) iqResponse.getQueryResponse().getAccountsInfoListResponse();

//			GetAccountsInfoListResponse getAccountsInfoListResponse = (GetAccountsInfoListResponse) iqResponse.getQueryResponse().getResult();
//			log.info("info list response check: " + getAccountsInfoListResponse.getAccounts().get(0).getFirstname());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConnectionFailedException("Cannot get accounts");
		}
	}

	public String createAccount(Set<Attribute> createAttributes) {
		Account account = new Account();
		createAttributes.forEach(attribute -> {
			if (attribute.getName().equals(IceWarpConnector.NAME)) {
				account.setEmail(String.valueOf(attribute.getValue().get(0)));
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
		});

		if (configuration.getObject().toLowerCase().equals(ObjectClass.ACCOUNT_NAME)) {
			account.setAccounttype("0");
		} else if (configuration.getObject().toLowerCase().equals(ObjectClass.GROUP_NAME)) {
			account.setAccounttype("7");
		}

		// TODO handle if something is missing
		CreateAccount createAccount = new CreateAccount();
		createAccount.setDomainStr(configuration.getDomain());
		Item name = new Item("A_Name", new PropertyName(account.getFirstName(), account.getLastName()));
		Item type = new Item("U_Type", new PropertyVal("NativeInt", account.getAccounttype()));
		Item email = new Item("U_Mailbox", new PropertyVal("TPropertyString", account.getEmail()));
		Item accountState = new Item("A_State", new PropertyState(account.getAccountstate()));
		Item adminType = new Item("A_AdminType", new PropertyVal("TPropertyString", account.getAdmintype()));
		List<Item> items = Arrays.asList(name, type, email, adminType, accountState);
		createAccount.setItems(items);

		try {
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(createAccount));
			if (response.getStatus() != 200) {
				throw new ConnectionFailedException("Can't connect to system, return code " + response.getStatus());
			}
			log.info(response.getBody());
			return account.getEmail();
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ConnectionFailedException("Cannot create");
		}
	}

	public void setAccountProperties(Uid uid, Set<Attribute> replaceAttributes) {
		SetAccountProperties setAccountProperties = new SetAccountProperties();

		// identifier which is email must be set
		setAccountProperties.setAccountemail(uid.getUidValue());

		PropertyName propertyName = new PropertyName();
		Item name = new Item();
		name.setPropertyname(propertyName);
		name.setPropname(Collections.singletonList("A_Name"));
		setAccountProperties.addItem(name);

		replaceAttributes.forEach(attribute -> {
			if (attribute.getName().equals(IceWarpConnector.FIRST_NAME)) {
				propertyName.setFirstname(String.valueOf(attribute.getValue().get(0)));
				return;
			}
			if (attribute.getName().equals(IceWarpConnector.LAST_NAME)) {
				propertyName.setSurname(String.valueOf(attribute.getValue().get(0)));
				return;
			}
			if (attribute.getName().equals(IceWarpConnector.EMAIL) || attribute.getName().equals(IceWarpConnector.NAME)) {
				Item email = new Item("U_Mailbox", new PropertyVal("TPropertyString", String.valueOf(attribute.getValue().get(0))));
				setAccountProperties.addItem(email);
				return;
			}
			if (attribute.getName().equals(IceWarpConnector.ACCOUNT_STATE)) {
				Item state = new Item("A_State", new PropertyState(String.valueOf(attribute.getValue().get(0))));
				setAccountProperties.addItem(state);
				return;
			}
			if (attribute.getName().equals(IceWarpConnector.ADMIN_TYPE)) {
				Item adminType = new Item("A_AdminType", new PropertyVal("TPropertyString", String.valueOf(attribute.getValue().get(0))));
				setAccountProperties.addItem(adminType);
				return;
			}
		});

		try {
			log.info(getWrappedXml(setAccountProperties));
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(setAccountProperties));
			if (response.getStatus() != 200) {
				throw new ConnectionFailedException("Can't connect to system, return code " + response.getStatus());
			}
			log.info(response.getBody());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public GetAccountMemberInfoListResponse getGroupMembers(String uid) {
		GetAccountMemberInfoList getAccountMemberInfoList = new GetAccountMemberInfoList();
		getAccountMemberInfoList.setGroupUid(uid);

		GetAccountMemberInfoListResponse getAccountMemberInfoListResponse = new GetAccountMemberInfoListResponse();
		QueryResponseMemberInfoList queryResponse = new QueryResponseMemberInfoList();
		queryResponse.setGetAccountMemberInfoListResponse(getAccountMemberInfoListResponse);
		IqResponseMemberInfoList iqResponse = new IqResponseMemberInfoList();
		iqResponse.setQueryResponse(queryResponse);

		try {
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(getAccountMemberInfoList));
			if (response.getStatus() != 200) {
				throw new ConnectionFailedException("Can't connect to system, return code " + response.getStatus());
			}
			log.info(response.getBody());
			iqResponse = (IqResponseMemberInfoList) getObject(response.getBody(), iqResponse);

			return (GetAccountMemberInfoListResponse) iqResponse.getQueryResponse().getGetAccountMemberInfoListResponse();

		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ConnectionFailedException("Cannot get group members");
		}
	}

	public void addMemberToGroup(String userUid, String groupUid) {
		MemberItem member = new MemberItem();
		member.setUserUid(userUid);
		Members members = new Members();
		members.setItems(Collections.singletonList(member));
		AddAccountMembers addAccountMembers = new AddAccountMembers();
		addAccountMembers.setGroupEmail(groupUid);
		addAccountMembers.setMembers(members);

		IqResponse iqResponse = new IqResponse();

		try {
			log.info(getWrappedXml(addAccountMembers));

			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(addAccountMembers));
			if (response.getStatus() != 200) {
				throw new ConnectionFailedException("Can't connect to system, return code " + response.getStatus());
			}
			log.info(response.getBody());
			iqResponse = (IqResponse) getObject(response.getBody(), iqResponse);

			if (iqResponse.getQueryResponse().getResult().equals("0")) {
				throw new ConnectionFailedException("Cannot add member to group");
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ConnectionFailedException("Cannot add member to group");
		}
	}

	public void deleteAccount(Uid uid) {
		AccountList accountList = new AccountList();
		accountList.addAccounts(uid.getUidValue());
		DeleteAccounts deleteAccounts = new DeleteAccounts();
		deleteAccounts.setDomainstr(configuration.getDomain());
		deleteAccounts.setAccountList(accountList);
		deleteAccounts.setLeavedata("0");

		try {
			log.info(getWrappedXml(deleteAccounts));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private String getWrappedXml(Object request) throws JAXBException {
		Query query = new Query();
		query.setCommand(request);
		Iq iq = new Iq();
		iq.setSid(this.sid);
		iq.setQuery(query);
		return getXMLBody(iq);
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

	private Object getObject(String xml, Object response) throws JAXBException {
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

		JAXBContext jaxbContext = JAXBContext.newInstance(response.getClass());
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return unmarshaller.unmarshal(inputStream);
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
