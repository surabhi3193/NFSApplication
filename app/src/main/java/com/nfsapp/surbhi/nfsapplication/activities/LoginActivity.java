package com.nfsapp.surbhi.nfsapplication.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.beans.User;
import com.nfsapp.surbhi.nfsapplication.constants.GPSTracker;
import com.nfsapp.surbhi.nfsapplication.constants.Utility;
import com.nfsapp.surbhi.nfsapplication.notification.Config;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;
import com.nfsapp.surbhi.nfsapplication.smsPack.Sms;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.constants.GeocodingLocation.getAddressFromLatlng;
import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.saveData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;

public class LoginActivity extends AppCompatActivity {

    private int count = 0;
    private boolean isForgetClicked = false;
    private TextView create_account, codeTV, forgot_pass, btn_text;
    private EditText phoneEt, passEt;
    private RelativeLayout login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utility.checkFINELOCATION(LoginActivity.this);
        login_btn = findViewById(R.id.login_btn);
        create_account = findViewById(R.id.create_account);
        btn_text = findViewById(R.id.btn_text);
        codeTV = findViewById(R.id.spinner1);
        forgot_pass = findViewById(R.id.forgot_pass);
        phoneEt = findViewById(R.id.phoneEt);
        passEt = findViewById(R.id.passEt);

        codeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                picker.setListener(new CountryPickerListener() {
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        codeTV.setText(dialCode);
                        picker.dismiss();
                    }
                });
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    String phone = phoneEt.getText().toString();
                    String code = codeTV.getText().toString();
                    if (phone.length() == 0) {
                        phoneEt.setFocusable(true);
                        Toast.makeText(getApplicationContext(), "Enter your phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isForgetClicked) {
                        forgotPassword(phone, code);

                    } else {
                        String password = passEt.getText().toString();


                        if (password.length() == 0) {
                            passEt.setFocusable(true);
                            Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        loginUser(code + phone, password);
                    }
                }

            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isForgetClicked = true;
                passEt.setVisibility(View.GONE);
                forgot_pass.setVisibility(View.GONE);
                create_account.setVisibility(View.GONE);
                btn_text.setText("Reset");

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isForgetClicked)
        {
            isForgetClicked = false;
            passEt.setVisibility(View.VISIBLE);
            forgot_pass.setVisibility(View.VISIBLE);
            create_account.setVisibility(View.VISIBLE);
            btn_text.setText("Login");
        }
        else
            finish();
    }


    private void loginUser(final String phone, final String password) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(LoginActivity.this, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);
        @SuppressLint("HardwareIds") String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        params.put("user_phone", phone);
        params.put("user_password", password);
        params.put("user_device_type", "1");
        params.put("user_device_token", regId);
        params.put("user_device_id", device_id);

        System.out.println("============= signin api ==============");
        System.err.println(params);
        client.setConnectTimeout(10*6000);
        client.post(BASE_URL_NEW + "login", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        saveData(getApplicationContext(), "login", "1");
                        saveData(getApplicationContext(), "user_id", response.getString("user_id"));
                        final User user = User.getInstance();
                        GPSTracker gps = new GPSTracker(LoginActivity.this);
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        String city = getAddressFromLatlng(new LatLng(latitude, longitude), LoginActivity.this, 0);

                        user.setId(response.getString("user_id"));
                        user.setProfile_pic(response.getString("user_pic"));
                        user.setName(response.getString("full_name"));
                        user.setLocation(city);
                        user.setProfile_percent(response.getString("profile_sttaus"));
                        user.setEmail(response.getString("user_email"));
                        user.setPhone(response.getString("user_phone"));
                        user.setAccount_no(response.getString("deposit_account"));
                        user.setId_image(response.getString("valid_identity"));

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
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


    private void forgotPassword(final String phone, final String code) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(LoginActivity.this, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        params.put("user_phone", code + phone);


        client.setConnectTimeout(30000);
        client.post(BASE_URL_NEW + "forget_password", params, new JsonHttpResponseHandler() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                ringProgressDialog.dismiss();
                System.out.println(response);
                try {

                    if (response.getString("status").equals("1")) {
                        count++;
                        login_btn.setBackground(getResources().getDrawable(R.drawable.grey_bg));
                        String userid = response.getString("user_id");
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Sms.class)
                                .putExtra("phone", phone)
                                .putExtra("code", code)
                                .putExtra("password", "")
                                .putExtra("user_id", userid));
                    } else {
                        count = 0;
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

        });
    }


}
