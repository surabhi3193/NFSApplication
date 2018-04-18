package com.nfsapp.surbhi.nfsapplication.other;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;

import org.json.JSONArray;
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

public class NetworkClass {

    public static final String BASE_URL_NEW = "http://18.218.89.83/NFS/index.php/Webservice/";
    public static final String BASE_IMAGE_URL = "http://18.218.89.83/NFS/uploads/users/temp/";

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


    public static void getTraveller(final Activity context, String traveller_id,final Class toAct,final String act_from) {


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
                   makeToast(context,response.getString("message"));
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

    private static void openNextAct(Activity context, JSONObject responseDEtailsOBJ,
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
}
