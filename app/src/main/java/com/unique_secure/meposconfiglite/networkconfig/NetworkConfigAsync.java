package com.unique_secure.meposconfiglite.networkconfig;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.persistence.TestSharedPreferences;
import com.uniquesecure.meposconnect.MePOS;
import com.uniquesecure.meposconnect.MePOSConfigurationCallback;
import com.uniquesecure.meposconnect.MePOSConnectionManager;


public class NetworkConfigAsync extends AsyncTask<NetworkConfiguration, NetworkConfigurationProgress, Boolean> implements MePOSConfigurationCallback {

    public interface OnNetworkConfigCompleted {
        void onComplete(Boolean success);
    }

    private Context context;
    private MePOS mepos;
    private OnNetworkConfigCompleted callbackComplete;
    private ProgressDialog progressDialog;
    private TestSharedPreferences testSharedPreferences;

    private static final String TAG = NetworkConfigAsync.class.getSimpleName();


    public NetworkConfigAsync(Context context, MePOS mepos, OnNetworkConfigCompleted callback) {
        this.context = context;
        this.mepos = mepos;
        this.callbackComplete = callback;
        testSharedPreferences = new TestSharedPreferences(context);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Network Configuration progress");
        progressDialog.setMessage(context.getString(R.string.it_will_take_a_minute));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(5);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(NetworkConfiguration... params) {
        if (params == null || params.length < 1) {
            return false;
        }
        NetworkConfiguration config = params[0];
        MePOSConnectionManager manager = mepos.getConnectionManager();
        boolean result = false;
        switch (config.getMode()) {
            case DEFAULT:
                result = manager.MePOSConnectDefault(this);
                try {
                    String wifiipaddress = mepos.getConnectionManager().MePOSGetAssignedIP();
                    String wifissid = mepos.getConnectionManager().getSSID();
                    testSharedPreferences.saveTestInfo("wifipassword", "12345678");
                    testSharedPreferences.saveTestInfo("wifiipaddress", wifiipaddress);
                    testSharedPreferences.saveTestInfo("wifissid", wifissid);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                break;
            case ETHERNET_CLIENT:
                result = manager.MePOSConnectEthernet(this);
                try {
                    testSharedPreferences.saveTestInfo("wifipassword", context.getString(R.string.not_aviliable));
                    testSharedPreferences.saveTestInfo("wifiipaddress", manager.MePOSGetAssignedIP());
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                break;
            case WIFI_CLIENT:
                result = manager.MePOSConnectWiFi(config.getSsid(), config.getIp(), config.getNetmask(), config.getEncryption(), config.getPassword(), this);
                try {
                    String wifissid = manager.getSSID();
                    String wifiipaddress = manager.MePOSGetAssignedIP();
                    testSharedPreferences.saveTestInfo("wifiipaddress", wifiipaddress);
                    testSharedPreferences.saveTestInfo("wifissid", wifissid);
                    if (TextUtils.isEmpty(wifiipaddress)) {
                        result = false;
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    result = false;
                }
                break;
            case AP:
                result = manager.MePOSSetAccessPoint(config.getSsid(), config.getEncryption(), config.getPassword(), this);
                try {
                    manager.setConnectionIPAddress("192.168.16.254");
                    String wifissid = manager.getSSID();
                    String wifiipaddress = manager.MePOSGetAssignedIP();
                    testSharedPreferences.saveTestInfo("wifiipaddress", wifiipaddress);
                    testSharedPreferences.saveTestInfo("wifissid", wifissid);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(NetworkConfigurationProgress... values) {
        NetworkConfigurationProgress progress = values[0];
        progressDialog.setMessage(progress.getMessage());
        progressDialog.setProgress(progress.getStep());
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        callbackComplete.onComplete(success);
    }

    @Override
    public void onConfigurationProgress(int step, String message) {
        publishProgress(new NetworkConfigurationProgress(step, message));
    }

}
