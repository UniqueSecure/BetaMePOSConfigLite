package com.unique_secure.meposconfiglite.usb;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import com.unique_secure.meposconfiglite.R;
import com.unique_secure.meposconfiglite.persistence.MePOSSingleton;
import com.uniquesecure.meposconnect.MePOSConnectionType;


public class MePOSUSBReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("EnableDisableAction");

        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
            if (isAMePOS(device)) {
                try {
                    MePOSSingleton.createInstance(context.getApplicationContext(), MePOSConnectionType.USB);
                    Toast.makeText(context, context.getString(R.string.mepos_connected), Toast.LENGTH_SHORT).show();
                    broadcastIntent.putExtra("enableordisable", true);
                    MePOSSingleton.lastStateUsbAttached = true;
                } catch (Exception e) {
                    Log.e(context.getString(R.string.usb), e.getMessage());
                }
            } else if (isFTDI(device)) {
                MePOSSingleton.lastStateFTDI = true;
            }
        } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
            if (isAMePOS(device)) {
                try {
                    Toast.makeText(context, context.getString(R.string.mepos_disconnected), Toast.LENGTH_SHORT).show();
                    broadcastIntent.putExtra("enableordisable", false);
                    MePOSSingleton.lastStateUsbAttached = false;
                } catch (Exception e) {
                    Log.e(context.getString(R.string.usb), e.getMessage());
                }
            } else if (isFTDI(device)) {
                MePOSSingleton.lastStateFTDI = false;
            }
        }
        context.sendBroadcast(broadcastIntent);
    }

    protected boolean isAMePOS(UsbDevice device) {
        return device.getVendorId() == UsbDeviceHelper.VENDOR_ID_MEPOS &&
                device.getProductId() == UsbDeviceHelper.PRODUCT_ID_DEVICE_MEPOS;
    }

    protected boolean isFTDI(UsbDevice device) {
        return device.getVendorId() == UsbDeviceHelper.VENDOR_ID_FTDI &&
                device.getProductId() == UsbDeviceHelper.PRODUCT_ID_DEVICE_FTDI;
    }
}
