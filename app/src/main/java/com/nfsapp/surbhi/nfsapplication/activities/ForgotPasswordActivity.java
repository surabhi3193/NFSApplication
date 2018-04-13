package com.nfsapp.surbhi.nfsapplication.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;


import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.isValidPassword;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText passEt, confirmEt;
    private String user_id = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_forgot_password);

        TextView submit_btn = (TextView) findViewById(R.id.submit_btn);
        passEt = findViewById(R.id.passEt);
        confirmEt = findViewById(R.id.confirmEt);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.user_id = bundle.getString("user_id");
        }

        submit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String pass = passEt.getText().toString().trim();
                String cpass = confirmEt.getText().toString().trim();

                System.out.println("=========== passwords ========");
                System.out.println("pass ::: " + pass);
                System.out.println("cpass ::: " + cpass);


                if (pass.length() < 6 || !isValidPassword(pass)) {
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Enter a combination of atleast 6 characters ,includes atleast 1 uppercase character , 1 symbol and 1 number. ", Toast.LENGTH_LONG).show();
                    return;
                }
                if (cpass.length() < 6) {
                    confirmEt.setFocusable(true);
                    confirmEt.setCursorVisible(true);
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Enter a combination of atleast 6 characters ,includes atleast 1 uppercase character , 1 symbol and 1 number. ", Toast.LENGTH_LONG).show();
                    return;
                } else if (!pass.equalsIgnoreCase(cpass)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password mismatch, Try again ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.equalsIgnoreCase(cpass) && pass.length() > 6 && cpass.length() > 6 && isValidPassword(pass)) {

                    passEt.setError(null);
                    confirmEt.setError(null);
                    ForgotPasswordActivity.this.updatePassword(pass, ForgotPasswordActivity.this.user_id);

                }


            }
        });
    }

    private void updatePassword(String password, String user_id) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final Dialog ringProgressDialog = new Dialog(ForgotPasswordActivity.this, R.style.Theme_AppCompat_Dialog);
        ringProgressDialog.setContentView(R.layout.loading);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ringProgressDialog.show();
        GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
        gifview.setGifImageResource(R.drawable.loader2);
        params.put("new_password", password);
        params.put("user_id", user_id);

        client.setConnectTimeout(30000);
        client.post(BASE_URL_NEW + "update_password", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                try {
                    if (response.getString("status").equals("1")) {
                        ForgotPasswordActivity.this.startActivity(new Intent(ForgotPasswordActivity.this.getApplicationContext(), LoginActivity.class));
                        ForgotPasswordActivity.this.finish();
                    } else
                        Toast.makeText(ForgotPasswordActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.setCancelable(false);

            }
        });
    }
}