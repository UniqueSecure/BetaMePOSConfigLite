package com.unique_secure.meposconfiglite.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.dialogs.Dialogs;
import com.unique_secure.meposconfiglite.networkconfig.NetworkConfigAsync;
import com.unique_secure.meposconfiglite.networkconfig.NetworkConfiguration;
import com.unique_secure.meposconfiglite.persistence.ConfigurationNetModes;
import com.unique_secure.meposconfiglite.persistence.MePOSSingleton;
import com.unique_secure.meposconfiglite.persistence.TestSharedPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WifiConfirm extends MePOSAbstractActivity implements NetworkConfigAsync.OnNetworkConfigCompleted {

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


    TestSharedPreferences information;


    Dialogs dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_confirm);
        ButterKnife.bind(this);
        dialogs = new Dialogs(this);
        information = new TestSharedPreferences(this);

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
                    dialogs.WifiConfirmAdvanced(mAdvacedTable, mAdvacedSwitch);
                } else
                    mAdvacedTable.setVisibility(View.GONE);
            }
        });

        mBtnContinueClientWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                    NetworkConfiguration networkConfig = new NetworkConfiguration();
                    networkConfig.setMode(ConfigurationNetModes.WIFI_CLIENT);
                    networkConfig.setSsid(mTxtSSIDWiFi.getText().toString());
                    networkConfig.setIp(mtxtIpwifi.getText().toString());
                    networkConfig.setNetmask(mtxtNetmaskwifi.getText().toString());
                    networkConfig.setPassword(mTxtPasswordWifi.getText().toString());
                    networkConfig.setEncryption(encryption);
                    new NetworkConfigAsync(WifiConfirm.this, MePOSSingleton.getInstance(), WifiConfirm.this).execute(networkConfig);
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

    @Override
    public void onComplete(Boolean success) {
        if (success) {
            startActivity(new Intent(WifiConfirm.this, WiFiComplete.class)); //next
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        } else {
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
}
