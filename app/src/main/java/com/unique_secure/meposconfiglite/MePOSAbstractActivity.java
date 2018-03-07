package com.unique_secure.meposconfiglite;


import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.unique_secure.meposconfiglite.persistence.MePOSSingleton;
import com.unique_secure.meposconfiglite.usb.UsbDeviceHelper;
import com.uniquesecure.meposconnect.MePOS;
import com.uniquesecure.meposconnect.MePOSConnectionType;

import java.util.HashMap;

public class MePOSAbstractActivity extends AppCompatActivity {
    public Typeface typefaceAvenir;
    public Typeface typefaceAvenirLight;
    public Typeface typefaceAvenirBold;
    public Typeface typefaceAntonioBold;
    public Typeface typefaceAntonioRegular;
    public Typeface typefaveAntonioLight;
    public Typeface typefaceAvenirMedium;
    private UsbDeviceHelper usbDeviceHelper;
    private PendingIntent mPermissionIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typefaceAvenir = Typeface.createFromAsset(getAssets(), "fonts/avenirltstd.medium.otf");
        typefaceAvenirLight = Typeface.createFromAsset(getAssets(), "fonts/avenirltstd-light.otf");
        typefaceAvenirBold = Typeface.createFromAsset(getAssets(), "fonts/avenirltsrd-book.otf");
        typefaveAntonioLight = Typeface.createFromAsset(getAssets(), "fonts/antonio-light.ttf");
        typefaceAntonioBold = Typeface.createFromAsset(getAssets(), "fonts/antonio-bold.ttf");
        typefaceAntonioRegular = Typeface.createFromAsset(getAssets(), "fonts/antonio-regular.ttf");
        typefaceAvenirMedium = Typeface.createFromAsset(getAssets(), "fonts/avenirltstd.medium.otf");
        usbDeviceHelper = new UsbDeviceHelper(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkMePOSPresentViaUSB();
    }


    protected void checkMePOSPresentViaUSB() {
        MePOS meposInstance = MePOSSingleton.getInstance();
        if (meposInstance == null) {
            return;
        }
        String meposConnectionManaggerClassName = meposInstance.getConnectionManager().getClass().getSimpleName();
        if ("MePOSConnectionSerialUsb".equals(meposConnectionManaggerClassName)) {
            if (!meposInstance.isMePOSConnected()) {
                createUSBInstance();
            }
        }
    }

    public void createUSBInstance() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Connection manager");
        builder.setMessage("Welcome back!, would you like to resume communication over USB between this app and the MePOS?”");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               try {
                   MePOSSingleton.createInstance(getApplicationContext(), MePOSConnectionType.USB);
                   dialog.dismiss();
               }catch (Exception e){
                   dialog.dismiss();
               }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMePOSConnected() {
        try{
            return MePOSSingleton.getInstance().isMePOSConnected();
        }catch (Exception e){
            return false;
        }
    }

    protected void findMePOSUSB() {
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            if (isAMePOS(device)) {
                MePOSSingleton.createInstance(getApplicationContext(), MePOSConnectionType.USB);
                MePOSSingleton.lastStateUsbAttached = true;
            }
        }
    }


    private void requestUsbPermissionMePOS() {
        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        try {
            UsbDevice deviceUsb = usbDeviceHelper.lookForUsbDevice(UsbDeviceHelper.VENDOR_ID_MEPOS, UsbDeviceHelper.PRODUCT_ID_DEVICE_MEPOS);
            mUsbManager.requestPermission(deviceUsb, mPermissionIntent);
        } catch (Exception e) {
            Log.e("Error: ", e.toString());
        }
    }


    protected boolean isAMePOS(UsbDevice device) {
        return device.getVendorId() == UsbDeviceHelper.VENDOR_ID_MEPOS &&
                device.getProductId() == UsbDeviceHelper.PRODUCT_ID_DEVICE_MEPOS;
    }

}
