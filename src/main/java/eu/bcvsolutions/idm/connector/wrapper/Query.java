package eu.bcvsolutions.idm.connector.wrapper;

import eu.bcvsolutions.idm.connector.entity.Authenticate;
import eu.bcvsolutions.idm.connector.entity.GetAccountProperties;
import eu.bcvsolutions.idm.connector.entity.GetAccountsInfoList;
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
	private GetAccountsInfoList getAccountsInfoList;
	private GetAccountProperties getAccountProperties;

	@XmlAttribute
	private final String xmlns = "\'admin:iq:rpc\'";
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
