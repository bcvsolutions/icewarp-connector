package eu.bcvsolutions.idm.connector;

import eu.bcvsolutions.idm.connector.communication.Connection;
import eu.bcvsolutions.idm.connector.entity.AccountResponse;
import eu.bcvsolutions.idm.connector.entity.Filter;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoListResponse;

import java.util.Set;
import java.util.UUID;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.operations.ResolveUsernameApiOp;
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
import org.identityconnectors.framework.common.objects.SyncResultsHandler;
import org.identityconnectors.framework.common.objects.SyncToken;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.AuthenticateOp;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.SyncOp;
import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.UpdateAttributeValuesOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;

/**
 * This sample connector provides (empty) implementations for all ConnId operations, but this is not mandatory: any
 * connector can choose which operations are actually to be implemented.
 */
@ConnectorClass(configurationClass = IceWarpConfiguration.class, displayNameKey = "icewarp.connector.display")
public class IceWarpConnector implements Connector,
        CreateOp, UpdateOp, UpdateAttributeValuesOp, DeleteOp,
        AuthenticateOp, ResolveUsernameApiOp, SchemaOp, SyncOp, TestOp, SearchOp<String> {

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

	public static final String ROLE_TYPE = "7";
	public static final String USER_TYPE = "0";

	public static final String GROUP_NAME = "groupName";
	public static final String GROUP_ALIAS = "groupAlias";
	public static final String GROUP_MEMBERS = "members";

    @Override
    public IceWarpConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void init(final Configuration configuration) {
		log.info("--- INIT ---");
        this.configuration = (IceWarpConfiguration) configuration;
		// init connection and authenticate
		this.connection = new Connection(this.configuration);
		this.connection.authenticate();
        log.ok("Connector {0} successfully inited", getClass().getName());
    }

    @Override
    public void dispose() {
        // dispose of any resources the this connector uses.
		log.info("--- DISPOSE ---");
    }

    @Override
    public Uid create(
            final ObjectClass objectClass,
            final Set<Attribute> createAttributes,
            final OperationOptions options) {
    	log.info("--- CREATE ---");
		this.connection.createAccount(createAttributes);

        return new Uid(UUID.randomUUID().toString());
    }

    @Override
    public Uid update(
            final ObjectClass objectClass,
            final Uid uid,
            final Set<Attribute> replaceAttributes,
            final OperationOptions options) {
		log.info("--- UPDATE ---");
		log.info("Object class");
		log.info(objectClass.getDisplayNameKey() + " " + objectClass.getObjectClassValue());
		log.info("Uid");
		log.info(uid.getUidValue());
		log.info("replace attributes");
		for (Attribute attribute : replaceAttributes) {
			log.info(attribute.getName() + " " + attribute.getValue());
		}
		log.info("operation options to string");
		log.info(options.toString());
		connection.setAccountProperties(replaceAttributes);

        return uid;
    }

    @Override
    public Uid addAttributeValues(
            final ObjectClass objclass,
            final Uid uid,
            final Set<Attribute> valuesToAdd,
            final OperationOptions options) {

        return uid;
    }

    @Override
    public Uid removeAttributeValues(
            final ObjectClass objclass,
            final Uid uid,
            final Set<Attribute> valuesToRemove,
            final OperationOptions options) {

        return uid;
    }

    @Override
    public void delete(
            final ObjectClass objectClass,
            final Uid uid,
            final OperationOptions options) {
		log.info("--- DELETE ---");

		this.connection.deleteAccount(uid);

    }

    @Override
    public Uid authenticate(
            final ObjectClass objectClass,
            final String username,
            final GuardedString password,
            final OperationOptions options) {
		log.info("--- AUTHENTICATE ---");

        return new Uid(username);
    }

    @Override
    public Uid resolveUsername(
            final ObjectClass objectClass,
            final String username,
            final OperationOptions options) {

        return new Uid(username);
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
    public void sync(
            final ObjectClass objectClass,
            final SyncToken token,
            final SyncResultsHandler handler,
            final OperationOptions options) {
		log.info("--- SYNC ---");
    }

    @Override
    public SyncToken getLatestSyncToken(final ObjectClass objectClass) {
        return new SyncToken(null);
    }

    @Override
    public void test() {
    	log.info("starting test..");
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
		log.info("--- EXECUTE QUERY ---");

		// find one user/group or get all.
		if (query != null) {
			log.info("query: " + query);
			if (configuration.getObject().equals(ObjectClass.ACCOUNT_NAME)) {
				Filter filter = new Filter();
				filter.setTypemask(USER_TYPE);
				filter.setNamemask(query);
				handleAccount(objectClass, handler, filter);
			} else if (configuration.getObject().equals(ObjectClass.GROUP_NAME)) {
				Filter filter = new Filter();
				filter.setTypemask(ROLE_TYPE);
				filter.setNamemask(query);
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
				hadleMembers(objectClass, handler);
			}
		}

    }

	private void handleAccount(ObjectClass objectClass, ResultsHandler handler, Filter filter) {
		GetAccountsInfoListResponse accountsInfoList = connection.getAccountsInfoList(filter);
		if (accountsInfoList != null) {
			for (AccountResponse account : accountsInfoList.getAccounts()) {
				log.info("account.getName(): " + account.getName());
				ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
				builder.setUid(account.getEmail());
				builder.setName(account.getEmail());
				builder.setObjectClass(objectClass);
				builder.addAttribute(AttributeBuilder.build(FULL_NAME, account.getName()));
				builder.addAttribute(AttributeBuilder.build(ADMIN_TYPE, account.getAdmintype()));
				builder.addAttribute(AttributeBuilder.build(ACCOUNT_STATE, account.getAccountstate().getState()));
				handler.handle(builder.build());
			}
		}
	}

	private void hadleMembers(ObjectClass objectClass, ResultsHandler handler) {

	}

}
