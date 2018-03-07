package com.unique_secure.meposconfiglite.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.dialogs.Dialogs;
import com.unique_secure.meposconfiglite.networkconfig.NetworkConfigAsync;
import com.unique_secure.meposconfiglite.networkconfig.NetworkConfiguration;
import com.unique_secure.meposconfiglite.persistence.ConfigurationNetModes;
import com.unique_secure.meposconfiglite.persistence.MePOSSingleton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfigureWiFiActivity extends MePOSAbstractActivity implements NetworkConfigAsync.OnNetworkConfigCompleted {

    @BindView(R.id.lblLetsConfigure) TextView mLblLetsConfigure;
    @BindView(R.id.lblWiFi) TextView mLblWiFi;
    @BindView(R.id.btnWiFi) ImageButton mBtnWiFi;
    @BindView(R.id.btnEthernet) ImageButton mBtnEthernet;
    @BindView(R.id.buttonBackWiFi) Button mButtonBackWiFi;
    @BindView(R.id.buttonNextWiFi) Button mButtonNextWiFi;
    @BindView(R.id.lblMessage) TextView mLblMessage;
    @BindView(R.id.txtByWiFi) TextView mTxtByWiFi;
    @BindView(R.id.txtByEthernet) TextView mTxtByEthernet;
    NetworkConfiguration networkConfig = new NetworkConfiguration();

    private IntentFilter intfilt;
    private boolean enableOrDisable = false;
    Dialogs dialogs;



    BroadcastReceiver enableDisableBrodcastRecr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            enableOrDisable = intent.getExtras().getBoolean("enableordisable");
            if (enableOrDisable) {
                mBtnWiFi.setBackgroundResource(R.drawable.ic_wifi);
                mBtnEthernet.setBackgroundResource(R.drawable.ic_ethernet);
            } else {
                mBtnWiFi.setBackgroundResource(R.drawable.ic_wifi_gray);
                mBtnEthernet.setBackgroundResource(R.drawable.ic_ethernet_gray);
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(enableDisableBrodcastRecr, intfilt);

        mBtnWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enableOrDisable) {
                    startActivity(new Intent(ConfigureWiFiActivity.this, WifiConfirm.class)); //next
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                } else {
                    dialogs.PlugAndUnplugTablet(getString(R.string.unplug_plug_txt), getString(R.string.ok));
                }
            }
        });

        mBtnEthernet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enableOrDisable) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(ConfigureWiFiActivity.this).create();
                    alertDialog.setTitle(R.string.dialogwifi_modes_alerttitle);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(getString(R.string.wificonfirmmsg));
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            networkConfig.setMode(ConfigurationNetModes.ETHERNET_CLIENT);
                            new NetworkConfigAsync(ConfigureWiFiActivity.this, MePOSSingleton.getInstance(), ConfigureWiFiActivity.this).execute(networkConfig);
                        }
                    });
                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    dialogs.PlugAndUnplugTablet(getString(R.string.unplug_plug_txt), getString(R.string.ok));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(enableDisableBrodcastRecr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_wi_fi);
        ButterKnife.bind(this);
        dialogs = new Dialogs(ConfigureWiFiActivity.this);


        mLblLetsConfigure.setTypeface(typefaceAntonioRegular);
        mLblWiFi.setTypeface(typefaceAntonioBold);
        mLblMessage.setTypeface(typefaceAvenirLight);
        mButtonNextWiFi.setTypeface(typefaceAvenirLight);
        mButtonBackWiFi.setTypeface(typefaceAvenirLight);
        mTxtByWiFi.setTypeface(typefaceAvenirLight);
        mTxtByEthernet.setTypeface(typefaceAvenirLight);

        intfilt = new IntentFilter();
        intfilt.addAction("EnableDisableAction");


        mButtonBackWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfigureWiFiActivity.this, WelcomeActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right); //back
            }
        });


    }

    @Override
    public void onComplete(Boolean success) {
        startActivity(new Intent(ConfigureWiFiActivity.this, WiFiComplete.class));
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left); //next
    }
}
