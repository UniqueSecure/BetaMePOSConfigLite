package com.unique_secure.meposconfiglite;


import android.content.Context;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.unique_secure.meposconfiglite.persistence.MePOSSingleton;
import com.unique_secure.meposconfiglite.usb.UsbDeviceHelper;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
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


    protected boolean CheckMePOSPermissions() {
        boolean MePOSPermissions = false;
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            if (isAMePOS(device)) {
                MePOSPermissions = manager.hasPermission(device);
            }
        }
        return MePOSPermissions;
    }

    protected boolean isAMePOS(UsbDevice device) {
        return device.getVendorId() == UsbDeviceHelper.VENDOR_ID_MEPOS &&
                device.getProductId() == UsbDeviceHelper.PRODUCT_ID_DEVICE_MEPOS;
    }

}
