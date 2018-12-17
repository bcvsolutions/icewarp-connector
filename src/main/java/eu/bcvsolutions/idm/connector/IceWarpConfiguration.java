package eu.bcvsolutions.idm.connector;

import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

public class IceWarpConfiguration extends AbstractConfiguration {

    private String host = "";
    private String username = "";
    private GuardedString password;
    private String stringPassword;
    private String domain;
	// user / group
    private String object;

    @ConfigurationProperty(displayMessageKey = "host.display",
            helpMessageKey = "host.help", order = 1,
            required = true, confidential = false)
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    @ConfigurationProperty(displayMessageKey = "username.display",
            helpMessageKey = "username.help", order = 2,
            required = true, confidential = false)
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @ConfigurationProperty(displayMessageKey = "password.display",
            helpMessageKey = "password.help", order = 3,
            required = false, confidential = true)
    public GuardedString getPassword() {
        return password;
    }
    public void setPassword(GuardedString password) {
        this.password = password;
    }

    @ConfigurationProperty(displayMessageKey = "strpassword.display",
            helpMessageKey = "strpassword.help", order = 4,
            required = false, confidential = false)
    public String getStringPassword() {
        return stringPassword;
    }
    public void setStringPassword(String password) {
        this.stringPassword = password;
    }

    @ConfigurationProperty(displayMessageKey = "domain.display",
            helpMessageKey = "domain.help", order = 5,
            required = true, confidential = false)
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @ConfigurationProperty(displayMessageKey = "object.display",
            helpMessageKey = "object.help", order = 6,
            required = true, confidential = false)
    public String getObject() {
        return object;
    }
    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public void validate() {
        if (StringUtil.isBlank(username)) {
            throw new ConfigurationException("Username must not be blank!");
        }
    }

}
