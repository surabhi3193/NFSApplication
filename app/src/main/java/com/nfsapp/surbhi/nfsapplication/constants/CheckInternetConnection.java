package com.nfsapp.surbhi.nfsapplication.constants;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternetConnection extends Activity
{
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    static boolean grantLOc;
    static String provider;
    private static LocationManager locationManager;

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager

                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    }
