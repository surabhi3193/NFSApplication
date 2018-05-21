package com.nfsapp.surbhi.nfsapplication.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.addLocation;

public class LocationService extends BroadcastReceiver {


    public static Runnable runnable = null;
    public Handler handler = null;
    Context context;

    public static void startAlarm(PendingIntent pendingIntent,AlarmManager manager,String id) {
        int interval = 10000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        System.out.println("========== start alarm============");
    }

    public static void cancelAlarm(PendingIntent pendingIntent,AlarmManager manager) {
        if (manager != null) {
            manager.cancel(pendingIntent);
            System.out.println("========== cancel alarm============");
        }
    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
//        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();

        String action = arg1.getAction();

        Log.i("Receiver", "Broadcast received: " + action);
        String id="";
        if(action.equals("location_receiver")){
            id = arg1.getExtras().getString("product_id","");

            System.out.println("=========== product_id on receive ========");
            System.out.println(id);
        }
        context=arg0;
     addLocation(arg0,id);
    }
}