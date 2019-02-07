package eu.bcvsolutions.idm.connector;

import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

public class IceWarpConfiguration extends AbstractConfiguration {

    private String host;
    private String username;
    private GuardedString password;
    private String domain;
    // object class __ACOUNT__ or __GROUP__
    private String object;
    private Boolean debug;
    private int maxTries;

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
            required = true, confidential = true)
    public GuardedString getPassword() {
        return password;
    }
    public void setPassword(GuardedString password) {
        this.password = password;
    }

    @ConfigurationProperty(displayMessageKey = "domain.display",
            helpMessageKey = "domain.help", order = 4,
            required = true, confidential = false)
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @ConfigurationProperty(displayMessageKey = "object.display",
            helpMessageKey = "object.help", order = 5,
            required = true, confidential = false)
    public String getObject() {
        return object;
    }
    public void setObject(String object) {
        this.object = object;
    }

    @ConfigurationProperty(displayMessageKey = "debug.display",
            helpMessageKey = "debug.help", order = 6,
            required = true, confidential = false)
    public Boolean getDebug() {
        return debug;
    }
    public void setDebug(Boolean debug) {
        this.debug = debug;
    }
    
    @ConfigurationProperty(displayMessageKey = "maxtries.display",
            helpMessageKey = "maxtries.help", order = 7,
            required = true, confidential = false)
    public int getMaxTries() {
        return maxTries;
    }
    public void setMaxTries(int maxTries) {
        this.maxTries = maxTries;
    }

    @Override
    public void validate() {
        if (StringUtil.isBlank(host)) {
            throw new ConfigurationException("Hostname must not be blank!");
        }
        if (StringUtil.isBlank(username)) {
            throw new ConfigurationException("Username must not be blank!");
        }
        if (StringUtil.isBlank(domain)) {
            throw new ConfigurationException("Domain must not be blank!");
        }
        if (StringUtil.isBlank(object)) {
            throw new ConfigurationException("Object must not be blank!");
        }
    }

}
