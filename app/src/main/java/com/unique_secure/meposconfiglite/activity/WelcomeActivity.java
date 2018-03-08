package com.unique_secure.meposconfiglite.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unique_secure.meposconfiglite.BuildConfig;
import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends MePOSAbstractActivity {
    @BindView(R.id.lblHello) TextView mLblHello;
    @BindView(R.id.lblSubTitle) TextView mLblSubtitle;
    @BindView(R.id.btnconnect) Button mBtnConnect;
    @BindView(R.id.img_circle) ImageView imgCircle;
    @BindView(R.id.linear_welcome) LinearLayout mLinearWelcome;
    @BindView(R.id.versionName) TextView mVersionName;
    private IntentFilter intfilt;
    private boolean enableOrDisable = false;


    BroadcastReceiver enableDisableBrodcastRecr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            enableOrDisable = intent.getExtras().getBoolean("enableordisable");
            if (enableOrDisable) {
               SetToConnected();
            } else {
                SetToDisconnected();
            }

        }
    };


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(enableDisableBrodcastRecr, intfilt);
    }

    private void SetToConnected(){
        mLblHello.setText(getString(R.string.mepos_found));
        mLblHello.setTextColor(getResources().getColor(R.color.white));
        mLblSubtitle.setText(getString(R.string.mepos_found_lbl));
        mLblSubtitle.setTextColor(getResources().getColor(R.color.white));
        mBtnConnect.setText(getString(R.string.connect));
        mBtnConnect.setTextColor(getResources().getColor(R.color.colorAccent));
        mBtnConnect.setBackground(getResources().getDrawable(R.drawable.ic_brightness_1_white_24dp));
        mLinearWelcome.setBackground(getResources().getDrawable(R.drawable.meposconfig8));
        imgCircle.setBackground(getResources().getDrawable(R.drawable.ic_spininng_circle));
    }

    private void SetToDisconnected(){
        mLblHello.setText(getString(R.string.hello));
        mLblHello.setTextColor(getResources().getColor(R.color.black));
        mLblSubtitle.setText(getString(R.string.tap_to_connect));
        mLblSubtitle.setTextColor(getResources().getColor(R.color.black));
        mBtnConnect.setText(getString(R.string.next));
        mBtnConnect.setTextColor(getResources().getColor(R.color.white));
        mBtnConnect.setBackground(getResources().getDrawable(R.drawable.ic_brightness_1_black_24dp));
        mLinearWelcome.setBackground(getResources().getDrawable(R.drawable.meposconfig5));
        imgCircle.setBackground(getResources().getDrawable(R.drawable.ic_spininng_circle_accent));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        mLblHello.setTypeface(typefaceAntonioBold);
        mLblSubtitle.setTypeface(typefaceAvenirLight);
        mBtnConnect.setTypeface(typefaceAvenirLight);
        mVersionName.setTypeface(typefaceAvenirLight);
        findMePOSUSB();


        if(CheckMePOSPermissions()){
            SetToConnected();
        } else{
            SetToDisconnected();
        }

        intfilt = new IntentFilter();
        intfilt.addAction("EnableDisableAction");

        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnConnect.setEnabled(false);
                RotateCircle();
            }
        });

        mVersionName.setText(getString(R.string.version_txt, BuildConfig.VERSION_NAME));
    }

    private void RotateCircle() {
        ObjectAnimator bigAnim = ObjectAnimator.ofFloat(imgCircle, "Rotation", 0f, 360f);
        bigAnim.setDuration(5000);
        bigAnim.start();


        bigAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBtnConnect.setEnabled(true);
                if (isMePOSConnected()) {
                    startActivity(new Intent(WelcomeActivity.this, ConfigureWiFiActivity.class));
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                } else {
                    startActivity(new Intent(WelcomeActivity.this, FinderActivity.class));
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                }
            }
        });
    }
}
