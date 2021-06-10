package com.sdin.tourstamprally.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;

public final class BTUtils
{
    private BTUtils()
    {
        throw new AssertionError();
    }


    public static boolean isBTLEEnabled(Context context)
    {
        // device does not support low-energy BT
        if (!isBTLECapable(context))
            return false;

        // check BT enabled
        return isBTEnabled(context);
    }

    public static boolean isBTLECapable(Context context)
    {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public static boolean isBTEnabled(Context context)
    {
        BluetoothManager manager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = manager.getAdapter();

        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }
}
