package eu.bcvsolutions.idm.connector.communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectionFailedException;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import eu.bcvsolutions.idm.connector.IceWarpConfiguration;
import eu.bcvsolutions.idm.connector.IceWarpConnector;
import eu.bcvsolutions.idm.connector.entity.Account;
import eu.bcvsolutions.idm.connector.entity.AddAccountMembers;
import eu.bcvsolutions.idm.connector.entity.Authenticate;
import eu.bcvsolutions.idm.connector.entity.CreateAccount;
import eu.bcvsolutions.idm.connector.entity.DeleteAccountMembers;
import eu.bcvsolutions.idm.connector.entity.Filter;
import eu.bcvsolutions.idm.connector.entity.GetAccountMemberInfoList;
import eu.bcvsolutions.idm.connector.entity.GetAccountMemberInfoListResponse;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoList;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoListResponse;
import eu.bcvsolutions.idm.connector.entity.Item;
import eu.bcvsolutions.idm.connector.entity.Logout;
import eu.bcvsolutions.idm.connector.entity.MemberItem;
import eu.bcvsolutions.idm.connector.entity.Members;
import eu.bcvsolutions.idm.connector.entity.PropertyName;
import eu.bcvsolutions.idm.connector.entity.PropertyState;
import eu.bcvsolutions.idm.connector.entity.PropertyVal;
import eu.bcvsolutions.idm.connector.entity.SetAccountPassword;
import eu.bcvsolutions.idm.connector.entity.SetAccountProperties;
import eu.bcvsolutions.idm.connector.wrapper.Iq;
import eu.bcvsolutions.idm.connector.wrapper.IqResponse;
import eu.bcvsolutions.idm.connector.wrapper.IqResponseAccountInfoList;
import eu.bcvsolutions.idm.connector.wrapper.IqResponseMemberInfoList;
import eu.bcvsolutions.idm.connector.wrapper.Query;
import eu.bcvsolutions.idm.connector.wrapper.QueryResponse;
import eu.bcvsolutions.idm.connector.wrapper.QueryResponseAccountInfoList;
import eu.bcvsolutions.idm.connector.wrapper.QueryResponseMemberInfoList;

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

	public void logout() {
		Logout logout = new Logout();

		QueryResponse queryResponse = new QueryResponse();
		IqResponse iqResponse = new IqResponse();
		iqResponse.setQueryResponse(queryResponse);
		try {
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(logout));
			if (response.getStatus() != 200) {
				log.error("Can't connect to system, return code " + response.getStatus());
			}
			iqResponse = (IqResponse) getObject(response.getBody(), new IqResponse());

			if (iqResponse.getQueryResponse().getResult().equals("0")) {
				log.error("Logout failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Logout failed");
		}
	}

	public void authenticate() {
		// send request for authentication and set sid to variable for later
		Authenticate authenticate = new Authenticate();
		authenticate.setEmail(configuration.getUsername());
		authenticate.setPassword(getPassword(configuration.getPassword()));
		authenticate.setPersistentlogin("1");
		authenticate.setAuthtype("0");

		QueryResponse queryResponse = new QueryResponse();
		IqResponse iqResponse = new IqResponse();
		iqResponse.setQueryResponse(queryResponse);

		try {
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(authenticate));
			if (response.getStatus() != 200) {
				throw new ConnectionFailedException("Can't connect to system, return code " + response.getStatus());
			}
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
		getAccountsInfoList.setFilter(filter);

		GetAccountsInfoListResponse getAccountsInfoListResponse = new GetAccountsInfoListResponse();
		QueryResponseAccountInfoList queryResponse = new QueryResponseAccountInfoList();
		queryResponse.setAccountsInfoListResponse(getAccountsInfoListResponse);
		IqResponseAccountInfoList iqResponse = new IqResponseAccountInfoList();
		iqResponse.setQueryResponse(queryResponse);

		try {
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(getAccountsInfoList));
			if (response.getStatus() != 200) {
				throw new ConnectorException("Can't connect to system, return code " + response.getStatus());
			}
			iqResponse = (IqResponseAccountInfoList) getObject(response.getBody(), iqResponse);
			return iqResponse.getQueryResponse().getAccountsInfoListResponse();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConnectorException("Cannot get accounts");
		}
	}

	public String createAccount(Set<Attribute> createAttributes) {
		Account account = new Account();
		List<String> groups = new ArrayList<>();
		GuardedString password = null;

		for (Attribute attribute : createAttributes) {
			
			if (attribute.getName().equals(IceWarpConnector.NAME)) {
				account.setEmail(String.valueOf(attribute.getValue().get(0)));
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.EMAIL)) {
				account.setEmail(String.valueOf(attribute.getValue().get(0)));
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.FIRST_NAME)) {
				account.setFirstName(String.valueOf(attribute.getValue().get(0)));
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.LAST_NAME)) {
				account.setLastName(String.valueOf(attribute.getValue().get(0)));
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.ACCOUNT_STATE)) {
				if (String.valueOf(attribute.getValue().get(0)).equals("true")) {
					account.setAccountstate("0");
				} else {
					account.setAccountstate("1");
				}
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.GROUPS)) {
				attribute.getValue().forEach(o -> groups.add(String.valueOf(o)));
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.PASSWORD)) {
				password = (GuardedString) attribute.getValue().get(0);
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.ADMIN_TYPE)) {
				account.setAdmintype(String.valueOf(attribute.getValue().get(0)));
			} else {
				account.setAdmintype("0");
			}
		}

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
				throw new ConnectorException("Can't connect to system, return code " + response.getStatus());
			}
			if (!groups.isEmpty()) {
				groups.forEach(group -> addMemberToGroup(account.getEmail(), group));
			}

			setAccountPassword(account.getEmail(), password);

			return account.getEmail();
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ConnectorException("Cannot create");
		}
	}

	public String setAccountProperties(Uid uid, Set<Attribute> replaceAttributes, List<String> oldGroups) {
		String newUid = uid.getUidValue();
		List<String> groups = null;

		SetAccountProperties setAccountProperties = new SetAccountProperties();

		// identifier which is email must be set
		setAccountProperties.setAccountemail(uid.getUidValue());

		PropertyName propertyName = new PropertyName();
		Item name = new Item();
		name.setPropertyname(propertyName);
		name.setPropname(Collections.singletonList("A_Name"));
		setAccountProperties.addItem(name);

		for (Attribute attribute : replaceAttributes) {
			if (attribute.getName().equals(IceWarpConnector.FIRST_NAME)) {
				propertyName.setFirstname(String.valueOf(attribute.getValue().get(0)));
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.LAST_NAME)) {
				propertyName.setSurname(String.valueOf(attribute.getValue().get(0)));
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.EMAIL) || attribute.getName().equals(IceWarpConnector.NAME)) {
				Item email = new Item("U_Mailbox", new PropertyVal("TPropertyString", String.valueOf(attribute.getValue().get(0))));
				setAccountProperties.addItem(email);
				newUid = String.valueOf(attribute.getValue().get(0));
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.ACCOUNT_STATE)) {
				Item state = new Item("A_State", new PropertyState(String.valueOf(attribute.getValue().get(0))));
				setAccountProperties.addItem(state);
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.ADMIN_TYPE)) {
				Item adminType = new Item("A_AdminType", new PropertyVal("TPropertyString", String.valueOf(attribute.getValue().get(0))));
				setAccountProperties.addItem(adminType);
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.PASSWORD)) {
				setAccountPassword(uid.getUidValue(), (GuardedString) attribute.getValue().get(0));
				continue;
			}
			if (attribute.getName().equals(IceWarpConnector.GROUPS)) {
				groups = new ArrayList<>();
				for (Object o : attribute.getValue()) {
					groups.add(String.valueOf(o));
				}
			}
		}

		try {
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(setAccountProperties));
			if (response.getStatus() != 200) {
				throw new ConnectorException("Can't update account properties" + response.getStatus());
			}

			if (groups != null) {
				Set<String> rolesForRemove = getRolesForRemove(oldGroups, groups);
				Set<String> rolesForAdd = getRolesForAdd(oldGroups, groups);
				if (!rolesForRemove.isEmpty()) {
					rolesForRemove.forEach(roleForRemove -> deleteMemberFromGroup(uid.getUidValue(), roleForRemove));
				}
				if (!rolesForAdd.isEmpty()) {
					rolesForAdd.forEach(roleForAdd -> addMemberToGroup(uid.getUidValue(), roleForAdd));
				}
			}
			return newUid;
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ConnectorException("Can't update account properties " + uid.getUidValue());
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
				throw new ConnectorException("Can't connect to system, return code " + response.getStatus());
			}
			iqResponse = (IqResponseMemberInfoList) getObject(response.getBody(), iqResponse);
			return iqResponse.getQueryResponse().getGetAccountMemberInfoListResponse();

		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ConnectorException("Cannot get group members for " + uid);
		}
	}

	private void addMemberToGroup(String userUid, String groupUid) {
		MemberItem member = new MemberItem();
		member.setUserUid(userUid);
		Members members = new Members();
		members.setItems(Collections.singletonList(member));
		AddAccountMembers addAccountMembers = new AddAccountMembers();
		addAccountMembers.setGroupEmail(groupUid);
		addAccountMembers.setMembers(members);

		QueryResponse queryResponse = new QueryResponse();
		IqResponse iqResponse = new IqResponse();
		iqResponse.setQueryResponse(queryResponse);

		try {
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(addAccountMembers));
			if (response.getStatus() != 200) {
				throw new ConnectorException("Can't connect to system, return code " + response.getStatus());
			}
			iqResponse = (IqResponse) getObject(response.getBody(), iqResponse);

			if (iqResponse.getQueryResponse().getResult().equals("0")) {
				throw new ConnectorException("Cannot add member " + userUid + " to group " + groupUid);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ConnectorException("Cannot add member " + userUid + " to group " + groupUid);
		}
	}

	private void deleteMemberFromGroup(String userUid, String groupUid) {
		MemberItem member = new MemberItem();
		member.setUserUid(userUid);
		Members members = new Members();
		members.setItems(Collections.singletonList(member));
		DeleteAccountMembers deleteAccountMembers = new DeleteAccountMembers();
		deleteAccountMembers.setGroupEmail(groupUid);
		deleteAccountMembers.setMembers(members);

		QueryResponse queryResponse = new QueryResponse();
		IqResponse iqResponse = new IqResponse();
		iqResponse.setQueryResponse(queryResponse);

		try {
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(deleteAccountMembers));
			if (response.getStatus() != 200) {
				throw new ConnectorException("Can't connect to system, return code " + response.getStatus());
			}
			iqResponse = (IqResponse) getObject(response.getBody(), iqResponse);

			if (iqResponse.getQueryResponse().getResult().equals("0")) {
				throw new ConnectorException("Cannot delete member " + userUid + " from group " + groupUid);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ConnectorException("Cannot delete member " + userUid + " from group " + groupUid);
		}
	}

	private void setAccountPassword(String userUid, GuardedString password) {
		SetAccountPassword setAccountPassword = new SetAccountPassword();
		setAccountPassword.setAccountemail(userUid);
		setAccountPassword.setPassword(getPassword(password));

		QueryResponse queryResponse = new QueryResponse();
		IqResponse iqResponse = new IqResponse();
		iqResponse.setQueryResponse(queryResponse);

		try {
			HttpResponse<String> response = post(configuration.getHost() + "/icewarpapi/", getWrappedXml(setAccountPassword));
			if (response.getStatus() != 200) {
				throw new ConnectorException("Can't connect to system, return code " + response.getStatus());
			}
			iqResponse = (IqResponse) getObject(response.getBody(), iqResponse);

			if (iqResponse.getQueryResponse().getResult().equals("0")) {
				throw new ConnectorException("Cannot set password for " + userUid);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ConnectorException("Cannot set password for " + userUid);
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
	private String getXMLBody(Object request) throws JAXBException {
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
			throw new ConnectionFailedException("Connection failed " + e.getMessage());
		}
	}

	private String getPassword(GuardedString password) {
		GuardedStringAccessor accessor = new GuardedStringAccessor();
		password.access(accessor);
		char[] result = accessor.getArray();
		return new String(result);
	}

	private Set<String> getRolesForRemove(List<String> old, List<String> groups) {
		Set<String> oldGroups = new HashSet<>(old);
		Set<String> newGroups = new HashSet<>(groups);
		oldGroups.removeAll(newGroups);
		return oldGroups;
	}

	private Set<String> getRolesForAdd(List<String> old, List<String> groups) {
		Set<String> oldGroups = new HashSet<>(old);
		Set<String> newGroups = new HashSet<>(groups);
		newGroups.removeAll(oldGroups);
		return newGroups;
	}
}
