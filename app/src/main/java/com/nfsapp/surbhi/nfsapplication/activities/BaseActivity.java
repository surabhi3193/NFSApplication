package com.nfsapp.surbhi.nfsapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.nfsapp.surbhi.nfsapplication.R;

public class BaseActivity extends AppCompatActivity {
    ImageView home_img, chat_img, notify_img, settings_img, profile_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        home_img = findViewById(R.id.home_img);
        chat_img = findViewById(R.id.chat_img);
        notify_img = findViewById(R.id.notify_img);
        settings_img = findViewById(R.id.settings_img);
        profile_img = findViewById(R.id.profile_img);
    }

}
