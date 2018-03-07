package com.unique_secure.meposconfiglite.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.dialogs.Dialogs;
import com.unique_secure.meposconfiglite.networkconfig.GetWifiInfo;
import com.unique_secure.meposconfiglite.networkconfig.NetworkConfigAsync;
import com.unique_secure.meposconfiglite.networkconfig.NetworkConfiguration;
import com.unique_secure.meposconfiglite.persistence.ConfigurationNetModes;
import com.unique_secure.meposconfiglite.persistence.MePOSSingleton;
import com.unique_secure.meposconfiglite.persistence.TestSharedPreferences;
import com.uniquesecure.meposconnect.EncryptionMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WifiConfirm extends MePOSAbstractActivity implements NetworkConfigAsync.OnNetworkConfigCompleted, Dialogs.OnDialogCompleted {

    @BindView(R.id.advacedswitch) Switch mAdvacedSwitch;
    @BindView(R.id.advacedTable) TableLayout mAdvacedTable;
    @BindView(R.id.titleWiFiConfirm) TextView mTitleWiFiConfirm;
    @BindView(R.id.lblssidwifi) TextView mLblSSSIDWiFi;
    @BindView(R.id.txtSsidwifi) TextView mTxtSSIDWiFi;
    @BindView(R.id.lblpwdwifi) TextView mLblPwdWiFi;
    @BindView(R.id.txtPasswordwifi) TextView mTxtPasswordWifi;
    @BindView(R.id.lblencryptionwifi) TextView mLblEncryptionWiFi;
    @BindView(R.id.lblnetmaskwifi) TextView mlblnetmaskwifi;
    @BindView(R.id.txtNetmaskwifi) EditText mtxtNetmaskwifi;
    @BindView(R.id.lbldhcpwifi) TextView mlbldhcpwifi;
    @BindView(R.id.txtIpwifi) EditText mtxtIpwifi;
    @BindView(R.id.buttonBackwificonfirm) Button mButtonBackjWiFiConfirm;
    @BindView(R.id.btncontinueclientwifi) Button mBtnContinueClientWiFi;
    @BindView(R.id.spinnerEncryptionClient) Spinner spinnerEncryption;
    @BindView(R.id.autoipbtn) Switch mautoipswitch;

    GetWifiInfo mGetWifiInfo;
    TestSharedPreferences information;
    int attempts = 0;
    Dialogs finderInterface;
    String encryption;


    Dialogs dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_confirm);
        ButterKnife.bind(this);
        dialogs = new Dialogs(this, WifiConfirm.this);
        information = new TestSharedPreferences(this);
        finderInterface = new Dialogs(finderInterface);

        mTitleWiFiConfirm.setTypeface(typefaceAntonioBold);
        mLblSSSIDWiFi.setTypeface(typefaceAvenirLight);
        mTxtSSIDWiFi.setTypeface(typefaceAvenirLight);
        mLblPwdWiFi.setTypeface(typefaceAvenirLight);
        mTxtPasswordWifi.setTypeface(typefaceAvenirLight);
        mLblEncryptionWiFi.setTypeface(typefaceAvenirLight);
        mlblnetmaskwifi.setTypeface(typefaceAvenirLight);
        mtxtNetmaskwifi.setTypeface(typefaceAvenirLight);
        mlbldhcpwifi.setTypeface(typefaceAvenirLight);
        mtxtIpwifi.setTypeface(typefaceAvenirLight);
        mButtonBackjWiFiConfirm.setTypeface(typefaceAvenirLight);
        mBtnContinueClientWiFi.setTypeface(typefaceAvenirLight);
        mGetWifiInfo = new GetWifiInfo(getApplicationContext());
        mTxtSSIDWiFi.setText(mGetWifiInfo.getWifiName());
        mTxtSSIDWiFi.setEnabled(false);
        mTxtSSIDWiFi.setTextColor(getResources().getColor(R.color.gray500));


        String capabilities = mGetWifiInfo.getCapabilitiesFor(mGetWifiInfo.getWifiName());
        try {
            int posCapabilityMostQualified = EncryptionMode.valueOf(capabilities).ordinal();
            spinnerEncryption.setSelection(posCapabilityMostQualified);
        } catch (Exception e) {
            spinnerEncryption.setSelection(EncryptionMode.NONE.ordinal());
        }


        mButtonBackjWiFiConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WifiConfirm.this, ConfigureWiFiActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right); //back
            }
        });

        mAdvacedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean selected) {
                if (selected) {
                    dialogs.WifiConfirmAdvanced(mAdvacedTable, mAdvacedSwitch, mTxtSSIDWiFi);
                } else
                    mAdvacedTable.setVisibility(View.GONE);
            }
        });

        mBtnContinueClientWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attempts = 0;
                if (mTxtSSIDWiFi.getText().toString().length() == 0) {
                    mTxtSSIDWiFi.setError(getString(R.string.field_required));
                }
                if (mTxtPasswordWifi.getText().toString().length() == 0) {
                    mTxtPasswordWifi.setError(getString(R.string.field_required));
                }
                if (mTxtSSIDWiFi.getText().toString().length() == 0 || mTxtPasswordWifi.getText().toString().length() == 0) {
                    dialogs.WifiNullFields();
                } else {
                    String encryption = spinnerEncryption.getSelectedItem().toString();
                    information.saveTestInfo("capabilities", encryption);
                    information.saveTestInfo("wifissid", mTxtSSIDWiFi.getText().toString());
                    information.saveTestInfo("wifipassword", mTxtPasswordWifi.getText().toString());

                    DoConfiguration(encryption);
                }
            }
        });

        mautoipswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean select) {
                if (select) {
                    mlblnetmaskwifi.setText(getString(R.string.netmasklbl));
                    buttons(View.VISIBLE);
                    mautoipswitch.setText("Manual");
                    autoText(1);
                } else {
                    mlblnetmaskwifi.setText(getString(R.string.ip_automatic_dhcp));
                    buttons(View.GONE);
                    mautoipswitch.setText("Automatic");
                    autoText(0);
                }
            }
        });
    }

    private void DoConfiguration(String encryption) {
        NetworkConfiguration networkConfig = new NetworkConfiguration();
        networkConfig.setMode(ConfigurationNetModes.WIFI_CLIENT);
        networkConfig.setSsid(mTxtSSIDWiFi.getText().toString());
        networkConfig.setIp(mtxtIpwifi.getText().toString());
        networkConfig.setNetmask(mtxtNetmaskwifi.getText().toString());
        networkConfig.setPassword(mTxtPasswordWifi.getText().toString());
        networkConfig.setEncryption(encryption);
        new NetworkConfigAsync(WifiConfirm.this, MePOSSingleton.getInstance(), WifiConfirm.this).execute(networkConfig);
    }

    @Override
    public void onComplete(Boolean success) {
        if (success) {
            startActivity(new Intent(WifiConfirm.this, WiFiComplete.class)); //next
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        } else {
            switch (attempts) {
                case 0:
                    attempts += 1;
                    dialogs.DisconectConectTablet(false);
                    break;
                case 1:
                    attempts += 1;
                    dialogs.RetryCommonDialog(getString(R.string.checkrouter), getString(R.string.retry), false);
                    break;
                case 2:
                    attempts += 1;
                    dialogs.ContactSupport(true);
                    break;
            }
            Snackbar.make(findViewById(android.R.id.content), R.string.wifi_client_config_problem, Snackbar.LENGTH_LONG).show();
        }

    }

    public void buttons(int show) {
        mtxtNetmaskwifi.setVisibility(show);
        mlbldhcpwifi.setVisibility(show);
        mtxtIpwifi.setVisibility(show);
    }

    public void autoText(int i) {
        if (i == 0) { // Automatic
            mtxtNetmaskwifi.setText(getString(R.string.netmask));
            mtxtIpwifi.setText(getString(R.string.dhcp));
        } else {
            mtxtNetmaskwifi.setText("");
            mtxtIpwifi.setText("");
        }
    }

    @Override
    public void onCompleteDialogs(Boolean success) {
        if (!success) {
            DoConfiguration(spinnerEncryption.getSelectedItem().toString());
        }
    }
}
