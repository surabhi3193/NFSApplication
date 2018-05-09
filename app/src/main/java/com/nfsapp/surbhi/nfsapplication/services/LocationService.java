package com.nfsapp.surbhi.nfsapplication.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.addLocation;

public class LocationService extends BroadcastReceiver {


    public static Runnable runnable = null;
    public Handler handler = null;
    Context context;

    public static void startAlarm(PendingIntent pendingIntent,AlarmManager manager) {
        int interval = 10000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        System.out.println("========== start alarm============");    }

    public static void cancelAlarm(PendingIntent pendingIntent,AlarmManager manager) {
        if (manager != null) {
            manager.cancel(pendingIntent);
            System.out.println("========== cancel alarm============");
        }
    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
        context=arg0;
      addLocation(arg0);
    }


//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
//
//
//    }
//
//    @Override
//    public void onDestroy() {
//        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
//        //handler.removeCallbacks(runnable);
//        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onStart(Intent intent, int startid) {
//        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
//
//        handler = new Handler();
//        runnable = new Runnable() {
//            public void run() {
//                Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
//                addLocation(context);
//                handler.postDelayed(runnable, 10000);            }
//        };
//    }
}