package com.nfsapp.surbhi.nfsapplication.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.notification.Config;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;
import com.nfsapp.surbhi.nfsapplication.other.NetworkClass;
import com.nfsapp.surbhi.nfsapplication.other.gif.GifWebView;
import com.nfsapp.surbhi.nfsapplication.smsPack.Sms;
import com.whygraphics.gifview.gif.GIFView;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.isValidPassword;

public class SignUpActivity extends AppCompatActivity {

    private  RelativeLayout next_signup;
    String regId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

         next_signup = findViewById(R.id.next_signup);
        TextView login_btn = findViewById(R.id.login_btn);
        
       final TextView codeTV = findViewById(R.id.spinner1);

        final EditText phoneEt = findViewById(R.id.phoneEt);
        final EditText nameEt = findViewById(R.id.fullnameEt);
        final EditText emailEt = findViewById(R.id.emailEt);
        final EditText passEt = findViewById(R.id.passEt);

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null)
        {
            regId = bundle.getString("FirebaseId","");
        }

        next_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = codeTV.getText().toString();
                String name = nameEt.getText().toString();
                String phone = phoneEt.getText().toString();
                String email = emailEt.getText().toString();
                String password = passEt.getText().toString();

                if (phone.length() == 0)
                {
                    phoneEt.setFocusable(true);
                    showTOast("Enter your phone number");
                    return;
                }

                if (name.length() == 0)
                {
                    nameEt.setFocusable(true);
                    showTOast("Enter your name");
                    return;
                }

                if (email.length() == 0)
                {
                    showTOast("Enter your email");
                    emailEt.setFocusable(true);
                    return;
                }
                if (!email.contains("@"))
                {
                    showTOast("Invalid email id");
                    emailEt.setFocusable(true);
                    return;
                }

                if (password.length() == 0)
                {
                    showTOast("Enter your phone number");
                    return;
                }

                if (!isValidPassword(password))
                {
                    showTOast("Enter a combination of atleast 6 characters ,includes atleast 1 uppercase character , 1 symbol and 1 number. ");
                    return;
                }

                signup(code,phone,name,email,password);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(SignUpActivity.this,LoginActivity.class));

            }
        });
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
    }

    private void signup(final String code, final String phone, String name, String email, final String password) {

        final Dialog dialog = new Dialog(SignUpActivity.this,R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        GifImageView gifview = dialog.findViewById(R.id.loaderGif);
      gifview.setGifImageResource(R.drawable.loader2);

      @SuppressLint("HardwareIds") String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        System.out.println("============= firebase id ==========" + regId);
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("user_phone", code + phone);
            params.put("user_password", password);
            params.put("user_email", email);
            params.put("full_name", name);
            params.put("user_device_type", "1");
            params.put("user_device_token", regId);
            params.put("user_device_id", device_id);
        System.out.println("================ signup api ==============");
        System.err.println(params);

            client.post(NetworkClass.BASE_URL_NEW + "signup", params, new JsonHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    dialog.dismiss();
                    System.out.println("================ signup api ========== sucsess ======");
                    System.err.println(response);
                    try {
                        if (response.getString("status").equals("0")) 
                        {
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        next_signup.setBackground(getResources().getDrawable(R.color.grey));
                        startActivity(new Intent(SignUpActivity.this, Sms.class)
                                .putExtra("user_id", response.getString("user_id")));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    dialog.dismiss();
                    System.err.println(errorResponse);
                }

                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                    System.err.println(responseString);
                }
            });


    }
    private void showTOast(String msg) {
        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
