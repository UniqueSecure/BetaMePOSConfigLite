package com.unique_secure.meposconfiglite.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.persistence.MePOSSingleton;
import com.unique_secure.meposconfiglite.persistence.TestSharedPreferences;
import com.unique_secure.meposconfiglite.printer.ReceiptBuilder;
import com.uniquesecure.meposconnect.MePOSColorCodes;
import com.uniquesecure.meposconnect.MePOSConnectionType;
import com.uniquesecure.meposconnect.MePOSException;
import com.uniquesecure.meposconnect.MePOSPrinterCallback;
import com.uniquesecure.meposconnect.MePOSReceipt;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WiFiComplete extends MePOSAbstractActivity implements MePOSPrinterCallback {

    @BindView(R.id.titleWiFiComplete) TextView mTitleComplete;
    @BindView(R.id.lblMessageComplete) TextView mMessageComplete;
    @BindView(R.id.ipFoundTxtComplete) TextView mIpFountTxt;
    @BindView(R.id.buttonBackwifiComplete) Button mBackComplete;
    @BindView(R.id.buttonNextComplete) Button mNextComplete;
    @BindView(R.id.btnPrint) Button mBtnPrint;
    private Dialog progressDialog;
    TestSharedPreferences information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_complete);
        ButterKnife.bind(this);
        information = new TestSharedPreferences(this);

        if (isMePOSConnected()) {
        }

        mTitleComplete.setTypeface(typefaceAntonioBold);
        mMessageComplete.setTypeface(typefaceAvenirLight);
        mIpFountTxt.setTypeface(typefaceAntonioBold);
        mBackComplete.setTypeface(typefaceAvenirLight);
        mNextComplete.setTypeface(typefaceAvenirLight);
        information = new TestSharedPreferences(this);
        mIpFountTxt.setText(information.getTestInfo("wifiipaddress"));

        mBtnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMePOSConnected()) {
                    printTicket();
                }
            }
        });

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

    public void printTicket() {
        mBtnPrint.setClickable(false);
        PrintAsync printAsync = new PrintAsync(WiFiComplete.this);
        printAsync.execute();
    }

    @Override
    public void onPrinterStarted(MePOSConnectionType mePOSConnectionType, String s) {
        MePOSSingleton.getInstance().setCosmeticLedCol(MePOSColorCodes.COSMETIC_RED);
    }

    @Override
    public void onPrinterCompleted(MePOSConnectionType mePOSConnectionType, String s) {
        MePOSSingleton.getInstance().setCosmeticLedCol(MePOSColorCodes.COSMETIC_GREEN);
        try {
            Thread.sleep(500);
            MePOSSingleton.getInstance().setCosmeticLedCol(MePOSColorCodes.LED_OFF);
            progressDialog.dismiss();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPrinterError(MePOSException e) {
        try {
            Thread.sleep(500);
            MePOSSingleton.getInstance().setCosmeticLedCol(MePOSColorCodes.LED_OFF);
            progressDialog.dismiss();
        } catch (InterruptedException d) {
            d.printStackTrace();
        }
    }

    public class PrintAsync extends AsyncTask<String, Integer, Boolean> {
        private Activity activity;

        public PrintAsync(WiFiComplete activity) {
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                MePOSReceipt mePOSReceipt = new ReceiptBuilder(WiFiComplete.this).BuildPrintReceipt("Printing IP");
                MePOSSingleton.getInstance().print(mePOSReceipt, WiFiComplete.this);
            } catch (Exception e) {
                progressDialog.dismiss();
                return false;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new Dialog(activity, R.style.printingDialogStyle);
            progressDialog.setContentView(R.layout.dialog_common_printing);
            TextView mTitle = progressDialog.findViewById(R.id.progress_dialog_title);
            TextView mMessage = progressDialog.findViewById(R.id.progress_dialog_message);
            mTitle.setTypeface(typefaceAntonioBold);
            mMessage.setTypeface(typefaceAvenirLight);
            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mBtnPrint.setClickable(true);
                }
            });
            progressDialog.show();
        }
    }
}
