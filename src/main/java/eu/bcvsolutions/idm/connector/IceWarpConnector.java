package eu.bcvsolutions.idm.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;

import eu.bcvsolutions.idm.connector.communication.Connection;
import eu.bcvsolutions.idm.connector.entity.AccountResponse;
import eu.bcvsolutions.idm.connector.entity.Filter;
import eu.bcvsolutions.idm.connector.entity.GetAccountMemberInfoListResponse;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoListResponse;

/**
 * This sample connector provides (empty) implementations for all ConnId operations, but this is not mandatory: any
 * connector can choose which operations are actually to be implemented.
 */
@ConnectorClass(configurationClass = IceWarpConfiguration.class, displayNameKey = "icewarp.connector.display")
public class IceWarpConnector implements Connector,
        CreateOp, UpdateOp, SchemaOp, TestOp, SearchOp<String> {

    private static final Log log = Log.getLog(IceWarpConnector.class);

    private IceWarpConfiguration configuration;
	private Connection connection;

    public static final String NAME = "__NAME__";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String EMAIL = "email";
	public static final String DISPLAY_EMAIL = "displayEmail";
	public static final String ACCOUNT_STATE = "accountState";
	public static final String ADMIN_TYPE = "adminType";
	public static final String FULL_NAME = "name";
	public static final String PASSWORD = "__PASSWORD__";
	public static final String ACCOUNT_TYPE = "accountType";
	public static final String GROUPS = "groups";

	public static final String ROLE_TYPE = "7";
	public static final String USER_TYPE = "0";

	public static final String GROUP_NAME = "groupName";
	public static final String GROUP_ALIAS = "groupAlias";
	public static final String GROUP_MEMBERS = "members";

	public static final int MAX_ROWS = 100;

    @Override
    public IceWarpConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void init(final Configuration configuration) {
        this.configuration = (IceWarpConfiguration) configuration;
		// init connection and authenticate
		this.connection = new Connection(this.configuration);
		this.connection.authenticate();
        log.ok("Connector {0} successfully inited", getClass().getName());
    }

    @Override
    public void dispose() {
        // dispose of any resources the this connector uses.
		if (connection != null) {
			connection.logout();
		}
    }

    @Override
    public Uid create(
            final ObjectClass objectClass,
            final Set<Attribute> createAttributes,
            final OperationOptions options) {
		String account = this.connection.createAccount(createAttributes);
		return new Uid(account);
    }

    @Override
    public Uid update(
            final ObjectClass objectClass,
            final Uid uid,
            final Set<Attribute> replaceAttributes,
            final OperationOptions options) {

		List<String> oldGroups = handleMembers(uid.getUidValue());
		return new Uid(connection.setAccountProperties(uid, replaceAttributes, oldGroups));
    }

    @Override
    public Schema schema() {
		ObjectClassInfoBuilder accountObjectClassBuilder = new ObjectClassInfoBuilder();
		accountObjectClassBuilder.setType(ObjectClass.ACCOUNT_NAME);
		accountObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(FIRST_NAME, String.class));
		accountObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(LAST_NAME, String.class));
		accountObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(EMAIL, String.class));
		accountObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(DISPLAY_EMAIL, String.class));
		accountObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(ACCOUNT_STATE, String.class));
		accountObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(ADMIN_TYPE, Boolean.class));
		accountObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(FULL_NAME, String.class));
		accountObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(PASSWORD, GuardedString.class));
		accountObjectClassBuilder.addAttributeInfo(
				AttributeInfoBuilder.define(GROUPS).setMultiValued(true).setType(String.class).build());

		ObjectClassInfoBuilder groupObjectClassBuilder = new ObjectClassInfoBuilder();
		groupObjectClassBuilder.setType(ObjectClass.GROUP_NAME);
		groupObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(GROUP_NAME, String.class));
		groupObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(EMAIL, String.class));
		groupObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(DISPLAY_EMAIL, String.class));
		groupObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(ACCOUNT_STATE, String.class));
		groupObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(ADMIN_TYPE, Boolean.class));
		groupObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(GROUP_ALIAS, String.class));
		groupObjectClassBuilder.addAttributeInfo(AttributeInfoBuilder.build(FULL_NAME, String.class));
		groupObjectClassBuilder.addAttributeInfo(
				AttributeInfoBuilder.define(GROUP_MEMBERS).setMultiValued(true).setType(String.class).build());

		SchemaBuilder schemaBuilder = new SchemaBuilder(IceWarpConnector.class);
		schemaBuilder.defineObjectClass(accountObjectClassBuilder.build());
		schemaBuilder.defineObjectClass(groupObjectClassBuilder.build());
		return schemaBuilder.build();
    }

    @Override
    public void test() {
        Connection connection = new Connection(configuration);
		// otestování autentizace
        connection.authenticate();
	}

    @Override
    public FilterTranslator<String> createFilterTranslator(
            final ObjectClass objectClass,
            final OperationOptions options) {

		if (objectClass.is(ObjectClass.ACCOUNT_NAME) || objectClass.is(ObjectClass.GROUP_NAME)) {
			return new AbstractFilterTranslator<String>() {
				@Override
				protected String createEqualsExpression(EqualsFilter filter, boolean not) {
					if (not) {
						throw new UnsupportedOperationException("This type of equals expression is not allow for now.");
					}

					Attribute attr = filter.getAttribute();

					if (attr == null || !attr.is(Uid.NAME)) {
						throw new IllegalArgumentException("Attribute is null or not UID attribute.");
					}

					return ((Uid) attr).getUidValue();
				}
			};
		}
		return null;
    }

    @Override
    public void executeQuery(
            final ObjectClass objectClass,
            final String query,
            final ResultsHandler handler,
            final OperationOptions options) {

		// find one user/group or get all.
		if (query != null) {
			if (configuration.getObject().equals(ObjectClass.ACCOUNT_NAME)) {
				Filter filter = new Filter();
				filter.setTypemask(USER_TYPE);
				filter.setNamemask(query);
				handleAccount(objectClass, handler, filter);
			} else if (configuration.getObject().equals(ObjectClass.GROUP_NAME)) {
				Filter filter = new Filter();
				filter.setTypemask(ROLE_TYPE);
				// For one group te identifier is only the part before @
				filter.setNamemask(query.split("@")[0]);
				handleAccount(objectClass, handler, filter);
			}
		} else {
			if (objectClass.getObjectClassValue().equals(configuration.getObject()) &&
					objectClass.getObjectClassValue().equals(ObjectClass.ACCOUNT_NAME)) {

				Filter filter = new Filter();
				filter.setTypemask(USER_TYPE);
				handleAccount(objectClass, handler, filter);
			} else if (objectClass.getObjectClassValue().equals(configuration.getObject()) &&
					objectClass.getObjectClassValue().equals(ObjectClass.GROUP_NAME)) {

				Filter filter = new Filter();
				filter.setTypemask(ROLE_TYPE);
				handleAccount(objectClass, handler, filter);
			}
		}
    }

	private void handleAccount(ObjectClass objectClass, ResultsHandler handler, Filter filter) {
		GetAccountsInfoListResponse accountsInfoList = connection.getAccountsInfoList(filter, 0);
		getOnePageOfAccounts(objectClass, handler, filter, accountsInfoList);

		if (Integer.valueOf(accountsInfoList.getOverallcount()) > MAX_ROWS) {
			int numOfPages = Integer.valueOf(accountsInfoList.getOverallcount()) / MAX_ROWS;
			for (int i = 1; i <= numOfPages; i++) {
				accountsInfoList = connection.getAccountsInfoList(filter, (MAX_ROWS * i));
				getOnePageOfAccounts(objectClass, handler, filter, accountsInfoList);
			}
		}

	}

	private void getOnePageOfAccounts(ObjectClass objectClass, ResultsHandler handler, Filter filter, GetAccountsInfoListResponse accountsInfoList) {
		if (accountsInfoList != null) {
			for (AccountResponse account : accountsInfoList.getAccounts()) {
				if (filter.getNamemaskAttribute() != null) {
					if (filter.getTypemaskAttribute().equals(ROLE_TYPE) && account.getEmail().equals(filter.getNamemaskAttribute() + "@" + configuration.getDomain())) {
						prepareConnObject(objectClass, handler, account);
					} else if(filter.getTypemaskAttribute().equals(USER_TYPE) && account.getEmail().equals(filter.getNamemaskAttribute())) {
						prepareConnObject(objectClass, handler, account);
					}
				} else {
					prepareConnObject(objectClass, handler, account);
				}
			}
		}
	}

	private void prepareConnObject(ObjectClass objectClass, ResultsHandler handler, AccountResponse account) {
		ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
		builder.setUid(account.getEmail());
		builder.setName(account.getEmail());
		builder.setObjectClass(objectClass);
		builder.addAttribute(AttributeBuilder.build(FULL_NAME, account.getName()));
		builder.addAttribute(AttributeBuilder.build(ADMIN_TYPE, getAdminType(account.getAdmintype())));
		builder.addAttribute(AttributeBuilder.build(ACCOUNT_STATE, account.getAccountstate().getState()));
		if (account.getAccounttype().equals(ROLE_TYPE)) {
			List<String> members = new ArrayList<>();
			searchMembers(null, false, members, account.getEmail());
			builder.addAttribute(AttributeBuilder.build(GROUP_MEMBERS, members));
		}
		if (account.getAccounttype().equals(USER_TYPE)) {
			builder.addAttribute(AttributeBuilder.build(GROUPS, handleMembers(account.getEmail())));
		}

		handler.handle(builder.build());
	}

	private Boolean getAdminType(String admintype) {
		return !admintype.equals("0");
	}

	private List<String> handleMembers(String account) {
		List<String> members = new ArrayList<>();
		Filter filter = new Filter();
		filter.setTypemask(ROLE_TYPE);
		GetAccountsInfoListResponse groups = connection.getAccountsInfoList(filter, 0);

		groups.getAccounts().forEach(accountResponse -> {
			String groupName = accountResponse.getEmail();
			searchMembers(account, true, members, groupName);
		});

		if (Integer.valueOf(groups.getOverallcount()) > MAX_ROWS) {
			int numOfPages = Integer.valueOf(groups.getOverallcount()) / MAX_ROWS;
			for (int i = 1; i <= numOfPages; i++) {
				groups = connection.getAccountsInfoList(filter, (MAX_ROWS * i));
				groups.getAccounts().forEach(accountResponse -> {
					String groupName = accountResponse.getEmail();
					searchMembers(account, true, members, groupName);
				});
			}
		}
		return members;
	}

	private void searchMembers(String user, Boolean forUser, List<String> members, String groupName) {
		GetAccountMemberInfoListResponse groupMembers = connection.getGroupMembers(groupName, 0);
		getOnePageOfMembers(user, forUser, members, groupName, groupMembers);

		if (Integer.valueOf(groupMembers.getOverallcount()) > MAX_ROWS) {
			int numOfPages = Integer.valueOf(groupMembers.getOverallcount()) / MAX_ROWS;
			for (int i = 1; i <= numOfPages; i++) {
				groupMembers = connection.getGroupMembers(groupName, (MAX_ROWS * i));
				getOnePageOfMembers(user, forUser, members, groupName, groupMembers);
			}
		}

	}

	private void getOnePageOfMembers(String user, Boolean forUser, List<String> members, String groupName, GetAccountMemberInfoListResponse groupMembers) {
		// In the email there is ";0" in testing env for some reason
		groupMembers.getItems().forEach(memberResponse -> {
			String member = memberResponse.getUserUid();
			if (user != null && forUser) {
				if (user.equals(member)) {
					members.add(groupName);
				}
			} else {
				members.add(member);
			}
		});
	}

}
