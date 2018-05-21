package com.nfsapp.surbhi.nfsapplication.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfsapp.surbhi.nfsapplication.R;

import com.nfsapp.surbhi.nfsapplication.other.GifImageView;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_URL_NEW;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.isValidPassword;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.makeToast;


public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        TextView header_text = findViewById(R.id.header_text);
        ImageView back_btn = findViewById(R.id.back_btn);

        final EditText oldPassEt = findViewById(R.id.oldpassEt);
        final EditText newPassEt = findViewById(R.id.newpassEt);
        final EditText confirmPassEt = findViewById(R.id.confirmPassET);
        Button submit_btn = findViewById(R.id.submit_btn);
        header_text.setText("Change Password");


        CheckBox mCbShowPwd =findViewById(R.id.cbShowPwd);

        // add onCheckedListener on checkbox
        // when user clicks on this checkbox, this is the handler.
        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    newPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    newPassEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPassEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String old = oldPassEt.getText().toString();
                String newpass = newPassEt.getText().toString();
                String confirmPass = confirmPassEt.getText().toString();

                if (old.length() == 0 || newpass.length() == 0 || confirmPass.length() == 0) {
                    makeToast(ChangePasswordActivity.this, "All fields are required");
                    return;
                }

                if (!isValidPassword(newpass))
                {
                    makeToast(ChangePasswordActivity.this,"Enter a combination of atleast 6 characters ,includes atleast 1 uppercase character , 1 symbol and 1 number. ");
                    return;
                }
                if (!newpass.equalsIgnoreCase(confirmPass))
                {
                    makeToast(ChangePasswordActivity.this, "Confirm password doesn't match to new password");
                return;
            }

            changePassword(old,newpass);

            }
        });


    }

    private void changePassword(String old, String newpass) {
        
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            final Dialog ringProgressDialog = new Dialog(ChangePasswordActivity.this, R.style.Theme_AppCompat_Dialog);
            ringProgressDialog.setContentView(R.layout.loading);
            ringProgressDialog.setCancelable(false);
            ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ringProgressDialog.show();
            GifImageView gifview = ringProgressDialog.findViewById(R.id.loaderGif);
            gifview.setGifImageResource(R.drawable.loader2);
            final String userid = getData(ChangePasswordActivity.this, "user_id", "");

            System.out.println("============ Change Password===========");

            params.put("user_id", userid);
            params.put("old_password", old);
            params.put("new_password", newpass);
            System.out.println(params);
            client.setConnectTimeout(30000);
            client.post(BASE_URL_NEW + "update_password", params, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    ringProgressDialog.dismiss();
                    System.out.println(response);
                    try {

                        if (response.getString("status").equals("0")) {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();

                        } else
                        {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                //            trevaller_id=4&product_id=85
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
}
