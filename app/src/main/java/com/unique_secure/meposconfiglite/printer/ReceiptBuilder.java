package com.unique_secure.meposconfiglite.printer;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.unique_secure.meposconfiglite.persistence.MeposInfo;
import com.unique_secure.meposconfiglite.persistence.TestSharedPreferences;
import com.uniquesecure.meposconnect.MePOS;
import com.uniquesecure.meposconnect.MePOSReceipt;
import com.uniquesecure.meposconnect.MePOSReceiptFeedLine;
import com.uniquesecure.meposconnect.MePOSReceiptImageLine;
import com.uniquesecure.meposconnect.MePOSReceiptSingleCharLine;
import com.uniquesecure.meposconnect.MePOSReceiptTextLine;

import java.io.IOException;
import java.io.InputStream;


public class ReceiptBuilder {
    private Context context;
    TestSharedPreferences information;
    MeposInfo myDevice;

    public ReceiptBuilder(Context context) {
        this.context = context;
    }


    public MePOSReceipt BuildPrintReceipt(String header) {

        myDevice = new MeposInfo(context);
        MePOSReceipt receipt = new MePOSReceipt();
        MePOSReceiptTextLine rcpt = new MePOSReceiptTextLine();

        receipt.addLine(new MePOSReceiptImageLine(getBitmapFromAsset(context, "ic_launcher_bmp.bmp")));
        receipt.addLine(new MePOSReceiptFeedLine(1));

        rcpt.setText("My device", MePOS.TEXT_STYLE_BOLD, MePOS.TEXT_SIZE_NORMAL, MePOS.TEXT_POSITION_CENTER);
        receipt.addLine(rcpt);
        receipt.addLine(new MePOSReceiptFeedLine(1));

        rcpt = new MePOSReceiptTextLine();
        rcpt.setText("IP address    :" + myDevice.IP(), MePOS.TEXT_STYLE_NONE, MePOS.TEXT_STYLE_NONE, MePOS.TEXT_POSITION_LEFT);
        receipt.addLine(rcpt);

        rcpt = new MePOSReceiptTextLine();
        rcpt.setText("Mac address   :" + myDevice.MacAddress(), MePOS.TEXT_STYLE_NONE, MePOS.TEXT_STYLE_NONE, MePOS.TEXT_POSITION_LEFT);
        receipt.addLine(rcpt);

        rcpt = new MePOSReceiptTextLine();
        rcpt.setText("SSID          :" + myDevice.SSID(), MePOS.TEXT_STYLE_NONE, MePOS.TEXT_STYLE_NONE, MePOS.TEXT_POSITION_LEFT);
        receipt.addLine(rcpt);

        rcpt = new MePOSReceiptTextLine();
        rcpt.setText("Serial number :" + myDevice.Serial(), MePOS.TEXT_STYLE_NONE, MePOS.TEXT_STYLE_NONE, MePOS.TEXT_POSITION_LEFT);
        receipt.addLine(rcpt);

        rcpt = new MePOSReceiptTextLine();
        rcpt.setText("Firmware      :" + myDevice.FirmWare(), MePOS.TEXT_STYLE_NONE, MePOS.TEXT_STYLE_NONE, MePOS.TEXT_POSITION_LEFT);
        receipt.addLine(rcpt);

        receipt.addLine(new MePOSReceiptSingleCharLine('-'));

        return receipt;
    }

    public Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            Log.e("No logo image", e.getLocalizedMessage());
        }
        return bitmap;
    }


}


