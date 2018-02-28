package com.unique_secure.meposconfiglite.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unique_secure.meposconfiglite.MePOSAbstractActivity;
import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.dialogs.Dialogs;
import com.uniquesecure.meposconnect.MePOSColorCodes;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeposFoundActivity extends MePOSAbstractActivity {
    @BindView(R.id.lblMePOSFound) TextView mLblMePOSFound;
    @BindView(R.id.btnconnect) Button mBtnConnect;
    @BindView(R.id.image_mepos) ImageView mImageMepos;
    Dialogs dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mepos_found);
        ButterKnife.bind(this);
        mLblMePOSFound.setTypeface(typefaceAvenirBold);
        mBtnConnect.setTypeface(typefaceAvenirLight);
        dialogs = new Dialogs(this);

        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.CheckTheMePOSDialog();
                dialogs.DisconectConectTablet();
                dialogs.ContactSupport();
            }
        });

        mImageMepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MeposFoundActivity.this, WifiConfirm.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });

    }

}
