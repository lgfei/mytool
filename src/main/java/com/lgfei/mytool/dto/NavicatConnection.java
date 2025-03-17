package com.lgfei.mytool.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lgfei
 * @date 2025/3/17 9:18
 */
@XmlRootElement(name = "Connection")
public class NavicatConnection {
    private String name;
    private String host;
    private String port;
    private String userName;
    private String password;
    private String database;

    @XmlAttribute(name = "ConnectionName")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @XmlAttribute(name = "Host")
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    @XmlAttribute(name = "Port")
    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    @XmlAttribute(name = "UserName")
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    @XmlAttribute(name = "Password")
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @XmlAttribute(name = "Database")
    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }
}
