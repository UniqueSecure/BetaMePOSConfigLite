package com.unique_secure.meposconfiglite.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.persistence.TestSharedPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WiFiComplete extends MePOSAbstractActivity {

    @BindView(R.id.titleWiFiComplete) TextView mTitleComplete;
    @BindView(R.id.lblMessageComplete) TextView mMessageComplete;
    @BindView(R.id.ipFoundTxtComplete) TextView mIpFountTxt;
    @BindView(R.id.buttonBackwifiComplete) Button mBackComplete;
    @BindView(R.id.buttonNextComplete) Button mNextComplete;

    TestSharedPreferences information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_complete);
        ButterKnife.bind(this);

        mTitleComplete.setTypeface(typefaceAntonioBold);
        mMessageComplete.setTypeface(typefaceAvenirLight);
        mIpFountTxt.setTypeface(typefaceAntonioBold);
        mBackComplete.setTypeface(typefaceAvenirLight);
        mNextComplete.setTypeface(typefaceAvenirLight);
        information = new TestSharedPreferences(this);
        mIpFountTxt.setText(information.getTestInfo("wifiipaddress"));

        mNextComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WiFiComplete.this, WelcomeActivity.class)); //next
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });

        mBackComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WiFiComplete.this, WifiConfirm.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right); //back

            }
        });

    }
}
