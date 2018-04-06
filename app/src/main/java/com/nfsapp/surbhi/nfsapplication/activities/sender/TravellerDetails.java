package com.nfsapp.surbhi.nfsapplication.activities.sender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;

public class TravellerDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_details);
        ImageView image = findViewById(R.id.back_btn);

        TextView header_text = findViewById(R.id.header_text);

        header_text.setText("Traveller Detail");
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }

        });
    }
}
