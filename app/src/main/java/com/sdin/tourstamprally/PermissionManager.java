package com.sdin.tourstamprally;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public final class PermissionManager
{
    private PermissionManager()
    {
        throw new AssertionError();
    }

    public static boolean hasLocationPermission(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        return true;
    }

    public static void requestLocationPermission(Activity activity, int requestCode)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            activity.requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, requestCode);
    }
}
