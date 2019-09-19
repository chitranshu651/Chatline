package Misc;

import java.io.Serializable;

public class IPClass implements Serializable {
    String username;
    String ip;
    int port;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }
}
