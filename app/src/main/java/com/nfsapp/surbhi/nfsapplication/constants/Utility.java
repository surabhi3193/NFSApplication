package com.nfsapp.surbhi.nfsapplication.constants;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build.VERSION;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Utility {
    public static final int MY_PERMISSIONS_CAMERA = 125;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 124;
    public static final int MY_PERMISSIONS_SMS = 126;
    public static final int MY_FINE_LOCATION = 121;
    public static final int MY_COARSE_LOCATION = 120;

    @TargetApi(16)
    public static boolean checkCameraPermission(final Context context) {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.CAMERA") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.CAMERA")) {
            Builder alertBuilder = new Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Camera permission is necessary");
            alertBuilder.setPositiveButton(android.R.string.yes, new OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.CAMERA"}, Utility.MY_PERMISSIONS_CAMERA);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.CAMERA"}, MY_PERMISSIONS_CAMERA);
        }
        return false;
    }

    public static boolean checkReadStoragePermission(final Context context) {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.READ_EXTERNAL_STORAGE")) {
            Builder alertBuilder = new Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("External storage permission is necessary");
            alertBuilder.setPositiveButton(android.R.string.yes, new OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        return false;
    }

    public static boolean checkWriteStoragePermission(final Context context) {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            Builder alertBuilder = new Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Storage permission is necessary");
            alertBuilder.setPositiveButton(android.R.string.yes, new OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, Utility.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        return false;
    }

    public static boolean checkSMSPermission(final Context context) {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.RECEIVE_SMS") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            Builder alertBuilder = new Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("SMS permission is necessary");
            alertBuilder.setPositiveButton(android.R.string.yes, new OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.RECEIVE_SMS"}, Utility.MY_PERMISSIONS_SMS);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.RECEIVE_SMS"}, MY_PERMISSIONS_SMS);
        }
        return false;
    }


    public static boolean checkFINELOCATION(final Context context) {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.ACCESS_FINE_LOCATION")) {
            Builder alertBuilder = new Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("SMS permission is necessary");
            alertBuilder.setPositiveButton(android.R.string.yes, new OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                            Utility.MY_FINE_LOCATION);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, MY_FINE_LOCATION);
        }
        return false;
    }

    public static boolean checkCOARSELOCATION(final Context context) {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.ACCESS_COARSE_LOCATION")) {
            Builder alertBuilder = new Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("SMS permission is necessary");
            alertBuilder.setPositiveButton(android.R.string.yes, new OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.ACCESS_COARSE_LOCATION"},
                            Utility.MY_COARSE_LOCATION);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, MY_COARSE_LOCATION);
        }
        return false;
    }
}
