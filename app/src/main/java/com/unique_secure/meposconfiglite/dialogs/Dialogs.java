package com.unique_secure.meposconfiglite.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.persistence.ConfigureImage;

public class Dialogs extends MePOSAbstractActivity {

    public interface OnDialogCompleted {
        void onCompleteDialogs(Boolean success);
    }

    Context context;
    ConfigureImage image;
    private Dialog checkMePOSDialog,
            disconnectReconnectDialog,
            contactSupportDialog,
            wifiConfirmAdvancedDialog,
            notNullFieldsDialog,
            retryCommonDialog;
    OnDialogCompleted completed;

    public Dialogs(Context context, OnDialogCompleted callback) {
        this.context = context;
        this.completed = callback;
    }

    public Dialogs(Context context) {
        this.context = context;
    }

    public void CheckTheMePOSDialog(String title, final boolean attempts) {
        checkMePOSDialog = new Dialog(context);
        checkMePOSDialog.setContentView(R.layout.dialog_check_the_mepos);
        TextView mTextCheckMePOSdialog = checkMePOSDialog.findViewById(R.id.textCheckMePOSdialog);
        Button mBtnGo = checkMePOSDialog.findViewById(R.id.btngo);
        mTextCheckMePOSdialog.setTypeface(typefaceAvenirLight);
        mTextCheckMePOSdialog.setText(title);
        mBtnGo.setTypeface(typefaceAvenirLight);
        checkMePOSDialog.setCancelable(false);
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completed.onCompleteDialogs(attempts);
                checkMePOSDialog.dismiss();
            }
        });
        checkMePOSDialog.show();
    }

    public void DisconectConectTablet(final boolean attempts) {
        disconnectReconnectDialog = new Dialog(context);
        disconnectReconnectDialog.setContentView(R.layout.dialog_disconect_reconect);
        TextView mTextCheckMePOSdialog = disconnectReconnectDialog.findViewById(R.id.textCheckMePOSdialog);
        Button mBtnGo = disconnectReconnectDialog.findViewById(R.id.btngo);
        mTextCheckMePOSdialog.setTypeface(typefaceAvenirLight);
        mBtnGo.setTypeface(typefaceAvenirLight);
        disconnectReconnectDialog.setCancelable(false);
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completed.onCompleteDialogs(attempts);
                disconnectReconnectDialog.dismiss();
            }
        });
        disconnectReconnectDialog.show();
    }

    public void ContactSupport(final boolean attempts) {
        contactSupportDialog = new Dialog(context);
        contactSupportDialog.setContentView(R.layout.dialog_contact_support);
        TextView mTextCheckMePOSdialog = contactSupportDialog.findViewById(R.id.textContactSupport);
        Button mBtnGo = contactSupportDialog.findViewById(R.id.btngo);
        mTextCheckMePOSdialog.setTypeface(typefaceAvenirLight);
        mBtnGo.setTypeface(typefaceAvenirLight);
        contactSupportDialog.setCancelable(false);
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completed.onCompleteDialogs(attempts);
                contactSupportDialog.dismiss();
            }
        });
        contactSupportDialog.show();
    }

    public void WifiConfirmAdvanced(final TableLayout mAdvacedTable, final Switch mAdvacedSwitch, final TextView mTxtSSIDWiFi) {
        wifiConfirmAdvancedDialog = new Dialog(context);
        wifiConfirmAdvancedDialog.setContentView(R.layout.dialog_wifi_confirm_advanced);
        TextView mTextCheckMePOSdialog = wifiConfirmAdvancedDialog.findViewById(R.id.textCheckMePOSdialog);
        Button mButtonContinue = wifiConfirmAdvancedDialog.findViewById(R.id.btnContinue);
        Button mButtonCancel = wifiConfirmAdvancedDialog.findViewById(R.id.btnCancel);
        mTextCheckMePOSdialog.setTypeface(typefaceAvenirLight);
        mButtonContinue.setTypeface(typefaceAvenirLight);
        mButtonCancel.setTypeface(typefaceAvenirLight);
        wifiConfirmAdvancedDialog.setCancelable(false);
        mButtonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdvacedTable.setVisibility(View.VISIBLE);
                mTxtSSIDWiFi.setText("");
                mTxtSSIDWiFi.setEnabled(true);
                wifiConfirmAdvancedDialog.dismiss();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiConfirmAdvancedDialog.dismiss();
                mAdvacedSwitch.setChecked(false);
            }
        });

        wifiConfirmAdvancedDialog.show();
    }

    public void WifiNullFields() {
        notNullFieldsDialog = new Dialog(context);
        notNullFieldsDialog.setContentView(R.layout.dialog_wifi_confirm_advanced);
        LinearLayout layout = notNullFieldsDialog.findViewById(R.id.imageDialogLayout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        TextView mTextCheckMePOSdialog = notNullFieldsDialog.findViewById(R.id.textCheckMePOSdialog);
        Button mButtonContinue = notNullFieldsDialog.findViewById(R.id.btnContinue);
        Button mButtonCancel = notNullFieldsDialog.findViewById(R.id.btnCancel);
        mTextCheckMePOSdialog.setTypeface(typefaceAvenirLight);
        mTextCheckMePOSdialog.setText(R.string.field_required);
        mButtonContinue.setTypeface(typefaceAvenirLight);
        mButtonContinue.setText(context.getString(R.string.ok));
        mButtonCancel.setTypeface(typefaceAvenirLight);
        mButtonCancel.setVisibility(View.GONE);
        notNullFieldsDialog.setCancelable(false);
        params.width = 500;
        params.height = 250;
        layout.setLayoutParams(params);

        mButtonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notNullFieldsDialog.dismiss();
            }
        });
        notNullFieldsDialog.show();
    }


    public void RetryCommonDialog(String title, String button, final boolean attempts) {
        retryCommonDialog = new Dialog(context);
        retryCommonDialog.setContentView(R.layout.dialog_retry_common);
        TextView mTextTitle = retryCommonDialog.findViewById(R.id.textTitle);
        Button mBtnAction = retryCommonDialog.findViewById(R.id.btnAction);
        mTextTitle.setText(title);
        mBtnAction.setText(button);
        mTextTitle.setTypeface(typefaceAvenirLight);
        mBtnAction.setTypeface(typefaceAvenirLight);
        retryCommonDialog.setCancelable(false);
        mBtnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completed.onCompleteDialogs(attempts);
                retryCommonDialog.dismiss();
            }
        });
        retryCommonDialog.show();
    }

    public void PlugAndUnplugTablet(String title, String button) {
        retryCommonDialog = new Dialog(context);
        retryCommonDialog.setContentView(R.layout.dialog_retry_common);
        TextView mTextTitle = retryCommonDialog.findViewById(R.id.textTitle);
        Button mBtnAction = retryCommonDialog.findViewById(R.id.btnAction);
        mTextTitle.setText(title);
        mBtnAction.setText(button);
        mTextTitle.setTypeface(typefaceAvenirLight);
        mBtnAction.setTypeface(typefaceAvenirLight);
        retryCommonDialog.setCancelable(false);
        mBtnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retryCommonDialog.dismiss();
            }
        });
        retryCommonDialog.show();
    }


    public void TroubleDialog(String title, final boolean attempts) {
        image = new ConfigureImage(context);
        final Dialog printtestdialog = new Dialog(context);
        printtestdialog.setContentView(R.layout.dialog_connect_disconnect);
        printtestdialog.setTitle(title);
        TextView mPrintdialogtext = printtestdialog.findViewById(R.id.printdialogtext);
        ImageView mPrintDialogImage = printtestdialog.findViewById(R.id.printDialogImage);
        Button mButtonyesprint = printtestdialog.findViewById(R.id.buttonyesprint);
        mPrintdialogtext.setTypeface(typefaceAvenirLight);
        mButtonyesprint.setTypeface(typefaceAvenirLight);
        mPrintdialogtext.setText(title);
        try {
            int idImage = context.getResources().getIdentifier("raw/reconnect", null, context.getPackageName());
            image.SetImage(idImage, mPrintDialogImage);
        } catch (Exception e){
            e.printStackTrace();
        }
        mButtonyesprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printtestdialog.dismiss();
                completed.onCompleteDialogs(attempts);
            }
        });
        printtestdialog.show();
    }
}
