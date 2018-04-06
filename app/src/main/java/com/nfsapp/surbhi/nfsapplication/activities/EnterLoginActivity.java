package com.nfsapp.surbhi.nfsapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.nfsapp.surbhi.nfsapplication.R;

public class EnterLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_login);

        RelativeLayout login_btn = findViewById(R.id.login_btn);
        RelativeLayout signup_btn = findViewById(R.id.signup_btn);

        Bundle bundle = getIntent().getExtras();
        String regId="";
        if (bundle!=null)
        {
             regId = bundle.getString("FirebaseId","");
        }


        final String finalRegId = regId;
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(EnterLoginActivity.this,LoginActivity.class)
                        .putExtra("FirebaseId", finalRegId));

            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnterLoginActivity.this,SignUpActivity.class)
                        .putExtra("FirebaseId", finalRegId));
            }
        });
    }
}
