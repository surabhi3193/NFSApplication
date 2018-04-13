package com.nfsapp.surbhi.nfsapplication.activities;

import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.adapter.SliderAdapter;
import com.nfsapp.surbhi.nfsapplication.fragment.AddItemFragment;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class FullImageActivity extends AppCompatActivity {
    private ViewPager mPager2;
    private CircleIndicator indicator2;
    private ImageView back_btn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        mPager2 = findViewById(R.id.pager2);
        indicator2 = findViewById(R.id.indicator2);
        back_btn = findViewById(R.id.back_btn);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
               finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            ArrayList<Uri> imageList = new ArrayList<>();
            imageList = bundle.getParcelableArrayList("imageArray");
            System.err.println("=== full image=======");
            System.err.println(imageList.get(0));
            init(imageList);
        }
    }

    private void init(final ArrayList<Uri> imageArray) {
        final int[] currentPage = {0};

        mPager2.setAdapter(new SliderAdapter(FullImageActivity.this, imageArray,imageArray.size()));
        mPager2.setBackgroundColor(getResources().getColor(R.color.black));
        indicator2.setViewPager(mPager2);

        // Auto start of viewpager
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 2500, 2500);
    }
}
