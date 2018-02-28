package com.unique_secure.meposconfiglite.networkconfig;


import com.unique_secure.meposconfiglite.persistence.ConfigurationNetModes;

public class NetworkConfiguration {

    private ConfigurationNetModes mode;
    private String ssid;
    private String ip;
    private String netmask;
    private String encryption;
    private String password;

    public ConfigurationNetModes getMode() {
        return mode;
    }

    public void setMode(ConfigurationNetModes mode) {
        this.mode = mode;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
