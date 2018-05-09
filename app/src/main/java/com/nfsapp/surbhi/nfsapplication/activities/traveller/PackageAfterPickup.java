package com.nfsapp.surbhi.nfsapplication.activities.traveller;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;
import com.nfsapp.surbhi.nfsapplication.services.LocationService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.makeToast;

public class PackageAfterPickup extends AppCompatActivity {

    private String product_id, sender_id, sender_name, sender_image_url;
    private LinearLayout otpMainLay;
    private TextView header_text;
    private TextView stausTV;
    private EditText o1, o2, o3, o4;
    private Button pickup_btn;
    private String booking_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_after_pickup);


        ImageView product_img = findViewById(R.id.product_img);
        ImageView back_btn = findViewById(R.id.back_btn);
        ImageView senderIV = findViewById(R.id.senderIV);
        TextView product_name = findViewById(R.id.product_name);
        TextView traveler_nameTV = findViewById(R.id.traveler_nameTV);
        TextView departureTv = findViewById(R.id.departureTv);
        TextView arrivalTv = findViewById(R.id.arrivalTv);
        stausTV = findViewById(R.id.stausTV);
        TextView departuredateTV = findViewById(R.id.departuredateTV);

        o1 = findViewById(R.id.otp1);
        o2 = findViewById(R.id.otp2);
        o3 = findViewById(R.id.otp3);
        o4 = findViewById(R.id.otp4);

        otpMainLay = findViewById(R.id.otpMainLay);
        TextView verify_btn = findViewById(R.id.verifyOtp);
        header_text = findViewById(R.id.header_text);
         pickup_btn = findViewById(R.id.pickup_btn);
        Button chat_btn = findViewById(R.id.chat_btn);

        header_text.setText("Package Details");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String response = bundle.getString("responseObj");
            try {
                JSONObject responseObj = new JSONObject(response);
                System.out.println("=========response obj at package after pickup =========");
                System.out.println(responseObj);

                String trevaller_to = responseObj.getString("trevaller_to");
                String trevaller_from = responseObj.getString("trevaller_from");
                String product = responseObj.getString("product_name");
                 booking_status = responseObj.getString("booking_status");

                String type = responseObj.getString("type");
                String product_image_url = responseObj.getString("product_pic");

                String departure_date = responseObj.getString("departure_date");
                product_id = responseObj.getString("product_id");
                sender_id = responseObj.getString("id");
                sender_image_url = responseObj.getString("user_pic");
                sender_name = responseObj.getString("user_name");

                departuredateTV.setText(departure_date);
                product_name.setText(product);
                traveler_nameTV.setText(sender_name);
                departureTv.setText(trevaller_from);
                arrivalTv.setText(trevaller_to);
                Picasso.with(getApplicationContext()).load(sender_image_url).placeholder(R.drawable.profile_pic).into(senderIV);
                Picasso.with(getApplicationContext()).load(product_image_url).placeholder(R.drawable.no_pic).into(product_img);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (booking_status.equalsIgnoreCase("1"))
        {
            pickup_btn.setText("Pick-up");
        }
        else if (booking_status.equalsIgnoreCase("2"))
        {
            pickup_btn.setText("Deliver");
        }

        else if (booking_status.equalsIgnoreCase("3"))
        {
            pickup_btn.setBackgroundResource(R.drawable.green_rect);
            pickup_btn.setText("Delivered");
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChatActivity.class)
                        .putExtra("product_id", product_id)
                        .putExtra("sender_id", sender_id)
                        .putExtra("sender_name", sender_name)
                        .putExtra("sender_image", sender_image_url)
                );
            }
        });

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String otp = "";
                String otp1 = o1.getText().toString();
                String otp2 = o2.getText().toString();
                String otp3 = o3.getText().toString();
                String otp4 = o4.getText().toString();

                if (otp1.length() > 0 && otp2.length() > 0 && otp3.length() > 0 && otp4.length() > 0) {
                    otp = otp1 + otp2 + otp3 + otp4;
                    verifyOtp(sender_id, product_id, otp);
                } else {
                    makeToast(getApplicationContext(), "Invalid OTP");
                }

            }
        });


        pickup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pickup_btn.getText().toString().equalsIgnoreCase("Pick-up")) {
                    header_text.setText("Pickup Code");
                    otpMainLay.setVisibility(View.VISIBLE);
                }
                else
                {
                    System.out.println(pickup_btn.getText().toString());
                    makeToast(getApplicationContext(),"Under Development");

                    // Retrieve a PendingIntent that will perform a broadcast
//                    Intent alarmIntent = new Intent(PackageAfterPickup.this, LocationService.class);
//                    PendingIntent    pendingIntent = PendingIntent.getBroadcast(PackageAfterPickup.this,
//                            0, alarmIntent, 0);
//                    AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//
//                    LocationService.startAlarm(pendingIntent,manager);
                }
            }
        });


        o1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                o1.setCursorVisible(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    o1.setCursorVisible(true);
                    o2.requestFocus();
                } else
                    o1.setCursorVisible(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        o2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                o2.setCursorVisible(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    o2.setCursorVisible(true);
                    o3.requestFocus();
                } else
                    o2.setCursorVisible(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        o3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                o3.setCursorVisible(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    o3.setCursorVisible(true);
                    o4.requestFocus();
                } else
                    o3.setCursorVisible(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        o4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                o4.setCursorVisible(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    o4.setCursorVisible(true);
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } else
                    o4.setCursorVisible(true);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void verifyOtp(String sender_id, String product_id, String otp) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(PackageAfterPickup.this, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);
        final String userid = getData(PackageAfterPickup.this, "user_id", "");

        System.out.println("============ pickup code verify =============");

        params.put("traveller_id", userid);
        params.put("sender_id", sender_id);
        params.put("product_id", product_id);
        params.put("otp_code", otp);

        System.out.println(params);
        client.setConnectTimeout(30000);
        client.post(BASE_URL_NEW + "pickup_product", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                System.out.println(response);
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(getApplicationContext(),
                                response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                response.getString("message"), Toast.LENGTH_SHORT).show();
                        stausTV.setText("On the way");
                        pickup_btn.setText("Deliver");
                        onBackPressed();

                        // Retrieve a PendingIntent that will perform a broadcast
                        Intent alarmIntent = new Intent(PackageAfterPickup.this, LocationService.class);
                        PendingIntent    pendingIntent = PendingIntent.getBroadcast(PackageAfterPickup.this, 0, alarmIntent, 0);
                        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                        LocationService.startAlarm(pendingIntent,manager);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (otpMainLay.getVisibility() == View.VISIBLE) {
            o1.setText("");
            o2.setText("");
            o3.setText("");
            o4.setText("");

            header_text.setText("Package Details");
            otpMainLay.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
            finish();
        }
    }
}
