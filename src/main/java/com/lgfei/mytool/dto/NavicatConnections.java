package com.lgfei.mytool.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author lgfei
 * @date 2025/3/17 9:22
 */
@XmlRootElement(name = "Connections")
public class NavicatConnections {
    private List<NavicatConnection> connections;

    @XmlElement(name = "Connection")
    public List<NavicatConnection> getConnections() { return connections; }
    public void setConnections(List<NavicatConnection> connections) { this.connections = connections; }
}
