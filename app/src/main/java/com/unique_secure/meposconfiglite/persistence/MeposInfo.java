package com.unique_secure.meposconfiglite.persistence;


import android.content.Context;

import com.unique_secure.meposconfiglite.R;


public class MeposInfo {
    private Context context;

    public MeposInfo(Context context) {
        this.context = context;
    }

    public String Serial() {
        String var_serial;
        try {
            var_serial = MePOSSingleton.getInstance().getSerialNumber();
        } catch (Exception e) {
            var_serial = context.getString(R.string.no_mepos);
        }
        return var_serial;
    }

    public String FirmWare() {
        String var_firmware;
        try {
            var_firmware = MePOSSingleton.getInstance().getFWVersion();
        } catch (Exception e) {
            var_firmware = context.getString(R.string.no_mepos);
        }
        return var_firmware;
    }

    public String IP() {
        String var_ip;
        try {
            var_ip = MePOSSingleton.getInstance().getConnectionManager().MePOSGetAssignedIP();
        } catch (Exception e) {
            var_ip = context.getString(R.string.no_mepos);
        }
        return var_ip;
    }

    public String SSID() {
        String var_ssid;
        try {
            var_ssid = MePOSSingleton.getInstance().getConnectionManager().getSSID();
        } catch (Exception e) {
            var_ssid = context.getString(R.string.no_mepos);
        }
        return var_ssid;
    }

    public String WiFi() {
        String var_wififw;
        try {
            var_wififw = MePOSSingleton.getInstance().getConnectionManager().getRouterFirmware();
        } catch (Exception e) {
            var_wififw = context.getString(R.string.no_mepos);
        }
        return var_wififw;
    }

    public String MacAddress() {
        String var_mac;
        try {
            var_mac = MePOSSingleton.getInstance().getConnectionManager().getMACAddress();
        } catch (Exception e) {
            var_mac = context.getString(R.string.no_mepos);
        }
        return var_mac;
    }
}
