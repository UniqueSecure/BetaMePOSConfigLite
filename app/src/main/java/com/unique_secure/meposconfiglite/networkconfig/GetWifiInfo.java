package com.unique_secure.meposconfiglite.networkconfig;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.uniquesecure.meposconnect.EncryptionMode;

import java.util.List;


public class GetWifiInfo {
    private Context context;
    private WifiManager wifiManager;

    public GetWifiInfo(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public String getWifiName() {
        String ssid = "";
        WifiInfo wifiInfo = getWifiInfo();
        if (wifiInfo != null) {
            NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
            if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                ssid = wifiInfo.getSSID();
                ssid = ssid.replace("\"", "");
            }
        }
        return ssid;
    }

    public String getCapabilitiesFor(String ssid) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks == null) {
            return "";
        }
        WifiConfiguration current = null;
        for (WifiConfiguration wifiConfiguration : configuredNetworks) {
            String ssidFromConfig = TextUtils.isEmpty(wifiConfiguration.SSID)? "" : wifiConfiguration.SSID.replace("\"", "");
            if (TextUtils.equals(ssidFromConfig, ssid)) {
                current = wifiConfiguration;
                break;
            }
        }
        if (current == null) {
            return "";
        }
        if (current.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            if (current.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.CCMP)) {
                return EncryptionMode.WPAWPA2_AES.toString();
            } else if (current.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.TKIP)) {
                return EncryptionMode.WPAWPA2_TKIP.toString();
            }
        }
        return (current.wepKeys[0] != null) ? EncryptionMode.WEP.toString() : EncryptionMode.NONE.toString();
    }

    private WifiInfo getWifiInfo() {
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo;
            }
        }
        return null;
    }
}
