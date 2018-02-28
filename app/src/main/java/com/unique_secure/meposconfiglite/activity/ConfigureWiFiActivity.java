package com.unique_secure.meposconfiglite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfigureWiFiActivity extends MePOSAbstractActivity {

    @BindView(R.id.lblLetsConfigure) TextView mLblLetsConfigure;
    @BindView(R.id.lblWiFi) TextView mLblWiFi;
    @BindView(R.id.btngo) Button mBtnGo;
    @BindView(R.id.buttonBackWiFi) Button mButtonBackWiFi;
    @BindView(R.id.buttonNextWiFi) Button mButtonNextWiFi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_wi_fi);
        ButterKnife.bind(this);

        mLblLetsConfigure.setTypeface(typefaceAntonioRegular);
        mLblWiFi.setTypeface(typefaceAntonioBold);
        mBtnGo.setTypeface(typefaceAvenirMedium);
        mButtonNextWiFi.setTypeface(typefaceAvenirLight);
        mButtonBackWiFi.setTypeface(typefaceAvenirLight);

        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfigureWiFiActivity.this, WifiConfirm.class)); //next
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });

        mButtonBackWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfigureWiFiActivity.this, WelcomeActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right); //back
            }
        });
    }
}
