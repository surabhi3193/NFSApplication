package com.nfsapp.surbhi.nfsapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;

public class TrackFlightAware extends AppCompatActivity {

  private   WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_flight_aware);
         webView =findViewById(R.id.webView);
      TextView header_text = findViewById(R.id.header_text);
        ImageView back_btn = findViewById(R.id.back_btn);
        header_text.setText("Track Flight");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        openUrl("https://uk.flightaware.com/");


    }

    private void openUrl(String s) {
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearView();
        webView.loadUrl(s);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
