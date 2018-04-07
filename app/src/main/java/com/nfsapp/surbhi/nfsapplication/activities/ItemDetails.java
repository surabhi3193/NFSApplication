package com.nfsapp.surbhi.nfsapplication.activities;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.adapter.SliderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class ItemDetails extends AppCompatActivity {


    private ViewPager mPager;
    private CircleIndicator indicator;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        mPager = findViewById(R.id.pager);
        indicator =findViewById(R.id.indicator);
     TextView productNameTv =findViewById(R.id.productNameTv);
     TextView destinationTV =findViewById(R.id.destinationTV);
     TextView pickupTV =findViewById(R.id.pickupTV);



        setupWindowAnimations();
        ImageView image = findViewById(R.id.back_btn);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null)
        {
            try {
                JSONObject responseObj = new JSONObject(bundle.getString("detail_obj"));

                String product_name = responseObj.getString("product_name");
                String pickup_location = responseObj.getString("pickup_location");
                String destination_location = responseObj.getString("destination_location");
                String product_pic = responseObj.getString("product_pic");
                String date = responseObj.getString("date");

                productNameTv.setText(product_name);
                pickupTV.setText(pickup_location);
                destinationTV.setText(destination_location);

                ArrayList<Uri> imageArray = new ArrayList<>();
                imageArray.add(Uri.parse(product_pic));
                init(imageArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

    private void init(final ArrayList<Uri> imageArray) {
        final int[] currentPage = {0};

        mPager.setAdapter(new SliderAdapter(ItemDetails.this, imageArray));
        mPager.setBackgroundColor(getResources().getColor(R.color.black));
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage[0] == imageArray.size()) {
                    currentPage[0] = 0;
                }
                mPager.setCurrentItem(currentPage[0]++, true);
            }
        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 2500, 2500);
    }
}
