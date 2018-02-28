package com.unique_secure.meposconfiglite.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.persistence.MePOSSingleton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends MePOSAbstractActivity {
    @BindView(R.id.lblHello) TextView mLblHello;
    @BindView(R.id.lblSubTitle) TextView mLblTap;
    @BindView(R.id.btnconnect) Button mBtnConnect;
    @BindView(R.id.img_circle) ImageView imgCircle;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (isMePOSConnected()) {
            mBtnConnect.setText(getString(R.string.connect));
            mLblTap.setText(getString(R.string.mepos_found_lbl));
        } else {
            mBtnConnect.setText(getString(R.string.next));
            mLblTap.setText(getString(R.string.fmepos_not_found_lbl));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        mLblHello.setTypeface(typefaceAntonioBold);
        mLblTap.setTypeface(typefaceAvenirLight);
        mBtnConnect.setTypeface(typefaceAvenirLight);
        findMePOSUSB();

        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnConnect.setEnabled(false);
                RotateCircle();
            }
        });
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
