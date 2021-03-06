package com.nfsapp.surbhi.nfsapplication.other;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.EditProfileActivity;
import com.nfsapp.surbhi.nfsapplication.activities.traveller.PackageAfterPickup;
import com.nfsapp.surbhi.nfsapplication.constants.GPSTracker;
import com.nfsapp.surbhi.nfsapplication.services.LocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.saveData;

public class NetworkClass extends AppCompatActivity{


    private static PendingIntent pendingIntent;
    private static AlarmManager manager;
//    public static final String BASE_URL_NEW = "http://18.218.89.83/NFS/index.php/Webservice/";
    public static final String BASE_URL_NEW = "http://mindinfodemo.com/NFS/index.php/Webservice/";
   public static final String BASE_ID_IMAGE_URL = "http://mindinfodemo.com/NFS/uploads/users/temp/";
    public static final String BASE_IMAGE_URL = "http://mindinfodemo.com/NFS/uploads/users/temp/";


    private static JSONObject responseDEtailsOBJ;

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public static void getPostDetails(final Activity context, String user_id, final String post_id,
                                      final Class toAct,final String act_from) {


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);


        responseDEtailsOBJ = new JSONObject();

        params.put("user_id", user_id);
        params.put("post_id", post_id);

        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
        client.post(BASE_URL_NEW + "post_details", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                responseDEtailsOBJ = response;
                ringProgressDialog.dismiss();
                System.out.println(response);
                try {
                    String response_fav = response.getString("status");
                    if (response_fav.equals("1")) {
                        JSONObject obj = response.getJSONObject("post");
                        System.out.println(obj);
                        responseDEtailsOBJ = obj;
                        openNextAct(context, responseDEtailsOBJ, toAct,act_from);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println("**** fav api ****fail***** " + post_id);
                System.out.println(errorResponse);
                responseDEtailsOBJ = errorResponse;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                ringProgressDialog.dismiss();
                System.out.println("**** fave api ****fail***** " + post_id);
                System.out.println(responseString);

            }

        });

    }


    public static void getTraveller(final Context context, String traveller_id,final Class toAct,final String act_from) {


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);


        responseDEtailsOBJ = new JSONObject();

        params.put("trevaller_id", traveller_id);

        System.out.println("========== details traveller  api =======");
        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
        System.out.println(params);
        client.post(BASE_URL_NEW + "trevaller_details", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                responseDEtailsOBJ = response;
                ringProgressDialog.dismiss();
                System.out.println(response);
                try {
                    String response_fav = response.getString("status");
                    if (response_fav.equals("1")) {
                        JSONObject obj = response.getJSONObject("result");
                        System.out.println(obj);
                        responseDEtailsOBJ = obj;
                        openNextAct(context, responseDEtailsOBJ, toAct,act_from);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);
                responseDEtailsOBJ = errorResponse;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                ringProgressDialog.dismiss();
                System.out.println(responseString);

            }

        });

    }


    public static void getAirportList(final Activity activity) {


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
        System.out.println(params);
        client.post(BASE_URL_NEW + "airport_list", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println(response);
                try {
                    String response_fav = response.getString("status");
                    if (response_fav.equals("1")) {
                        JSONArray obj = response.getJSONArray("notifications");
                        List<String> airportList = new ArrayList<>();
                        for (int i = 0; i<obj.length();i++)
                        {
                            JSONObject jsonObject =obj.getJSONObject(i);
                            String name = jsonObject.getString("airport_name");
                            String city = jsonObject.getString("airport_city");
                            String code = jsonObject.getString("airport_code");

                            airportList.add(city+"," + code);

                        }
                        String list = TextUtils.join("/",airportList);


                        saveData(activity,"country_list",list);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);

            }

        });

    }

    public static void getAirlineList(final Activity activity) {


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
        System.out.println(params);
        client.post(BASE_URL_NEW + "airline_code", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println(response);
                try {
                    String response_fav = response.getString("status");
                    if (response_fav.equals("1")) {
                        JSONArray obj = response.getJSONArray("notifications");
                        List<String> airportList = new ArrayList<>();
                        for (int i = 0; i<obj.length();i++)
                        {
                            JSONObject jsonObject =obj.getJSONObject(i);
                            String name = jsonObject.getString("airline_name");
                            String iata = jsonObject.getString("iata_code");
                            String icao = jsonObject.getString("icao_code");

                            airportList.add(name+"," + icao);

                        }
                        String list = TextUtils.join("/",airportList);


                        saveData(activity,"airline_list",list);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);

            }

        });

    }



    public static void confirmBooking(final Activity context, String trevaller_id,
                                      final String product_id,final String booking_status) {


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        String sender_id = getData(context,"user_id","");
        params.put("trevaller_id", trevaller_id);
        params.put("sender_id", sender_id);
        params.put("product_id", product_id);
        params.put("booking_status", booking_status);

        System.out.println("========== confirm_booking api =======");
        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
        System.out.println(params);
        client.post(BASE_URL_NEW + "confirm_booking", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                responseDEtailsOBJ = response;
                ringProgressDialog.dismiss();
                System.out.println(response);
                try {
                    String response_fav = response.getString("status");
                    if (response_fav.equals("1"))
                    {
                   makeToast(context,response.getString("message"));
                   context.finish();
                   return;
                    }

                    if (response_fav.equals("0"))
                    {
//                   makeToast(context,response.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);
                responseDEtailsOBJ = errorResponse;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                ringProgressDialog.dismiss();
                System.out.println(responseString);

            }

        });

    }

    private static void openNextAct(Context context, JSONObject responseDEtailsOBJ,
                                    Class toact,String act_from) {
        if (responseDEtailsOBJ != null) {
            Bundle bundle = new Bundle();
            bundle.putString("productDetails", responseDEtailsOBJ.toString());
            bundle.putString("act_name",act_from);
            context.startActivity(new Intent(context.getApplicationContext(), toact)
                    .putExtras(bundle));
        }


    }

    public static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public static void updateLabel(TextView textEdit, Calendar myCalendar) {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textEdit.setText(sdf.format(myCalendar.getTime()));
    }

    public static String getRealPathFromURI(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static void addLocation(final Context context, final String id) {

        final String userid = getData(context, "user_id", "");

        GPSTracker gps = new GPSTracker(context);
        double latitude = 0.0;
        latitude =gps.getLatitude();
        double  longitude =0.0;
        longitude=gps.getLongitude();

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        params.put("product_id", id);
        params.put("traveller_id", userid);
        params.put("traveller_lat", latitude);
        params.put("trevaller_log", longitude);

        System.out.println("==========update user location=======");
        client.setTimeout(60 * 1000);
        client.setConnectTimeout(60 * 1000);
        client.setResponseTimeout(60 * 1000);
        System.out.println(params);
        client.post(BASE_URL_NEW + "add_location", params, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                try {
                    if (response.getString("status").equalsIgnoreCase("0"))
                    {
//                        Intent alarmIntent = new Intent(, LocationService.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//                        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//                        LocationService.cancelAlarm(pendingIntent,manager);

                        stopLocationService();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                System.out.println(responseString);

            }

        });

    }

    public static void startLocationService(Activity activity,String product_id) {

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent("location_receiver");
        alarmIntent.putExtra("product_id",product_id);
        pendingIntent = PendingIntent.getBroadcast(activity, 0,
                alarmIntent, 0);
        manager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        LocationService.startAlarm(pendingIntent,manager,product_id);
    }


    public static void stopLocationService()
    {
        System.out.println("=========== stop location service=======");
        LocationService.cancelAlarm(pendingIntent,manager);
    }


    public static void sendToProfile(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.profile_update_alert);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView update_btn = dialog.findViewById(R.id.update_btn);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
             context.startActivity(new Intent(context, EditProfileActivity.class));
            }
        });
    }

}
