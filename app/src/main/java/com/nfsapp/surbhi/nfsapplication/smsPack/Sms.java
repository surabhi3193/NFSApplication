package com.nfsapp.surbhi.nfsapplication.smsPack;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.LoginActivity;
import com.nfsapp.surbhi.nfsapplication.activities.SignUpActivity;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.constants.CheckInternetConnection.isNetworkAvailable;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;

public class Sms extends AppCompatActivity

{
    EditText ed1, ed2, ed3, ed4;
    String otp_generated, otp_entered, phone, code, password, user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            phone = bundle.getString("phone");
            code = bundle.getString("code");
            password = bundle.getString("password");
            user_id = bundle.getString("user_id");
        }

        TextView tv = findViewById(R.id.verifyOtp);
        ed1 = findViewById(R.id.otp1);
        ed2 = findViewById(R.id.otp2);
        ed3 = findViewById(R.id.otp3);
        ed4 = findViewById(R.id.otp4);


        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ed1.setCursorVisible(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ed1.setCursorVisible(true);
                    ed2.requestFocus();
                } else
                    ed1.setCursorVisible(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ed2.setCursorVisible(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ed2.setCursorVisible(true);
                    ed3.requestFocus();
                } else
                    ed2.setCursorVisible(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ed3.setCursorVisible(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ed3.setCursorVisible(true);
                    ed4.requestFocus();
                } else
                    ed3.setCursorVisible(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ed4.setCursorVisible(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ed4.setCursorVisible(true);
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } else
                    ed4.setCursorVisible(true);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        SmsReciever.bindListener(new SmsListner() {
            @Override
            public void messageReceived(String messageText) {
                otp_generated = messageText;
                String o1 = String.valueOf(otp_generated.charAt(0));
                String o2 = String.valueOf(otp_generated.charAt(1));
                String o3 = String.valueOf(otp_generated.charAt(2));
                String o4 = String.valueOf(otp_generated.charAt(3));

                ed1.setText(o1);
                ed2.setText(o2);
                ed3.setText(o3);
                ed4.setText(o4);
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                otp_entered = (ed1.getText().toString() + ed2.getText().toString() +
                        ed3.getText().toString() + ed4.getText().toString());
               verifyOtp(user_id, otp_entered);
            }


        });


        TextView resend_btn = findViewById(R.id.resend_btn);
        resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable(Sms.this))
                    resendOTP(Sms.this, user_id);
                else
                    Toast.makeText(Sms.this, "Internet connection unavailable, try again !", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
   super.onBackPressed();
   finish();
    }


    private void verifyOtp(final String userid, final String otp_entered) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(Sms.this,R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        params.put("user_id", userid);
        params.put("otp_code", otp_entered);

        client.setConnectTimeout(30000);
        client.post(BASE_URL_NEW + "verified_otp", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0"))
                    {
                        Toast.makeText(getApplicationContext(),
                                response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else if (response.getString("status").equals("1"))
                    {
                        Toast.makeText(getApplicationContext(),"Mobile number verified successfully, Login to continue", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(Sms.this, LoginActivity.class)
                                .putExtra("phone", phone)
                                .putExtra("code", code)
                                .putExtra("password", phone)
                                .putExtra("user_id", userid));

                        finish();
                    }
                    else {
//                        user_id = response.getString("bk_userid");
//                        startActivity(new Intent(Sms.this, ForgotPasswordActivity.class)
//                                .putExtra("user_id", userid));
//
//                        finish();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
            }
        });
    }


    public  void resendOTP(final Activity activity, final String userid) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        final Dialog ringProgressDialog = new Dialog(Sms.this,R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);

        params.put("user_id", userid);

        client.setConnectTimeout(30000);
        client.post(BASE_URL_NEW + "resend_otp", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(activity,
                                response.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                    else if (response.getString("status").equals("1")){
                        Toast.makeText(activity,"OTP sent to registered number .", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
            }
        });
    }

}
