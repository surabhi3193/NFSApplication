package com.nfsapp.surbhi.nfsapplication.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.constants.Utility;
import com.nfsapp.surbhi.nfsapplication.notification.Config;
import com.nfsapp.surbhi.nfsapplication.notification.NotificationUtils;
import com.nfsapp.surbhi.nfsapplication.other.MySharedPref;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.saveData;

public class SplashScreen extends AppCompatActivity {
    private static final long SPLASH_TIME_OUT = 2000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String regId = "";

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
//
//                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };

        String login =getData(getApplicationContext(), "login", "");

        if (login.equals("1"))
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        else {
            Utility.checkSMSPermission(this);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, EnterLoginActivity.class
                    ).putExtra("FirebaseId", regId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();

                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
         regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            System.err.println("====== firebaseRegId=========" + regId);
        else
            System.err.println("====== firebaseRegId not registered =========" + regId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }



    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_SMS /*126*/:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Toast.makeText(this, " Allow Permission To Continue", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent intent = new Intent(SplashScreen.this, EnterLoginActivity.class
                    ).putExtra("FirebaseId",regId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                    return;
                }
            default:
                return;
        }
    }
}
