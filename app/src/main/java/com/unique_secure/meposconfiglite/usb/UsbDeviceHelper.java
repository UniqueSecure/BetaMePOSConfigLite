package com.unique_secure.meposconfiglite.usb;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.util.Map;

public class UsbDeviceHelper {

    public static final int VENDOR_ID_MEPOS = 11406;
    public static final int PRODUCT_ID_DEVICE_MEPOS = 9220;
    public static final int VENDOR_ID_FTDI = 1027;
    public static final int PRODUCT_ID_DEVICE_FTDI = 24597;

    private Context context;

    public UsbDeviceHelper(Context context) {
        this.context = context;
    }

    public UsbDevice lookForUsbDevice(int vendorId, int prodId) throws Exception {
        UsbManager mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        Map<String, UsbDevice> mapDevices = mUsbManager.getDeviceList();
        for (UsbDevice dev : mapDevices.values()) {
            if (dev.getVendorId() == vendorId && dev.getProductId() == prodId) {
                return dev;
            }
        }
        throw new Exception(String.format("Could not look for usb device with id %s and productId %s", vendorId, prodId));
    }
}
