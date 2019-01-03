package eu.bcvsolutions.idm.connector.wrapper;

import eu.bcvsolutions.idm.connector.entity.AddAccountMembers;
import eu.bcvsolutions.idm.connector.entity.Authenticate;
import eu.bcvsolutions.idm.connector.entity.CreateAccount;
import eu.bcvsolutions.idm.connector.entity.DeleteAccountMembers;
import eu.bcvsolutions.idm.connector.entity.DeleteAccounts;
import eu.bcvsolutions.idm.connector.entity.EditAccountMembers;
import eu.bcvsolutions.idm.connector.entity.GetAccountMemberInfoList;
import eu.bcvsolutions.idm.connector.entity.GetAccountProperties;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoList;
import eu.bcvsolutions.idm.connector.entity.Logout;
import eu.bcvsolutions.idm.connector.entity.SetAccountPassword;
import eu.bcvsolutions.idm.connector.entity.SetAccountProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Query {
	private Authenticate authenticate;
	private AddAccountMembers addAccountMembers;
	private CreateAccount createAccount;
	private DeleteAccounts deleteAccounts;
	private EditAccountMembers editAccountMembers;
	private DeleteAccountMembers deleteAccountMembers;
	private GetAccountsInfoList getAccountsInfoList;
	private GetAccountMemberInfoList getAccountMemberInfoList;
	private GetAccountProperties getAccountProperties;
	private Logout logout;
	private SetAccountProperties setAccountProperties;
	private SetAccountPassword setAccountPassword;

	@XmlAttribute
	private final String xmlns = "admin:iq:rpc";
	@XmlElement
	private String commandname;
	@XmlElement(name = "commandparams")
	private Object command;

	public String getCommandname() {
		return commandname;
	}

	public void setCommandname(String commandname) {
		this.commandname = commandname;
	}

	public Object getCommand() {
		return command;
	}

	public void setCommand(Object command) {
		this.command = command;
		this.commandname = command.getClass().getSimpleName().toLowerCase();
	}
}
