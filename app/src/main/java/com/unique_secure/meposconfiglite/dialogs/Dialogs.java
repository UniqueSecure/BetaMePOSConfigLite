package com.unique_secure.meposconfiglite.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;

public class Dialogs extends MePOSAbstractActivity {

    public interface OnDialogCompleted {
        void onComplete(Boolean success);
    }

    Context context;
    private Dialog checkMePOSDialog,
            disconnectReconnectDialog,
            contactSupportDialog,
            wifiConfirmAdvancedDialog,
            notNullFieldsDialog,
            retryCommonDialog,
            finalDialog;
    OnDialogCompleted completed;


    public Dialogs(Context context, OnDialogCompleted callback) {
        this.context = context;
        this.completed = callback;
    }

    public Dialogs(Context context){
        this.context = context;
    }

    public void CheckTheMePOSDialog() {
        checkMePOSDialog = new Dialog(context);
        checkMePOSDialog.setContentView(R.layout.dialog_check_the_mepos);
        TextView mTextCheckMePOSdialog = checkMePOSDialog.findViewById(R.id.textCheckMePOSdialog);
        Button mBtnGo = checkMePOSDialog.findViewById(R.id.btngo);
        mTextCheckMePOSdialog.setTypeface(typefaceAvenirLight);
        mBtnGo.setTypeface(typefaceAvenirLight);
        checkMePOSDialog.setCancelable(false);
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMePOSDialog.dismiss();
            }
        });
        checkMePOSDialog.show();
    }

    public void DisconectConectTablet() {
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
                disconnectReconnectDialog.dismiss();
            }
        });
        disconnectReconnectDialog.show();
    }

    public void ContactSupport() {
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
                contactSupportDialog.dismiss();
            }
        });
        contactSupportDialog.show();
    }

    public void WifiConfirmAdvanced(final TableLayout mAdvacedTable, final Switch mAdvacedSwitch) {
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


    public void RetryCommonDialog(String title, String button, final int attempts) {
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

                if (attempts==4){
                    completed.onComplete(true);
                }

                retryCommonDialog.dismiss();
            }
        });
        retryCommonDialog.show();
    }

}
