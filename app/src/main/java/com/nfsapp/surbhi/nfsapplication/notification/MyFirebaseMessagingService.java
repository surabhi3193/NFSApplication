package com.nfsapp.surbhi.nfsapplication.notification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nfsapp.surbhi.nfsapplication.activities.ItemDetails;
import com.nfsapp.surbhi.nfsapplication.activities.sender.TravellerDetails;

import org.json.JSONException;
import org.json.JSONObject;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.saveData;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Intent resultIntent;
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0)
        {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }
    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json)
    {
        System.out.println("==========  handling notification ==========");
        System.out.println(json);
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String type = data.getString("type");
            String id = data.getString("id");
            String product_id = data.getString("product_id");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = "";
            String timestamp = data.getString("timestamp");
            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);
//            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                System.out.println("===== app is in foreground=======");
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();
//            }
//            else {
                System.out.println("===== app is in background=======");
                if (type.equalsIgnoreCase("1") ||
                        type.equalsIgnoreCase("3") ||
                        type.equalsIgnoreCase("4"))
                {
                     resultIntent = new Intent(getApplicationContext(), ItemDetails.class);
                    resultIntent.putExtra("notification_message", message);
                    resultIntent.putExtra("type", type);
                    resultIntent.putExtra("post_id", id);
                }
                else if (type.equalsIgnoreCase("2"))

                {
                    saveData(getApplicationContext(),"product_id",product_id);

                    resultIntent = new Intent(getApplicationContext(), TravellerDetails.class);
                    resultIntent.putExtra("notification_message", message);
                    resultIntent.putExtra("act_name", "notification");
                    resultIntent.putExtra("trevaller_id", id);
                    resultIntent.putExtra("product_id", product_id);

                }
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
//            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message,
                                                     String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

}
