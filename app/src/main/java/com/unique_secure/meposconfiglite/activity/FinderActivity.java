package com.unique_secure.meposconfiglite.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.dialogs.Dialogs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinderActivity extends MePOSAbstractActivity implements Dialogs.OnDialogCompleted {

    @BindView(R.id.lblLetsConfigure) TextView mLblLetsConfigure;
    @BindView(R.id.imgProgressCircle) ImageView lblImgProgressCircle;
    ObjectAnimator anim;
    Intent batteryStatus;
    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Dialogs dialogs;
    int attempts = 0;
    private Dialog finalDialog;


    @Override
    protected void onResume() {
        super.onResume();
        AnimateCircle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder);
        ButterKnife.bind(this);
        mLblLetsConfigure.setTypeface(typefaceAvenirBold);
        batteryStatus = this.registerReceiver(null, ifilter);
        dialogs = new Dialogs(FinderActivity.this, FinderActivity.this);


        lblImgProgressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimateCircle();
            }
        });
    }

    private void AnimateCircle() {
        lblImgProgressCircle.setClickable(false);
        anim = ObjectAnimator.ofFloat(lblImgProgressCircle, "Rotation", 0f, -360f);
        anim.setDuration(5000);
        anim.start();

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                lblImgProgressCircle.setClickable(true);
                if (isMePOSConnected()) {
                    startActivity(new Intent(FinderActivity.this, ConfigureWiFiActivity.class));
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                } else {
                    loadBatterySection();
                }
            }
        });
    }


    private void loadBatterySection() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryInfoReceiver, intentFilter);
    }


    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryData(intent);
        }
    };

    private void updateBatteryData(Intent intent) {
        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        if (present) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                Toast.makeText(FinderActivity.this, "Unplug and plug the Tablet", Toast.LENGTH_SHORT).show();

            } else {
                switch (attempts) {
                    case 0:
                        attempts += 1;
                        dialogs.RetryCommonDialog(getString(R.string.checkPower), getString(R.string.retry), attempts);
                        break;
                    case 1:
                        attempts += 1;
                        dialogs.RetryCommonDialog(getString(R.string.reconectMePOS), getString(R.string.retry), attempts);
                        break;
                    case 2:
                        attempts += 1;
                        dialogs.RetryCommonDialog(getString(R.string.cyclepowerMePOS), getString(R.string.retry), attempts);
                        break;
                    case 3:
                        attempts += 1;
                        dialogs.RetryCommonDialog(getString(R.string.contactSupportTeam), getString(R.string.contact), attempts);
                        break;
                }
            }

        }
        Unregister();
    }

    private void Unregister() {
        try {
            unregisterReceiver(batteryInfoReceiver);
        } catch (Exception e) {
            Log.e("Battery", e.getMessage(), e);
        }
    }

    public void FinalDialog() {
        finalDialog = new Dialog(this);
        finalDialog.setContentView(R.layout.dialog_final);
        TextView mTextCheckMePOSdialog = finalDialog.findViewById(R.id.textTitleFinal);
        Button mButtonContinue = finalDialog.findViewById(R.id.btnContinueFinal);
        Button mButtonCancel = finalDialog.findViewById(R.id.btnCancelFinal);
        mTextCheckMePOSdialog.setTypeface(typefaceAvenirLight);
        mButtonContinue.setTypeface(typefaceAvenirLight);
        mButtonCancel.setTypeface(typefaceAvenirLight);
        finalDialog.setCancelable(false);
        mButtonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
                startActivity(new Intent(FinderActivity.this, WelcomeActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right); //back
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
                lblImgProgressCircle.setClickable(false);
            }
        });

        finalDialog.show();
    }

    @Override
    public void onComplete(Boolean success) {
        FinalDialog();
    }
}
