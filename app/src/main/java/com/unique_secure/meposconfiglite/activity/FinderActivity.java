package com.unique_secure.meposconfiglite.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.dialogs.Dialogs;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinderActivity extends MePOSAbstractActivity implements Dialogs.OnDialogCompleted {

    @BindView(R.id.lblLetsConfigure) TextView mLblLetsConfigure;
    Intent batteryStatus;
    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Dialogs dialogs;
    Dialogs finderInterface;
    int attempts = 0;
    private Dialog finalDialog;
    private Dialog finderDialog;


    @Override
    protected void onResume() {
        super.onResume();
        FinderDialog();
        attempts = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder);
        ButterKnife.bind(this);
        mLblLetsConfigure.setTypeface(typefaceAvenirBold);
        batteryStatus = this.registerReceiver(null, ifilter);
        dialogs = new Dialogs(FinderActivity.this, FinderActivity.this);
        finderInterface = new Dialogs(finderInterface);
    }


    public void FinderDialog() {
        finderDialog = new Dialog(this, R.style.printingDialogStyle);
        finderDialog.setContentView(R.layout.dialog_common_printing);
        TextView mTitle = finderDialog.findViewById(R.id.progress_dialog_title);
        TextView mMessage = finderDialog.findViewById(R.id.progress_dialog_message);
        mTitle.setText(R.string.searching);
        mTitle.setTypeface(typefaceAntonioBold);
        mMessage.setTypeface(typefaceAvenirLight);
        finderDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isMePOSConnected()) {
                    startActivity(new Intent(FinderActivity.this, ConfigureWiFiActivity.class));
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                } else {
                    loadBatterySection();
                }
            }
        });

        finderDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        finderDialog.dismiss();
                        timer2.cancel();
                    }
                }, 2000);
            }
        });

        finderDialog.show();


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
                switch (attempts) {
                    case 0:
                        attempts += 1;
                        dialogs.TroubleDialog(getResources().getString(R.string.dialog_cdisconect_reconect), false);
                        break;
                    case 1:
                        attempts += 1;
                        dialogs.RetryCommonDialog(getString(R.string.cyclepowerMePOS), getString(R.string.next), false);
                        break;
                    case 2:
                        dialogs.ContactSupport(true);
                        break;
                }
            } else {
                switch (attempts) {
                    case 0:
                        attempts += 1;
                        dialogs.CheckTheMePOSDialog(getResources().getString(R.string.dialog_check_mepos_text),false);
                        break;
                    case 1:
                        attempts += 1;
                        dialogs.TroubleDialog(getResources().getString(R.string.dialog_cdisconect_reconect), false);
                        break;
                    case 2:
                        attempts += 1;
                        dialogs.CheckTheMePOSDialog(getResources().getString(R.string.unplug_power_source),false);
                        break;
                    case 3:
                        dialogs.ContactSupport(true);
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
                //lblImgProgressCircle.setClickable(false);
            }
        });

        finalDialog.show();
    }


    @Override
    public void onCompleteDialogs(Boolean success) {
        if (success) {
            FinalDialog();
        } else {
            FinderDialog();
        }

    }


}
